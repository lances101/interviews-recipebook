# Running the project

## Prerequisites

- Docker 
  - used for both development and integration tests
  - Docker version - `4.10.1` 
  - Docker compose version - `1.29.2`
  
Notes: 
- all commands assume you are in the project root directory.
- commands tested on MacOS and Linux, Windows might have some quirks.

## Running the project via Docker (easiest)

1. Build the Docker image via Maven:
```bash
./mvnw clean compile jib:dockerBuild
```
2. Run everything with Docker Compose:
```bash
docker-compose -p recipebook-local-run up
```
3. Open the Swagger UI docs in a browser at
   [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

## Running in "local" dev mode

To run the project locally in a development environment, we will need an instance of MariaDB.
In order to do so, run the following command:

```bash
docker-compose -f bin/docker-compose-local-infra.yaml -p recipebook-local-dev-infra up -d
```

This will start up the containers. After that you can run the application via the `recipebook202207Application` main class
via the IDE of your choice or by running the following Maven command:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Open the Swagger UI docs in a browser at
[http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

## Packaging the image

Image is packaged by default on build via JIB. Currently it's not pushed anywhere. 