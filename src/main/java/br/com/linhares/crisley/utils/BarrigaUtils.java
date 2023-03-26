package br.com.linhares.crisley.utils;

import br.com.linhares.crisley.tests.Movimentacao;

import static io.restassured.RestAssured.get;

public class BarrigaUtils {

    public static Integer getIdContaPeloNome(String nomeConta){
        return get("/contas?nome=" + nomeConta).then().extract().path("id[0]");
    }

    public static Integer getIdMovimentacaoPelaDescricao(String descricao){
        return get("/transacoes?descricao=" + descricao).then().extract().path("id[0]");
    }

    public static Movimentacao getMovimentacaoValida(){
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
        movimentacao.setDescricao("Descrição da movimentação");
        movimentacao.setEnvolvido("Envolvido na movimentação");
        movimentacao.setTipo("REC");
        movimentacao.setData_transacao(DateUtils.getDataDiferencaDias(-1));
        movimentacao.setData_pagamento(DateUtils.getDataDiferencaDias(5));
        movimentacao.setValor(100f);
        movimentacao.setStatus(true);

        return movimentacao;
    }
}
