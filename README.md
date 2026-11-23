# 🚪 Sistema de Controle de Portaria

![Java](https://img.shields.io/badge/Java-8-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.6.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.1.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

## 📖 Sobre o projeto

O **Sistema de Controle de Portaria** é uma aplicação web desenvolvida em **Java e Spring Boot** para apoiar o registro e o acompanhamento das movimentações em uma portaria, com foco no controle de **veículos, objetos e usuários**.

A aplicação reúne funcionalidades de cadastro, consulta, atualização de registros, identificação de responsáveis pelas movimentações e emissão de relatórios. A interface é renderizada no servidor com **Thymeleaf**, enquanto o armazenamento dos dados é realizado em **MySQL**, com acesso por meio do **Spring Data JPA**.

## 🚀 Funcionalidades

### 🚗 Controle de veículos

- Registro de entrada com informações do veículo, placa, motorista, apartamento e tipo de acesso.
- Registro automático da data, hora e usuário responsável pela entrada.
- Finalização de registros com data, hora e usuário responsável pela saída.
- Listagem de entradas e consulta por **placa** e **status** (aberta ou finalizada).
- Telas para edição e exclusão de registros.
- **Exportação de relatório de entradas em PDF**.

### 📦 Controle de objetos

- Cadastro de movimentações de objetos com funcionário responsável e observações.
- Classificação da movimentação (saída com terceiros, saída com a mesma pessoa ou utilização interna).
- Acompanhamento dos registros com saída pendente ou finalizada.
- Registro dos horários e dos usuários responsáveis pela entrada e saída.
- Listagem, filtragem por tipo, edição e exclusão de registros.

### 👥 Gerenciamento de usuários

- Autenticação por formulário com **Spring Security**.
- Cadastro, consulta, edição e exclusão de usuários.
- Perfis definidos no sistema: **ADMIN**, **USER** e **SUP**.
- Acesso às rotas de administração de usuários restrito ao perfil **ADMIN**.
- Armazenamento de senhas com hash **BCrypt** no fluxo de cadastro.

## 🛠️ Tecnologias utilizadas

| Camada | Tecnologias |
| --- | --- |
| Back-end | Java 8, Spring Boot 2.6.3, Spring MVC |
| Persistência | Spring Data JPA, Hibernate, MySQL |
| Segurança | Spring Security, BCrypt |
| Interface | Thymeleaf, HTML, CSS, Bootstrap 5.1.3 |
| Relatórios | OpenPDF |
| Build e dependências | Maven (com Maven Wrapper) |
| Testes | Spring Boot Test, JUnit 5 |

## 🗂️ Estrutura do projeto

```text
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/      # Controladores das páginas e ações
│   │   ├── data/            # Configuração de conexão com o banco
│   │   ├── exception/       # Exceções da aplicação
│   │   ├── model/           # Entidades, enums e exportação de PDF
│   │   ├── repository/      # Acesso aos dados com Spring Data JPA
│   │   ├── security/        # Autenticação e autorização
│   │   └── service/         # Regras de negócio
│   └── resources/
│       ├── templates/       # Páginas Thymeleaf
│       ├── static/          # CSS, Bootstrap e ícones
│       └── application.properties
└── test/                    # Teste de inicialização do contexto
```

## ⚙️ Como executar localmente

### Pré-requisitos

- **JDK 8** (versão definida no `pom.xml`).
- **MySQL** em execução.
- Git (opcional, para clonar o repositório).
- Não é necessário instalar Maven separadamente: o projeto já inclui o **Maven Wrapper**.

### 1. Obtenha o projeto

Clone o repositório pelo endereço disponível no GitHub ou baixe o código em ZIP. Em seguida, abra um terminal na pasta que contém o arquivo `pom.xml`.

### 2. Prepare o banco de dados

No MySQL, crie o banco utilizado pela configuração original:

```sql
CREATE DATABASE demo;
```

A conexão é definida em `src/main/java/com/example/demo/data/DataConfiguration.java` e, na versão original, aponta para o MySQL local na porta `3306`.

**Importante:** a configuração atual contém credenciais diretamente no código. Antes de executar ou publicar o projeto, remova essas credenciais e configure os dados de conexão de forma segura, preferencialmente por variáveis de ambiente ou por configuração externa não versionada. Ajuste também o nome do banco e o usuário conforme seu ambiente.

### 3. Inicie a aplicação

No **Windows (PowerShell)**:

```powershell
.\mvnw.cmd spring-boot:run
```

No **Linux/macOS**:

```bash
./mvnw spring-boot:run
```

Depois de iniciar a aplicação, acesse:

**http://localhost:8080/login**

> O projeto utiliza autenticação e **não inclui uma conta administrativa inicial configurada automaticamente**. Para entrar no sistema em uma instalação nova, é necessário provisionar um usuário no banco com o perfil `ADMIN` e uma senha armazenada como hash BCrypt. Não existe login ou senha padrão documentado no código fornecido.

## 🧭 Principais rotas

| Rota | Finalidade |
| --- | --- |
| `/login` | Página de autenticação |
| `/home` | Tela inicial do sistema |
| `/entradas/new` | Formulário de entrada de veículos |
| `/entradas/listar` | Listagem de entradas |
| `/entradas/status?statusEntrada=ABERTA` | Consulta de entradas em aberto |
| `/entradas/buscar` | Busca de entradas por placa |
| `/entradas/export/pdf` | Exportação de relatório em PDF |
| `/objetos/new` | Formulário de registro de objetos |
| `/objetos/listar` | Listagem de objetos |
| `/usuarios/listar` | Gerenciamento de usuários (ADMIN) |

As rotas de operação exigem autenticação. Os endereços de usuários são restritos ao perfil administrador.

## 🧪 Testes

Para executar os testes incluídos no projeto:

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux/macOS:**

```bash
./mvnw test
```

O projeto possui um teste básico de carregamento do contexto Spring (`DemoApplicationTests`). Ainda não há uma suíte abrangente de testes automatizados para as regras de negócio.

## 🔎 Melhorias futuras

- Externalizar completamente as configurações sensíveis do banco de dados.
- Automatizar a criação segura do primeiro usuário administrador em ambiente de desenvolvimento.
- Ampliar a cobertura de testes automatizados.
- Revisar e aprimorar as consultas por data e os fluxos de validação e edição.
- Atualizar as dependências e revisar aspectos de segurança antes de utilizar a aplicação em produção.

---

**Projeto desenvolvido para apoiar a organização e o acompanhamento das operações de portaria por meio de uma aplicação web.**
