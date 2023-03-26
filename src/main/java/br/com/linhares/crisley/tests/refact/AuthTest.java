package br.com.linhares.crisley.tests.refact;

import br.com.linhares.crisley.rest.core.BaseTest;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class AuthTest extends BaseTest {

    @Test
    public void naoDeveAcessarAPISemToken(){
        FilterableRequestSpecification request = (FilterableRequestSpecification) requestSpecification;
        request.removeHeader("Authorization");

        given()
                .when()
                    .get("/contas")
                .then()
                    .statusCode(401)
        ;
    }
}
