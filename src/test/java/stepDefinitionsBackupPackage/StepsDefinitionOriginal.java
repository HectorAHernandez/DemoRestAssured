package stepDefinitionsBackupPackage;
/* Version Original Raw version of the code. Contains the following:
 *  - The code to build the pojo to be used in the body().
 *  - The code to define RequestSpecification and ResponseSpecfication using SpecificationBulder() method.
*/

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

public class StepsDefinitionOriginal {

		String wkPlaceId = "";  //global to be used in multiple tests.
		String wkKey = "qaclick123";    // Initialize the value for the key parameter
		RequestSpecification requetSpec;    //this variable only contain PART of code for the RestAssure Request: common code (in req01 global variable) and 
				 // the serialized body (in variable postRequestBody )
		ResponseSpecification respSpecBuilder; // Using specBuilder for the common code of the Response (in the global "respSpecBuilder" variable of type ResponseSpecification)
		Response respnRaw; //as global variable respnRaw will hold the Response in Raw format 
		
		@Given("The AddPlace Payload")
		public void the_add_place_payload() {
		    // Write code here that turns the phrase above into concrete actions
			// API information:  Using SpecificationBuilder defined above global section for Request and Response, see METHOD #1 and METHOD #2
			// * Google Maps Add API (POST): 
			//   	This API Will add new place into Server
	 		//	Complete URL : https://rahulshettyacademy.com/maps/api/place/add/json?key= qaclick123
	 		//	Base URL :  https://rahulshettyacademy.com 
			//	Resource : /maps/api/place/add/json
			//	Query Parameters: key. *** Note : Key value is hard coded and it is always "qaclick123"  ***
			//	Http Request Method : POST
				   
			//  1- To build the JSON body to be used in the Request:
			//     1.1 Create an object of the pojo class AddPlace.java.
			    AddPlace postRequestBody = new AddPlace(); // ** For Framework structure,  this can be moved as a global variable to be used in all other requests  
			                 //       and be preparing the condition for when building the Framework (See project "RestAssured_Maven_Cucumber_BDD_Framework_E2E")
			//     1.2 using the setters to move the values to the corresponding java objects.
			    postRequestBody.setAccuracy(45);
			    postRequestBody.setAddress("4512 Sky blue");
			    postRequestBody.setLanguage("English");
			    
			    // set the values for the embedded Location childJSON
			    Location wkLocation = new Location();
			    wkLocation.setLat(52.0124);
			    wkLocation.setLng(3.254);
			    postRequestBody.setLocation(wkLocation);
			    
			    // continue with manual setting values:
			    postRequestBody.setName("Filantropica");
			    postRequestBody.setPhone_number("1-995-541-5858");
			    
			    // set the value for the List<String> datatype of the JSON "types" attribute:
			    List<String> wkTypesList = new ArrayList<String>();  // *** The List<String> type must be instantiated in the new as "ArrayList<String>()"
			    wkTypesList.add("Red Shoes");
			    wkTypesList.add("Flower");
			    wkTypesList.add("Car Small");
			    
			    postRequestBody.setTypes(wkTypesList);
			    
			 // continue with manual setting values:
			    postRequestBody.setWebsite("https://rahulshettyacademy.com");		        
			
			// 2- build the RestAssured Request using "spec builder" and the serialized JSON payload.   
				
			    // *** METHOD # 1: Using an object of type RequestSpecification (based on the global variable requetSpec), for only the request part of the 
			    //RestAssure Request code, and separated from the .when().post(....) ANd then joining it to get the Response respn. This gives flexibility 
			    // to play with the request but it is not needed because we can create it together as in METHOD # 2. Method #2 is my favorite one when the 
			    // project is not in a Framework. If so, use #1.  
				//RestAssured.baseURI="https://rahulshettyacademy.com"; not needed because it is included in the "req01" SpecBuilder defined in global variable level line 74.
			
				RequestSpecification req01 = new RequestSpecBuilder()  // Build variable "req01" to contain all the common codes in the Request specification,
						                                               // this allow to break the whole RestAssure request into multiple pieces to be used in the multiple 
						  											   // methods to implement the @Given, @When...
						.setBaseUri("https://rahulshettyacademy.com")
						.addQueryParam("key", wkKey)  //Only use addQueryParam to avoid below error.
					  //  .addParam("key", "qaclick123")  Never use "addParm" because it will generate error "You can either send parameters OR body content in the POST, not both!"
					    .setContentType(ContentType.JSON)
					    .build();

			           //RequestSpecification requetSpec =    //this variable only contain PART of code for the RestAssure Request: common code (in req01 global variable) and 
					    requetSpec =    //this global variable only contain PART of code for the RestAssure Request: common code (in req01 global variable) and
								                             // the serialized body (in variable postRequestBody ** this can be modified for Framework )
							given()
	                           .spec(req01)      // Using specBuilder for the common code of the Request (in the global "req01" variable of type RequestSpecification)
	                          // .log().all()
	                           .body(postRequestBody);  //Using the serialized JSON body. 

		}

		
		@When("The client calls {string} API with post http request")
		public void the_client_calls_api_with_post_http_request(String parmAPIName) {
		
			// Using specBuilder for the common code of the Response (in the global "respSpecBuilder" variable of type ResponseSpecification)
			respSpecBuilder = new ResponseSpecBuilder().expectStatusCode(200)  // Build variable "respSpecBuilder" to contain all the common codes in the 
					                   // Response Specification, this allow to break the whole RestAssure request into multiple pieces to be used in the  
					                   // multiple methods to implement the @Given, @When...
						.expectContentType(ContentType.JSON)
						.build(); 
			
			respnRaw = requetSpec  //This is the complete RestAssure call using the global SpecBuilder variables for: the req01 (in requetSpec) and resp02.
					.when()              // To be used here, the requetSpec variable must be of type RequestSpecificatin.
				    	.post("/maps/api/place/"+parmAPIName+"/json")  
					.then()
					   .spec(respSpecBuilder)  // Using specBuilder for the common code of the Response (in the global "respSpecBuilder" variable of type ResponseSpecification)
					 //  .log().all()
                    .extract().response();
		}
		
		
		@Then("The API call is successful with status code {int}")
		public void the_api_call_is_successful_with_status_code(int parmIntStatusExpectedValue) {
		    // Assert the Raw Response StatusCode with getStatusCode() method (int type) and compare with the received expected status.
			assertEquals(respnRaw.getStatusCode(), parmIntStatusExpectedValue);				    
		}
		
		@Then("The {string} in the response body is {string}")
		public void the_in_the_response_body_is(String parmJSONAttribute, String parmExpectedValue) {
		    // Convert the Raw Response to JSON so that we can access the attributes in the JSON response.
			String respInString = respnRaw.asString();
			JsonPath respInJson = new JsonPath(respInString);
			// Assert the expected value of the indicated JSON attribute in the Response received.
			assertEquals("** Indicated attribute:",parmExpectedValue, respInJson.get(parmJSONAttribute).toString());			 					    
		}
		
		
}
