package com.wictor.service;

import com.wictor.Dto.UserDto;
import com.wictor.Security.PasswordService;
import com.wictor.Security.CpfService;
import com.wictor.model.User;
import com.wictor.repository.UserRepository;
import com.wictor.util.AgeValidator;
import com.wictor.util.CpfValidator;
import com.wictor.util.EmailValidator;
import com.wictor.util.NumberValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean existeUser() {
        return repository.count() > 0;
    }

    public User cadastrarUsuario(UserDto userDTO) {

        if (repository.existsByCpf(userDTO.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        if (repository.existsByEmail1(userDTO.email1())) {
            throw new RuntimeException("Email já cadastrado");
        }
        if (repository.existsByTel1(userDTO.tel1())) {
            throw new RuntimeException("Telefone já cadastrado");
        }
        if (!CpfValidator.validar(userDTO.cpf())) {
            throw new RuntimeException("CPF inválido");
        }
        if (!EmailValidator.validar(userDTO.email1())) {
            throw new RuntimeException("Primeiro email inválido");
        }
        if (userDTO.email2() != null) {
            if (!EmailValidator.validar(userDTO.email2())) {
                throw new RuntimeException("Segundo email inválido");
            }
        }
        if (!NumberValidator.validar(userDTO.tel1())) {
            throw new RuntimeException("Primeiro telefone inválido");
        }
        if (userDTO.tel2() != null) {
            if (!NumberValidator.validar(userDTO.tel2())) {
                throw new RuntimeException("Segundo telefone inválido");
            }
        }
        if (!AgeValidator.validar(userDTO.datanasc())) {
            throw new RuntimeException("Data de nascimento inválida");
        }
        String cpfCript = CpfService.Criptografia(userDTO.cpf());

        String hash = PasswordService.Criptografia(userDTO.senha());

        User user = User.builder()
                .cpf(cpfCript)
                .senha(hash)
                .nome(userDTO.nome())
                .email1(userDTO.email1())
                .email2(userDTO.email2())
                .tel1(userDTO.tel1())
                .tel2(userDTO.tel2())
                .sexo(userDTO.sexo())
                .cep(userDTO.cep())
                .bairro(userDTO.bairro())
                .rua(userDTO.rua())
                .num(userDTO.num())
                .comp(userDTO.comp())
                .datanasc(userDTO.datanasc())
                .ativo(true)
                .build();

        return repository.save(user);
    }

    public User autenticar(String cpf, String senhaDigitada) {

        String cpfCript = CpfService.Criptografia(cpf);

        Optional<User> userOpt = repository.findByCpf(cpfCript);

        if (userOpt.isPresent()) {

            User user = userOpt.get();

            boolean senhaValida = PasswordService.verificarSenha(
                    senhaDigitada,
                    user.getSenha()
            );

            if (senhaValida) {
                return user;
            }
        }

        return null;
    }
}