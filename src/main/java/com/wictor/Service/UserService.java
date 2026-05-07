package com.wictor.Service;

import com.wictor.Dto.UserDto;
import com.wictor.Dto.UserRoleUpdateDto;
import com.wictor.Dto.UserUpdateDto;
import com.wictor.Security.PasswordService;
import com.wictor.Security.CpfService;
import com.wictor.enums.Role;
import com.wictor.enums.Sexo;
import com.wictor.enums.Tipo;
import com.wictor.exception.*;
import com.wictor.model.User;
import com.wictor.repository.UserRepository;
import com.wictor.util.AgeValidator;
import com.wictor.util.CpfValidator;
import com.wictor.util.NumberValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Value("${file.upload-dir-user}")
    private String uploadDir;
    int maximumSizeMB = 5000000;


    @Bean
    public CommandLineRunner init(UserRepository repository) {
        return args -> {
            if (repository.count() == 0) {

                User admin = User.builder()
                        .nome("Admin")
                        .cpf(CpfService.Criptografia("12345678900"))
                        .senha(PasswordService.Criptografia("admin123"))
                        .email1("admin@adm.com")
                        .tel1("11912345678")
                        .sexo(Sexo.M)
                        .cep("12345678")
                        .bairro("Jardim Adm")
                        .rua("Rua Administrador")
                        .numeroCasa(1)
                        .datanasc(java.time.LocalDate.of(1990, 1, 1))
                        .role(Role.ADMIN)
                        .tipo(Tipo.FUNCIONARIO)
                        .ativo(true)
                        .build();

                repository.save(admin);

                System.out.println("Admin inicial criado!");
            }
        };
    }

    public User cadastrar(UserDto userDTO, MultipartFile foto, User logado) {

        String cpfCript = CpfService.Criptografia(userDTO.cpf());
        String tel1 = NumberValidator.limpar(userDTO.tel1());
        String tel2 = NumberValidator.limpar(userDTO.tel2());


        if (repository.existsByCpf(cpfCript)) {
            throw new ConflitoException("CPF já cadastrado");
        }
        if (repository.existsByEmail1(userDTO.email1())) {
            throw new ConflitoException("Email já cadastrado");
        }
        if (repository.existsByTel1(tel1)) {
            throw new ConflitoException("Telefone já cadastrado");
        }
        if (!CpfValidator.validar(userDTO.cpf())) {
            throw new RegraException("CPF inválido");
        }
        if (!AgeValidator.validar(userDTO.datanasc())) {
            throw new ConflitoException("Data de nascimento inválida");
        }

        String cepp = userDTO.cep().replaceAll("[^0-9]", "");
        String hash = PasswordService.Criptografia(userDTO.senha());

        User.UserBuilder builder = User.builder()
                .cpf(cpfCript)
                .senha(hash)
                .nome(userDTO.nome())
                .email1(userDTO.email1())
                .email2(userDTO.email2())
                .tel1(tel1)
                .tel2(tel2)
                .sexo(userDTO.sexo())
                .cep(cepp)
                .bairro(userDTO.bairro())
                .rua(userDTO.rua())
                .numeroCasa(userDTO.numeroCasa())
                .comp(userDTO.comp())
                .datanasc(userDTO.datanasc())
                .ativo(true);
        boolean isAdmin = logado != null && logado.getRole().equals(Role.ADMIN);
        if (isAdmin) {
            builder.role(userDTO.role());
            builder.tipo(userDTO.tipo());
        } else {
            builder.role(Role.USER);
            builder.tipo(Tipo.ALUNO);
        }
        User user = builder.build();

        User savedUser = repository.save(user);
        Integer lastId = savedUser.getId();

        if (foto != null && !foto.isEmpty()) {
            String tipo = foto.getContentType();

            if (tipo == null || !tipo.startsWith("image/")) {
                throw new InvalidImageException("Arquivo não é uma imagem válida");
            }

            if (foto.getSize() > maximumSizeMB) {
                throw new SizeException("A imagem é muito grande. Tamanho máximo é de 5 MB");
            }

            String newName = "user_" + lastId + ".png";
            byte[] fotoBytes;
            Path basePath = Paths.get(uploadDir);
            Path finalPath = basePath.resolve(newName);
            try {
                fotoBytes = foto.getBytes();
                Files.write(finalPath, fotoBytes);
            } catch (IOException e) {
                throw new InternalErrorException("Erro ao salvar imagem");
            }
            savedUser.setFoto("User_img/" + newName);
            repository.save(savedUser);
        }
        return savedUser;
    }

    public User autenticar(String cpf, String senhaDigitada) {

        String cpfCript = CpfService.Criptografia(cpf);

        User user = repository.findByCpf(cpfCript)
                .orElseThrow(() -> new RegraException("CPF ou senha inválidos"));

        boolean senhaValida = PasswordService.verificarSenha(
                senhaDigitada,
                user.getSenha()
        );

        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }

        if (!senhaValida) {
            throw new RegraException("CPF ou senha inválidos");
        }

        return user;
    }

    public User atualizar(Integer id, UserUpdateDto dto, MultipartFile foto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }

        if (dto.cpf() != null && !dto.cpf().isBlank()) {
            if (!CpfValidator.validar(dto.cpf())) {
                throw new RegraException("CPF inválido");
            }

            String Cpfcript = CpfService.Criptografia(dto.cpf());
            if (!Cpfcript.equals(user.getCpf()) &&
                    repository.existsByCpf(Cpfcript)) {
                throw new ConflitoException("CPF já cadastrado");
            }

            user.setCpf(Cpfcript);
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            user.setSenha(PasswordService.Criptografia(dto.senha()));
        }

        if (dto.nome() != null && !dto.nome().isBlank()) {
            user.setNome(dto.nome());
        }

        if (dto.email1() != null && !dto.email1().isBlank()) {
            if (repository.existsByEmail1(dto.email1())) {
                throw new ConflitoException("Email já cadastrado");
            }
            user.setEmail1(dto.email1());
        }

        if (dto.email2() != null && !dto.email2().isBlank()) {
            user.setEmail2(dto.email2());
        }

        if (dto.tel1() != null && !dto.tel1().isBlank()) {

            String telLimpo = NumberValidator.limpar(dto.tel1());

            if (repository.existsByTel1(telLimpo)) {
                throw new ConflitoException("Telefone já cadastrado");
            }
            user.setTel1(telLimpo);
        }

        if (dto.tel2() != null && !dto.tel2().isBlank()) {
            user.setTel2(NumberValidator.limpar(dto.tel2()));
        }

        if (dto.sexo() != null) {
            user.setSexo(dto.sexo());
        }

        if (dto.cep() != null && !dto.cep().isBlank()) {
            String cepp = dto.cep().replaceAll("[^0-9]", "");
            user.setCep(cepp);
        }

        if (dto.bairro() != null && !dto.bairro().isBlank()) {
            user.setBairro(dto.bairro());
        }

        if (dto.rua() != null && !dto.rua().isBlank()) {
            user.setRua(dto.rua());
        }

        if (dto.numeroCasa() != null) {
            user.setNumeroCasa(dto.numeroCasa());
        }

        if (dto.comp() != null && !dto.comp().isBlank()) {
            user.setComp(dto.comp());
        }

        if (dto.datanasc() != null) {

            if (!AgeValidator.validar(dto.datanasc())) {
                throw new ConflitoException("Data de nascimento inválida");
            }
            user.setDatanasc(dto.datanasc());
        }

        if (foto != null && !foto.isEmpty()) {
            String tipo = foto.getContentType();

            if (tipo == null || !tipo.startsWith("image/")) {
                throw new InvalidImageException("Arquivo não é uma imagem válida");
            }

            if (foto.getSize() > maximumSizeMB) {
                throw new SizeException("A imagem é muito grande. Tamanho máximo é de 5 MB");
            }

            String newName = "user_" + user.getId() + ".png";
            byte[] fotoBytes;
            Path basePath = Paths.get(uploadDir);
            Path finalPath = basePath.resolve(newName);
            try {
                fotoBytes = foto.getBytes();
                Files.write(finalPath, fotoBytes);
            } catch (IOException e) {
                throw new InternalErrorException("Erro ao salvar imagem");
            }
            user.setFoto("User_img/" + newName);
        }
        return repository.save(user);

    }

    public User atualizarRole(Integer id, UserRoleUpdateDto dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }

        if (dto.role() != null) {
            user.setRole(dto.role());
        }

        if (dto.tipo() != null) {
            user.setTipo(dto.tipo());
        }
        return repository.save(user);
    }

    public void desativar(Integer id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setAtivo(false);

        repository.save(user);
    }

    public void reativar(Integer id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setAtivo(true);

        repository.save(user);
    }

    public void deletar(Integer id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        if (user.getFoto() != null) {
            try {
                Path path = Paths.get(user.getFoto());
                Files.deleteIfExists(path);

            } catch (IOException e) {
                System.err.println("Falha ao deletar a imagem: " + e.getMessage());
            }
        }

        repository.deleteById(id);
    }
}