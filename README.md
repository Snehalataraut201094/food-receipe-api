# Food Recipe REST API

## Description:
- Spring Boot Food Recipes REST API application includes CRUD operations to store and retrieve the recipes.
- Also includes search functionality to search recipes based on multiple criteria like isVegeterian, excludeIngredient, includeIngredients, instructionText, servings etc.
- API documentation is provided using Swagger 3 and OpenAPI Generator.
- The application uses PostgreSQL as the database to store recipe data.
- The project is built using Maven for dependency management and build automation.
- The application is developed using Java 21 and Spring Boot framework.
- The project follows best practices for RESTful API design and implementation.
- The code is organized into layers including controller, service, repository, and model for better maintainability and scalability.
- Unit tests are included to ensure the functionality of the API endpoints.

## Technology Used:
- Java 21
- Spring Boot
- REST API
- Spring Swagger 3
- OpenAPI Generator
- Spring data JPA
- PostgreSQL

## Tools USed:
- IntelliJ
- Postman
- Maven 3.X.X
- JDK21

# How to clone the project:
1. Open the git bash  
2. Clone the "https://github.com/Snehalataraut201094/food-receipe-api.git" url from github
4. git checkout master
5. git pull

# How to import the application in IDE:
Before importing the project please install JDK 2.1 on our machine.
1. Go to the File  menu of the IntelliJ IDE
2. Select open option
3. Select existing maven project workspace Project(i.e like "workspace name from folder" in our case it is "food-recipe-api") from our system directory.
4. In the root directory select the project folder from workspace where we keep the project. 
5. Check the check box of pom.xml of API
6. Finish

# How to do maven build after importing into IntelliJ IDE
This application is packaged as a jar which has Tomcat embedded. No Tomcat or JBoss installation is necessary. You run it using the java -jar command.
1. Select maven option on the right hand of the IDE window
2. Put mvn clean install -e -U in the goals.
3. Click Apply and then Run.

# How to run the server
1.Right click on the project select "Run As " from there select "Java Application".

Once the application runs you should see something like this

2025-08-29 17:31:23.091  INFO 19387 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8081 (http)
2025-08-29 17:31:23.097  INFO 19387 --- [           main] com.khoubyari.example.Application        : Started Application in 22.285 seconds (JVM running for 23.032)

 # Here is what this little application demonstrates

1. Full integration with the latest Spring Framework: inversion of control, dependency injection, data JPA etc.
2. Packaging as a single jar with embedded container (tomcat 8): No need to install a container separately on the host just run using the java -jar command.
3. Written a RESTful service using annotations: supports JSON request / response; simply used desired Accept and content-type header in the your request.
4. Exception mapping from application exceptions to the right HTTP response with exception details in the body
5. Spring Data Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations.
6. Automatic CRUD functionality against the database using Spring Repository pattern
7. Written test-cases for written all the layer from controller to service layer using Mockito framework.
8. Also added the Integration test cases using spring dependency which is spring-starter-test.
9  All APIs are "self-documented" by Swagger using annotations.

# To view Swagger 2 API docs After running the application
Run the server and browse to below link:

1.SwaggerUI - http://localhost:8081/swagger-ui/index.html

2.Swagger API-Doc - http://localhost:8081/api-docs

# Database Configuration
- PostgreSQL should be installed on your machine.
- Create database with name "food_recipe_db"
- Update your database username and password in application.properties file.


# API Endpoints
The following endpoints are available:
1. Create Recipe
   - URL: POST /api/recipes
   - Description: Create a new food recipe.
   - Request Body: JSON representation of the recipe.
   - Response: Created recipe object with ID.
   - Status Codes: 201 Created, 400 Bad Request
   - Example:
     ```
     POST /api/recipes
     {
       "name": "Pasta",
       "ingredients": "Noodles, Sauce, Cheese",
       "instructions": "Boil noodles, add sauce, sprinkle cheese"
     }
     ```
2. Get All Recipes
    - URL: GET /api/recipes
    - Description: Retrieve a list of all food recipes.
    - Response: List of recipe objects.
    - Status Codes: 200 OK
    - Example:
        ```
        GET /api/recipes
        Response:
        [
            {
            "id": 1,
            "name": "Pasta",
            "ingredients": "Noodles, Sauce, Cheese",
            "instructions": "Boil noodles, add sauce, sprinkle cheese"
            },
            ...
        ]
        ```
3. Get Recipe by ID
    - URL: GET /api/recipes/{id}
    - Description: Retrieve a specific food recipe by its ID.
    - Response: Recipe object.
    - Status Codes: 200 OK, 404 Not Found
    - Example:
      ```
      GET /api/recipes/1
      Response:
      {
         "id": 1,
         "name": "Pasta",
         "ingredients": "Noodles, Sauce, Cheese",
         "instructions": "Boil noodles, add sauce, sprinkle cheese"
      }
      ```
4. Update Recipe
    - URL: PUT /api/recipes/{id}
    - Description: Update an existing food recipe by its ID.
    - Request Body: JSON representation of the updated recipe.
    - Response: Updated recipe object.
    - Status Codes: 200 OK, 400 Bad Request, 404 Not Found
    - Example:
      ```
      PUT /api/recipes/1
      {
        "name": "Spaghetti",
        "ingredients": "Spaghetti Noodles, Tomato Sauce, Parmesan Cheese",
        "instructions": "Boil spaghetti, add tomato sauce, sprinkle parmesan cheese"
      }
      ```
5. Delete Recipe
    - URL: DELETE /api/recipes/{id}
    - Description: Delete a specific food recipe by its ID.
    - Response: No content.
    - Status Codes: 204 No Content, 404 Not Found
    - Example:
      ```
      DELETE /api/recipes/1
      ```
6. Search Recipes either by isVegeterian, excludeIngredient, includeIngredients, instructionText, or servings

    - URL: GET /api/recipes/search
    - Description: Search for food recipes based on various criteria.
    - Query Parameters:
      - isVegeterian (boolean): Filter recipes that are vegetarian.
      - excludeIngredient (string): Exclude recipes containing this ingredient.
      - includeIngredients (string): Include recipes containing these ingredients (comma-separated).
      - instructionText (string): Search recipes by instruction text.
      - servings (integer): Filter recipes by number of servings.
    - Response: List of recipe objects matching the search criteria.
    - Status Codes: 200 OK
    - Example:
      ```
      GET /api/recipes/search?isVegeterian=true&includeIngredients=Tomato,Cheese&servings=4
      Response:
      [
            {
            "id": 2,
            "name": "Vegetarian Pizza",
            "ingredients": "Tomato, Cheese, Bell Peppers",
            "instructions": "Prepare dough, add toppings, bake"
            },
            ...
      ]
      ```
      
















    

      
