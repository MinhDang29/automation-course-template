package com.step;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import com.utils.BasicTest;

public class Hooks extends BasicTest {

    @Before
    public void setUp() {
        // Use BasicTest preCondition to initialize WebDriver and helpers
        preCondition();
    }

    @After
    public void tearDown() {
        // Use BasicTest postCondition to cleanup (currently does nothing or quits if enabled)
        postCondition();
    }
}
