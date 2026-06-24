package com.wictor.service;

import com.wictor.dto.user.UserDto;
import com.wictor.dto.user.UserResponseDto;
import com.wictor.dto.user.UserRoleUpdateDto;
import com.wictor.dto.user.UserUpdateDto;
import com.wictor.enums.Role;
import com.wictor.enums.Tipo;
import com.wictor.exception.*;
import com.wictor.model.User;
import com.wictor.repository.UserRepository;
import com.wictor.util.AgeValidator;
import com.wictor.util.CpfValidator;
import com.wictor.util.NumberValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserService(UserRepository userRepository, ImageService imageService)
    {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Value("${file.upload-dir-user}")
    private String uploadDir;
    private static final String PREFIXO_IMAGEM  = "user";

    @Transactional
    public UserResponseDto cadastrar(UserDto userDTO, MultipartFile foto, User logado) {

        String cpfCript = CpfService.Criptografia(userDTO.cpf());
        String tel1 = NumberValidator.limpar(userDTO.tel1());
        String tel2 = NumberValidator.limpar(userDTO.tel2());


        if (userRepository.existsByCpf(cpfCript)) {
            throw new ConflitoException("CPF já cadastrado");
        }
        if (userRepository.existsByEmail1(userDTO.email1())) {
            throw new ConflitoException("Email já cadastrado");
        }
        if (userRepository.existsByTel1(tel1)) {
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

        User savedUser = userRepository.save(user);

        atualizarFoto(savedUser, foto);

        return toResponseDto(savedUser);
    }

    public User autenticar(String cpf, String senhaDigitada) {

        String cpfCript = CpfService.Criptografia(cpf);

        User user = userRepository.findByCpf(cpfCript)
                .orElseThrow(() -> new RegraException("CPF ou senha inválidos"));

        boolean senhaValida = PasswordService.verificarSenha(
                senhaDigitada,
                user.getSenha()
        );

        validarAtivo(user);

        if (!senhaValida) {
            throw new RegraException("CPF ou senha inválidos");
        }

        return user;
    }

    @Transactional
    public UserResponseDto atualizar(Integer id, UserUpdateDto dto, MultipartFile foto) {
        User user = buscarUserPorId(id);

        validarAtivo(user);

        if (dto.cpf() != null && !dto.cpf().isBlank()) {
            if (!CpfValidator.validar(dto.cpf())) {
                throw new RegraException("CPF inválido");
            }

            String Cpfcript = CpfService.Criptografia(dto.cpf());
            if (!Cpfcript.equals(user.getCpf()) &&
                    userRepository.existsByCpf(Cpfcript)) {
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
            if (userRepository.existsByEmail1(dto.email1())) {
                throw new ConflitoException("Email já cadastrado");
            }
            user.setEmail1(dto.email1());
        }

        if (dto.email2() != null && !dto.email2().isBlank()) {
            user.setEmail2(dto.email2());
        }

        if (dto.tel1() != null && !dto.tel1().isBlank()) {

            String telLimpo = NumberValidator.limpar(dto.tel1());

            if (userRepository.existsByTel1(telLimpo)) {
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

        atualizarFoto(user, foto);
        userRepository.save(user);
        return toResponseDto(user);
    }

    public UserResponseDto atualizarRole(Integer id, UserRoleUpdateDto dto) {
        User user = buscarUserPorId(id);

       validarAtivo(user);

        if (dto.role() != null) {
            user.setRole(dto.role());
        }

        if (dto.tipo() != null) {
            user.setTipo(dto.tipo());
        }
        return toResponseDto(userRepository.save(user));
    }

    public void desativar(Integer id) {
        alterarStatus(id, false);
    }

    public void reativar(Integer id) {
        alterarStatus(id, true);
    }

    private void alterarStatus(Integer id, boolean ativo) {
        User user = buscarUserPorId(id);

        user.setAtivo(ativo);

        userRepository.save(user);
    }

    @Transactional
    public void deletar(Integer id) {

        User user = buscarUserPorId(id);

        imageService.deletarImagem(uploadDir, user.getFoto());

        userRepository.deleteById(id);
    }

    public List<UserResponseDto> listar() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public UserResponseDto buscarPorId(Integer id) {

        return toResponseDto(buscarUserPorId(id));
    }

    private User buscarUserPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getNome(),
                user.getEmail1(),
                user.getEmail2(),
                user.getTel1(),
                user.getTel2(),
                user.getSexo(),
                user.getCep(),
                user.getBairro(),
                user.getRua(),
                user.getNumeroCasa(),
                user.getComp(),
                user.getDatanasc(),
                user.getRole(),
                user.getTipo()
        );
    }

    private void atualizarFoto(User user, MultipartFile foto) {

        if (foto == null || foto.isEmpty()) {
            return;
        }

        imageService.validarImagem(foto);

        user.setFoto(
                imageService.salvarImagem(
                        foto,
                        user.getId(),
                        uploadDir,
                        PREFIXO_IMAGEM
                )
        );
    }

    private void validarAtivo(User user) {

        if (!user.isAtivo()) {
            throw new RegraException("Usuário desativado");
        }
    }

}