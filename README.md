# Movie Library Backend

Bem-vindo ao backend do projeto Movie Library, uma aplicação de biblioteca de filmes.

## Tecnologias Utilizadas

- **Framework:** Spring Boot
- **Banco de Dados:** PostgreSQL
- **Documentação da API:** Swagger/OpenAPI

## Estrutura do Projeto

O projeto do backend está organizado da seguinte forma:

- **Controllers**: Responsáveis por lidar com as requisições recebidas e definir as rotas da API REST.
- **Movie**: Representa os filmes exibidos no site. Inclui entidade, DTOs e repositório de Movie.
- **User**: Representa os usuários do sistema. Inclui entidade, DTOs e repositório de User.
- **Rating**: Representa as avaliações dos usuários. Inclui entidade, DTO e repositório de Rating

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
