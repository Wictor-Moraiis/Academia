package com.wictor.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(new Info()
                        .title("Academia API")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento completo de academias, desenvolvida com Java 21 e Spring Boot, utilizando arquitetura em camadas, Spring Security com JWT, JPA/Hibernate, documentação OpenAPI e integração com gateway de pagamentos.
                                
                                Funcionalidades disponíveis:
                                
                                • Autenticação com JWT
                                • Controle de acesso por perfis (RBAC)
                                • Gerenciamento de alunos e funcionários
                                • Controle de planos
                                • Treinos e exercícios
                                • Controle de produtos e estoque
                                • Gestão financeira
                                • Integração com AbacatePay
                                • Auditoria de ações
                                • Geração de relatórios em PDF
                                
                                Projeto desenvolvido por Wictor Morais.
                                
                                Linkedin: https://www.linkedin.com/in/wictor-morais 
                                """)
                        .contact(
                                new Contact()
                                        .name("Wictor Morais")
                                        .url("https://github.com/wictor-moraiis")
                        )
                        .license(new License().name("MIT License"))
                )

                .components(new Components().addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        Insira o token JWT no formato:
                                        
                                        Bearer seu_token_aqui
                                        
                                        Observações:
                                        • O token é obtido através do endpoint de autenticação.
                                        • O token possui validade de 1 hora após sua emissão.
                                        • Após a expiração, é necessário realizar um novo login para obter um novo token.
                                        """)
                        )
                );

    }
}
