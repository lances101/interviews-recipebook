# TODO:

- delete all "(?)"

# Intro
The project is essentially a "Favorite recipe management system". See PDF for details.  

Also note the following quote from the recruiters email:
> In the attachment you find the document for the assignment. We expect you to spend around 4-8 hours on this 
> assignment. When you open the document, you will find all the instructions you need. I would like to ask you to 
> return the assignment before 26th of July, 9:00 CET.

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

## Some considerations

- Document the codebase religiously on public methods and wherever complexity arises. As this is a test assignment, 
  there are no set standards for the codebase.  