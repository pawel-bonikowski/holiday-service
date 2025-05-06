# Holiday-service

Service serves simple API to find nearest common holiday for two countries.
It uses https://www.openholidaysapi.org/en/api/ as a source of data

## How to run locally

### With Docker:
 * Open terminal and navigate to main project folder
 * Build application with maven:
  ```
     mvn clean install
  ```
 * Run Docker compose
```
  docker compose up -d
```
 * Application will available on port 3000

Application requires java 21

### With IDE:
 * Build project with maven
 * Run main method in src/main/java/bluestone/task/holiday/infrastructure/Application.java
 * Application will available on port 8080

### How to test app

Application exposes single Rest Endpoint.
* To test API: [testFile](requests/testRequests.http) can be uses ( Only with IntliJ)
* sample call with curl ( for docker): 
```
curl -X GET --location "http://localhost:3000/firstCommonHoliday?date=2025-01-01&countryCode1=PL&countryCode2=ES"
```

## API Limitation:
 - The maximum range of search is 3 years. So only holidays from this area are consider in queries.
 - In this service we consider only Holidays that are nationwide. Local once are ignored

