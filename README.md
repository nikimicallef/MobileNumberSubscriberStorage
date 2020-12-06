## Design Decisions/Assumptions
### API
- The names of the API fields have been taken directly from the spec sheet since the assumption is that these names have been agreed upon between architects/team leads previously.
- Even though the `id` and the `service_start_date` are set as mandatory in the spec. sheet, this has been interpreted as the fields being mandatory in the database, not in the POST/PUT request.
- In an effort to favor the uniform interface approach, the user can pass in the `id` and the `service_start_date` in the POST and PUT requests however if passed in, a 400 response is returned.

### Misc.
- Security/authentication has been totally omitted from the implementation since was not mentioned in the specification.
- OpenAPI Generator has been used however ideally the generated code goes under the source folder.
- E164 format assumed that the number has no + and tje the number is up to 15 digits. It was not assumed that the system will only handle numbers with the local prefix (i.e. +356).
