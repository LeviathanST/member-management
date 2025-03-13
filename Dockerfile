FROM gradle:8.13.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build

FROM tomcat:10.1.39-jre21-temurin-jammy AS final
COPY --from=build /app/build/libs/member-management-1.0.war $CATALINA_HOME/webapps/

EXPOSE 8080