package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Fornecedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Faz o acesso ao banco para a tabela fornecedor.
 * Permite listar, cadastrar, alterar, excluir, inativar e verificar lotes.
 * Responsável: Renan.
 */
public class FornecedorDAO {

    /**
     * Busca todos os fornecedores, em ordem de razão social, inclusive os inativos
     */
    public ArrayList<Fornecedor> listar() throws StokiException {


        String sql = "SELECT id, razao_social, cnpj, endereco, ativo FROM fornecedor ORDER BY razao_social";

        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = preparacao.executeQuery()) {
            ArrayList<Fornecedor> fornecedores = new ArrayList<>();
            while (resultado.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setCnpj(resultado.getString("cnpj"));
                fornecedor.setRazaoSocial(resultado.getString("razao_social"));
                fornecedor.setEndereco(resultado.getString("endereco"));
                fornecedor.setId(resultado.getInt("id"));
                fornecedor.setAtivo(resultado.getBoolean("ativo"));
                fornecedores.add(fornecedor);
            }
            return fornecedores;
        } catch (SQLException e) {
            throw new StokiException("Não foi possível listar os fornecedores.", e);
        }

    }

    /**
     * Grava um fornecedor novo
     */
    public void cadastrar(Fornecedor fornecedor) throws StokiException {
        String sql = "INSERT INTO fornecedor (razao_social, cnpj, endereco) VALUES (?, ?, ?)";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            preparacao.setString(1, fornecedor.getRazaoSocial());
            preparacao.setString(2, fornecedor.getCnpj());
            preparacao.setString(3, fornecedor.getEndereco());
            preparacao.executeUpdate();

        } catch (SQLException e) {
            throw new StokiException("Não foi possível cadastrar o fornecedor. O CNPJ pode já estar cadastrado.", e);

        }


    }

    /**
     * Atualiza os dados de um Fornecedor
     */

    public void alterar(Fornecedor fornecedor) throws StokiException {
        String sql = "UPDATE fornecedor SET razao_social = ?, cnpj = ?, endereco = ? WHERE id = ?";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            preparacao.setString(1, fornecedor.getRazaoSocial());
            preparacao.setString(2, fornecedor.getCnpj());
            preparacao.setString(3, fornecedor.getEndereco());
            preparacao.setInt(4, fornecedor.getId());
            preparacao.executeUpdate();
        } catch (SQLException e) {
            throw new StokiException("Não foi possível alterar o fornecedor.", e);
        }


    }

    /**
     * Remove um Fornecedor pelo id
     */

    public void excluir(int id) throws StokiException {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            preparacao.setInt(1, id);
            preparacao.executeUpdate();
        } catch (SQLException e) {
            throw new StokiException("Não foi possível remover o fornecedor.", e);
        }
    }

    /**
     * Inativa um fornecedor pelo id
     */

    public void inativar(int id) throws StokiException {
        String sql = "UPDATE fornecedor SET ativo = FALSE WHERE id = ?";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            preparacao.setInt(1, id);
            preparacao.executeUpdate();
        } catch (SQLException e) {
            throw new StokiException("Não foi possível inativar o fornecedor.", e);
        }

    }

    /**
     * Verifica se o fornecedor tem lotes
     */

    public boolean verificarLotes(int id) throws StokiException {
        String sql = "SELECT 1 FROM lote WHERE fornecedor_id = ?";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            preparacao.setInt(1, id);
            ResultSet resultado = preparacao.executeQuery();
            return resultado.next();
        } catch (SQLException e) {
            throw new StokiException("Não foi possível verificar lotes do fornecedor.", e);
        }

    }
}



