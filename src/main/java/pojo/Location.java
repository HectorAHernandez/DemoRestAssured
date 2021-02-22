package pojo;

public class Location {
	/*   **** Note: @JsonProperty
     *  The @JsonProperty annotation is used to map property names with JSON keys during serialization and deserialization. By default, if you try
     *  to serialize a POJO, the generated JSON will have keys mapped to the fields of the POJO. If you want to override this behavior, you can 
     *  use the @JsonProperty annotation on the fields. It takes a String attribute that specifies the name that should be mapped to the field 
     *  during serialization.
     *  You can also use this annotation during deserialization when the property names of the JSON and the field names of the Java object 
     *  do not match, but also we can change the name of the field in the pojo or add the new JSON attribute in the pojo as below for latitude and longitude*/
	
	
	private double lat;
	private double lng;

	private String latitude;    //These are to handle the PlaceResponse.java pojo During deserialization of the GetPlace API. Note that
	private String longitude;   // they have different name and data type than the lat and lng used in the serialization of the AddPlace
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public String getlatitude() {              //These are to handle the PlaceResponse.java pojo During deserialization of the GetPlace API. Note that
		return latitude;					   // they have different name and data type than the lat and lng used in the serialization of the AddPlace	
	}
	public void setlatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getlongitude() {
		return longitude;
	}
	public void setlongitude(String longitude) {
		this.longitude = longitude;
	}
 
	
}
