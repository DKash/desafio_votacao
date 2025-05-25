#Build da aplicação usando Maven e JDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom e o código fonte
COPY pom.xml ./pom.xml
COPY src ./src

# Faz o build do projeto, pulando os testes para acelerar
RUN mvn clean package -DskipTests

# Gera a imagem final com apenas o JRE para rodar a aplicação
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o .jar gerado na etapa build
COPY --from=build app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
