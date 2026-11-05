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

    public ArrayList<Fornecedor> listar() throws StokiException {

        String sql = "SELECT id, razao_social, cnpj, endereco, ativo FROM fornecedor ORDER BY razao_social";

        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = preparacao.executeQuery()) {
        } catch (SQLException e) {
            throw new StokiException("Não foi possível listar os fornecedores.", e);
        }

    }
}

