package tests;

import exceptions.StokiException;
import models.Lote;
import models.Produto;
import services.AcessoService;
import services.CatalogoService;
import services.EstoqueService;

import java.math.BigDecimal;

/**
 * Responsável: Gabriel.
 * Testa entrada, saída e perda, conferindo pelo saldo.
 */
public class EstoqueTest {

    private final EstoqueService estoqueService = new EstoqueService();
    private final CatalogoService catalogoService = new CatalogoService();
    private Produto produto;
    private String numeroLote;

    // Entra como gerente e roda os testes do estoque, em ordem.
    public void iniciar() {
        System.out.println("=== ESTOQUE ===");
        try {
            new AcessoService().entrar("gerente", "123");
        } catch (Exception e) {
            System.err.println("Login: Falhou. " + e.getMessage());
        }
        entrada();
        saida();
        saidaMaiorQueSaldo();
        perda();
    }

    // Procura o lote de teste na lista de lotes com saldo.
    private Lote buscarLote() throws Exception {
        for (Lote lote : estoqueService.listarLotesComSaldo(produto)) {
            if (lote.getNumeroLote().equals(numeroLote)) {
                return lote;
            }
        }
        return null;
    }

    // Devolve o saldo do lote de teste, ou zero se ele não tiver mais saldo.
    private BigDecimal buscarSaldo() throws Exception {
        Lote lote = buscarLote();
        return lote == null ? BigDecimal.ZERO : lote.getSaldo();
    }

    // Compara o saldo do lote de teste com o esperado e imprime o resultado.
    private void conferir(String teste, String esperado) throws Exception {
        BigDecimal saldo = buscarSaldo();
        if (saldo.compareTo(new BigDecimal(esperado)) == 0) {
            System.out.println(teste + ": Passou");
        } else {
            System.err.println(teste + ": Falhou. Saldo esperado " + esperado + ", veio " + saldo + ".");
        }
    }
    // Cria um lote de teste com 10 unidades e confere o saldo.
    private void entrada() {
        try {
            for (Produto p : catalogoService.listarProdutosAtivos()) {
                if ("UN".equals(p.getUnidadeEstoque()) && !Boolean.TRUE.equals(p.getTemValidade())) {
                    produto = p;
                    break;
                }
            }
            numeroLote = "TESTE-" + System.currentTimeMillis();

            Lote lote = new Lote();
            lote.setProduto(produto);
            lote.setFornecedor(catalogoService.listarFornecedoresAtivos().get(0));
            lote.setNumeroLote(numeroLote);
            lote.setCustoUnitario(new BigDecimal("1.00"));

            estoqueService.registrarEntrada(lote, new BigDecimal("10"), null);
            conferir("Entrada", "10");
        } catch (Exception e) {
            System.err.println("Entrada: Falhou. " + e.getMessage());
        }
    }

    // Tira 3 unidades do lote de teste e confere o saldo.
    private void saida() {
        try {
            estoqueService.registrarSaida(produto, buscarLote(), new BigDecimal("3"));
            conferir("Saída", "7");
        } catch (Exception e) {
            System.err.println("Saída: Falhou. " + e.getMessage());
        }
    }

    // Tenta tirar mais do que o lote tem. O teste passa se o sistema recusar.
    private void saidaMaiorQueSaldo() {
        try {
            estoqueService.registrarSaida(produto, buscarLote(), new BigDecimal("100"));
            System.err.println("Saída maior que o saldo: Falhou. O sistema aceitou.");
        } catch (StokiException e) {
            System.out.println("Saída maior que o saldo: Passou");
        } catch (Exception e) {
            System.err.println("Saída maior que o saldo: Falhou. " + e.getMessage());
        }
    }

    // Registra a perda de 2 unidades e confere o saldo.
    private void perda() {
        try {
            estoqueService.registrarPerda(produto, buscarLote(), new BigDecimal("2"), "AVARIA", "Teste automatizado");
            conferir("Perda", "5");
        } catch (Exception e) {
            System.err.println("Perda: Falhou. " + e.getMessage());
        }
    }
}