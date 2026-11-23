# 🚪 Controle de Portaria

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.1.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

O **Controle de Portaria** é um projeto deenvolvido para colocar em prática meus conhecimentos em Java e Spring Boot, trabalhando com autenticação, regras de negócio, validação de dados e persistência em banco de dados.

A ideia é reunir em um só lugar as principais operações de uma portaria: **entrada e saída de veículos, movimentação de objetos e gerenciamento de usuários**. A aplicação também conta com indicadores na página inicial, filtros de consulta e exportação de relatórios.

## ⚙️ Funcionalidades

- **Acesso ao sistema:** autenticação com Spring Security, senhas protegidas com BCrypt e perfis de usuário.
- **Painel inicial:** visão geral das entradas, movimentações pendentes e usuários cadastrados.
- **Controle de veículos:** cadastro, edição, consulta e finalização de entradas, com filtros e relatório em PDF.
- **Controle de objetos:** registro, edição, consulta e finalização das movimentações.
- **Gerenciamento de usuários:** cadastro, edição, listagem e exclusão, respeitando as permissões de acesso.
- **Validação dos formulários:** verificação de campos obrigatórios e formatos, com mensagens de erro na própria página.
- **Interface responsiva:** menu lateral, navegação simplificada e opção de mostrar ou ocultar a senha no login.

## 🖥️ Telas do sistema

As imagens abaixo são espaços reservados para capturas reais da aplicação. Para adicioná-las, crie a pasta `docs/imagens/`, salve os arquivos com os nomes indicados e retire os marcadores de comentário `<!--` e `-->` das linhas das imagens.

### 1. Login

<img width="1365" height="764" alt="image" src="https://github.com/user-attachments/assets/fb40c1d6-94f2-4c1b-8fb7-40a3865b87b3" />

### 2. Painel inicial

<img width="1363" height="766" alt="image" src="https://github.com/user-attachments/assets/595cf5a7-ca77-4b4b-bebf-33a1bd969560" />

### 3. Registro de veículos

<img width="1365" height="766" alt="image" src="https://github.com/user-attachments/assets/4cf26abd-468d-4205-b971-6055244d3871" />

### 4. Listagem de entradas

<img width="1365" height="767" alt="image" src="https://github.com/user-attachments/assets/2783325f-495b-4c03-9261-be4d3e39a1cf" />

## 🛠️ Tecnologias utilizadas

| Área | Tecnologias |
| --- | --- |
| Back-end | Java 17, Spring Boot 4.1.3, Spring MVC |
| Persistência | MySQL, Spring Data JPA, Hibernate |
| Segurança | Spring Security, BCrypt, proteção CSRF |
| Front-end | Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap 5 |
| Relatórios | OpenPDF |
| Testes | JUnit 5, Mockito, H2 e Jakarta Bean Validation |
| Build | Maven Wrapper |

## 🚀 Como executar o projeto

As instruções abaixo são para **Windows com PowerShell**. Como o projeto inclui o Maven Wrapper, não é necessário instalar o Maven separadamente.

### 1. Pré-requisitos

- **JDK 17 ou superior** instalado (`java -version`).
- **MySQL 8 ou superior** em execução.
- Uma IDE ou editor, como o VS Code.

### 2. Criar o banco de dados

No MySQL Workbench, utilizando uma conexão com permissões administrativas, execute:

```sql
CREATE DATABASE IF NOT EXISTS portaria_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Crie um usuário para a aplicação, caso ainda não tenha um:

```sql
CREATE USER 'portaria_app'@'localhost' IDENTIFIED BY 'DEFINA_UMA_SENHA_FORTE';
GRANT ALL PRIVILEGES ON portaria_db.* TO 'portaria_app'@'localhost';
```

Se você já possui um usuário MySQL com acesso ao banco, pode reutilizá-lo. **Não é necessário recriar o banco ou os usuários a cada execução.**

### 3. Configurar a conexão local

No arquivo `src/main/resources/application.properties`, confirme que existe a configuração:

```properties
spring.profiles.default=local
```

Na raiz do projeto, crie a pasta `config` e, dentro dela, o arquivo **`application-local.properties`**:

```text
controle-portaria/
├── config/
│   └── application-local.properties
├── src/
├── pom.xml
└── mvnw.cmd
```

Preencha o arquivo com os dados da sua instalação do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/portaria_db
spring.datasource.username=portaria_app
spring.datasource.password=SUA_SENHA_DO_MYSQL
```

