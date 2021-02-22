package stepDefinitionsBackupPackage;
/* With this version we started structuring the code for Framework level. Below the modifications implemented:
 * - Create the "resources" package in "src/test/java"
 * - Moving the pojo serialization to the TestDataBuilder.java class in the "resources" package:
 * 			. In this code created the global variable "data" of type TestDataBuilder.
 *          . In method "@Given("The AddPlace Payload")" 
 *          	 + changed statement ".body()" to use the method "data.setTheAddPlacePojo()"
 * - Moving the RequestSpecification and ResposeSpecification code to to the UtilityCodes.java class in the "resources" package:
 *          . Modify the declaration of the "StepsDefinition" class to "extends" the UtilityCodes.java class as a parent, this 
 *            allow to use all the methods in the UtilityCodes.java class without the need of creating object of this class, for
 *            example the method getCommonRequestSpecifications().
 * 			. In this code created the global variables "requetSpec" of type RequestSpecification,
 *                                                      "responseSpecBuilder" of type ResponseSpecification.
 *          . In method "@Given("The AddPlace Payload")" 
 *          	 + changed statement ".spec()" to use the method "getCommonRequestSpecifications()"    
 *          . In method  @When("The client calls {string} API with post http request")             
 *               + changed statement ".spec()" to use the method "getCommonResponseSpecifications()" 
 * - Started using the global.properties file to define all project related global variables.
 * 			. Created the "Global.properties" files, containing the "baseUrl" property, in the resources package.
 *          . Created method getGlobalProptValues(String parmProprty) in the UtilityCodes.java class in the "resources" package.
 *          . Modified the UtilityCodes.java for the method getCommonRequestSpecifications() to use the baseURL from the global
 *            properties file: changed ".setBaseUri("https://rahulShetyAcademy.com")" to ".setBaseUri(getGlobalProptValues("baseUrl"))"
 * - Implement logging to log all the Request and Response Results and JSON detail into the "logging.txt" file.  
 * 			. Modified the UtilityCodes.java for the method getCommonRequestSpecifications() to insert the logging commands.
 * 				 + Defined/declare the file to contains the log with: PrintStream logFile = new PrintStream(new FileOutputStream("logging.txt"));
 * 				 + Modified the statements to build by adding these:
 * 						 .addFilter(RequestLoggingFilter.logRequestTo(logFile))   //This logs all the API Requests executed.
 *       			     .addFilter(ResponseLoggingFilter.logResponseTo(logFile)) //This logs all the API Responses returned.          
 *
 * */

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import resources.TestDataBuilder;
import resources.UtilityCodes;

public class StepsDefinition210212 extends UtilityCodes {  //By using "extends" we are making the UtilityCodes class the parent of the StesDefinition class, the
	         // advantage of this is that we can use, through INHERITANCE, all the methods in the UtilityCodes class without the need to create an 
	         // object of type UtilityCodes class. this class is imported as "import resources.UtilityCodes;"
             // this is a good implementation because the UtilityCodes class will contains several reusable utility codes.
	
		String wkPlaceId = "";  //global to be used in multiple tests.
		String wkKey = "qaclick123";    // Initialize the value for the key parameter
		RequestSpecification requetSpec;    //this variable only contain PART of code for the RestAssure Request: common code (in req01 global variable) and 
				 // the serialized body (in variable postRequestBody )
		ResponseSpecification responseSpecBuilder; // Using specBuilder for the common code of the Response (in the global "responseSpecBuilder" variable of type ResponseSpecification)
		Response respnRaw; //as global variable respnRaw will hold the Response in Raw format 
		TestDataBuilder data = new TestDataBuilder();  // Global variable to access the methods with Serialization to build the pojos to be used for JSON body to be used in the 
													   // Request, for example addPlacePojo() method that create the pojo for JSON to be used in the AddPlace Request.
		String wkName, wkAddress, wkLanguage, wkAccuracy;  // to make or receive parameters as global.

		/*    Commented old version ****		
		@Given("The AddPlace Payload")
		public void the_add_place_payload() throws IOException {
		    // Write code here that turns the phrase above into concrete actions
			// API information:  Using SpecificationBuilder defined above global section for Request and Response, see METHOD #1 and METHOD #2
			// * Google Maps Add API (POST): 
			//   	This API Will add new place into Server
	 		//	Complete URL : https://rahulshettyacademy.com/maps/api/place/add/json?key= qaclick123
	 		//	Base URL :  https://rahulshettyacademy.com 
			//	Resource : /maps/api/place/add/json
			//	Query Parameters: key. *** Note : Key value is hard coded and it is always "qaclick123"  ***
			//	Http Request Method : POST
			
			// 2- build the RestAssured Request using "spec builder" and the serialized JSON payload.  
			    //  ** 01: Below was modified for Framework structure **
			    requetSpec =    // this global variable (requetSpec) of type RequestSpecification, contain PART of code for the RestAssure Request, 
						        // the common code using the method getCommonRequestSpecifications() and the serialized body using method data.setTheAddPlacePojo()
					given()
                       .spec(getCommonRequestSpecifications())  // Using specBuilder for the common code of the Request. the method getCommonRequestSpecifications()
                                                                // return a variable of type RequestSpecification.
                       											// this method is available here through INHERITANCE from the extends of the UtilityCodes class in this
                       											// program, see "public class StepsDefinition extends UtilityCodes {..." therefore we don't 
                       											// need to create an object of type UtilityCodes to have access to this method.
                      // .log().all()
                       .body(data.setTheAddPlacePojo());  //Using the pojo, to serialized JSON body or Payload, from TestDataBuilder method setTheAddPlacePojo().
			          **** Note: The above error is because for this method parameters were introduced in version-210215
			          
		}
*/
		
		@When("The client calls {string} API with post http request")
		public void the_client_calls_api_with_post_http_request(String parmAPIName) {
		//  ** 01: Below was modified for Framework structure **
		    // Use the Specification Builder "requestSpec" (created in @Given method) and "getCommonResponseSpecifications()" to structure the Call for  
			// the RestAssured API and extract the response in Raw format. 
			respnRaw = requetSpec  //This is the complete RestAssure call using the global SpecBuilder variable "requetSpec" of type RequestSpecificatin
				.when()            //  and the ResponseSpecifications using the method getCommonResponseSpecifications() 
			    	.post("/maps/api/place/"+parmAPIName+"/json")  
				.then()
				   .spec(getCommonResponseSpecifications()) // Using specBuilder for the common code of the Request. the method getCommonResposeSpecifications()
									                        // return a variable of type ResponseSpecification.
															// this method is available here through INHERITANCE from the extends of the UtilityCodes class in this
															// program, see "public class StepsDefinition extends UtilityCodes {..." therefore we don't 
															// need to create an object of type UtilityCodes to have access to this method.
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
			assertEquals("** Failed Response JSON attribute validation for attribute: \""+parmJSONAttribute+"\", ",parmExpectedValue, respInJson.get(parmJSONAttribute).toString());			 					    
		}
		
		
}
