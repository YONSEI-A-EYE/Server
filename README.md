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
    access-token-expire-time: <access token expire time>
    refresh-token-expire-time: <access token expire time>

chatGpt:
  api-key: <chat gpt api key>
````

### 3. build and run application

-----------
## ISSUE
I thought I was using the official Bard API, but I realized that it was an unauthorized API. 
I realized this at the last minute, so I had to quickly change it to OpenAI.
If you can provide me with the Bard API, I can change it back to that API.
