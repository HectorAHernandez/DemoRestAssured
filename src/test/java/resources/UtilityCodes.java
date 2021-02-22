package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UtilityCodes {
	
	public static RequestSpecification reqCommonSpecs; // defined as global to contain all the specifications that are common to all Requests.
	      // Changed as "public static", to indicate to Java Run Time environment to use a single instance of the reqCommonSpecs variable throughout the
	      // entire execution of the StepsDefinition.java during the TestRunning.java running And making the value available to all programs/classes.
	public static ResponseSpecification responseCommonSpecs; // defined as global to contain all the specifications that are common to all Requests.
	
	public RequestSpecification getCommonRequestSpecifications() throws IOException {
		
		if (reqCommonSpecs == null) {  // to control the creation of the of enclosed code ONLY ONE TIME, this avoid replaced the created the logging.txt file
            // every time that a request is executed, either by every new set of test data or RestAssured Request in the "StepsDefinition.java"
			PrintStream logFile = new PrintStream(new FileOutputStream("logging.txt")); //To log to a separate file we have to create an object
															//of type PrintStream and then replace it by the stream argument in line 31.
		                                                 // the "logging.txt" file will be created at the level of the pom.xml file.
		
			reqCommonSpecs = new RequestSpecBuilder()  // Build variable "reqCommonSpecs" to contain all the common codes in the Request specification,
										               // this allow to break the whole RestAssure request into multiple pieces to be used in the multiple 
													   // methods to implement the @Given, @When...        
					.setBaseUri(getGlobalProptValues("baseUrl"))
					.addQueryParam("key", "qaclick123")  //Only use addQueryParam to avoid below error.
				  //  .addParam("key", "qaclick123")  Never use "addParm" because it will generate error "You can either send parameters OR body content in the POST, not both!"
				    .setContentType(ContentType.JSON)
				    .addFilter(RequestLoggingFilter.logRequestTo(logFile))   //This logs all the API Requests executed.
				    .addFilter(ResponseLoggingFilter.logResponseTo(logFile)) //This logs all the API Responses returned.
				    .build();
		}
		return reqCommonSpecs;
	}
	
	
	public ResponseSpecification getCommonResponseSpecifications() {
		responseCommonSpecs = new ResponseSpecBuilder()  // Build variable "responseCommonSpecs" to contain all the common codes in the Response specification,
									               // this allow to break the whole RestAssure request into multiple pieces to be used in the multiple 
												   // methods to implement the @Given, @When... 
				.expectStatusCode(200) 
				.expectContentType(ContentType.JSON)
				.build(); 
		return responseCommonSpecs;
	}
	
	public static String getGlobalProptValues(String parmProprty) throws IOException {
		//Note: this method has been defined as "static" to allow to be used, in this class (see line #30), from other methods without the need to create an
		// object of this class. The other way to avoid creating object of a class is by making the class a parent of the calling class by using the
		// "extends" keyword in the calling class definition. 
		Properties proptyFile = new Properties();   // Dedine property file to be used.
		FileInputStream fileLocation = new FileInputStream("C:\\Eclipse\\Work_Area\\RestAssured_Maven_Cucumber_BDD_Framework_E2E\\src\\test\\java\\resources\\Global.properties");
		proptyFile.load(fileLocation);    //Load the file from the indicated location and make all property value availabe by using the getProperty() method.
		return proptyFile.getProperty(parmProprty); // Return the value of the proporty requested in the parmProprty.
	}
	
	public String getJsonAttributeValue(Response parmRespnse, String parmAttributeName) {
		// this utility returns the value of a given parmAttributeName in a JSON response in Raw format (parmResnse)
		String respsInString = parmRespnse.asString();
		JsonPath respsInJson = new JsonPath(respsInString);
		return respsInJson.get(parmAttributeName).toString();	
	}

}
  