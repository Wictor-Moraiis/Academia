package com.wictor.service;

import com.wictor.Dto.UserDto;
import com.wictor.Dto.UserRoleUpdateDto;
import com.wictor.Dto.UserUpdateDto;
import com.wictor.Security.PasswordService;
import com.wictor.Security.CpfService;
import com.wictor.enums.Role;
import com.wictor.enums.Tipo;
import com.wictor.exception.ConflitoException;
import com.wictor.exception.NotFoundException;
import com.wictor.exception.RegraException;
import com.wictor.model.User;
import com.wictor.repository.UserRepository;
import com.wictor.util.AgeValidator;
import com.wictor.util.CpfValidator;
import com.wictor.util.NumberValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }


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
                        .sexo("m")
                        .cep("12345678")
                        .bairro("Jardim Adm")
                        .rua("Rua Administrador")
                        .num("1")
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
    public User cadastrar(UserDto userDTO, boolean isAdminRequest) {

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
                .num(userDTO.num())
                .comp(userDTO.comp())
                .datanasc(userDTO.datanasc())
                .ativo(true);
        if (isAdminRequest) {
            builder.role(userDTO.role());
            builder.tipo(userDTO.tipo());
        } else {
            builder.role(Role.USER);
            builder.tipo(Tipo.ALUNO);
        }
        User user = builder.build();


        return repository.save(user);
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

    public User atualizar(Long id, UserUpdateDto dto){
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

        if (dto.sexo() != null && !dto.sexo().isBlank()) {
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

        if (dto.num() != null && !dto.num().isBlank()) {
            user.setNum(dto.num());
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
        return repository.save(user);

    }

    public User atualizarRole(Long id, UserRoleUpdateDto dto){
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

    public void desativar(long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setAtivo(false);

        repository.save(user);
    }

    public void reativar(long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setAtivo(true);

        repository.save(user);
    }

    public void deletar(long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        repository.deleteById(id);
    }
}