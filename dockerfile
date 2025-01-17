FROM corretto-17

WORKDIR /app

COPY target/task-management-1.0.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
