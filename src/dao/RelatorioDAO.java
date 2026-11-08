package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsável: Erick.
 * Executa o SQL com PreparedStatement.
 */
public class RelatorioDAO {

    // Gera os dados para relatórios de lotes e stock.
    public List gerarRelatorioSaldoLotes() throws SQLException, StokiException {
        List resultados = new ArrayList<>();

        String sql = "SELECT l.id AS lote_id, l.numero_lote, p.nome AS produto_nome, " +
                "f.razao_social AS fornecedor_nome, l.validade, l.custo_unitario, l.data_entrada " +
                "FROM lote l " +
                "JOIN produto p ON l.produto_id = p.id " +
                "LEFT JOIN fornecedor f ON l.fornecedor_id = f.id " +
                "ORDER BY l.data_entrada DESC";

        try (Connection conexao = ConexaoPostgreSQL.get();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("loteId", rs.getInt("lote_id"));
                linha.put("numeroLote", rs.getString("numero_lote"));
                linha.put("produtoNome", rs.getString("produto_nome"));
                linha.put("fornecedorNome", rs.getString("fornecedor_nome"));
                linha.put("validade", rs.getDate("validade"));
                linha.put("custoUnitario", rs.getBigDecimal("custo_unitario"));
                linha.put("dataEntrada", rs.getTimestamp("data_entrada"));

                resultados.add(linha);
            }
        }
        return resultados;
    }
}