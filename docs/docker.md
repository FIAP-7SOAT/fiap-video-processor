# docker compose

1. Subindo o ambiente com alguns dados no banco, para facilitar o processo de testes de código:
```shell
docker compose up --build
```
2. Caso precise fazer um reset no ambiente, usar o comando:
```shell
docker compose down --rmi all
```