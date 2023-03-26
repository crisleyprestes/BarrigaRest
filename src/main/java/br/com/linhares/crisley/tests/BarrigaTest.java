package br.com.linhares.crisley.tests;

import br.com.linhares.crisley.rest.core.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

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
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(1670383);
        // movimentacao.setUsuario_id();
        movimentacao.setDescricao("Descrição da movimentação");
        movimentacao.setEnvolvido("Envolvido na movimentação");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao("01/01/2000");
        movimentacao.setData_pagamento("10/05/2010");
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);

        given()
                    .header("Authorization", "JWT " + TOKEN)
                    .body(movimentacao)
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(201)
        ;
    }
}

