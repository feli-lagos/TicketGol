FROM ubuntu:latest
LABEL authors="ale"

ENTRYPOINT ["top", "-b"]

# Usa una imagen base oficial de OpenJDK (ej. versión 21)
FROM eclipse-temurin:21-jdk-alpine

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado desde tu máquina local al contenedor
COPY target/*.jar app.jar

# Expone el puerto en el que corre tu microservicio (Ajustado al de clubes)
EXPOSE 8110

# Comando para ejecutar la aplicación cuando el contenedor arranque
ENTRYPOINT ["java","-jar","/app/app.jar"]