# Authorization_MicroService

This MicroService works as an Authorisation and Authentication service.
Every Other Microservice communicates with this Microservice before proceeding further.

Authenticates User when user provides valid credentials and provides JWT Token stating that user is legit.
If User Provides invalid credentials, Throws Forbidden error with error message "Invalid Credentials"

If Jwt Token is modified and the same JWT token is sent for verification, then the service sets isValid boolean as False. 

For More details, refer the below details


## End Points

### 1. POST -> http://{{baseUrl}}/v1/authorize/user

  Request Body
  
  ```
    media type : application/json
  {
    "userName": String,
    "password" : String
  }
  
  ```
  
  Response Body 
  ```
    media type : application/json
    status : 200 OK
  {
    "accessToken": String,
    "errors": boolean,
    "errorMsg": String
  }
  
  ```
  
### 2. GET -> http://{{baseUrl}}/v1/authorize/user/{{token}}
  
  Response Body 
  ```
    media type : application/json
    status : 200 OK
  {
    "isValid": boolean
  }
  
  ```  
  
  
  
  
