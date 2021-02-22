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
 * 	Version 200215: Introduces DataDriven Scenario using Parameters ("<address>","<language>"..) so that we can run the Tests with multiple set of data And
 *                  reuse the same parameter in different @Given, @When, @Then, @And and @ But... To accomplish this we have to 
 *                  modify the code as follow:
 *                  	- Insert the parameter in the .feature file:
 *                  			+ Modify the Scenario command to include Outline, example "Scenario Outline:"
 *                  			+ Insert the needed parameters: "<name>" "<address>" "<language>" <accuracy>   
 *                                    Note: <accuracy> is of dataType "int" all the others are String because of enclosed in "" ("<address>").
 *                                    The parameters are indicated by using <> and the data type of each parameter is indicated by the use or 
 *                                    not use of the "", for example, "<xxx>" define as String, and <xxx> indicate numeric and then we can indicate as 
 *                                    int or double.
 *                  			+ Insert the values for each parameter in the created "Examples:" section
 *                  				Example: 
										| name		| address			| language	| accuracy|
										|Juan Carlos|154 Apple Dr		|Spanish	|65       |
 *						- Run the TestRunner.java to generate the error then copy the suggested snippet code.
 *					    - Modify the affected @xxx name  with the copied snippet code and modify it to accept each one of the defined parameters.
 *						- To make the received parameters available to all methods define Global variable (wkXXXX) for each of then a populate them 
 *                        as soon as the parameters are received. 										
 *                  	- In the TestDataBuilder.java class, UPDATE, as needed, all methods to handled receiving data from the new parameters.
 *                  	  For example: setTheAddPlacePojo() to setTheAddPlacePojo(String parmName, String parmAddress, String parmLanguage, int parmAccuracy)		                
 *                      - Update the .body() commands to use the modified method: example .body(data.setTheAddPlacePojo(wkName, wkAddress, wkLanguage, wkAccuracy));
 *                      
 *                  After parameterization, to make the Scenario to run with MULTIPLE SET OF DATA we have to just insert additional lines in the "*.feature" file and 
 *                  modify the SpecBuilder Utility method that create the logging.txt file (this is UtilityCodes.getCommonRequestSpecifications()) to create the log file
 *                  only the first time it is executed (reqCommonSpecs == null) so that it accumulate the log for each data set ran. Following the code modifications:
 *                  	- Insert the if (reqCommonSpecs == null) {}. to control the execution of the of enclosed code ONLY ONE TIME, this avoid replaced the created logging.txt file
			              every time that a request is executed, either by every new set of test data or RestAssured Request in the "StepsDefinition.java"
 *                      - Change the variable for reqCommonSpecs as "public static", to indicate to Java Run Time environment to use a single instance of the reqCommonSpecs variable throughout the
 *                        entire execution of the StepsDefinition.java during the TestRunning.java running And making the value available to all programs/classes.
 *                        This is, before the first time running it will be == null after running, for any subsequence run, it will be change to NOT-null: 
 *                        		+ public static RequestSpecification reqCommonSpecs; // defined as global to contain all the specifications that are common to all Requests. Also changed the 
 *                                other one "public static ResponseSpecification responseCommonSpecs;"
 *                   
 *                   Use "Enum" java class to define constants and methods to centralize the definition of all Request's HTTP method Resources details (/maps/api/place/add/json)
 *                   in el central location for easy of maintenance:
 *                   	- In the resources package, create a new class for the Enum class with name APIHttpResourcesEnum.java to define all Resource constant needed and the 
 *                        constructor method: APIHttpResourcesEnum(String parmResource) and the getter method: getResource().
 *                      - In the StepsDefinition.java program, modify the @When method to use the "resource" constant defined Enum class:
			                   + Create the object "reqstResource" of Enum type APIResource  and assign the value of the CONSTANT defined for APIName indicate in
								  the String parameter of this @When... method. And:
								    Modify the HTTP Request (.post(...) to use the getResource() method defined in the Enum class)
								    Modify the "*.feature" file to use the CONSTANTs defined in the Enum class.

					 Continue building the EndToEnd code by adding the use of the key (place_id) from the just added PLACE and the getPlaceAPI to 
					 verify that the data inserted matches the data provisioned as input in the "Examples: " section of the "*.feature" file. Using
					 the GetPlaceAPI. To implement this below was done:
					     - The Scenario in .feature file was modified with this validation: "And With created place_id and "GetPlaceAPI" verify returned name matches "<name>" provisioned"
					     - The corresponding code was created in the StepsDefinition.java under this method:
					           " @Then("With created place_id and {string} verify returned name matches {string} provisioned")
		                         public void with_created_place_id_and_verify_returned_name_matches_provisioned(String parmAPIName, String parmFromInputExpected) throws IOException {"					     
					     - Created the GENERIC method "getJsonAttributeValue(parmRespnse, parmAttributeName)" to get the value of any attribute from a
					       Response in JSON Raw format.
					     - Get the value of the just added "place_id" attribute, using the new GENERIC method.To be used in the GET Request.
					     - Prepare the global variable "requestSpec" to use the GetPlaceAPI Request using the method created for the @When.. keyword.
					       Adding a queryParameter to received the "place_id" value to search with the GET; as follow:
					            requetSpec = 
									given()
							           .spec(getCommonRequestSpecifications())
									   .queryParam("place_id", wkPlaceId);      						    
						  - Reuse the call API method created for the implementation of the @When keyword:
						    the_client_calls_api_with_request_http_method(parmAPIName, "GET"); the GET Http method is hard coded.
						  - Get the actual Value of the attribute to assert from the respnRaw and then assert it against the expected value from 
						    the Scenario "Examples:" section.
						  - Modified the validation implemented in "@Then("The {string} in the response body is {string}")" to use the 
						    new GENERIC method "getJsonAttributeValue(parmRespnse, parmAttributeName)" 

  tO DELTE LATER.   210216 Add more Tests and implement "Tagging" mechanism to run selected Tests from the "TestRunner.java" file.						    
 * */

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIHttpResourcesEnum;
import resources.TestDataBuilder;
import resources.UtilityCodes;

public class StepsDefinition210215 extends UtilityCodes {  //By using "extends" we are making the UtilityCodes class the parent of the StesDefinition class, the
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
		String wkName, wkAddress, wkLanguage;
		int wkAccuracy;  // to make or receive parameters as global.
		
		JsonPath respInJson; //** 210215 Moved to global the declaration of "JsonPath respInJson" so that it can be used from any where and the 
		                     // values of the attributes can be obtaining using the  Utility method getJsonAttributeValue()
		
		@Given("The AddPlace Payload with {string} {string} {string} {int}")
		public void the_add_place_payload_with_name_address_language_accuracy(String parmName, String parmAddress, String parmLanguage, int parmAccuracy) throws IOException {
		    // Write code here that turns the phrase above into concrete actions
			// API information:  Using SpecificationBuilder defined above global section for Request and Response, see METHOD #1 and METHOD #2
			// * Google Maps Add API (POST): 
			//   	This API Will add new place into Server
	 		//	Complete URL : https://rahulshettyacademy.com/maps/api/place/add/json?key= qaclick123
	 		//	Base URL :  https://rahulshettyacademy.com 
			//	Resource : /maps/api/place/add/json
			//	Query Parameters: key. *** Note : Key value is hard coded and it is always "qaclick123"  ***
			//	Http Request Method : POST
			
			// 1- Move all received parameter to global variables:
			wkName = parmName;
			wkAddress = parmAddress;
			wkLanguage = parmLanguage;
			wkAccuracy = parmAccuracy;
			
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
                       .body(data.setTheAddPlacePojo(wkName, wkAddress, wkLanguage, wkAccuracy));  //Using the pojo, to serialized JSON body or Payload, from TestDataBuilder method setTheAddPlacePojo(). 
		}

		
		@When("The client calls {string} API with {string} request http method")
		public void the_client_calls_api_with_request_http_method(String parmAPIName, String parmHTTPMethod) {

		//  ** 01: Below was modified for Framework structure **
		// **** 210215 :	
			// Create the object "reqstResource" of Enum type APIResource  and assign the value of the CONSTANT defined for APIName indicate in
			// the String parameter of this @When... method. And:
			// Modify the HTTP Request (.post(...) to use the getResource() method defined in the Enum class)
			// Modify the "*.feature" file to use the CONSTANTs defined in the Enum class.
			APIHttpResourcesEnum reqstResource = APIHttpResourcesEnum.valueOf(parmAPIName); // By using this valueOf() method it will automatically trigger the execution of
			                                           // the APIResource(String parmAPIName) method which will load the value of the CONSTANT indicated
			                                           // by the parmAPIName parameter into the "resource" variable defined in the Enum class and therefore
			                                           // making it available to be returned wherever we use the APIHttpResourcesEnum/reqstResource.getResource() method.
			System.out.println("***** API resource for "+parmAPIName+" is: "+reqstResource.getResource()); // print the API resource corresponding to the received API name.
			
			
		// **** 210215 :
			// Make the code more generic or smart to dynamically handle the API HTTP Method related to the APIResource Indicated:
            //	- Modify the "*.feature" file to have the HTTP Method as a parameter to be passed in the @When... keyword, example:
			//           "When The client calls "AddPlaceAPI" API with "post" request http method"
			//  - Ran the TestRunning.java to generate the error, copy and paste here the suggested code snippet
			//           "@When("The client calls {string} API with {string} request http method")
			//            public void the_client_calls_api_with_request_http_method(String parmAPIName, String parmHTTPMethod) {"
			//  - Inserted if statement: if-then-else structure (parmHTTPMethod.equalsIgnoreCase("HTTPMethod")) {..} to determine which HttpMethod to execute:

			if (parmHTTPMethod.equalsIgnoreCase("POST")) {			
		// **** 210212 :	
			// Use the Specification Builder "requestSpec" (created in @Given method) and "getCommonResponseSpecifications()" to structure the Call for  
			// the RestAssured API and extract the response in Raw format. 

				respnRaw = requetSpec  //This is the complete RestAssure call using the global SpecBuilder variable "requetSpec" of type RequestSpecificatin
					.when()            //  and the ResponseSpecifications using the method getCommonResponseSpecifications() 
				    	.post(reqstResource.getResource())  // **** 210215 : Modified to use the Enum class to get the corresponding Resource for the parmAPIName.
					.then()
					   .spec(getCommonResponseSpecifications()) // Using specBuilder for the common code of the Request. the method getCommonResposeSpecifications()
										                        // return a variable of type ResponseSpecification.
																// this method is available here through INHERITANCE from the extends of the UtilityCodes class in this
																// program, see "public class StepsDefinition extends UtilityCodes {..." therefore we don't 
																// need to create an object of type UtilityCodes to have access to this method.
					 //  .log().all()
	                .extract().response();		
			}
			else if (parmHTTPMethod.equalsIgnoreCase("PUT")) { // **** 210215 : Inserted to make the code smarter by using the parameter parmHTTPMethod.
				respnRaw = requetSpec  
						.when()        
					    	.put(reqstResource.getResource())  // **** 210215 : Modified to use the Enum class to get the corresponding Resource for the parmAPIName.
						.then()
						   .spec(getCommonResponseSpecifications()) 
						 //  .log().all()
		                .extract().response();										
			}
			else if (parmHTTPMethod.equalsIgnoreCase("DELETE")) { // **** 210215 : Inserted to make the code smarter by using the parameter parmHTTPMethod.
				respnRaw = requetSpec  
						.when()        
					    	.post(reqstResource.getResource())  // **** 210215 : Modified to use the Enum class to get the corresponding Resource for the parmAPIName.
						.then()
						   .spec(getCommonResponseSpecifications()) 
						 //  .log().all()
		                .extract().response();														
			}
			else if (parmHTTPMethod.equalsIgnoreCase("GET")) { // **** 210215 : Inserted to make the code smarter by using the parameter parmHTTPMethod.
				respnRaw = requetSpec
						.when()        
					    	.get(reqstResource.getResource())  // **** 210215 : Modified to use the Enum class to get the corresponding Resource for the parmAPIName.
						.then()
						   .spec(getCommonResponseSpecifications()) 
						 //  .log().all()
		                .extract().response();														
			}
		}
		
		
		@Then("The API call is successful with status code {int}")
		public void the_api_call_is_successful_with_status_code(int parmIntStatusExpectedValue) {
		    // Assert the Raw Response StatusCode with getStatusCode() method (int type) and compare with the received expected status.
			assertEquals(respnRaw.getStatusCode(), parmIntStatusExpectedValue);				    
		}
		
		@Then("The {string} in the response body is {string}")
		public void the_in_the_response_body_is(String parmJSONAttribute, String parmExpectedValue) {
		    // Convert the Raw Response to JSON so that we can access the attributes in the JSON response.
	    	// old:	String respInString = respnRaw.asString(); 
			// old:	respInJson = new JsonPath(respInString); //** 210215 Moved to global the declaration of "JsonPath respInJson"
			// old:	wkPlaceId = respInJson.getString("place_id");    // Save value of attribute "place_id" in global variable to be used later
			
			//** 210215 New version of getting the attribute value using the new utility method "getJsonAttributeValue(parmRespnse, parmAttributeName)"
			
			// Assert the expected value of the indicated JSON attribute in the Response received.
			assertEquals("** Failed Response JSON attribute validation for attribute: \""+parmJSONAttribute+"\", "
					         ,parmExpectedValue, getJsonAttributeValue(respnRaw, parmJSONAttribute)); // 210215: new using the Utility method
	            	   //    ,parmExpectedValue, respInJson.get(parmJSONAttribute).toString());	//Original method		 					    
		}
		
		@Then("With created place_id and {string} verify returned name matches {string} provisioned")
		public void with_created_place_id_and_verify_returned_name_matches_provisioned(String parmAPIName, String parmFromInputExpected) throws IOException {
			
			// 1 - get the value of the just added "place_id" attribute:
			wkPlaceId = getJsonAttributeValue(respnRaw, "place_id"); // Using the just created method "getJsonAttributeValue(parmRespnse, parmAttributeName)"
			               // get the value of the "place_id" from the Response of the AddPlaceAPI Request and save it in the global variable to be 
						   // used here and later in any other requiring it method.
			
		    // 2- Prepare the global variable "requestSpec" to use the GetPlaceAPI Request using the method created for the @When.. keyword this is the method:
			// "public void the_client_calls_api_with_request_http_method(String parmAPIName, String parmHTTPMethod)" that is going to be called here
			// to execute Request GetPlaceAPI with the "GET" Http Request method.
		    requetSpec =    // this global variable (requetSpec) of type RequestSpecification, contain PART of code for the RestAssure Request, 
            		         // the common code using the method getCommonRequestSpecifications() 
				given()
		           .spec(getCommonRequestSpecifications())  // Using specBuilder for the common code of the Request. the method getCommonRequestSpecifications()
				   .queryParam("place_id", wkPlaceId);      // Adding the value of the key attribute to get()
		    
		    // 3- Reuse the call API method created for the implementation of the @When keyword:
		    the_client_calls_api_with_request_http_method(parmAPIName, "GET"); // Executing this generic method that implement the @When keyword, with 
		                    // the GetPlaceAPI (passed in parmAPIName) and the "GET" HTTP method. FROM NOW ON the global variable "respnRaw" (Updated in 
		                    // the method execution) contains the Response in Raw JSON of the GetPlaceAPI Request; instead of the one for the AddPlaceAPI Request.
		    
		    // 4- Get the actual Value of the attribute to assert from the respnRaw and then assert it against the expected value from the Scenario Example:
		    String actualValue = getJsonAttributeValue(respnRaw, "name"); //Using the Utility getJsonAttributeValue()
		    System.out.println("parmFromInputExpected: "+parmFromInputExpected+"  actualName in Response: "+actualValue);
		    assertEquals("name validation fail: ",parmFromInputExpected, actualValue);
		    
		    // Below code is for practicing getting more value of others attributes in the respnRaw Response :
		    System.out.println("address is: "+getJsonAttributeValue(respnRaw, "address"));
		    System.out.println("Telephone Number is: "+getJsonAttributeValue(respnRaw, "phone_number"));
		    System.out.println("the types is: "+getJsonAttributeValue(respnRaw, "types"));
		    System.out.println("latitude is: "+getJsonAttributeValue(respnRaw, "location.latitude"));
		    System.out.println("logitude is: "+getJsonAttributeValue(respnRaw, "location.longitude"));
		    System.out.println("language is: "+getJsonAttributeValue(respnRaw, "language"));
		 
		}

		
}
