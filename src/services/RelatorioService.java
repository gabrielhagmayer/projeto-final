package services;

import dao.RelatorioDAO;

import exceptions.StokiException;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import utils.Utilidade;

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

    private String formatarData(Date data) {
        return data == null ? "-" : Utilidade.formatarData(data.toLocalDate());
    }

    private String formatarDataHora(Timestamp momento) {
        return momento == null ? "-" : Utilidade.formatarData(momento.toLocalDateTime().toLocalDate());
    }

    private Map<String, Object> montarRelatorio(String[] colunas, List<Object[]> linhas) {
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("colunas", colunas);
        relatorio.put("linhas", linhas);
        return relatorio;
    }

    private Map<String, Object> relatorioLotes() throws StokiException {
        String[] colunas = {"Nº Lote", "Produto", "Fornecedor", "Validade", "Custo unitário", "Data de entrada"};
        List<Object[]> linhas = new ArrayList<>();
        for (Object item : gerarRelatorioSaldoLotes()) {
            Map<?, ?> lote = (Map<?, ?>) item;
            linhas.add(new Object[]{lote.get("numeroLote"), lote.get("produtoNome"), lote.get("fornecedorNome"),
                    formatarData((Date) lote.get("validade")),
                    Utilidade.formatarMoeda((BigDecimal) lote.get("custoUnitario")),
                    formatarDataHora((Timestamp) lote.get("dataEntrada"))});
        }
        return montarRelatorio(colunas, linhas);
    }

    private Map<String, Object> relatorioVencimentos() throws StokiException {
        String[] colunas = {"Produto", "Nº Lote", "Validade", "Saldo", "Situação"};
        List<Object[]> linhas = new ArrayList<>();
        try {
            for (Map<String, Object> lote : relatorioDAO.listarVencimentos()) {
                int dias = (int) lote.get("dias");
                String situacao = dias < 0 ? "Vencido há " + (-dias) + " dia(s)" : "Vence em " + dias + " dia(s)";
                linhas.add(new Object[]{lote.get("nome"), lote.get("numeroLote"),
                        formatarData((Date) lote.get("validade")),
                        Utilidade.formatarQuantidade((BigDecimal) lote.get("saldo")), situacao});
            }
        } catch (SQLException e) {
            throw new StokiException("Erro ao consultar o relatório de vencimentos.", e);
        }
        return montarRelatorio(colunas, linhas);
    }

    private Map<String, Object> relatorioAbaixoDoMinimo() throws StokiException {
        String[] colunas = {"Produto", "Estoque mínimo", "Saldo válido"};
        List<Object[]> linhas = new ArrayList<>();
        try {
            for (Map<String, Object> p : relatorioDAO.listarAbaixoDoMinimo()) {
                linhas.add(new Object[]{p.get("nome"),
                        Utilidade.formatarQuantidade((BigDecimal) p.get("estoqueMinimo")),
                        Utilidade.formatarQuantidade((BigDecimal) p.get("saldoValido"))});
            }
        } catch (SQLException e) {
            throw new StokiException("Erro ao consultar o relatório de abaixo do mínimo.", e);
        }
        return montarRelatorio(colunas, linhas);
    }

    private Map<String, Object> relatorioMaiorValorParado() throws StokiException {
        String[] colunas = {"Produto", "Saldo", "Valor parado"};
        List<Object[]> linhas = new ArrayList<>();
        try {
            for (Map<String, Object> p : relatorioDAO.listarMaiorValorParado()) {
                linhas.add(new Object[]{p.get("nome"),
                        Utilidade.formatarQuantidade((BigDecimal) p.get("saldo")),
                        Utilidade.formatarMoeda((BigDecimal) p.get("valor"))});
            }
        } catch (SQLException e) {
            throw new StokiException("Erro ao consultar o relatório de maior valor parado.", e);
        }
        return montarRelatorio(colunas, linhas);
    }

    private Map<String, Object> relatorioPerdas() throws StokiException {
        String[] colunas = {"Data", "Produto", "Nº Lote", "Quantidade", "Motivo", "Observação", "Valor perdido"};
        List<Object[]> linhas = new ArrayList<>();
        try {
            for (Map<String, Object> p : relatorioDAO.listarPerdas()) {
                linhas.add(new Object[]{formatarDataHora((Timestamp) p.get("dataHora")), p.get("nome"),
                        p.get("numeroLote"),
                        Utilidade.formatarQuantidade((BigDecimal) p.get("quantidade")),
                        p.get("motivoPerda"), p.get("observacao"),
                        Utilidade.formatarMoeda((BigDecimal) p.get("valor"))});
            }
        } catch (SQLException e) {
            throw new StokiException("Erro ao consultar o relatório de perdas.", e);
        }
        return montarRelatorio(colunas, linhas);
    }

    public Map<String, Object> gerarRelatorio(int opcao) throws StokiException {
        switch (opcao) {
            case 0:
                return relatorioLotes();
            case 1:
                return relatorioVencimentos();
            case 2:
                return relatorioAbaixoDoMinimo();
            case 3:
                return relatorioMaiorValorParado();
            default:
                return relatorioPerdas();
        }
    }
}

