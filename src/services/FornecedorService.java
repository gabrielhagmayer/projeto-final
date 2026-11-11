package services;

import dao.FornecedorDAO;
import exceptions.StokiException;
import models.Fornecedor;
import models.Perfil;
import utils.Sessao;

import java.util.ArrayList;

public class FornecedorService {

    private final FornecedorDAO fornecedorDAO;

    public FornecedorService() {
        this.fornecedorDAO = new FornecedorDAO();
    }

    private void validarPermissao() throws StokiException {
        if (!Sessao.temPerfil(Perfil.GERENTE, Perfil.RESPONSAVEL)) {
            throw new StokiException("Acesso negado.");
        }
    }

    private String somenteNumeros(String s) {
        if (s == null) return "";
        return s.replaceAll("\\D", "");
    }

    private boolean cnpjValido(String cnpj) {
        if (cnpj == null) return false;
        cnpj = somenteNumeros(cnpj);

        if (cnpj.length() != 14) return false;
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int sm = 0;
            int peso = 2;
            for (int i = 11; i >= 0; i--) {
                int num = (int) (cnpj.charAt(i) - '0');
                sm += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }

            int r = sm % 11;
            char dig1 = (r < 2) ? '0' : (char) ((11 - r) + '0');

            sm = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                int num = (int) (cnpj.charAt(i) - '0');
                sm += num * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }

            r = sm % 11;
            char dig2 = (r < 2) ? '0' : (char) ((11 - r) + '0');

            return (dig1 == cnpj.charAt(12)) && (dig2 == cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Fornecedor> listarTodos() throws StokiException {
        validarPermissao();
        return fornecedorDAO.listar();
    }

    public void cadastrar(Fornecedor fornecedor) throws StokiException {
        validarPermissao();

        if (fornecedor == null) {
            throw new StokiException("Fornecedor inválido.");
        }

        String cnpjLimpo = somenteNumeros(fornecedor.getCnpj());
        if (!cnpjValido(cnpjLimpo)) {
            throw new StokiException("CNPJ inválido.");
        }

        fornecedor.setCnpj(cnpjLimpo);
        fornecedorDAO.cadastrar(fornecedor);
    }

    public void alterar(Fornecedor fornecedor) throws StokiException {
        validarPermissao();

        if (fornecedor == null) {
            throw new StokiException("Fornecedor inválido.");
        }

        String cnpjLimpo = somenteNumeros(fornecedor.getCnpj());
        if (!cnpjValido(cnpjLimpo)) {
            throw new StokiException("CNPJ inválido.");
        }

        fornecedor.setCnpj(cnpjLimpo);
        fornecedorDAO.alterar(fornecedor);
    }

    public void excluir(int id) throws StokiException {
        validarPermissao();
        fornecedorDAO.excluir(id);
    }


    public void inativar(int id) throws StokiException {
        validarPermissao();
        fornecedorDAO.inativar(id);
    }

    public boolean temLotes(int id) throws StokiException {
        validarPermissao();
        return fornecedorDAO.verificarLotes(id);
    }




}


