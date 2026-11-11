package services;

import dao.RelatorioDAO;

import exceptions.StokiException;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import java.time.LocalDate;

import java.math.BigDecimal;

/**
 * Responsável: Erick.
 * Valida os dados e aplica as regras de negócio.
 */
public class RelatorioService {

    private final RelatorioDAO relatorioDAO = new RelatorioDAO();

    // Retorna os dados para o relatório de saldo e lotes.
    public List gerarRelatorioSaldoLotes() throws StokiException {
        try {
            // Aqui podem entrar regras de negócio, filtros ou validações futuras
            return relatorioDAO.gerarRelatorioSaldoLotes();
        } catch (SQLException e) {
            throw new StokiException("Erro ao consultar os dados para o relatório de lotes.", e);
        }
    }

    public List<Map<String, Object>> listarEstoqueGeral() throws StokiException {
        try {
            return relatorioDAO.listarEstoqueGeral();
        } catch (SQLException e) {
            throw new StokiException("Erro ao consultar o estoque geral.", e);
        }
    }

    public Map<String, Object> montarPainel() throws StokiException {
        try {
            Map<String, Object> painel = relatorioDAO.resumoPainel();
            painel.put("abaixoDoMinimo", relatorioDAO.contarAbaixoDoMinimo());
            painel.put("promocoes", montarPromocoes(relatorioDAO.listarDadosPromocao()));
            Timestamp ultima = relatorioDAO.buscarUltimaContagemFinalizada();
            boolean atrasada = true;
            if (ultima != null) {
                LocalDate dataUltima = ultima.toLocalDateTime().toLocalDate();
                atrasada = dataUltima.plusDays(15).isBefore(LocalDate.now());
            }
            painel.put("contagemAtrasada", atrasada);
            return painel;
        } catch (SQLException e) {
            throw new StokiException("Erro ao carregar os dados do painel.", e);
        }
    }

    private List<Map<String, Object>> montarPromocoes(List<Map<String, Object>> dados) {
        List<Map<String, Object>> promocoes = new ArrayList<>();
        for (Map<String, Object> linha : dados) {
            boolean perto = (int) linha.get("lotesPerto") > 0;
            BigDecimal maximo = (BigDecimal) linha.get("estoqueMaximo");
            BigDecimal saldo = (BigDecimal) linha.get("saldoValido");
            boolean excesso = maximo != null && saldo.compareTo(maximo) > 0;
            String prioridade = null;
            String motivo = null;
            if (perto && excesso) {
                prioridade = "ALTA";
                motivo = "Perto de vencer e em excesso";
            } else if (perto) {
                prioridade = "MÉDIA";
                motivo = "Perto de vencer";
            } else if (excesso) {
                prioridade = "BAIXA";
                motivo = "Estoque em excesso";
            }

            if (prioridade != null) {
                Map<String, Object> promocao = new HashMap<>();
                promocao.put("produto", linha.get("nome"));
                promocao.put("prioridade", prioridade);
                promocao.put("motivo", motivo);
                promocoes.add(promocao);
            }

        }
        return promocoes;
    }
}

