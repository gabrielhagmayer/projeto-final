package controllers;

import exceptions.StokiException;
import models.Lote;
import services.LoteService;

import java.util.List;

/**
 * Responsável: Erick.
 * Liga a tela de lotes ao LoteService e trata o fluxo de informações.
 */

public class LoteController {

    private LoteService loteService = new LoteService();

    // Recebe a solicitação de cadastro vinda da interface, repassa para o serviço.
    public void cadastrar(Lote lote) throws StokiException {
        try {
            loteService.cadastrar(lote);
        } catch (StokiException e) {
            throw e;
        } catch (Exception e) {
            throw new StokiException("Erro inesperado ao cadastrar lote: " + e.getMessage());
        }
    }

    // Retorna a lista de todos os lotes cadastrados no sistema.
    public List<Lote> listar() throws StokiException {
        try {
            return loteService.listar();
        } catch (StokiException e) {
            throw e;
        } catch (Exception e) {
            throw new StokiException("Erro inesperado ao listar lotes: " + e.getMessage());
        }
    }
}