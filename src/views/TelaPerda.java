package views;

//Responsável: Gabriel.
// Perdas por vencimento, avaria ou outro motivo.

import controllers.EstoqueController;
import models.Lote;
import models.Produto;
import utils.Tema;
import utils.Utilidade;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaPerda extends TelaBase {
    private final EstoqueController estoqueController = new EstoqueController();
    private final JComboBox<Produto> caixaProduto = new JComboBox<>();
    private final JComboBox<Lote> caixaLote = new JComboBox<>();
    private final JLabel rotuloSaldo = new JLabel();
    private final JTextField campoQuantidade = new JTextField();
    private final JComboBox<String> caixaMotivo = new JComboBox<>(new String[]{"VENCIMENTO", "AVARIA", "OUTRO"});
    private final JTextField campoObservacao = new JTextField();

    public TelaPerda() {
        super("Perdas", "Vencimento, avaria ou outro motivo, só de lotes ainda no depósito.");

        JPanel formulario = new JPanel(new GridLayout(0, 2, 12, 12));
        formulario.add(new JLabel("Produto:"));
        formulario.add(caixaProduto);
        formulario.add(new JLabel("Lote:"));
        formulario.add(caixaLote);
        formulario.add(new JLabel("Saldo do lote:"));
        formulario.add(rotuloSaldo);
        formulario.add(new JLabel("Quantidade:"));
        formulario.add(campoQuantidade);
        formulario.add(new JLabel("Motivo:"));
        formulario.add(caixaMotivo);
        formulario.add(new JLabel("Observação:"));
        formulario.add(campoObservacao);
        conteudo.add(formulario, BorderLayout.NORTH);

        caixaProduto.addActionListener(evento -> estoqueController.carregarLotesPerda(this, (Produto) caixaProduto.getSelectedItem()));
        caixaLote.addActionListener(evento -> atualizarLoteEscolhido());

        JButton botaoRegistrar = Tema.botaoPrimario("Registrar perda");
        botaoRegistrar.addActionListener(evento -> estoqueController.registrarPerda(
                this,
                (Produto) caixaProduto.getSelectedItem(),
                (Lote) caixaLote.getSelectedItem(),
                campoQuantidade.getText(),
                (String) caixaMotivo.getSelectedItem(),
                campoObservacao.getText()));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acoes.setOpaque(false);
        acoes.add(botaoRegistrar);
        conteudo.add(acoes, BorderLayout.CENTER);
    }

    // Chamado sempre que esta tela aparece. Recarrega os produtos.
    @Override
    public void aoAbrir() {
        estoqueController.carregarProdutosPerda(this);
    }

    // Adiciona os produtos na caixa de seleção.
    public void preencherProdutos(ArrayList<Produto> produtos) {
        caixaProduto.removeAllItems();
        for (Produto produto : produtos) {
            caixaProduto.addItem(produto);
        }
    }

    // Adiciona os lotes com saldo, inclusive os vencidos, que aparecem primeiro.
    public void preencherLotes(ArrayList<Lote> lotes) {
        caixaLote.removeAllItems();
        for (Lote lote : lotes) {
            caixaLote.addItem(lote);
        }
        atualizarLoteEscolhido();
    }

    // Mostra o saldo do lote escolhido.
    private void atualizarLoteEscolhido() {
        Lote lote = (Lote) caixaLote.getSelectedItem();
        if (lote == null) {
            rotuloSaldo.setText("");
            return;
        }
        rotuloSaldo.setText(Utilidade.formatarQuantidade(lote.getSaldo()));
    }

    // Informa os erros.
    public void informarErro(String mensagem) {
        mostrarErro(mensagem);
    }

    // Avisa quando tudo deu certo.
    public void informarSucesso(String mensagem) {
        mostrarSucesso(mensagem);
    }

    // Limpa a quantidade e a observação.
    public void limparCampos() {
        campoQuantidade.setText("");
        campoObservacao.setText("");
    }
}