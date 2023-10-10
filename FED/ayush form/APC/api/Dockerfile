from  adoptopenjdk:latest
arg JAR_FILE=target/*.jar
copy ${JAR_FILE} app.jar
entrypoint ["java","-jar","/app.jar"]
