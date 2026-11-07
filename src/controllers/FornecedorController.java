package controllers;

import models.Fornecedor;
import services.FornecedorService;
import exceptions.StokiException;
import java.util.ArrayList;

/**
 * Responsável: Renan.
 * Liga a tela ao FornecedorService e mostra sucesso ou erro.
 */
public class FornecedorController {

    private final FornecedorService fornecedorService = new FornecedorService();

    /** Devolve todos os fornecedores cadastrados */
    public ArrayList<Fornecedor> listar() throws StokiException {
        return fornecedorService.listar();
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

    /** Inativa um fornecedor */
    public void inativar(int id) throws StokiException {
        fornecedorService.inativar(id);
    }

    /** Verifica se o fornecedor possui lotes vinculados */
    public boolean verificarLotes(int id) throws StokiException {
        return fornecedorService.verificarLotes(id);
    }

    public ArrayList<Fornecedor> listarAtivos() throws StokiException {
        return fornecedorService.listarAtivos();
    }
}
