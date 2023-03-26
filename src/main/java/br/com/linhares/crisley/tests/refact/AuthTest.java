package br.com.linhares.crisley.tests.refact;

import br.com.linhares.crisley.rest.core.BaseTest;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class AuthTest extends BaseTest {

    public Integer getIdContaPeloNome(String nomeConta){
        return get("/contas?nome=" + nomeConta).then().extract().path("id[0]");
    }

    @BeforeClass
    public static void login(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "crisley@email.com");
        login.put("senha", "123456");

        String TOKEN = given()
                    .body(login)
                .when()
                    .post("/signin")
                .then()
                    .statusCode(200)
                    .extract().path("token")
                ;

        requestSpecification.header("Authorization", "JWT " + TOKEN);

        get("/reset").then().statusCode(200);
    }

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
