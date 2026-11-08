package controllers;

import exceptions.StokiException;
import models.Produto;
import services.ProdutoService;

import java.util.List;

/**
 * Responsável: Erick.
 * Liga a tela ao ProdutoService e mostra sucesso ou erro.
 */
public class ProdutoController {

    private ProdutoService produtoService = new ProdutoService();

    // Recebe a solicitação de cadastro vindo da interface, repassa para a camada de serviço e trata o fluxo de informações.
    public void cadastrar(Produto produto) throws StokiException {
        try {
            //Chama o serviço que executa as validações e o salvamento do DAO
            produtoService.cadastrar(produto);
        } catch (StokiException e) {
            // Repassa a exceção tratada para a View exibir ao utilizado
            throw e;
        } catch (Exception e) {
            // Captura qualquer erro inesperado
            throw new StokiException("Erro inesperado ao cadastrar produto: " + e.getMessage());
        }
    }

    // Retorna a lista de todos os produtos ativos cadastrados no sistema
    public List<Produto> listar() throws StokiException {
        try {
            // Chama o serviço para buscar os produtos
            return produtoService.listar();
        } catch (StokiException e) {
            throw e;
        } catch (Exception e) {
            throw new StokiException("Erro inesperado ao listar produtos: " + e.getMessage());
        }
    }

    public void atualizar(Produto produto) throws StokiException {
        try {
            produtoService.atualizar(produto);
        } catch (java.sql.SQLException e) {
            throw new StokiException("Erro de banco de dados ao atualizar produto: " + e.getMessage());
        }
    }
}
