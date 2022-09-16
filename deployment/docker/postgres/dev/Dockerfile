#DEV

#Dowload image Wildfly
FROM postgres:latest

#Config 
RUN apt-get update -y && apt-get upgrade -y

#modules postgres
RUN apt-get install postgresql-contrib

#Copy script sql to execute
COPY ./init.sql /docker-entrypoint-initdb.d/
COPY ./insert.sql /docker-entrypoint-initdb.d/
