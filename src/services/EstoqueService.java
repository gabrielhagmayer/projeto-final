package services;

import dao.ContagemDAO;
import dao.LoteDAO;
import dao.MovimentacaoDAO;
import exceptions.StokiException;
import models.Lote;
import models.Movimentacao;
import models.Perfil;
import models.Produto;
import utils.Sessao;
import utils.Utilidade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Responsável: Gabriel.
 * Valida os dados e aplica as regras de negócio.
 */
public class EstoqueService {

    private final LoteDAO loteDAO = new LoteDAO();
    private final ContagemDAO contagemDAO = new ContagemDAO();
    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

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
    public void registrarSaida(Produto produto, Lote lote, BigDecimal quantidade) throws StokiException {
        if (Sessao.getUsuario() == null) {
            throw new StokiException("Entre no sistema para registrar saídas.");
        }
        if (contagemDAO.existeContagemAberta()) {
            throw new StokiException("Há uma contagem aberta. Finalize-a antes de registrar saídas.");
        }
       if (produto == null || lote == null){
           throw new StokiException("Escolha o produto e o lote.");
       }
        if (quantidade == null || quantidade.signum() <= 0) {
            throw new StokiException("Informe uma quantidade maior que zero.");
        }
        if ("UN".equals(produto.getUnidadeEstoque()) && quantidade.stripTrailingZeros().scale() > 0) {
            throw new StokiException("Este produto é contado em unidades. Use um número inteiro.");
        }
        if (lote.getValidade() != null && lote.getValidade().isBefore(LocalDate.now())) {
            throw new StokiException("Este lote está vencido. Registre como perda.");
        }
        if (quantidade.compareTo(lote.getSaldo()) > 0) {
            throw new StokiException("A quantidade é maior que o saldo do lote ("
                    + Utilidade.formatarQuantidade(lote.getSaldo()) + ").");
        }
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setLote(lote);
        movimentacao.setUsuario(Sessao.getUsuario());
        movimentacao.setTipo("SAIDA");
        movimentacao.setQuantidade(quantidade.negate());
        movimentacaoDAO.registrar(movimentacao);
    }
    // Repassa ao DAO o pedido dos lotes disponíveis do produto escolhido.
    public ArrayList<Lote> listarLotesDisponiveis(Produto produto) throws StokiException {
        if (produto == null) {
            return new ArrayList<>();
        }
        return loteDAO.listarDisponiveis(produto.getId());
    }
}
