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

    public List<Map<String, Object>> listarEstoqueGeral() throws SQLException, StokiException {
        List<Map<String, Object>> resultados = new ArrayList<>();

        String sql = "SELECT p.nome, s.numero AS setor_numero, s.nome AS setor_nome, p.prateleira, " +
                "p.unidade_estoque, p.conteudo, p.unidade_conteudo, " +
                "COALESCE(SUM(v.saldo), 0) AS saldo, " +
                "MIN(v.validade) FILTER (WHERE v.saldo > 0) AS validade, " +
                "COALESCE(SUM(v.saldo * v.custo_unitario), 0) AS valor " +
                "FROM produto p " +
                "JOIN setor s ON s.id = p.setor_id " +
                "LEFT JOIN vw_saldo_lote v ON v.produto_id = p.id " +
                "WHERE p.ativo = TRUE " +
                "GROUP BY p.id, s.numero, s.nome " +
                "ORDER BY s.numero, p.prateleira, p.nome";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("produtoNome", rs.getString("nome"));
                linha.put("setorNumero", rs.getInt("setor_numero"));
                linha.put("setorNome", rs.getString("setor_nome"));
                linha.put("prateleira", rs.getString("prateleira"));
                linha.put("unidadeEstoque", rs.getString("unidade_estoque"));
                linha.put("conteudo", rs.getBigDecimal("conteudo"));
                linha.put("unidadeConteudo", rs.getString("unidade_conteudo"));
                linha.put("saldo", rs.getBigDecimal("saldo"));
                linha.put("validade", rs.getDate("validade"));
                linha.put("valor", rs.getBigDecimal("valor"));

                resultados.add(linha);
            }
        }
        return resultados;
    }
}