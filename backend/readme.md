
# Backend
Este repositório contém o backend do sistema de gerenciamento de instituições e eventos. A aplicação permite o cadastro, edição e remoção de instituições, bem como o gerenciamento de eventos vinculados a cada instituição.

O sistema também utiliza RabbitMQ para controlar a vigência dos eventos, inativando automaticamente os eventos ao atingirem a data de expiração.

# Tecnologias Utilizadas
- Java 21 - Linguagem principal
- Spring Boot - Framework para criação de microserviços
- Spring Data JPA - Persistência de dados
- PostgreSQL - Banco de dados relacional
- RabbitMQ - Mensageria para processamento de eventos
- Docker & Docker Compose - Containerização do backend e banco de dados
- JUnit & Mockito - Testes unitários e de integração

# Funcionalidades Implementadas

- Gerenciamento de Instituições
  - Criar uma nova instituição ```(POST /institution)```
  - Listar todas as instituições com paginação ```(GET /institution?page=0&size=10&sort=id)```
  - Buscar instituição por ID ```(GET /institution/{id})```
  - Atualizar dados de uma instituição ```(PUT /institution)```
  - Excluir uma instituição, desde que não tenha eventos vinculados ```(DELETE /institution/{id})```
- Gerenciamento de Eventos
  - Criar um novo evento vinculado a uma instituição ```(POST /event/{institution})```
  - Listar eventos por instituição com paginação ```(GET /event/{institution}?page=0&size=10&sort=startDate)```
  - Buscar evento pelo ID ```(GET /event/{institution}/{id})```
  - Atualizar um evento ```(PUT /event/{institution})```
  - Excluir um evento ```(DELETE /event/{id})```