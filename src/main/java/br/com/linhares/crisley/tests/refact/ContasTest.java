package br.com.linhares.crisley.tests.refact;

import br.com.linhares.crisley.rest.core.BaseTest;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

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
    public void deveIncluirContaComSucesso(){
        given()
                    .body("{\"nome\": \"Conta inserida\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso(){
        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");

        given()
                    .body("{\"nome\": \"Conta alterada\"}")
                    .pathParam("id", CONTA_ID)
                .when()
                    .put("/contas/{id}")
                .then()
                    .statusCode(200)
                    .body("nome", is("Conta alterada"))
        ;
    }

    @Test
    public void naoDeveIncluirContaComMesmoNome(){
        given()
                    .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(400)
                    .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }
}
