package com.wictor.config;

import com.wictor.enums.Role;
import com.wictor.enums.Sexo;
import com.wictor.enums.Tipo;
import com.wictor.model.User;
import com.wictor.repository.UserRepository;
import com.wictor.service.PasswordService;
import com.wictor.service.CpfService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository repository) {
        return args -> {

            if (repository.count() > 0) {
                return;
            }

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
        };
    }
}