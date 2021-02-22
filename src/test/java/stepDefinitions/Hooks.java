package stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;

public class Hooks {
	// Create an object of the StepsDefinition.java class so that we can use the method defined in the StepsDefinition.java for 
	// all @Given, @When, @Then...
	StepsDefinition implementedStepsDef = new StepsDefinition();

	
	@Before("@DeletePlace")
	public void beforeDeleteScenario() throws IOException {

		// Write the code to generate the static global variable "wkPlaceId". This variable must be initialized with the value "null" for the if () here. 
		// insert if () to execute the code only when place_id = null
		// if (implementedStepsDef.wkPlaceId==null) {  // because the wkPlaceId is an "static" (global) variable it is better access it with
		// the name of the level class where it is defined ("StepsDefinition.wkPlaceId") instead of using the object level with 
		// "implementedStepsDef.wkPlaceId", so to avoid the warning message we have change to the class level as below:
		if (StepsDefinition.wkPlaceId==null) {
			// Use the methods in the StepsDefinition object to execute the needed steps.
			implementedStepsDef.the_add_place_payload_with_name_address_language_accuracy("Hector Lopez", "16 Main Street ", "korean", 58);
			implementedStepsDef.the_client_calls_api_with_request_http_method("AddPlaceAPI", "POST");
			implementedStepsDef.with_created_place_id_and_verify_returned_name_matches_provisioned("GetPlaceAPI", "Hector Lopez");
			System.out.println("*****  EXECUTED THE BEFORE-DeletePlace Hooks ****");
		}
	}
	
	@Before("@GetPlaceTest")
	public void beforeGetScenario() throws IOException {
		
		if (StepsDefinition.wkPlaceId==null) {
    // The first and second call must use the values in the last row processed in the "Example:" section in Scenario @AddPlaceTest, name = Susane 
			implementedStepsDef.the_add_place_payload_with_name_address_language_accuracy("Susane", "6154 Grape Creeck", "Spanish", 25);
			implementedStepsDef.the_client_calls_api_with_request_http_method("AddPlaceAPI", "POST");
			implementedStepsDef.with_created_place_id_and_verify_returned_name_matches_provisioned("GetPlaceAPI", "Susane");
			System.out.println("*****  EXECUTED THE BEFORE-GetPlaceTest Hooks ****");
		}
		
	}

}
