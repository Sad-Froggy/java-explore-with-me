FROM amazoncorretto:11-alpine-jdk
COPY stats-service/stats-service-server/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]