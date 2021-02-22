package resources;

import java.util.ArrayList;
import java.util.List;

import pojo.AddPlace;
import pojo.DeletePlace;
import pojo.Location;

public class TestDataBuilder {
	
	public AddPlace setTheAddPlacePojo(String parmName, String parmAddress, String parmLanguage, int parmAccuracy) {
		//  1- This method: 
		//     1.1 Create an object of the pojo class AddPlace.java.
		//     1.2 Uses the pojo set methods to move the data to be used in the Serialization to build the JSON body for the AddPlace Request.
		//     1.3 Return the populated pojo postRequestBody of type AddPlace.java, which is used by RestAssured to build the JSON body during 
		//         the Serialization.
		
		    AddPlace postRequestBody = new AddPlace(); // ** For Framework structure,  this can be moved as a global variable to be used in all other requests  
		                 //       and be preparing the condition for when building the Framework (See project "RestAssured_Maven_Cucumber_BDD_Framework_E2E")
		
		    //     2 using the setters to move the values to the corresponding java objects.
		    postRequestBody.setAccuracy(parmAccuracy);
		    postRequestBody.setAddress(parmAddress);
		    postRequestBody.setLanguage(parmLanguage);
		    
		    // set the values for the embedded Location childJSON
		    Location wkLocation = new Location();
		    wkLocation.setLat(52.0124);
		    wkLocation.setLng(3.254);
		    postRequestBody.setLocation(wkLocation);
		    
		    // continue with manual setting values:
		    postRequestBody.setName(parmName);
		    postRequestBody.setPhone_number("1-995-541-5858");
		    
		    // set the value for the List<String> datatype of the JSON "types" attribute:
		    List<String> wkTypesList = new ArrayList<String>();  // *** The List<String> type must be instantiated in the new as "ArrayList<String>()"
		    wkTypesList.add("Red Shoes");
		    wkTypesList.add("Flower");
		    wkTypesList.add("Car Small");
		    
		    postRequestBody.setTypes(wkTypesList);
		    
		 // continue with manual setting values:
		    postRequestBody.setWebsite("https://rahulshettyacademy.com");		
		    
		    return postRequestBody;
	}

	public DeletePlace setTheDeletePlacePojo(String parmPlaceId) {
		//  1- This method: 
		//     1.1 Create an object of the pojo class DeletePlace.java.
		//     1.2 Uses the pojo set methods to move the data to be used in the Serialization to build the JSON body for the DeletePlace Request.
		//     1.3 Return the populated pojo postRequestBody of type DeletePlace.java, which is used by RestAssured to build the JSON body during 
		//         the Serialization.
		DeletePlace postRequestBody = new DeletePlace();
		
		// Set the place_id attribute.
		postRequestBody.setPlace_id(parmPlaceId);
		
		return postRequestBody;		
	}
	
}
