package br.com.linhares.crisley.tests;

import br.com.linhares.crisley.rest.core.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest{

    private String TOKEN;

    @Before
    public void login(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "crisley@email.com");
        login.put("senha", "123456");

        TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token")
                ;
    }

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
        given()
                    .header("Authorization", "JWT " + TOKEN)
                    .body("{\"nome\": \"conta qualquer\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso(){
        given()
                    .header("Authorization", "JWT " + TOKEN)
                    .body("{\"nome\": \"conta alterada\"}")
                .when()
                    .put("/contas/1670383")
                .then()
                    .statusCode(200)
                    .body("nome", is("conta alterada"))
        ;
    }
}

