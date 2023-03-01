# Projeto Sistema Escolar

## Sobre esse projeto

Esta é uma API simples que tem o objetivo de gerenciar usuários, cursos e matrículas.

São utilizadas as tecnologias:

- Java 11
- Maven 3+  
- Spring Boot
- Spring Web
- Bean Validation
- Spring Data JPA
- H2, o BD relacional em memória

Abra o projeto na IDE da sua preferência.

Execute os testes automatizados. 

Suba a aplicação e explore a API com uma ferramenta como [cURL](https://curl.se/), [Insomnia](https://insomnia.rest/), [Postman](https://www.postman.com/).

Alguns exemplos de chamadas usando cURL estão em [exemplos-curl.md](exemplos-curl.md).

### O que está implementado

Os seguintes endpoints estão implementados:

- `GET /users/{username}` obtém os detalhes de um usuário
- `POST /users` adiciona um novo usuário
- `GET /courses` lista os cursos já cadastrado
- `POST /courses` adiciona um novo curso
- `GET /courses/{code}` obtém os detalhes de um curso
- `POST /courses/{courseCode}/enroll` realiza a matrícula de um usuário em um curso
- `GET /courses/enroll/report` obtém relatório com e-mail e quantidade de matrículas por usuário
