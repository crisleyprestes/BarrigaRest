package br.com.linhares.crisley.tests.refact;

import br.com.linhares.crisley.rest.core.BaseTest;
import br.com.linhares.crisley.tests.Movimentacao;
import br.com.linhares.crisley.utils.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    public Integer getIdContaPeloNome(String nomeConta){
        return get("/contas?nome=" + nomeConta).then().extract().path("id[0]");
    }

    public Integer getIdMovimentacaoPelaDescricao(String descricao){
        return get("/transacoes?descricao=" + descricao).then().extract().path("id[0]");
    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
        movimentacao.setDescricao("Descrição da movimentação");
        movimentacao.setEnvolvido("Envolvido na movimentação");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao(DateUtils.getDataDiferencaDias(-1));
        movimentacao.setData_pagamento(DateUtils.getDataDiferencaDias(5));
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);

        return movimentacao;
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
    public void deveInserirMovimentacaoComSucesso(){
        Movimentacao movimentacao = getMovimentacaoValida();

        given()
                    .body(movimentacao)
                .when()
                    .post("/transacoes")
                .then()
                    .statusCode(201)
                    .body("descricao", is("Descrição da movimentação"))
                    .body("envolvido", is("Envolvido na movimentação"))
                    .body("tipo", is("REC"))
                    .body("valor", is("100.00"))
                    .body("usuario_id", is(36700))
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao(){
        given()
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
        Movimentacao movimentacao = getMovimentacaoValida();
        movimentacao.setData_transacao(DateUtils.getDataDiferencaDias(2));

        given()
                    .body(movimentacao)
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
                    .pathParam("id", getIdContaPeloNome("Conta com movimentacao"))
                .when()
                    .delete("/contas/{id}")
                .then()
                    .statusCode(500)
                    .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void deveRemoverMovimentacao(){
        given()
                    .pathParam("id", getIdMovimentacaoPelaDescricao("Movimentacao para exclusao"))
                .when()
                    .delete("/transacoes/{id}")
                .then()
                    .statusCode(204)
        ;
    }
}
