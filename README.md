# Apresentação
Este desafio consiste na criação de uma API para gerenciamento de clientes e listas de produtos favoritados por estes.

# Tecnologias
- Java 8
- Spring Boot 2.4.4
- Banco de dados MySql
- Redis  3.2.100

## Instalação

1. mvn compile
2. mvn package
3. docker run -d -p 6379:6379 -i -t redis:3.2.5-alpine
4. Criação de um schema em um Banco de Dados MySql com o nome db_clients
5. java -jar magalu-0.0.1-SNAPSHOT.jar
> Obs: No arquivo application.properties, é necessário substituir o "//localhost:3306" da property "spring.datasource.url" pelo ip:porta onde estará localizado o seu banco de dados. Além disso, caso queira inicar o Redis em outra porta, é necessário alterar o host e port  dentro do mesmo arquivo citado anteriormente.

## Decisões

- Utilização de Cache dos produtos somente por 18 horas devido ha possíveis diferenças de preços ao longo dos dias.
- Para performance foram utilizados:
	- Indexação nas tabelas cliente e product.
	- Utilização de Cache Redis.
	- Paginação na busca dos favoritos de um cliente.
- O banco de dados MySql, para persistência,  irá salvar somente o UUID dos produtos solicitados para adicionar à lista de favoritos.
- Autenticação e autorização com token JWT válido por 30 minutos. 


# Respostas

|     Ação       | Endpoint                      |Codigo Response |
|----------------|-------------------------------|---------------|
|Criar usuário(POST)   | /api/client/          |200            |
|Login - Buscar Token (GET)          |/api/client/login/          |200            |
|Leitura de um Cliente (GET)|/api/client/{idCliente}|200|
|Atualização de um cliente (PUT)|/api/client/{idCliente}|200
|Deleção de um cliente (DELETE)|/api/client/{idCliente}|204
|Adicionar uma produto favorito (POST) |/api/client/{idCliente}/favorite|200
|Remover uma produto favorito (DELETE) |/api/client/{idCliente}/favorite|204
|Leitura Lista de Favoritos (GET) |/api/client/2/favorite?page=X&size=Y|200

# Exemplo de chamadas aos endpoints com suas respectivas Responses
- Criação de um usuário:
>curl -i -X POST -H "Content-Type: application/json" -d "{\"name\":\"Glauco Amaral Geraldino\", \"email\":\"glauco_amaral@gmail.com\"}" http://localhost:8080/api/client/
>
Resposta
>HTTP/1.1 201
Location: http://localhost:8080/api/client/7
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Wed, 21 Apr 2021 01:29:20 GMT
**********************************************************************************************************
- Buscar Token para Autenticar
```
curl -i -X POST -H "Content-Type: application/json" -d "{\"name\":\"Glauco Amaral Geraldino\", \"email\":\"glauco_amaral@gmail.com\"}" http://localhost:8080/api/client/login/
```
Resposta:
>HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json;charset=UTF-8
Content-Length: 268
Date: Wed, 21 Apr 2021 16:45:17 GMT
>
```js
{"token":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIEdlcmFsZGlubyIsImV4cCI6MTYxOTAyMzgxNywicm9sIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6ImdsYXVjb19hbWFyYWxAZ21haWwuY29tIiwiaWQiOjF9.soRZBvhKOD-JvWVHeg62bl2ezHHMGf-RF0z7yvgYVTFRKXa8w0l2Fw2aQIg02EYBGt9aufRl0R3034AfLnbKbg"}
```
- Leitura de um cliente:
```
curl -i -X GET -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIEdlcmFsZGlubyIsImV4cCI6MTYxOTAyMzgxNywicm9sIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6ImdsYXVjb19hbWFyYWxAZ21haWwuY29tIiwiaWQiOjF9.soRZBvhKOD-JvWVHeg62bl2ezHHMGf-RF0z7yvgYVTFRKXa8w0l2Fw2aQIg02EYBGt9aufRl0R3034AfLnbKbg"  http://localhost:8080/api/client/1
```
Resposta:
>HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 21 Apr 2021 16:48:13 GMT
``` js
{"id":1,"name":"Glauco Amaral Geraldino","email":"glauco_amaral@gmail.com"}
```
- Atualização Cliente
```
curl -i -X PUT -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIEdlcmFsZGlubyIsImV4cCI6MTYxOTAyMzgxNywicm9sIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6ImdsYXVjb19hbWFyYWxAZ21haWwuY29tIiwiaWQiOjF9.soRZBvhKOD-JvWVHeg62bl2ezHHMGf-RF0z7yvgYVTFRKXa8w0l2Fw2aQIg02EYBGt9aufRl0R3034AfLnbKbg" -H "Content-Type: application/json" -d "{\"name\":\"Glauco Amaral\", \"email\":\"glauco_amaral@gmail.com\"}"  http://localhost:8080/api/client/1
``` 
Resposta
>HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Wed, 21 Apr 2021 17:07:29 GMT
>
- Deletar um cliente:
```
curl -i -X DELETE -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIiwiZXhwIjoxNjE5MDI1MzEzLCJyb2wiOlsiUk9MRV9VU0VSIl0sImVtYWlsIjoiZ2xhdWNvX2FtYXJhbEBnbWFpbC5jb20iLCJpZCI6MX0.Kjn_QdncYHYAq3q25B9S4lndd-yHjum2Wp5e0tsT-o7SFxrwx_Ztdu8CCZ2tn7EV_Lywfh-CuFkeYFxep1nx-g"  http://localhost:8080/api/client/1
```
Resposta:
>HTTP/1.1 204
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Date: Wed, 21 Apr 2021 17:12:02 GMT
>

