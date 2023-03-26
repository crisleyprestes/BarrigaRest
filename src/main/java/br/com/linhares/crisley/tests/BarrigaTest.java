package br.com.linhares.crisley.tests;

import br.com.linhares.crisley.rest.core.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void deveIncluirContaComSucesso(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "crisley@email.com");
        login.put("senha", "123456");

        String token = given()
                    .body(login)
                .when()
                    .post("/signin")
                .then()
                    .statusCode(200)
                    .extract().path("token")
        ;

        given()
                    .header("Authorization", "JWT " + token)
                    .body("{\"nome\": \"conta qualquer\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
        ;
    }
}

