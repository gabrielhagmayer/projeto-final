package services;

import dao.LoteDAO;
import exceptions.StokiException;
import models.Lote;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Responsável: Erick.
 * Valida os dados e aplica as regras de negócio para os Lotes.
 */

public class LoteService {

    private LoteDAO loteDAO = new LoteDAO();

    // Valida as regras de negócio e cadastra um novo lote no sistema.
    public void cadastrar(Lote lote) throws StokiException, SQLException {
        // 1. Validar se o produto foi informado
        if (lote.getProduto() == null || lote.getProduto().getId() == null) {
            throw new StokiException("O produto associado ao lote é obrigatório.");
        }

        // Valida o número do lote
        if (lote.getNumeroLote() == null || lote.getNumeroLote().trim().isEmpty()) {
            throw new StokiException("O número do lote é obrigatório.");
        }

        // Valida o custo unitário (não pode ser negativo ou nulo)
        if (lote.getCustoUnitario() == null || lote.getCustoUnitario().signum() < 0) {
            throw new StokiException("O custo unitário deve ser informado e não pode ser negativo.");
        }

        // Valida as regra de validade se o produto exigir
        // (Caso o produto exija validade, a data não pode ser nula e não pode estar vencida)
        if (lote.getProduto().getTemValidade() != null && lote.getProduto().getTemValidade()) {
            if (lote.getValidade() == null) {
                throw new StokiException("Este produto exige controle de validade por lote. Informe a data de validade.");
            }
            if (lote.getValidade().isBefore(LocalDate.now())) {
                throw new StokiException("A data de validade não pode ser anterior à data atual.");
            }
        }

        // Se passou por todas as validações, chama o DAO para salvar no banco
        loteDAO.cadastrar(lote);
    }

    // Lista todos os lotes cadastrados.
    public List<Lote> listar() throws StokiException {
        try {
            return loteDAO.listar();
        } catch (Exception e) {
            throw new StokiException("Erro ao listar lotes no serviço: " + e.getMessage());
        }
    }
}
