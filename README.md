# Academia API

Sistema completo de gerenciamento para academias desenvolvido em **Java 21** utilizando **Spring Boot**, estruturado como uma **API REST** com arquitetura em camadas, aplicação de regras de negócio e integração com serviços externos.

A aplicação tem como objetivo gerenciar os principais processos de uma academia, incluindo cadastro de alunos, funcionários, planos, treinos, exercícios, produtos e movimentações financeiras.

O projeto foi desenvolvido com foco em boas práticas de desenvolvimento backend, organização de código, segurança, escalabilidade, documentação e implementação de regras de negócio próximas a um ambiente real.

---

# 🚀 Funcionalidades

* Cadastro e gerenciamento de usuários
* Controle de acesso utilizando diferentes níveis de permissão
* Gerenciamento de alunos e funcionários
* Cadastro e gerenciamento de planos
* Controle de treinos e exercícios
* Gerenciamento de produtos e controle de estoque
* Controle financeiro com entradas, saídas e vendas
* Geração de relatórios financeiros em PDF
* Sistema de auditoria para registro de alterações
* Autenticação utilizando JWT
* Autorização baseada em Roles
* Integração com gateway de pagamentos Abacate Pay
* Processamento de eventos através de Webhooks
* Documentação completa da API utilizando Swagger/OpenAPI

---

# 🛠️ Tecnologias utilizadas

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* JWT
* Swagger/OpenAPI
* Lombok
* Bean Validation
* OpenPDF
* JFreeChart
* Abacate Pay (gateway de pagamentos)

---

# 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada em camadas, separando responsabilidades e facilitando manutenção, testes e evolução da aplicação.

## Controller

Responsável pela exposição dos endpoints REST, recebimento das requisições HTTP, validações iniciais e comunicação com a camada de serviço.

## Service

Centraliza as regras de negócio da aplicação, garantindo validações, processamento das operações e controle dos fluxos internos.

## Repository

Responsável pela comunicação com o banco de dados utilizando Spring Data JPA e abstraindo operações de persistência.

## Model / Entity

Representa as entidades de domínio da aplicação e seus relacionamentos utilizando JPA/Hibernate.

## DTO

Responsável pela transferência de dados entre as camadas, evitando exposição direta das entidades e definindo os contratos de entrada e saída da API.

## Security

Responsável pela autenticação e autorização da aplicação utilizando Spring Security e JWT.

## Exception Handler

Centraliza o tratamento de exceções e padroniza as respostas de erro retornadas pela API.

## Auditoria

Sistema responsável pelo registro das ações realizadas dentro da aplicação.

A auditoria utiliza interceptação de métodos e contexto de execução para armazenar informações importantes sobre alterações realizadas pelos usuários.

---

# 🔐 Segurança

A aplicação utiliza autenticação baseada em **JWT (JSON Web Token)** com controle de acesso através de permissões.

Perfis disponíveis:

* ADMIN
* GERENTE
* RECEPCIONISTA
* PROFESSOR
* ALUNO

Cada perfil possui permissões específicas para utilização dos recursos da aplicação.

Além disso, o projeto possui:

* Hash seguro de senhas
* Pepper configurado através de variável de ambiente
* Controle de acesso por endpoint
* Validação de Webhooks utilizando segredo privado
* Tratamento padronizado de erros de autenticação e autorização

---

# 💳 Integração com Abacate Pay

A aplicação possui integração com o gateway de pagamentos **Abacate Pay** para gerenciamento dos planos e pagamentos dos alunos.

Recursos implementados:

* Criação de cobranças
* Pagamentos únicos
* Assinaturas recorrentes
* Cancelamento de assinaturas
* Alteração de planos
* Atualização automática através de Webhooks

Os eventos recebidos pelo Webhook são utilizados para atualizar automaticamente informações relacionadas aos pagamentos e assinaturas.

---

# ⚙️ Como executar o projeto

## Pré-requisitos

Antes de iniciar, é necessário possuir instalado:

* Java 21+
* Maven
* MySQL

---

## Configuração das variáveis de ambiente

A aplicação utiliza variáveis de ambiente para armazenar informações sensíveis.

Configure as seguintes variáveis:

```env
ABACATE_API_KEY=sua_chave_api
ABACATE_WEBHOOK_SECRET=seu_segredo_webhook
APP_PEPPER=sua_pepper_de_senha
```

Utilização das variáveis:

| Variável                 | Utilização                              |
| ------------------------ | --------------------------------------- |
| `ABACATE_API_KEY`        | Autenticação com o gateway Abacate Pay  |
| `ABACATE_WEBHOOK_SECRET` | Validação da origem dos Webhooks        |
| `APP_PEPPER`             | Camada adicional de proteção das senhas |

---

## Executando a aplicação

Clone o repositório:

```bash
git clone https://github.com/Wictor-Moraiis/Academia.git
```

Acesse o diretório:

```bash
cd Academia
```

Execute a aplicação:

```bash
mvn spring-boot:run
```

A API estará disponível em:

```
http://localhost:8080
```

---

# 📚 Documentação da API

A API possui documentação interativa utilizando **Swagger/OpenAPI**.

A documentação apresenta:

* Endpoints disponíveis
* Descrição das operações
* Parâmetros de entrada
* Modelos de requisição e resposta
* Códigos HTTP retornados
* Autenticação via Bearer Token
* Documentação dos DTOs
* Documentação dos Enums
* Respostas padronizadas de erro

Swagger disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

---

# 🗄️ Banco de dados

O projeto utiliza **MySQL** como banco de dados relacional.

A estrutura inicial do banco está disponível através do arquivo:

```
src/main/java/com/wictor/resources/schema.sql
```

O schema contém:

* Criação das tabelas
* Relacionamentos entre entidades
* Estrutura inicial necessária para execução da aplicação

---

# 📌 Objetivo do projeto

Projeto desenvolvido com o objetivo de aplicar conceitos de desenvolvimento backend profissional, incluindo:

* Arquitetura em camadas
* Segurança com JWT
* Controle de permissões
* Persistência de dados
* Integrações externas
* Processamento de Webhooks
* Auditoria
* Documentação de API
* Tratamento global de exceções
* Implementação de regras de negócio reais

O projeto representa uma API completa de gerenciamento para academias, aplicando boas práticas utilizadas em aplicações backend modernas.
