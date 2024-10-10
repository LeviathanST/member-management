FROM mysql:8.0.39-debian

ENV MYSQL_ROOT_PASSWORD=admin
ENV MYSQL_DATABASE=member-management
ENV MYSQL_USER=user
ENV MYSQL_PASSOWRD=pwd

EXPOSE 3306
COPY ./sql /docker-entrypoint-initdb.d/
