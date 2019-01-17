# rest-shorturl
A Spring Boot application implementing a RESTful service for creating and using short URLs

See Swagger UI at <base_url>/swagger-ui.html for API documentation

CircleCi config is in .circleci/config.yml

Currently circleci is deploying builds as a Docker container to <http://hub.docker.com/r/callalyf/restshorturl>

Latest docker tag is running at <http://140.203.205.15:8080/swagger-ui.html>

**Getting started**

The script run.sh can be used to get the app running for testing. It will require a jdk, docker and docker-compose.

This will run:
* ./mvnw clean package  - uses maven to compile, run unit tests and package the jar. Requires a java-8 jdk.
* docker-compose build - uses deploy/docker-compose.yml to build a restshorturl:dev docker image. Requires docker, docker-compose.
* docker-compose up - starts a containerized mysql instance and links it to a restshorturl:dev container. The mysql schema will be initialized from src/main/resources/schema.sql

**Example of running docker image**:

docker run -itd --name latestrestshorturl -p 140.203.205.15:8080:8111 -e DB_HOST=<db_host> -e DB_USERNAME=<db_username> -e DB_PASSWORD=<db_password> -e SERVER_PORT=8111 callalyf/restshorturl:latest
