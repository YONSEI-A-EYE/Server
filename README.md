# A-eye Server

## Prerequisites
To run this application, you need to have the following installed on your machine:
- Java 17
- Spring boot 3.0.2

## Getting Started

### 1. Git Clone
Clone the repository.
````
git clone git@github.com:YONSEI-A-EYE/Server.git
````
### 2. Setting 'application-secret.yml' file
Create a file called application-secret.yml in the src/main/resources directory of the project. 
This file should contain the following information:
````
# application-secret-yml
spring:
  datasource:
    url: jdbc:mysql://<host>:<port>/<database>
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: <username>
    password: <password>

jwt:
    secret: <jwt-secret-key>
    access-token-expire-time: 86400000
    refresh-token-expire-time: 86400000

````

### 3. build and run application