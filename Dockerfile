# Stage 1: Build the application
FROM gradle:8.5-jdk17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia o Gradle Wrapper e arquivos de configuração primeiro para aproveitar cache
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle /app/gradle

# Dá permissão de execução ao Gradle Wrapper
RUN chmod +x gradlew

# Pré-baixa as dependências antes de copiar o código-fonte (melhora cache)
RUN ./gradlew dependencies --no-daemon

# Copia o código-fonte do projeto
COPY src /app/src

# Compila o projeto
RUN ./gradlew build --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o artefato gerado no build para o container final
COPY --from=builder /app/build/libs/*.jar fiap-video-processor-0.0.1-SNAPSHOT.jar

# Expondo a porta da aplicação
EXPOSE 8084

# Define o comando de entrada para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "fiap-video-processor-0.0.1-SNAPSHOT.jar"]
