package resources;

public enum APIHttpResourcesEnum {
	// enum is special class in java which is used to define collections of CONSTANT values and methods to access them.
	
	// Define below constant using the format: ConstantName1("value1"), ConstantName2("value2"),... ConstantNameX("valueX"); 
	AddPlaceAPI("/maps/api/place/add/json"),       // comma ',' separated because java Run time environment will read all defined.
	DeletePlaceAPI("/maps/api/place/delete/json"), // constants as a collection.
	GetPlaceAPI("/maps/api/place/get/json"),
	UpdatePlaceAPI("/maps/api/place/update/json"); // close with semicolon ';' 
	
	private String apiResource; // to hold the value of the parameter received by the constructor method and then return it with the getResource() method.
	
	// we have to declare a Constructor of type String for this Enum class, with this rules:
	//    1- Must have the same name of the Enum class (example APIHttpResourcesEnum).
	//    2- Must accept the same number of String parameter as the constants defined, one in this case. and maybe always.
	APIHttpResourcesEnum(String parmResource) {  // just one parameter, the constructor method will always be automatically executed when the  
		                                 // Enum class method valueOf(constantName) is used. Example APIHttpResourcesEnum.valueOf(GetPlaceAPI) 
										 // Note: The constructor method does not need the "public void" only the className and the parameter,
										 //       this is only "APIHttpResourcesEnum(String parmResource) {...} " 
		this.apiResource = parmResource;		
	}
	
	public String getResource() {
		return apiResource;
	}
	
	
}
