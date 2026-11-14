package controllers;

import exceptions.StokiException;
import models.Fornecedor;
import models.Lote;
import models.Produto;
import services.CatalogoService;
import services.EstoqueService;
import utils.Utilidade;
import views.TelaEntrada;
import views.TelaPerda;
import views.TelaSaida;

import java.math.BigDecimal;

//Responsável: Gabriel.
//Liga a tela ao EstoqueService e mostra sucesso ou erro.

public class EstoqueController {

    private final CatalogoService catalogoService = new CatalogoService();
    private final EstoqueService estoqueService = new EstoqueService();

    public void carregarListas(TelaEntrada tela) {
        try {
            tela.preencherProdutos(catalogoService.listarProdutosAtivos());
            tela.preencherFornecedores(catalogoService.listarFornecedoresAtivos());
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    public void registrarEntrada(TelaEntrada tela, Produto produto, Fornecedor fornecedor,
                                 String numeroLote, String validade, String custo, String quantidade, String unidadesPorCaixa) {
        Lote lote = new Lote();
        lote.setProduto(produto);
        lote.setFornecedor(fornecedor);
        lote.setNumeroLote(numeroLote);
        lote.setValidade(Utilidade.paraData(validade));
        lote.setCustoUnitario(Utilidade.paraDecimal(custo));

        if (!validade.isBlank() && lote.getValidade() == null) {
            tela.informarErro("Use a data no formato dd/mm/aaaa.");
            return;
        }
        BigDecimal unidades = Utilidade.paraDecimal(unidadesPorCaixa);
        if (!unidadesPorCaixa.isBlank() && unidades == null) {
            tela.informarErro("Informe as unidades por caixa como um número.");
            return;
        }
        try {
            estoqueService.registrarEntrada(lote, Utilidade.paraDecimal(quantidade), unidades);
            tela.informarSucesso("Entrada registrada.");
            tela.limparCampos();
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    // Carrega os produtos na tela de saída.
    public void carregarProdutosSaida(TelaSaida tela) {
        try {
            tela.preencherProdutos(catalogoService.listarProdutosAtivos());
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    // Carrega os lotes disponíveis do produto escolhido.
    public void carregarLotes(TelaSaida tela, Produto produto) {
        try {
            tela.preencherLotes(estoqueService.listarLotesDisponiveis(produto));
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    // Registra a saída e recarrega os lotes com o saldo novo.
    public void registrarSaida(TelaSaida tela, Produto produto, Lote lote, String quantidade) {
        try {
            estoqueService.registrarSaida(produto, lote, Utilidade.paraDecimal(quantidade));
            tela.informarSucesso("Saída registrada.");
            tela.limparQuantidade();
            carregarLotes(tela, produto);
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    // Carrega os produtos na tela de perda.
    public void carregarProdutosPerda(TelaPerda tela) {
        try {
            tela.preencherProdutos(catalogoService.listarProdutosAtivos());
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    // Carrega os produto na tela de perda.
    public void carregarLotesPerda(TelaPerda tela, Produto produto) {
        try {
            tela.preencherLotes(estoqueService.listarLotesComSaldo(produto));
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }

    // Registra a perda e recarrega os lotes com o saldo novo.
    public void registrarPerda(TelaPerda tela, Produto produto, Lote lote, String quantidade, String motivo, String observacao) {
        try {
            estoqueService.registrarPerda(produto, lote, Utilidade.paraDecimal(quantidade), motivo, observacao);
            tela.informarSucesso("Perda registrada.");
            tela.limparCampos();
            carregarLotesPerda(tela, produto);
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }
}
