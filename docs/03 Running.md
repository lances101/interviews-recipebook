# Running the project

## Prerequisites

- Docker 
  - used for both development and integration tests
  - Docker version - `4.10.1` 
  - Docker compose version - `1.29.2` 

## Running in "local" dev mode

To run the project locally in a development environment, we will need an instance of MariaDB and WHAT ELSE(?).
In order to do so, run the following command:

```bash
docker-compose -f bin/docker-compose-local-infra.yaml -p abnamro-local-dev-infra up -d
```

This will start up the containers. After that you can run the application via the `Abnamro202207Application` main class
via the IDE of your choice or by running the following Maven command:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Packaging the image