## Design Decisions/Assumptions

### Running the application
1. Build the application using the `mvn clean install` command.
1. Build the docker image using the `docker build --tag mobilenumbersubscriptionstorage .` command.
1. Run the docker image using the `docker run -p 8080:8080 -d --name --name mobilenumbersubscriptionstorage mobilenumbersubscriptionstorage` command.
    1. You can send API requests using the `http://localhost:8080/subscriptions` URL.
    1. The H2 console can be accessed via the `http://localhost:8080/h2-console` URL. The username is `sa` with no password.
1. You can stop running the application using the `docker stop mobilenumbersubscriptionstorage` command. Once the image is stopped, all data within the H2 database will be wiped out.
1. You can delete the container using the `docker rm mobilenumbersubscriptionstorage` command.
1. You can delete the original image using the `docker rmi mobilenumbersubscriptionstorage` command.

### API
- The names of the API fields have been taken directly from the spec sheet since the assumption is that these names have been agreed upon between architects/team leads previously.
- Even though the `id` and the `service_start_date` are set as mandatory in the spec. sheet, this has been interpreted as the fields being mandatory in the database, not in the POST/PUT request.
- In an effort to favor the uniform interface approach, the user can pass in the `id` and the `service_start_date` in the PUT request however if the values change, a 400 response is returned.
- The user can't pass in the `id` and the `service_start_date` fields in the POST request. If they do, a 400 response is returned.
- Try and get validation (null check) from API generator

### Misc.
- Security/authentication has been totally omitted from the implementation since was not mentioned in the specification.
- OpenAPI Generator has been used however ideally the generated code goes under the source folder.
- E164 format assumed that the number has no + and tje the number is up to 15 digits. It was not assumed that the system will only handle numbers with the local prefix (i.e. +356).
- Unit tests only
