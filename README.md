## Microserviços de Cadastro de Usuário e Notificação por Email
</br>

> 📌 NOTA
Este conteúdo foi desenvolvido seguindo o tutorial de [Michelli Brito](www.youtube.com/@MichelliBrito).
> Além do exemplo fornecido por ela, foram adicionados tratamentos de exceções e finalizado o CRUD, deixando-o completo.

### Visão Geral

Este repositório contém uma aplicação baseada em microserviços para cadastro de usuários e notificações por email, construída usando Java 17, Spring Boot, RabbitMQ, PostgreSQL e Lombok. O sistema consiste em dois microserviços principais: Serviço de Usuário e Serviço de Email. O Serviço de Usuário lida com os cadastros de usuários e publica mensagens no RabbitMQ após um registro bem-sucedido. O Serviço de Email escuta essas mensagens e envia um email de boas-vindas aos usuários recém-registrados.

### Tecnologias Utilizadas

- **Java 17**: A versão LTS mais recente do Java, oferecendo novos recursos e melhorias de desempenho.
- **Spring Boot**: Um framework que simplifica o desenvolvimento de aplicações prontas para produção.
- **RabbitMQ**: Um broker de mensagens para comunicação entre microserviços.
- **PostgreSQL**: Um poderoso sistema de banco de dados objeto-relacional de código aberto.
- **Lombok**: Uma biblioteca Java que ajuda a reduzir o código boilerplate.
- **CloudAMQP**: Um serviço RabbitMQ baseado na nuvem para intermediação de mensagens.

### Arquitetura dos Microserviços
#### Serviço de Usuário

O Serviço de Usuário é responsável pela gestão de usuários, incluindo criação, leitura, atualização e exclusão de registros de usuários. Após um registro bem-sucedido, ele publica uma mensagem no RabbitMQ para notificar outros serviços sobre o novo usuário.

Principais recursos do Serviço de Usuário incluem:
- Operações CRUD completas para gestão de usuários.
- Tratamento de exceções e validação para garantir a integridade dos dados.
- Integração com PostgreSQL para armazenamento persistente.
- Publicação de mensagens no RabbitMQ para comunicação assíncrona.

### Serviço de Email

O Serviço de Email escuta a fila do RabbitMQ para mensagens de registro de usuários. Quando um novo usuário é registrado, este serviço constrói e envia um email de boas-vindas ao usuário.

Principais recursos do Serviço de Email incluem:
- Escuta de mensagens do RabbitMQ para acionar o envio de emails.
- Construção de emails de boas-vindas personalizados.
- Utilização do Spring Boot e JavaMailSender para funcionalidade de envio de emails.
- Tratamento de exceções para gerenciar falhas no envio de emails.

### Começando

#### Pré-requisitos

- **Java 17** instalado na sua máquina.
- **Servidor RabbitMQ** (ou conta CloudAMQP).
- **Servidor de banco de dados PostgreSQL**.
- **Maven** para construir o projeto.

### Executando a Aplicação

1. **Clone o repositório**:
    -  ```git clone https://github.com/GiselleBarbosa/java-microservice-rabbitMQ```
    -  ```cd java-microservice-rabbitMQ```
   

2. **Configure o PostgreSQL**:
    - Crie um banco de dados para a aplicação.
    - Atualize as propriedades da aplicação com suas credenciais do PostgreSQL.

3. **Configure o RabbitMQ**:
    - Certifique-se de que o RabbitMQ está em execução e acessível.
    - Atualize as propriedades da aplicação com suas credenciais do RabbitMQ (ou URL do CloudAMQP).

4. **Construa e execute os serviços**:
    - Navegue até o diretório do Serviço de Usuário e execute:
        ```sh
        mvn clean install
        mvn spring-boot:run
        ```
    - Navegue até o diretório do Serviço de Email e execute:
        ```sh
        mvn clean install
        mvn spring-boot:run
        ```
### Exemplo de Configuração

A configuração para RabbitMQ e PostgreSQL pode ser encontrada no arquivo `application.properties` de cada serviço. Certifique-se de atualizar essas configurações para corresponder ao seu ambiente de configuração.

# Configuração do Arquivo `application.properties`

O arquivo `application.properties` contém as configurações necessárias para a execução do Serviço de Email. Abaixo está uma descrição detalhada de cada configuração e orientações sobre como preenchê-las.

## Nome da Aplicação e Porta do Servidor
> ⚠️ **Atenção!**
> </br>
> Tenha cuidado para não expor seus dados pessoais como *email* e *senha*. 
> 

```properties
# Nome da aplicação
spring.application.name=email

# Porta do servidor - Altere a porta de cada um dos microserviços (EX: 8081 e 8082)
server.port=8081

# URL do banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/ms-email

# Nome de usuário do banco de dados
spring.datasource.username=postgres

# Senha do banco de dados
spring.datasource.password=admin

# Configuração de inicialização do Hibernate
spring.jpa.hibernate.ddl-auto=update

# Exibir SQL no console
spring.jpa.show-sql=true

# Endereço do RabbitMQ
spring.rabbitmq.addresses=${SPRING_RABBITMQ_ADDRESSES}

# Nome da fila de email
broker.queue.email.name=${BROKER_QUEUE_EMAIL_NAME}

# Host do servidor de email
spring.mail.host=smtp.gmail.com

# Porta do servidor de email
spring.mail.port=587

# Nome de usuário da conta de email
spring.mail.username=${EMAIL_ACCOUNT}

# Senha da conta de email - Esta senha não é a do email, mas sim, uma senha que você irá obter nas configurações de sua conta Google chamada de *Senha de app*.

spring.mail.password=${PASSWORD_ACCOUNT} 

# Autenticação SMTP
spring.mail.properties.mail.smtp.auth=true

# Habilitar STARTTLS
spring.mail.properties.mail.smtp.starttls.enable=true