Substitua os valores de exemplo pelas suas credenciais. O arquivo `application-local.properties` deve permanecer **fora do Git**: confira se o `.gitignore` contém esta regra:

```gitignore
application-local.properties
```

> A senha acima é a senha do **usuário do MySQL**, não a senha utilizada para entrar na aplicação. O arquivo `.env.example` é apenas um exemplo e não é carregado automaticamente pelo Spring Boot.

### 4. Criar o primeiro administrador (somente na primeira instalação)

Se o banco estiver vazio e ainda não existir uma conta `ADMIN`, abra um PowerShell na raiz do projeto e execute:

```powershell
$env:APP_BOOTSTRAP_ADMIN_ENABLED="true"
$env:APP_ADMIN_USERNAME="admin"
$env:APP_ADMIN_NAME="Administrador"
$env:APP_ADMIN_MATRICULA="0001"

$senhaAdmin = Read-Host "Crie uma senha de administrador (minimo 12 caracteres)" -AsSecureString
$env:APP_ADMIN_PASSWORD = [System.Net.NetworkCredential]::new("", $senhaAdmin).Password
Remove-Variable senhaAdmin

.\mvnw.cmd spring-boot:run
```

A aplicação cria a conta administrativa apenas se ainda não houver outro administrador. A senha é armazenada com BCrypt.

Depois de confirmar que o login funciona, interrompa o servidor com `Ctrl + C` e desative a inicialização da conta:

```powershell
$env:APP_BOOTSTRAP_ADMIN_ENABLED="false"
Remove-Item Env:APP_ADMIN_PASSWORD -ErrorAction SilentlyContinue
```

**Se o administrador já existe, pule esta etapa.** Não precisa criá-lo novamente em cada inicialização.

### 5. Iniciar a aplicação normalmente

Com o MySQL em execução, abra um terminal na raiz do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Acesse **http://localhost:8080/login** e entre com o usuário e a senha cadastrados na aplicação.

Para encerrar o servidor, pressione `Ctrl + C` no terminal. Os dados permanecem salvos no MySQL.

## 🧪 Testes

O projeto inclui testes automatizados para o carregamento do contexto, a criação inicial do administrador, regras dos serviços e validações dos formulários.

Para executar os testes:

```powershell
.\mvnw.cmd clean test
```

Para executar os testes e também empacotar a aplicação:

```powershell
.\mvnw.cmd clean verify
```

Durante os testes manuais, vale conferir login com senha incorreta, cadastro de veículos, campos inválidos, finalização de entradas, movimentação de objetos, permissões dos usuários e geração de relatórios PDF.

## 💡 Dicas e problemas comuns

- **`Access denied for user`:** verifique o usuário e a senha em `config/application-local.properties` e as permissões no MySQL.
- **`Port 8080 was already in use`:** confira se outra instância da aplicação já está rodando e encerre-a antes de iniciar novamente.
- **Mudou o código e apareceu um erro antigo:** execute `.\mvnw.cmd clean test` no PowerShell para limpar os arquivos compilados e rodar os testes novamente.
- **Erro de validação da unidade/apartamento em instalações antigas:** confira se existe uma restrição antiga no MySQL limitando `numero_apt` ao intervalo de 1101 a 2432. Se ela existir e não corresponder mais às regras do projeto, revise-a antes de remover. Não apague a tabela ou o banco para resolver isso.
- **Mudanças nas tabelas:** em ambiente local, o Hibernate pode atualizar o esquema com `ddl-auto=update`. Para uso em produção, o ideal é trabalhar com migrações versionadas e uma configuração mais restritiva.
- **Segurança:** nunca envie `application-local.properties`, `.env` com senhas ou backups com dados reais para o GitHub.

## 📁 Estrutura do projeto

```text
controle-portaria/
├── config/                              # Configuração local não versionada
├── src/
│   ├── main/
│   │   ├── java/com/controleportaria/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── static/               # CSS, JavaScript e ícones
│   │       ├── templates/            # Páginas Thymeleaf
│   │       └── application.properties
│   └── test/
├── docs/imagens/                       # Capturas de tela para o README
├── .gitignore
├── pom.xml
├── mvnw.cmd
└── README.md
```

## 📝 Sobre o projeto

A aplicação foi pensada praticar o desenvolvimento de aplicações web de ponta a ponta, com atenção especial ao Back-end, à organização do código e à segurança das operações. Antes de disponibilizá-la em produção, ainda seriam necessárias etapas adicionais de revisão de segurança, configuração de HTTPS, gerenciamento de sessões, migrações de banco e testes em um ambiente de homologação.
