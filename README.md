# Raízes do Nordeste - API Back-End

API REST desenvolvida com Spring Boot para o sistema de pedidos da rede Raízes do Nordeste.

## Tecnologias
- Java 22
- Spring Boot 3.2.5
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- Swagger/OpenAPI

## Requisitos
- Java 22
- PostgreSQL instalado e rodando
- Maven

## Configuração

1. Crie o banco de dados:
```sql
CREATE DATABASE raizes_db;
```

2. Configure o arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/raizes_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

## Como rodar

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8081`

## Documentação

Swagger UI: `http://localhost:8081/swagger-ui/index.html`

## Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /auth/registro | Cadastrar usuário |
| POST | /auth/login | Autenticar e obter token |
| POST | /pedidos | Criar pedido |
| GET | /pedidos | Listar pedidos |
| GET | /pedidos/{id} | Buscar pedido |
| PATCH | /pedidos/{id}/status | Atualizar status |
| POST | /pagamentos/processar/{id} | Processar pagamento mock |

## Fluxo principal

1. Registrar usuário → `/auth/registro`
2. Fazer login → `/auth/login` (guarde o token)
3. Criar pedido → `/pedidos` (use o token no header)
4. Processar pagamento → `/pagamentos/processar/{pedidoId}`