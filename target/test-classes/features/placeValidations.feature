Feature: Validate Place API
# This is the original version
# Version 200215: Introduces DataDriven Scenario using Parameters (<address>,<language>..) so that
#                 we can run the Tests with multiple set of data And reuse the same parameter in different
#									@Given, @When, @Then, @And and @ But...

@AddPlaceTest
Scenario Outline: Verify if a Place is Successfully Added using AddPlace API
			Given The AddPlace Payload with "<name>" "<address>" "<language>" <accuracy>   
			# <accuracy> is of dataType "int" all the others are String because of enclosed in "" ("<address>")
			When The client calls "AddPlaceAPI" API with "POST" request http method
			Then The API call is successful with status code 200
			And The "status" in the response body is "OK"
			And The "scope" in the response body is "APP"
			And With created place_id and "GetPlaceAPI" verify returned name matches "<name>" provisioned
			And The "address" in the response body is "<address>" 
			And The "language" in the response body is "<language>"
			And The "accuracy" in the response body is "<accuracy>"
			And The "phone_number" in the response body is "1-995-541-5858"
			And The "location.latitude" in the response body is "52.0124"
			And The "location.longitude" in the response body is "3.254"

# Examples must be in plural.
Examples: 
		| name			| address				| language	| accuracy|
#		|Juan Carlos|154 Apple Dr		|Spanish		|65       |
#		|Johnson Mary|4184 Broad Way|English		|41       |
		|Susane			|6154 Grape Creeck|Spanish	|25       |


# Below introduced in version 210216:
@GetPlaceTest @Regression
Scenario Outline: Verify if the GetPlaceAPI functionality is working successfuly
# This Scenario/Test only can be run for the last row in the AddPlaceAPI Scenario, because it run 3 times and when finish then start the
# execution of this Scenario, therefore the "place_id" used will be for the last row added, so if here we test for the first two row then
# we will have the error. "org.junit.ComparisonFailure: ** Failed Response JSON attribute validation for 
#                          attribute: "name",  expected:<[Johnson Mary]> but was:<[Susane]>"
			Given A valid GetPlaceAPI Request
			When The client calls "GetPlaceAPI" API with "GET" request http method
			Then The API call is successful with status code 200
			And The "name" in the response body is "<name>" 
			And The "address" in the response body is "<address>"
			And The "language" in the response body is "<language>"
			And The "accuracy" in the response body is "<accuracy>"
			And The "phone_number" in the response body is "1-995-541-5858"
			And The "location.latitude" in the response body is "52.0124"
			And The "location.longitude" in the response body is "3.254"

Examples: 
		| name			| address				| language	| accuracy|
#		|Juan Carlos|154 Apple Dr		|Spanish		|65       |
#		|Johnson Mary|4184 Broad Way|English		|41       |
		|Susane			|6154 Grape Creeck|Spanish	|25       |
		
			
@DeletePlace
Scenario: Verify if the DeletePlace API functionality is working successfuly
			Given A valid DeletePlace Payload
			When The client calls "DeletePlaceAPI" API with "POST" request http method
			Then The API call is successful with status code 200
			And The "status" in the response body is "OK"