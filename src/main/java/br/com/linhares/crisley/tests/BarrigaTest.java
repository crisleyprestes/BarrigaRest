package br.com.linhares.crisley.tests;

import br.com.linhares.crisley.rest.core.BaseTest;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class BarrigaTest extends BaseTest{

    @Test
    public void naoDeveAcessarAPISemToken(){
        given()
                .when()
                    .get("/contas")
                .then()
                    .statusCode(401)
        ;
    }
}
