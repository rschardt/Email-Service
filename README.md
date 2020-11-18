#  Email Service
Backend Track

I decided to write a general endpoint for SendGrid and MailGun.

If the application fails to send a request to the chosen provider it will try to use the other one.

To set the primary provider for your request please use the provider field in the json request. 

Be sure to set all fields in the json request, there is no optional field.

Examples for using the corresponding APIs:
- https://documentation.mailgun.com/en/latest/quickstart-sending.html 
- https://sendgrid.com/solutions/email-api/

Version: 0.0.1
## How to build, test and run with gradle
### edit src/resources/application.properties (required to run tests)
```
MAILGUN_API_KEY=yourKey
MAILGUN_DOMAIN=yourDomain
SENDGRID_API_KEY=yourKey
```
### build
```
./gradlew build
```
### test
DISCLAIMER: the tests need the properties from application.properties
```
./gradlew test
```
### run
```
./gradlew bootRun
```
## How to build and run the Dockerimage
### build Dockerimage
```
docker build --build-arg JAR_FILE=build/libs/emailservice-0.0.1-SNAPSHOT.jar -t  email-service .
```
### run Dockerimage
```
docker run -p 8080:8080 email-service 
```
## How to access and test the documentation
### read api documentation in json: 
```
http://127.0.0.1:8080/v3/api-docs
```
### read api documentation in yaml:
```
http://127.0.0.1:8080/v3/api-docs.yaml 
```
### read and test swagger-ui documentation:
```
http://127.0.0.1:8080/swagger-ui/index.html
```