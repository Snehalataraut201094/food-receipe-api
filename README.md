**Project Name** 
Spring Boot Rest favorite food recipe api project.

**Description:**

Favorite-food-recipe-api is the java standalone application which is basically a recipe API . It contains the spring rest endpoints which are used for storing and retrieving the data from database. The recipe API holds the below endpoints.

**Technology Used:**
Java8, Spring Boot, Spring data JPA, SQL .

**Tools USed:**
Eclipse, Postman, Maven 3.X.X, JDK1.8

**How to clone the project:**

1. open the git bash  
2. copy the "https://github.com/snehalataraut2010/recipe-api.git" url from github
3. Use git clone "https://github.com/snehalataraut2010/recipe-api.git"
4. git checkout master
5. git pull

**How to import the application in IDE:**

Before importing the project we shoulf install JDK 1.8 on our machine.

1. Go to the File  menu of the Eclipse IDE
2. Select Import option
3. Select existing maven project worspace Project(i.e like "workspace name from folder" in our case it is "recipe-api") from our system directory.
4. In the root directory select the project folder from workspace where we keep the project. 
5. Check the check box of pom.xml of API
6. Finish

**How to do maven build after importing into Eclipse IDE.**

This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the java -jar command.

1. Right click on project.
2. Select Run As then maven build
3. Put clean install -e in the goals.
4. Click Apply and then Run.

**How to run the server.**

1.Right click on the project select "Run As " from there select "Java Application".

Once the application runs you should see something like this

2017-08-29 17:31:23.091  INFO 19387 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2017-08-29 17:31:23.097  INFO 19387 --- [           main] com.khoubyari.example.Application        : Started Application in 22.285 seconds (JVM running for 23.032)

**About the Service:**

The service is just a simple food recipe REST service. It uses an in-memory database (H2) to store the data. We can also do with a relational database like MySQL or PostgreSQL. To call some REST endpoints defined in com.burk.food.recipe.api.controller on port 8080.

** Here is what this little application demonstrates:**

1. Full integration with the latest Spring Framework: inversion of control, dependency injection, data JPA etc.

2. Packaging as a single jar with embedded container (tomcat 8): No need to install a container separately on the host just run using the java -jar command.
3. Written a RESTful service using annotations: supports JSON request / response; simply used desired Accept and content-type header in the your request.
4. Exception mapping from application exceptions to the right HTTP response with exception details in the body
5. Spring Data Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations.
6. Automatic CRUD functionality against the database using Spring Repository pattern
7. Written test-cases for written all the layer from controller to service layer using Mockito framework.
8. Also added the Integration test cases using spring dependency which is spring-starter-test.
9  All APIs are "self-documented" by Swagger2 using annotations.

**To view Swagger 2 API docs**

Run the server and browse to below link:

1.SwaggerUI - http://localhost:8080/swagger-ui.html

2.Swagger API-Doc - http://localhost:8080/v2/api-docs

***Here are some endpoints :***
1. http://localhost:8080/food-recipe-api/createRecipe -- POST operation
2. http://localhost:8080/food-recipe-api/updateRecipe -- PUT Operation
3. http://localhost:8080/food-recipe-api/deleteRecipe/{recipeId} -- GET Operation
4. http://localhost:8080/food-recipe-api/getAllRecipe -- GET OPeration
5. http://localhost:8080/food-recipe-api/checkRecipe/recipeName/{recipeName} -- GET Operation
6. http://localhost:8080/food-recipe-api/getAllRecipe/numberOfServes/{numberOfServes}/ingredient/{ingredient} -- GET Operation
7. http://localhost:8080/food-recipe-api/getAllRecipe/ingredient/{ingredient}/instruction/{instruction} -- GET Operation
8. http://localhost:8080/food-recipe-api/getAllRecipe/ingredient/{ingredient} -- GET Operation
9. http://localhost:8080/food-recipe-api/getAllRecipe/category/{category} -- GET Operation


**Create Recipe endpoint:**

POST /food-recipe-api/createRecipe

Accept: application/json

Content-Type: application/json

<img width="248" alt="image" src="https://user-images.githubusercontent.com/113285163/189531027-eea39ba3-3a6f-4837-946a-ff881fdc9100.png">

RESPONSE: HTTP 201 (Created)

Location header: http://localhost:8080/food-recipe-api/createRecipe


** TO Update Recipe **

<img width="290" alt="image" src="https://user-images.githubusercontent.com/113285163/189531054-cb0aa930-fdf7-48de-9d81-40b26cf85d29.png">

RESPONSE: HTTP 404 (No Data Found)

***About Spring Boot***

Spring Boot is an "opinionated" application bootstrapping framework that makes it easy to create new RESTful services (among other types of applications). It provides many of the usual Spring facilities that can be configured easily usually without any XML. In addition to easy set up of Spring Controllers, Spring Data, etc. Spring Boot comes with the Actuator module that gives the application the following endpoints helpful in monitoring and operating the service:

1.http://localhost:8080/food-recipe-api/createRecipe -- GET Operation -- insert the  entry into database.

3. http://localhost:8080/food-recipe-api/updateRecipe -- PUT Operation -- update the data into database

4. http://localhost:8080/food-recipe-api/deleteRecipe/{recipeId} -- DELET Operation -- To delete the entry from database

5. http://localhost:8080/food-recipe-api/getAllRecipe -- GET OPeration -- get list of the recipe

6. http://localhost:8080/food-recipe-api/checkRecipe/recipeName/{recipeName} -- GET Operation -- Return list of recipe for matching input recipe

7. http://localhost:8080/food-recipe-api/getAllRecipe/numberOfServes/{numberOfServes}/ingredient/{ingredient} -- GET Operation -- Return list of recipe for matching    input ingredients and noOfServings.

8. http://localhost:8080/food-recipe-api/getAllRecipe/ingredient/{ingredient}/instruction/{instruction} -- GET Operation -- Return list of recipe for matching input    instruction but not ingredients.

9. http://localhost:8080/food-recipe-api/getAllRecipe/ingredient/{ingredient} -- GET Operation -- Return list of recipe for matching input ingredient.

10. http://localhost:8080/food-recipe-api/getAllRecipe/category/{category} -- GET Operation -- Return list of recipe for matching input category.


****Running the project with MySQL****

This project uses an in-memory database so that you don't have to install a database in order to run it. However, converting it to run with another relational database such as MySQL is very easy. Since the project uses Spring Data and the Repository pattern, it's even fairly easy to back the same service with MongoDB.

Here is what you would do to back the services with MySQL, for example:

**In pom.xml add:**

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        


**DataSource Configuration in property file for mySQL:**

spring.jpa.hibernate.ddl-auto=update

spring.datasource.url=jdbc:mysql://localhost:3306/project_exam

spring.datasource.username=root

spring.datasource.password=root

spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect





