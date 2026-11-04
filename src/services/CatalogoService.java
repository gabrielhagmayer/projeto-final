package services;

import dao.CatalogoDAO;
import exceptions.StokiException;
import models.Fornecedor;
import models.Produto;
import models.Setor;

import java.util.ArrayList;

/**
 * Listas prontas de setores, fornecedores e produtos ativos.
 * Use esta classe quando precisar dessas listas em outro módulo.
 */
public class CatalogoService {

    private final CatalogoDAO catalogoDAO = new CatalogoDAO();

    public ArrayList<Setor> listarSetores() throws StokiException {
        return catalogoDAO.listarSetores();
    }

    public ArrayList<Fornecedor> listarFornecedoresAtivos() throws StokiException {
        return catalogoDAO.listarFornecedoresAtivos();
    }

    public ArrayList<Produto> listarProdutosAtivos() throws StokiException {
        return catalogoDAO.listarProdutosAtivos();
    }
}
