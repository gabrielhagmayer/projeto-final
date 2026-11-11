package controllers;

import exceptions.StokiException;
import services.RelatorioService;

import java.util.List;
import java.util.Map;

/**
 * Responsável: Erick.
 * Liga a tela ao RelatorioService e mostra sucesso ou erro.
 */
public class RelatorioController {

    private final RelatorioService relatorioService = new RelatorioService();

    // Busca os dados do relatório de saldo de lotes
    // para exibir na tela retorna a Lista de mapas contendo os dados do relatório.
    public List buscarRelatorioSaldoLotes() {
        try {
            return relatorioService.gerarRelatorioSaldoLotes();
        } catch (StokiException e) {
            // Em projetos Swing/MVC, podemos capturar e direcionar para uma mensagem de erro na UI
            System.err.println("Erro no Controller ao buscar relatório: " + e.getMessage());
            return null;
        }
    }

    public List<Map<String, Object>> listarEstoqueGeral() throws StokiException {
        return relatorioService.listarEstoqueGeral();
    }

    public Map<String, Object> montarPainel() throws StokiException {
        return relatorioService.montarPainel();
    }

}
