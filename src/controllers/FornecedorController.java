package controllers;

import exceptions.StokiException;
import models.Fornecedor;
import services.FornecedorService;
import java.util.ArrayList;

/**
 * Responsável: Renan.
 * Liga a tela ao FornecedorService e mostra sucesso ou erro.
 */
public class FornecedorController {

    private final FornecedorService fornecedorService = new FornecedorService();

    /** Devolve todos os fornecedores cadastrados */
    public ArrayList<Fornecedor> listar() throws StokiException {
        return fornecedorService.listarTodos();
    }

    /** Grava um fornecedor novo */
    public void cadastrar(Fornecedor fornecedor) throws StokiException {
        fornecedorService.cadastrar(fornecedor);
    }

    /** Atualiza os dados de um fornecedor */
    public void alterar(Fornecedor fornecedor) throws StokiException {
        fornecedorService.alterar(fornecedor);
    }

    /** Remove um fornecedor */
    public void excluir(int id) throws StokiException {
        fornecedorService.excluir(id);
    }
}