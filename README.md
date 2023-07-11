# cooperativa

Para guardar os dados da aplicação inseri no contexto o banco de dados relacional Postgres que manterá os dados que forem inseridos no mesmo, com credenciais possiveis de modificação no application.yaml.

A respeito do versionamento de aplicação utilizei o git onde as versões da aplicação podem ser validadas a cada commit.

A respeito da documentação da API utilizei o Swager onde na url, http://localhost:8082/cooperative/swagger-ui/index.html, pode ser visualizado todas as urls da aplicação.

Todas os métodos das APIs contam com logs informando como código está funcionando.

Realizado os testes unitários dos controllers.

Tratamento de erros tanto nas comunicações com banco de dados como na comunicação com serviço externo, mesmo não inserindo no contexto da aplicação.