- Adicionar favorito:
```
curl -i -X POST -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIEdlcmFsZGlubyIsImV4cCI6MTYxOTAyNTgyOCwicm9sIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6ImdsYXVjb19hbWFyYWwyQGdtYWlsLmNvbSIsImlkIjoyfQ.QAYMj8a23VUg2pJPJOiKEqZgCPlos9Kk_JeF_FTUPi9G_IS8w3CDmYPBDjvzyxzkmvEjao1M5Nn5SCnpNdm8rQ" -H "Content-Type: application/json" -d "{\"id\":\"8632e2c2-0c48-cf97-47ad-194ef4877caf\"}"  http://localhost:8080/api/client/2/favorite
```
Resposta
>HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 0
Date: Wed, 21 Apr 2021 17:20:13 GMT
>
- Remover Favorito:
```
curl -i -X DELETE -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIEdlcmFsZGlubyIsImV4cCI6MTYxOTAyNjE0NCwicm9sIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6ImdsYXVjb19hbWFyYWwyQGdtYWlsLmNvbSIsImlkIjoyfQ.X8XEDaGSjWk5PhfYeyPAVElfP20ylefjK25Wf8W8r7Uu__5oOZpmtVCAXMd_MDo79XK8y3B3tM7I8JY4jwrzqA" -H "Content-Type: application/json" -d "{\"id\":\"8632e2c2-0c48-cf97-47ad-194ef4877caf\"}" http://localhost:8080/api/client/2/favorite
```
>HTTP/1.1 204
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Date: Wed, 21 Apr 2021 17:26:03 GMT
>

- Obter favoritos:
```
curl -i -X GET -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHbGF1Y28gQW1hcmFsIEdlcmFsZGlubyIsImV4cCI6MTYxOTAyNTgyOCwicm9sIjpbIlJPTEVfVVNFUiJdLCJlbWFpbCI6ImdsYXVjb19hbWFyYWwyQGdtYWlsLmNvbSIsImlkIjoyfQ.QAYMj8a23VUg2pJPJOiKEqZgCPlos9Kk_JeF_FTUPi9G_IS8w3CDmYPBDjvzyxzkmvEjao1M5Nn5SCnpNdm8rQ" http://localhost:8080/api/client/2/favorite?page=1&size=10
```
>HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 21 Apr 2021 17:21:41 GMT
>
```js
{
    "content": [
        {
            "id": "8632e2c2-0c48-cf97-47ad-194ef4877caf"
        }
    ],
    "info": {
        "pageNumber": 1,
        "pageSize": 100
    }
}
```


