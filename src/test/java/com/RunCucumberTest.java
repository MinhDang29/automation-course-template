package com;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.step",
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
}
