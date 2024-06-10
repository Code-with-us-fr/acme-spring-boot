package com.acme.starters.test;

import io.restassured.RestAssured;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Spring {@link TestExecutionListener} that define automatically {@link RestAssured#port} with {@code local.server.port}.
 */
public class RestAssuredTestExecutionListener implements TestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) {
        String serverPort = testContext.getApplicationContext().getEnvironment().getProperty("local.server.port");
        if (serverPort != null) {
            RestAssured.port = Integer.parseInt(serverPort);
        }
    }
}
