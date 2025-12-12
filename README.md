# Apex Project Backend

This project was bootstrapped with Spring Boot 2.0.3 and is compatible with Java 8.

## Folder Structure
- config folder contains Authentication server config, jwt config, resource server config and other configs related to authentication and authorization
- controller contains api endpoint controllers
- domain contains Entity and Dto classes
- repository holds all jpa repositories
- service contains service interfaces, implementations, mapper functions and initial DataLoader class

## Getting Started

1. Install dependencies: maven
2. Change application properties active profile and provide local machines mysql server username and password
3. Start the development server: TemplateApplication main method. apex schema is created automatically with necessary entities inside mysql db.
4. DataLoder initially loads all the roles and creates user with role super admin and role manager inside db if not created/imported earlier
5. src/test contains all the test requirements mentioned in the assesment pdf
6. mvn clean install - command generates war file for tomcat based deployment


