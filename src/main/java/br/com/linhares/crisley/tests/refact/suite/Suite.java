package br.com.linhares.crisley.tests.refact.suite;

import br.com.linhares.crisley.rest.core.BaseTest;
import br.com.linhares.crisley.tests.refact.AuthTest;
import br.com.linhares.crisley.tests.refact.ContasTest;
import br.com.linhares.crisley.tests.refact.MovimentacaoTest;
import br.com.linhares.crisley.tests.refact.SaldoTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class Suite extends BaseTest {

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
}
