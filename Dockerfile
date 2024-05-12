FROM openjdk:11-jdk
COPY build/libs/crud-kotlin-0.0.1-SNAPSHOT.jar build/libs/app.jar
EXPOSE 8080
CMD ["java","-jar","build/libs/app.jar"]