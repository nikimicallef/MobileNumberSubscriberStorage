# epic Malta Code Challenge

## High-level Requirements

Implement a REST Web-Service responsible of maintaining a database of mobile numbers, that are assigned to clients, along with some related information. The service should support these functionalities:
- Return all mobile numbers from the database
- Return all mobile numbers that match the search criteria
- Add a mobile number to the database
- Change a mobile number plan from prepaid to postpaid or vice versa
- Delete a mobile number from the database
- Assign different owners / users of a service.
The following are the known attributes of a mobile subscriber:

| Key | Type | Mandatory | Description |
| --- | ---- | --------- | ----------- |
|Id | Integer | Y | Unique Entity ID |
| msisdn | String – E164 Format | Y | The mobile number in E164 format, ex. 35699123456 |
| customer_id_owner | Integer | Y | The ID referencing the owner of this mobile number. |
| customer_id_user | Integer | Y | The ID referencing the user of this mobile number. |
| service_type | String - Enum | Y | An enum defining the type of service. Possible values are: MOBILE_PREPAID, MOBILE_POSTPAID |
| service_start_date | Unix Epoch Millis | Y | The time this mobile number was created, encoded in Unix Epoch in Milliseconds |

## API Design Decisions

- An OpenAPI specification has been created for the `/subscriptions` endpoint. 
- It has been assumed that the names of the API fields specified are final and should be used as specified within the API. 
- Even though the `id` and the `service_start_date` are set as mandatory in the spec. sheet, this has been interpreted as the fields being mandatory in the database, not in the POST request. In fact, if they are passed in a POST request, then a 400 response is returned.
- In an effort to favor the uniform interface approach, the user can pass in the `id` and the `service_start_date` in the PUT request however if the values do not match the values stored within the database, a 400 response is returned.
- The [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator) was used to generate the controller interface in order to ensure that the specification and the implementation match. 

## Database Decisions
- An H2 in memory database has been used. The data within the database will be deleted upon the application being shut down.
- The `id` field is the primary key and it is auto-generated.
- The `msisdn` field is `unique` in order to protect at a database level concurrent inserts.
- The `id`, `msisdn` and `serviceStartDate` fields can not be updated. They are effectively final.

## Other Assumptions
- The E164 format adapted does not expect a `+` as the first character within the string. Furthermore, it was not assumed that the system will only handle numbers with the local prefix (i.e. 356).


## Running the application
1. Build the application using the `mvn clean install` command.
1. Build the docker image using the `docker build --tag mobilenumbersubscriptionstorage .` command.
1. Run the docker image using the `docker run -p 8080:8080 -d --name --name mobilenumbersubscriptionstorage mobilenumbersubscriptionstorage` command.
    1. You can send API requests using the `http://localhost:8080/subscriptions` URL.
    1. The H2 console can be accessed via the `http://localhost:8080/h2-console` URL. The username is `sa` with no password.
1. You can stop running the application using the `docker stop mobilenumbersubscriptionstorage` command. Once the image is stopped, all data within the H2 database will be wiped out.
1. You can delete the container using the `docker rm mobilenumbersubscriptionstorage` command.
1. You can delete the original image using the `docker rmi mobilenumbersubscriptionstorage` command.

NOTE: It is possible to import the application within an IDE however the application needs to be run first using the `mvn clean install ` command. This will generate the required code in the `target` folder. The project might need to be re-indexed.

## Improvements
- The `id` field should not-be auto-incremental. Ideally, a random number is used or better yet, a UUID.
- Adding security (authentication and authorization).
- `null` validation at an API level (rather than at a service layer).
- Component/integration testing, in order to ensure that the different application layers work well with each other.
- The OpenAPI generator code is generated under a `src` folder rather than the `target` folder in order to make importing of the project into an IDE simpler.
- Refactoring the validation logic to make it more obvious what is being validated within the POST and PUT requests.
