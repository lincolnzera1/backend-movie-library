# Movie Library Backend

Bem-vindo ao backend do projeto Movie Library, uma aplicação de biblioteca de filmes.

## Tecnologias Utilizadas

- **Framework:** Spring Boot
- **Banco de Dados:** PostgreSQL
- **Documentação da API:** Swagger/OpenAPI

## Estrutura do Projeto

O projeto do backend está organizado da seguinte forma:

- **Controllers:** Contêm os endpoints da API REST.
- **Movie:** Contém entidade, DTO's e repository de Movie.
- **User:** Contém entidade, DTO's e repository de User.
- **Rating:** Contém entidade, DTO e repository de Rating.

## Como Executar o Backend

1. **Configuração do Banco de Dados:**
   - Certifique-se de ter um banco de dados PostgreSQL instalado e configure as propriedades no arquivo `application.properties`.
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/movie
    spring.datasource.username=postgres 
    spring.datasource.password=senha  
    spring.jpa.hibernate.ddl-auto=update
    ```

2. **Execução do Aplicativo:**
   - Execute a aplicação MovielibraryApplication.

## Testes

Foram desenvolvidos testes abrangentes para avaliar o desempenho e a integridade dos endpoints.

## Documentação

A documentação detalhada da API pode ser encontrada em http://localhost:8080/swagger-ui.html. Consulte esta documentação para entender os endpoints disponíveis e como utilizá-los.
