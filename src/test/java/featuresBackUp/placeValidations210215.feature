Feature: Validate Place API
# This is the original version
# Version 200215: Introduces DataDriven Scenario using Parameters (<address>,<language>..) so that
#                 we can run the Tests with multiple set of data And reuse the same parameter in different
#									@Given, @When, @Then, @And and @ But...
Scenario Outline: Verify if a Place is Successfully Added using AddPlace API
Given The AddPlace Payload with "<name>" "<address>" "<language>" <accuracy>   
# <accuracy> is of dataType "int" all the others are String because of enclosed in "" ("<address>")
When The client calls "AddPlaceAPI" API with "DELETE" request http method
Then The API call is successful with status code 200
And The "status" in the response body is "OK"
And The "scope" in the response body is "APP"
And With created place_id and "GetPlaceAPI" verify returned name matches "<name>" provisioned 

# Examples must be in plural.
Examples: 
		| name			| address				| language	| accuracy|
		|Juan Carlos|154 Apple Dr		|Spanish		|65       |
#		|Johnson Mary|4184 Broad Way|English		|41       |
#		|Susane			|6154 Grape Creeck|Spanish	|25       |