# rest-shorturl
RESTful service for creating and using short URLs

See Swagger UI at <base_url>/swagger-ui.html for API documentation

CircleCi config is in .circleci/config.yml

Currently circleci is deploying builds as a Docker container to [dockerhub](http://hub.docker.com/r/callalyf/restshorturl)

**Example of running docker image**:

docker run -itd --name latestrestshorturl -p 140.203.205.15:8080:8111 -e DB_HOST=<db_host> -e DB_USERNAME=<db_username> -e DB_PASSWORD=<db_password> -e SERVER_PORT=8111 callalyf/restshorturl:latest
