package br.com.linhares.crisley.tests.refact;

import br.com.linhares.crisley.rest.core.BaseTest;
import br.com.linhares.crisley.utils.BarrigaUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SaldoTest extends BaseTest {

    @Test
    public void deveCalcularSaldoContas(){
        Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para saldo");

        given()
                .when()
                    .get("/saldo")
                .then()
                    .statusCode(200)
                    .body("find{it.conta_id == " + CONTA_ID + "}.saldo", is("534.00"))
        ;
    }
}
