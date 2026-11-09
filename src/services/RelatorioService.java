package services;

import dao.RelatorioDAO;
import exceptions.StokiException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
}

