package br.com.linhares.crisley.rest.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.*;

public class BaseTest implements Constants {

    @BeforeClass
    public static void setup(){
        baseURI = APP_BASE_URL;
        port = APP_PORT;
        basePath = APP_BASE_PATH;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(APP_CONTENT_TYPE);
        requestSpecification = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectResponseTime(Matchers.lessThan(MAX_TIMEOUT));
        responseSpecification = resBuilder.build();

        enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
