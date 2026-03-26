package com.wictor.service;

import com.wictor.Security.PasswordService;
import com.wictor.Security.CpfService;
import com.wictor.model.User;
import com.wictor.repository.UserRepository;
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

    public User cadastrarUsuario(User user) {

        // criptografa CPF
        String cpfCript = CpfService.Criptografia(user.getCpf());
        user.setCpf(cpfCript);

        // criptografa senha
        String hash = PasswordService.Criptografia(user.getSenha());
        user.setSenha(hash);

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

        System.out.println("CPF original: " + cpf);
        System.out.println("CPF criptografado: " + cpfCript);

        return null;
    }
}