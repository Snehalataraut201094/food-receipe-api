# Food Recipe REST API

## Description:
- It is a java standalone application which is basically a recipe API . It contains the CRUD operations which are used for storing and retrieving the data from database.

## Technology Used:
- Java 21
- Spring Boot
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
Before importing the project please install JDK 1.8 on our machine.
1. Go to the File  menu of the IntelliJ IDE
2. Select open option
3. Select existing maven project worspace Project(i.e like "workspace name from folder" in our case it is "recipe-api") from our system directory.
4. In the root directory select the project folder from workspace where we keep the project. 
5. Check the check box of pom.xml of API
6. Finish

# How to do maven build after importing into IntelliJ IDE
This application is packaged as a jar which has Tomcat embedded. No Tomcat or JBoss installation is necessary. You run it using the java -jar command.
1. Select maven opntion on the right hand of the IDE window
2. Put clean install -e -u in the goals.
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
9  All APIs are "self-documented" by Swagger2 using annotations.

# To view Swagger 2 API docs
Run the server and browse to below link:

1.SwaggerUI - http://localhost:8081/swagger-ui/index.html

2.Swagger API-Doc - http://localhost:8081/api-docs



