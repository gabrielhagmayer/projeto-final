package dao;

import connections.ConexaoPostgreSQL;

import exceptions.StokiException;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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

    public Map<String, Object> resumoPainel() throws SQLException, StokiException {
        Map<String, Object> resumo = new HashMap<>();
        String sql = "SELECT COALESCE(SUM(saldo * custo_unitario), 0) AS valor, " +
                "COUNT(*) FILTER (WHERE saldo > 0 AND validade < CURRENT_DATE) AS vencidos, " +
                "COUNT(*) FILTER (WHERE saldo > 0 AND validade BETWEEN CURRENT_DATE AND CURRENT_DATE + 30) AS perto_de_vencer " +
                "FROM vw_saldo_lote";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                resumo.put("valor", rs.getBigDecimal("valor"));
                resumo.put("vencidos", rs.getInt("vencidos"));
                resumo.put("pertoDeVencer", rs.getInt("perto_de_vencer"));
            }
        }
        return resumo;
    }

    public int contarAbaixoDoMinimo() throws SQLException, StokiException {
        String sql = "SELECT COUNT(*) AS total FROM (" +
                "SELECT p.id FROM produto p " +
                "LEFT JOIN vw_saldo_lote v ON v.produto_id = p.id AND (v.validade IS NULL OR v.validade >= CURRENT_DATE) " +
                "WHERE p.ativo = TRUE " +
                "GROUP BY p.id, p.estoque_minimo " +
                "HAVING COALESCE(SUM(v.saldo), 0) < p.estoque_minimo) abaixo";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt("total");
        }
    }

    public List<Map<String, Object>> listarDadosPromocao() throws SQLException, StokiException {
        List<Map<String, Object>> resultados = new ArrayList<>();

        String sql = "SELECT p.nome, p.estoque_maximo, " +
                "COALESCE(SUM(v.saldo) FILTER (WHERE v.validade IS NULL OR v.validade >= CURRENT_DATE), 0) AS saldo_valido, " +
                "COUNT(*) FILTER (WHERE v.saldo > 0 AND v.validade BETWEEN CURRENT_DATE AND CURRENT_DATE + 30) AS lotes_perto " +
                "FROM produto p " +
                "LEFT JOIN vw_saldo_lote v ON v.produto_id = p.id " +
                "WHERE p.ativo = TRUE " +
                "GROUP BY p.id, p.nome, p.estoque_maximo " +
                "ORDER BY p.nome";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("nome", rs.getString("nome"));
                linha.put("estoqueMaximo", rs.getBigDecimal("estoque_maximo"));
                linha.put("saldoValido", rs.getBigDecimal("saldo_valido"));
                linha.put("lotesPerto", rs.getInt("lotes_perto"));

                resultados.add(linha);
            }
        }
        return resultados;
    }

    public Timestamp buscarUltimaContagemFinalizada() throws SQLException, StokiException {
        String sql = "SELECT MAX(finalizada_em) AS ultima FROM contagem WHERE status = 'FINALIZADA'";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getTimestamp("ultima");
        }
    }

    public List<Map<String, Object>> listarAbaixoDoMinimo() throws SQLException, StokiException {
        List<Map<String, Object>> resultados = new ArrayList<>();

        String sql = "SELECT p.nome, p.estoque_minimo, COALESCE(SUM(v.saldo), 0) AS saldo_valido " +
                "FROM produto p " +
                "LEFT JOIN vw_saldo_lote v ON v.produto_id = p.id AND (v.validade IS NULL OR v.validade >= CURRENT_DATE) " +
                "WHERE p.ativo = TRUE " +
                "GROUP BY p.id, p.nome, p.estoque_minimo " +
                "HAVING COALESCE(SUM(v.saldo), 0) < p.estoque_minimo " +
                "ORDER BY p.nome";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("nome", rs.getString("nome"));
                linha.put("estoqueMinimo", rs.getBigDecimal("estoque_minimo"));
                linha.put("saldoValido", rs.getBigDecimal("saldo_valido"));

                resultados.add(linha);
            }
        }
        return resultados;
    }

    public List<Map<String, Object>> listarMaiorValorParado() throws SQLException, StokiException {
        List<Map<String, Object>> resultados = new ArrayList<>();

        String sql = "SELECT p.nome, COALESCE(SUM(v.saldo), 0) AS saldo, " +
                "COALESCE(SUM(v.saldo * v.custo_unitario), 0) AS valor " +
                "FROM produto p " +
                "LEFT JOIN vw_saldo_lote v ON v.produto_id = p.id " +
                "WHERE p.ativo = TRUE " +
                "GROUP BY p.id, p.nome " +
                "HAVING COALESCE(SUM(v.saldo * v.custo_unitario), 0) > 0 " +
                "ORDER BY valor DESC, p.nome";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("nome", rs.getString("nome"));
                linha.put("saldo", rs.getBigDecimal("saldo"));
                linha.put("valor", rs.getBigDecimal("valor"));

                resultados.add(linha);
            }
        }
        return resultados;
    }

    public List<Map<String, Object>> listarVencimentos() throws SQLException, StokiException {
        List<Map<String, Object>> resultados = new ArrayList<>();

        String sql = "SELECT p.nome, l.numero_lote, v.validade, v.saldo, (v.validade - CURRENT_DATE) AS dias " +
                "FROM vw_saldo_lote v " +
                "JOIN produto p ON p.id = v.produto_id " +
                "JOIN lote l ON l.id = v.lote_id " +
                "WHERE v.saldo > 0 AND v.validade IS NOT NULL " +
                "ORDER BY v.validade, p.nome";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("nome", rs.getString("nome"));
                linha.put("numeroLote", rs.getString("numero_lote"));
                linha.put("validade", rs.getDate("validade"));
                linha.put("saldo", rs.getBigDecimal("saldo"));
                linha.put("dias", rs.getInt("dias"));

                resultados.add(linha);
            }
        }
        return resultados;
    }

    public List<Map<String, Object>> listarPerdas() throws SQLException, StokiException {
        List<Map<String, Object>> resultados = new ArrayList<>();

        String sql = "SELECT m.data_hora, p.nome, l.numero_lote, ABS(m.quantidade) AS quantidade, " +
                "m.motivo_perda, m.observacao, (ABS(m.quantidade) * l.custo_unitario) AS valor " +
                "FROM movimentacao m " +
                "JOIN lote l ON l.id = m.lote_id " +
                "JOIN produto p ON p.id = l.produto_id " +
                "WHERE m.tipo = 'PERDA' " +
                "ORDER BY m.data_hora DESC";

        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("dataHora", rs.getTimestamp("data_hora"));
                linha.put("nome", rs.getString("nome"));
                linha.put("numeroLote", rs.getString("numero_lote"));
                linha.put("quantidade", rs.getBigDecimal("quantidade"));
                linha.put("motivoPerda", rs.getString("motivo_perda"));
                linha.put("observacao", rs.getString("observacao"));
                linha.put("valor", rs.getBigDecimal("valor"));

                resultados.add(linha);
            }
        }
        return resultados;
    }

    public Map<String, BigDecimal> valoresVencimento() throws SQLException, StokiException {
        String sql = "SELECT COALESCE(SUM(saldo * custo_unitario) FILTER (WHERE validade < CURRENT_DATE), 0) AS vencido, "
                + "COALESCE(SUM(saldo * custo_unitario) FILTER (WHERE validade >= CURRENT_DATE "
                + "AND validade <= CURRENT_DATE + 30), 0) AS perto "
                + "FROM vw_saldo_lote WHERE saldo > 0 AND validade IS NOT NULL";
        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            Map<String, BigDecimal> valores = new HashMap<>();
            valores.put("vencido", rs.getBigDecimal("vencido"));
            valores.put("perto", rs.getBigDecimal("perto"));
            return valores;
        }
    }

        public BigDecimal valorEmExcesso() throws SQLException, StokiException {
            String sql = "SELECT COALESCE(SUM((t.saldo_valido - t.estoque_maximo) * t.custo_medio), 0) AS excesso FROM ("
                    + "SELECT p.estoque_maximo, SUM(v.saldo) AS saldo_valido, "
                    + "SUM(v.saldo * v.custo_unitario) / SUM(v.saldo) AS custo_medio "
                    + "FROM produto p JOIN vw_saldo_lote v ON v.produto_id = p.id "
                    + "WHERE p.ativo = TRUE AND v.saldo > 0 AND (v.validade IS NULL OR v.validade >= CURRENT_DATE) "
                    + "GROUP BY p.id, p.estoque_maximo "
                    + "HAVING SUM(v.saldo) > p.estoque_maximo) t";
            Connection conexao = ConexaoPostgreSQL.get();
            try (PreparedStatement stmt = conexao.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getBigDecimal("excesso");
            }
        }

    public BigDecimal valorPerdidoNoMes() throws SQLException, StokiException {
        String sql = "SELECT COALESCE(SUM(ABS(m.quantidade) * l.custo_unitario), 0) AS perdido "
                + "FROM movimentacao m JOIN lote l ON l.id = m.lote_id "
                + "WHERE m.tipo = 'PERDA' "
                + "AND EXTRACT(YEAR FROM m.data_hora) = EXTRACT(YEAR FROM CURRENT_DATE) "
                + "AND EXTRACT(MONTH FROM m.data_hora) = EXTRACT(MONTH FROM CURRENT_DATE)";
        Connection conexao = ConexaoPostgreSQL.get();
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getBigDecimal("perdido");
        }
    }
}