package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável: Erick.
 * Executa o SQL com PreparedStatement.
 */
public class ProdutoDAO {

    public List<Produto> listar() throws StokiException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE ativo = true";

        try (Connection con = ConexaoPostgreSQL.get();
             PreparedStatement stmt = con.prepareStatement(sql); // note que a variável é 'con'
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                // Preencha os demais atributos se necessário

                lista.add(produto);
            }

        } catch (Exception e) {
            throw new StokiException("Erro ao listar produto: " + e.getMessage());
        }

        return lista;
    }

    public boolean temLotes(long produtoId) throws StokiException {
        String sql = "SELECT COUNT(*) FROM lote WHERE produto_id = ?";
        try (Connection con = ConexaoPostgreSQL.get();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, produtoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new StokiException("Erro ao verificar lotes: " + e.getMessage());
        }
        return false;
    }
}