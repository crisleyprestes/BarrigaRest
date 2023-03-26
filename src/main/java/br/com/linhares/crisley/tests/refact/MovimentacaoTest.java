package br.com.linhares.crisley.tests.refact;

import br.com.linhares.crisley.rest.core.BaseTest;
import br.com.linhares.crisley.tests.Movimentacao;
import br.com.linhares.crisley.utils.BarrigaUtils;
import br.com.linhares.crisley.utils.DateUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveInserirMovimentacaoComSucesso(){
        Movimentacao movimentacao = BarrigaUtils.getMovimentacaoValida();

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
        Movimentacao movimentacao = BarrigaUtils.getMovimentacaoValida();
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
                    .pathParam("id", BarrigaUtils.getIdContaPeloNome("Conta com movimentacao"))
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
                    .pathParam("id", BarrigaUtils.getIdMovimentacaoPelaDescricao("Movimentacao para exclusao"))
                .when()
                    .delete("/transacoes/{id}")
                .then()
                    .statusCode(204)
        ;
    }
}
