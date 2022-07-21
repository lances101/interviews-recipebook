
## Architecture & tooling

We will be developing a single REST service. The service will:
- be built with Spring Boot
- be built while following an API-first approach (using OpenApi3)
- be using openapi-generator to generate the controller and dto based on an initial spec
- be using springdoc for providing the documentation
- be using mapstruct for entity<->dto mappings
- be using a MariaDB database both real deployments, local development and integration tests (containerized)
- be using Flyway for database migrations
- provide all required recipe management and fetching API endpoints
- be containerized into a Docker image based on a distroless image (via JIB)

Additionally, out of habit:
- code will also be scanned with a local install of Sonarlint
- we will be introducing the checkstyle maven plugin into the project
- we will be using google-java-format for the styling


## Model
Based on the description, recipes are expected to have:
- a name
- a list of ingredients as strings
- a number of servings
- a vegetarian boolean flag
- a instructions in text form

This will result in a fairly simple model.

### API

We are mainly dealing with one controller, the recipe controller itself. It's responsible for:
- creating, updating, removing, fetching of recipes
- providing the option to search/filter for recipes by their attributes. Required criteria are:
    - specific ingredients being *present* partial name matches (e.g. "salmon")
    - specific ingredients being *excluded* partial name matches (e.g. "salmon")
    - portion sizes (lt,gt,eq should suffice)
    - instructions text
    - vegetarian flag

### Services and repositories

These controller-level functions are expected to have a service level counterpart as well as repositories.

### Testing

We will be mainly focusing on integration tests, while only covering some minor pieces with unit tests. There is barely
any standalone complicated logic where we could benefit from writing unit tests.  
