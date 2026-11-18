package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Responsável: Gabriel.
//Consulta os dados do mapa do depósito: setor, prateleira, saldo válido e alertas de cada produto.

public class MapaDAO {

    // Lista uma linha por produto ativo, já ordenada por setor, prateleira e produto.
    public List<Map<String, Object>> listarProdutosDoMapa() throws StokiException {
        String sql = "SELECT s.numero AS setor_numero, s.nome AS setor_nome, s.cor AS setor_cor, "
                + "p.prateleira, p.nome AS produto_nome, p.estoque_minimo, "
                + "COALESCE(SUM(v.saldo) FILTER (WHERE v.validade IS NULL OR v.validade >= CURRENT_DATE), 0) AS saldo_valido, "
                + "COUNT(v.lote_id) FILTER (WHERE v.saldo > 0 AND v.validade < CURRENT_DATE) AS lotes_vencidos, "
                + "COUNT(v.lote_id) FILTER (WHERE v.saldo > 0 AND v.validade BETWEEN CURRENT_DATE AND CURRENT_DATE + 30) AS lotes_perto "
                + "FROM produto p "
                + "JOIN setor s ON s.id = p.setor_id "
                + "LEFT JOIN vw_saldo_lote v ON v.produto_id = p.id "
                + "WHERE p.ativo "
                + "GROUP BY s.numero, s.nome, s.cor, p.id, p.prateleira, p.nome, p.estoque_minimo "
                + "ORDER BY s.numero, p.prateleira, p.nome";

        List<Map<String, Object>> linhas = new ArrayList<>();
        try (PreparedStatement consulta = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("setorNumero", resultado.getInt("setor_numero"));
                linha.put("setorNome", resultado.getString("setor_nome"));
                linha.put("setorCor", resultado.getString("setor_cor"));
                linha.put("prateleira", resultado.getInt("prateleira"));
                linha.put("produtoNome", resultado.getString("produto_nome"));
                linha.put("estoqueMinimo", resultado.getBigDecimal("estoque_minimo"));
                linha.put("saldoValido", resultado.getBigDecimal("saldo_valido"));
                linha.put("lotesVencidos", resultado.getInt("lotes_vencidos"));
                linha.put("lotesPerto", resultado.getInt("lotes_perto"));
                linhas.add(linha);
            }
        } catch (SQLException e) {
            throw new StokiException("Não foi possível carregar o mapa do depósito.", e);
        }
        return linhas;
    }
}