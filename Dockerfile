FROM gradle:8.13.0-jdk21-alpine AS build

WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM tomcat:10.1.35-jre21-temurin-jammy AS final
COPY --from=build /app/build/libs/member-management-1.0.war $CATALINA_HOME/webapps/
EXPOSE 8080
