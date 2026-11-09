package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Fornecedor;
import models.Lote;
import models.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável: Gabriel.
 * Executa o SQL com PreparedStatement.
 */
public class LoteDAO {

    public List<Lote> listar() throws SQLException, StokiException {
        String sql = "SELECT l.id, l.numero_lote, l.validade, l.custo_unitario, l.data_entrada, " +
                "p.id as produto_id, p.nome as produto_nome, " +
                "f.id as fornecedor_id, f.razao_social as fornecedor_nome " +
                "FROM lote l " +
                "INNER JOIN produto p ON l.produto_id = p.id " +
                "LEFT JOIN fornecedor f ON l.fornecedor_id = f.id " +
                "ORDER BY l.data_entrada DESC";

        List<Lote> lotes = new ArrayList<>();

        try (Connection conexao = ConexaoPostgreSQL.get();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Lote lote = new Lote();
                lote.setId(rs.getInt("id"));
                lote.setNumeroLote(rs.getString("numero_lote"));

                // Mapeando a data de validade (pode ser nula)
                java.sql.Date sqlValidade = rs.getDate("validade");
                if (sqlValidade != null) {
                    lote.setValidade(sqlValidade.toLocalDate());
                }

                lote.setCustoUnitario(rs.getBigDecimal("custo_unitario"));

                Timestamp sqlDataEntrada = rs.getTimestamp("data_entrada");
                if (sqlDataEntrada != null) {
                    lote.setDataEntrada(sqlDataEntrada.toLocalDateTime());
                }

                // Mapeando o Produto associado
                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                lote.setProduto(produto);

                // Mapeando o Fornecedor associado (se houver)
                if (rs.getObject("fornecedor_id") != null) {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setId(rs.getInt("fornecedor_id"));
                    // Utilizando setRazaoSocial corretamente conforme a classe Fornecedor
                    fornecedor.setRazaoSocial(rs.getString("fornecedor_nome"));
                    lote.setFornecedor(fornecedor);
                }

                lotes.add(lote);
            }
        } catch (SQLException e) {
            throw new StokiException("Erro ao listar lotes no banco de dados: " + e.getMessage());
        }

        return lotes;
    }

    public void cadastrar(Lote lote) throws SQLException, StokiException {
        String sql = "INSERT INTO lote (produto_id, fornecedor_id, numero_lote, validade, custo_unitario, data_entrada) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoPostgreSQL.get();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            if (lote.getProduto() != null && lote.getProduto().getId() != null) {
                stmt.setInt(1, lote.getProduto().getId());
            } else {
                throw new StokiException("O lote deve estar associado a um produto válido.");
            }

            if (lote.getFornecedor() != null && lote.getFornecedor().getId() != null) {
                stmt.setInt(2, lote.getFornecedor().getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setString(3, lote.getNumeroLote());

            if (lote.getValidade() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(lote.getValidade()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setBigDecimal(5, lote.getCustoUnitario());

            if (lote.getDataEntrada() != null) {
                stmt.setTimestamp(6, java.sql.Timestamp.valueOf(lote.getDataEntrada()));
            } else {
                stmt.setTimestamp(6, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new StokiException("Erro ao cadastrar lote: " + e.getMessage());
        }
    }
}