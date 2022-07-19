# TODO:

- delete all "(?)"

# Intro
The project is essentially a "Favorite recipe management system".

## Assumptions

There is no way to confirm the details further with the interviewer and the requirements are quite vague. As this 
assignment is planned for 4-8 hours, we assume we need to deliver the minimum viable product. 

Therefore, the following assumptions are made:
- the ingredients are a list of strings attached to a specific recipe
- the ingredients list does not contain any additional information about the amount of the ingredient
- there is no separate api to fetch or manage the ingredients
- we assume that both our application and CI/CD pipelines are running in a containerized environment that allows us to
  create containers within our code (the latter is needed for integration tests) 

As an additional note on the topic of ingredients, a more robust and realistic approach would be to consider ingredients
their own entity and introduce a many-to-many relationship between recipes and ingredients. This would allow users to 
search for specific ingredients, provide ingredient amounts within their recipes, etc. 

Due to the constraints of the assignment, it was decided to stick to MVP based on the requirements. 

## Architecture

We will be developing a single REST service. The service will:
- be built with Spring Boot
- be using a PostgreSQL database for real deployments
- be using H2 for local development
- be using Swagger (?) for documentation
- include a service layer, although it's not necessary based on the requirements
- provide all required recipe management and fetching API endpoints
- be containerized into a Docker image based on a distroless image (via JIB)

Additionally:
- we will be introducing the checkstyle maven plugin into the project


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

