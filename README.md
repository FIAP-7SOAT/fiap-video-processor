---

# FIAP Video Processor

## Descrição

O `fiap-video-processor` é uma API desenvolvida para processar vídeos, extrair frames e disponibilizar o resultado para download. A arquitetura utiliza serviços da AWS como S3, SQS, SNS, e a biblioteca FFmpeg para processamento de vídeos.

## Arquitetura

A arquitetura desta API `fiap-video-processor` é baseada na **Arquitetura Hexagonal** (também conhecida como Arquitetura de Portos e Adaptadores). As principais vantagens desta abordagem incluem:

- **Desacoplamento**: Facilita a substituição de componentes sem impacto significativo no restante da aplicação.
- **Testabilidade**: A separação das responsabilidades torna os componentes mais fáceis de testar.
- **Manutenibilidade**: Facilita a manutenção e evolução da aplicação ao isolar as dependências externas.
- **Flexibilidade**: Permite a integração fácil com diferentes tecnologias e serviços.

## Boas Práticas Implementadas

- **Princípios SOLID**: Design orientado a princípios SOLID para criar um código mais legível e fácil de manter.
- **Injeção de Dependência**: Uso de injeção de dependência para facilitar a troca de implementações e melhorar a testabilidade.
- **Tratamento de Erros**: Tratamento de erros e exceções para garantir a robustez da aplicação.
- **Documentação**: API documentada utilizando SpringDoc OpenAPI.
- **Testes Automatizados**: Testes unitários e de integração para garantir a qualidade do código.

## Vantagens da Arquitetura

- **Escalabilidade**: A arquitetura baseada em microserviços e o uso de serviços AWS permite escalar a aplicação de acordo com a demanda.
- **Desempenho**: Processamento assíncrono com AWS SQS e utilização eficiente de recursos com AWS S3.
- **Manutenibilidade**: Código modular e bem estruturado facilita a manutenção e evolução da aplicação.
- **Confiabilidade**: Uso de serviços gerenciados da AWS garante alta disponibilidade e confiabilidade.

## Fluxo da Aplicação

A API `fiap-video-processor` segue o seguinte fluxo:

### Autenticação

- A autenticação é realizada por meio da API `fiap-auth-service` que recebe um token JWT.

### Upload do Vídeo

1. O usuário faz upload do vídeo.
2. O arquivo é armazenado no AWS S3.
3. Uma mensagem é enviada para a AWS SQS informando o novo vídeo.

### Processamento do Vídeo

1. Um worker consome a mensagem da fila e inicia o processamento.
2. O FFmpeg extrai imagens do vídeo e gera um arquivo .zip com as imagens.
3. O arquivo .zip é armazenado no AWS S3.

### Notificações por E-mail

- O usuário recebe notificações por e-mail sobre o status do processamento.

### Consulta de Status

- O usuário pode consultar o status do processamento de um vídeo específico.
- O usuário pode obter a lista de status de todos os seus vídeos.

### Download do Conteúdo Processado

- O usuário pode baixar o arquivo .zip com as imagens extraídas.

## Imagem do Fluxo da Aplicação

![Diagrama](/docs/img/diagrama.png)

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Framework**: Spring Boot
- **Processamento de Vídeo**: FFmpeg
- **Serviços AWS**:
  - S3
  - SQS
  - SNS

## Como Executar

1. Clone o repositório:
    ```sh
    git clone https://github.com/seu-usuario/fiap-video-processor.git
    cd fiap-video-processor
    ```

2. Configure as credenciais da AWS:
    ```sh
    export AWS_ACCESS_KEY_ID=your_access_key_id
    export AWS_SECRET_ACCESS_KEY=your_secret_access_key
    ```

3. Execute a aplicação:
    ```sh
    ./gradlew bootRun
    ```

4. Acesse a documentação da API:
    ```
    http://localhost:8080/swagger-ui.html
    ```

---
