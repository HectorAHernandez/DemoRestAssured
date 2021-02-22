Feature: Validate Place API
# Version 210212: This the same as the the original version
Scenario: Verify if a Place is Successfully Added using AddPlace API
Given The AddPlace Payload
When The client calls "add" API with post http request
Then The API call is successful with status code 200
And The "status" in the response body is "OK"
And The "scope" in the response body is "APP"