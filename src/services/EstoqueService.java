package services;

import dao.ContagemDAO;
import dao.LoteDAO;
import exceptions.StokiException;
import models.Lote;
import models.Perfil;
import utils.Sessao;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Responsável: Gabriel.
 * Valida os dados e aplica as regras de negócio.
 */
public class EstoqueService {

    private final LoteDAO loteDAO = new LoteDAO();
    private final ContagemDAO contagemDAO = new ContagemDAO();

    public void registrarEntrada(Lote lote, BigDecimal quantidade, BigDecimal unidadesPorCaixa) throws StokiException {
        if (!Sessao.temPerfil(Perfil.GERENTE, Perfil.RESPONSAVEL)) {
            throw new StokiException("Você não tem permissão para registrar entradas.");
        }
        if (contagemDAO.existeContagemAberta()) {
            throw new StokiException("Há uma contagem aberta. Finalize-a antes de registrar entradas.");
        }
        if (lote.getProduto() == null || lote.getFornecedor() == null) {
            throw new StokiException("Escolha o produto e o fornecedor.");
        }
        if (lote.getNumeroLote() == null || lote.getNumeroLote().isBlank()) {
            throw new StokiException("Informe o número do lote.");
        }
        if (lote.getCustoUnitario() == null || lote.getCustoUnitario().signum() < 0) {
            throw new StokiException("Informe um custo válido.");
        }
        if (quantidade == null || quantidade.signum() <= 0) {
            throw new StokiException("Informe uma quantidade maior que zero.");
        }
        if (unidadesPorCaixa != null) {
            if (unidadesPorCaixa.signum() <= 0 || unidadesPorCaixa.stripTrailingZeros().scale() > 0) {
                throw new StokiException("Informe as unidades por caixa como um número inteiro maior que zero.");
            }
            quantidade = quantidade.multiply(unidadesPorCaixa);
        }
        if ("UN".equals(lote.getProduto().getUnidadeEstoque()) && quantidade.stripTrailingZeros().scale() > 0) {
            throw new StokiException("Este produto é contado em unidades. Use um número inteiro.");
        }

        if (Boolean.TRUE.equals(lote.getProduto().getTemValidade()) && lote.getValidade() == null) {
            throw new StokiException("Este produto exige validade.");
        }
        if (lote.getValidade() != null && lote.getValidade().isBefore(LocalDate.now())) {
            throw new StokiException("A validade informada já passou.");
        }
        loteDAO.registrarEntrada(lote, quantidade, Sessao.getUsuario());
    }
}
