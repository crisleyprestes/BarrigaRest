package br.com.linhares.crisley.tests;

import br.com.linhares.crisley.rest.core.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest{

    private String TOKEN;

    private Movement getMovimentacaoValida(){
        Movement movement = new Movement();
        movement.setConta_id(1670383);
        movement.setDescricao("Descrição da movimentação");
        movement.setEnvolvido("Envolvido na movimentação");
        movement.setTipo("REC");
        movement.setData_transacao("01/01/2000");
        movement.setData_pagamento("10/05/2010");
        movement.setValor(100f);
        movement.setStatus(true);

        return movement;
    }

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

    @Test
    public void naoDeveIncluirContaComMesmoNome(){
        given()
                    .header("Authorization", "JWT " + TOKEN)
                    .body("{\"nome\": \"conta alterada\"}")
                .when()
                    .post("/contas")
                .then()
                    .statusCode(400)
                    .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void deveInserirMovimentacaoComSucesso(){
        Movement movement = getMovimentacaoValida();

        given()
                    .header("Authorization", "JWT " + TOKEN)
                    .body(movement)
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao(){
        given()
                    .header("Authorization", "JWT " + TOKEN)
                    .body("{}")
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(400)
                    .body("$", hasSize(8))
                    .body("msg", hasItems(
                            "Data da Movimentação é obrigatório",
                            "Data do pagamento é obrigatório",
                            "Descrição é obrigatório",
                            "Interessado é obrigatório",
                            "Valor é obrigatório",
                            "Valor deve ser um número",
                            "Conta é obrigatório",
                            "Situação é obrigatório"
                    ))
        ;
    }

    @Test
    public void naoDeveInserirMovimentacaoComDataFutura(){
        Movement movement = getMovimentacaoValida();
        movement.setData_transacao("30/12/2100");

        given()
                .header("Authorization", "JWT " + TOKEN)
                    .body(movement)
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(400)
                    .body("$", hasSize(1))
                    .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void naoDeveRemoverContaComMovimentacao(){
        given()
                    .header("Authorization", "JWT " + TOKEN)
                .when()
                    .delete("/contas/1670383")
                .then()
                    .statusCode(500)
                    .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void deveCalcularSaldoContas(){
        given()
                    .header("Authorization", "JWT " + TOKEN)
                .when()
                    .get("/saldo")
                .then()
                    .statusCode(200)
                    .body("find{it.conta_id == 1670383}.saldo", is("100.00"))
        ;
    }
}

