FROM maven:3.9.9-eclipse-temurin-23-alpine AS build

WORKDIR app/
COPY . .

RUN mvn clean package

FROM tomcat:10.1.35-jre21-temurin-jammy AS final
COPY --from=build /app/target/member-management-1.0.war $CATALINA_HOME/webapps/
EXPOSE 8080

