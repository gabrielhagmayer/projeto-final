package services;

import dao.FornecedorDAO;
import models.Fornecedor;
import exceptions.StokiException;
import java.util.ArrayList;
import utils.Utilidade;
import utils.Sessao;

/**
 * Responsável: Renan.
 * Valida os dados e aplica as regras de negócio.
 */
public class FornecedorService {

    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();

    /** Devolve todos os fornecedores cadastrados */
    public ArrayList<Fornecedor> listar() throws StokiException {
        return fornecedorDAO.listar();
    }

    /** grava um fornecedor novo */

    public void cadastrar(Fornecedor fornecedor) throws StokiException {
        validarPermissao();
        validar(fornecedor);
        fornecedorDAO.cadastrar(fornecedor);
    }

    private void validarPermissao() throws StokiException {
        if (!Sessao.get().ehGerente() && !Sessao.get().ehResponsavel()) {
            throw new StokiException("Você não tem permissão para esta operação.");
        }
    }

    private void validar(Fornecedor fornecedor) throws StokiException {
        if (fornecedor.getRazaoSocial() == null || fornecedor.getRazaoSocial().trim().isEmpty()) {
            throw new StokiException("A razão social é obrigatória.");
        }
        String cnpjLimpo = Utilidade.somenteNumeros(fornecedor.getCnpj());
        if (!Utilidade.cnpjValido(cnpjLimpo)) {
            throw new StokiException("CNPJ inválido.");
        }
        fornecedor.setCnpj(cnpjLimpo);
    }

    /** Atualiza os dados de um fornecedor */
    public void alterar(Fornecedor fornecedor) throws StokiException {
        validarPermissao();
        validar(fornecedor);
        fornecedorDAO.alterar(fornecedor);
    }

    /** Remove um fornecedor do banco ou lança exceção se tiver lotes vinculados */
    public void excluir(int id) throws StokiException {
        validarPermissao();
        if (fornecedorDAO.verificarLotes(id)) {
            throw new StokiException("O fornecedor possui lotes vinculados e não pode ser excluído. Deseja inativá-lo?");
        }
        fornecedorDAO.excluir(id);
    }

    /** Inativa um fornecedor mantendo seu histórico */
    public void inativar(int id) throws StokiException {
        validarPermissao();
        fornecedorDAO.inativar(id);
    }

    /** Verifica se o fornecedor possui lotes vinculados */
    public boolean verificarLotes(int id) throws StokiException {
        return fornecedorDAO.verificarLotes(id);
    }


    /** Devolve apenas os fornecedores ativos para uso nas telas de cadastro/entrada */
    public ArrayList<Fornecedor> listarAtivos() throws StokiException {
        ArrayList<Fornecedor> todos = fornecedorDAO.listar();
        ArrayList<Fornecedor> ativos = new ArrayList<>();

        for (Fornecedor f : todos) {
            if (f.isAtivo()) {
                ativos.add(f);
            }
        }

        return ativos;
    }
}




