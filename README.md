# TUA Phone Assessment
tua-phone-api is a Spring Boot RESTful API for processing inbound and outbound SMS requests.

## Usage - Maven

### Prerequisites

* Java 11
* Apache Maven 3.8
* Any client for testing HTTP requests (e.g. curl, Postman).

### Setup

* Package tua-phone-api using Maven:
  ```maven
  mvn clean package
  ```

* Run .jar as Java application:
  ```cmd
  java -jar tua-phone-api-0.0.1-SNAPSHOT.jar
  ```

## Usage - Heroku

### Prerequisites

* Any client for testing HTTP requests (e.g. curl, Postman).

### Setup

* No setup needed for Heroku. The app has already been deployed on a Heroku dyno (see URL in Postman section).

## Description
The tua-phone-api module can be deployed as a standalone application with dependencies on repository and rest-resource libraries.
This application uses Spring Data JPA and QueryDSL (for dynamic SQL query definitions) for database access and CRUD operations.

This application uses Postgres. An instance of Postgres is running on Heroku and has been loaded with data from the dump provided.
A Google commons in-memory cache was used to implement caching.

A custom rate limiter aspect was written was used to implement rate limiting for no more than 50 requests per day.

JUnit is also available for expansive testing.
Two sample tests for the *inbound-sms* and *outbound-sms* endpoints have been implemented in the *SmsControllerTest.java* class.

#### Authentication
This application uses basic authentication.
* Username: Pass any valid username from the account table.
* Password: Pass the auth_id belonging to the username selected.

## Postman
I have also added an export of my postman collection for this assessment. It can easily be imported on Postman to test the HTTP Endpoints.

Environment Variables:
* LOCAL: {{TUA_API}}: http://localhost:8089
* HEROKU:  {{TUA_API}}: https://tua-phone-api.herokuapp.com

## Contact
Seyi S. Adedara - adedaraseyi@outlook.com
