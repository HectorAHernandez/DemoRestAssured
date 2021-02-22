package Backups;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/java/features",  //the whole folder, could also be specific .feature file: "src/test/java/features/placeValidatons.feature"
                 glue= {"stepDefinitions"})   //The package

public class TestRunner210212 {

}
