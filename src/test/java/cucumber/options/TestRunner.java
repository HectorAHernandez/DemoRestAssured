package cucumber.options;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
//@CucumberOptions(features="src/test/java/features",  //the whole folder, could also be specific .feature file: "src/test/java/features/placeValidatons.feature"
@CucumberOptions(features="src/test/java/features/placeValidations.feature"  //the whole folder, could also be specific .feature file: "src/test/java/features/placeValidatons.feature" The package
                 ,glue= {"stepDefinitions"}
                 ,plugin = "json:target/jsonReports/hector-cucumber-report.json" //to generate The test result in html with the "maven-cucumber-reporting" plugin in pom.xml 
// *** 210216 new functionality use of Tags to select which Scenarios/Tests to execute. more documentation can be found at https://cucumber.io/docs/cucumber/api/
              //   ,tags= "@AddPlaceTest or @GetPlaceTest"   // Execute Tests tagged with @AddPlaceTest OR @GetPlaceTest 
              //   ,tags= "@AddPlaceTest and @GetPlaceTest"  // Execute Tests tagged with BOTH "TOGETHER" @AddPlaceTest AND @GetPlaceTest
			  //  ,tags= "@AddPlaceTest or @GetPlaceTest or @DeletePlace"   // Execute Tests tagged with @AddPlaceTest OR @GetPlaceTest OR @Delete
              //   ,tags= "@AddPlaceTest"  // Execute Tests tagged with @AddPlaceTest
			       ,tags= "@DeletePlace"   // Execute Tests tagged with @DeletePlace
			  //     ,tags= "@GetPlaceTest"   // Execute Tests tagged with @GetPlaceTest		   
                )   // Needed to close the whole statement @CucumberOptions...

public class TestRunner {

}
