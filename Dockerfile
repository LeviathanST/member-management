FROM maven:3.8.5-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .
COPY . .

RUN mvn clean package

FROM amazoncorretto:21
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
WORKDIR /app
RUN apt-get update && apt-get install -y wget \
    && wget https://downloads.apache.org/tomcat/tomcat-9/v9.0.53/bin/apache-tomcat-9.0.53.tar.gz \
    && mkdir -p $CATALINA_HOME \
    && tar xzf apache-tomcat-9.0.53.tar.gz -C $CATALINA_HOME --strip-components=1 \
    && rm apache-tomcat-9.0.53.tar.gz
COPY --from=build /app/target/*.war $CATALINA_HOME/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
