package views;

//Responsável: Gabriel.
// Baixa com indicação do lote que vence primeiro.

import controllers.EstoqueController;
import models.Lote;
import models.Produto;
import utils.Tema;
import utils.Utilidade;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaSaida extends TelaBase {
    private final EstoqueController estoqueController = new EstoqueController();
    private final JComboBox<Produto> caixaProduto = new JComboBox<>();
    private final JComboBox<Lote> caixaLote = new JComboBox<>();
    private final JLabel rotuloSugerido = new JLabel();
    private final JLabel rotuloSaldo = new JLabel();
    private final JTextField campoQuantidade = new JTextField();

    public TelaSaida() {
        super("Saída", "Baixa com indicação do lote que vence primeiro.");

        JPanel formulario = new JPanel(new GridLayout(0, 2, 12, 12));
        formulario.add(new JLabel("Produto:"));
        formulario.add(caixaProduto);
        formulario.add(new JLabel("Lote:"));
        formulario.add(caixaLote);
        formulario.add(new JLabel(""));
        formulario.add(rotuloSugerido);
        formulario.add(new JLabel("Saldo do lote:"));
        formulario.add(rotuloSaldo);
        formulario.add(new JLabel("Quantidade:"));
        formulario.add(campoQuantidade);
        conteudo.add(formulario, BorderLayout.NORTH);
        caixaProduto.addActionListener(evento -> estoqueController.carregarLotes(this, (Produto) caixaProduto.getSelectedItem()));
        caixaLote.addActionListener(evento -> atualizarLoteEscolhido());

        JButton botaoRegistrar = Tema.botaoPrimario("Registrar saída");
        botaoRegistrar.addActionListener(evento -> estoqueController.registrarSaida(
                this,
                (Produto) caixaProduto.getSelectedItem(),
                (Lote) caixaLote.getSelectedItem(),
                campoQuantidade.getText()));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acoes.setOpaque(false);
        acoes.add(botaoRegistrar);
        conteudo.add(acoes, BorderLayout.CENTER);
    }

    //Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui.
    @Override
    public void aoAbrir() {
        estoqueController.carregarProdutosSaida(this);
    }
    // Adiciona os produtos na caixa de seleção.
    public void preencherProdutos(ArrayList<Produto> produtos) {
        caixaProduto.removeAllItems();
        for (Produto produto : produtos) {
            caixaProduto.addItem(produto);
        }
    }
    // Adiciona os lotes disponíveis, já na ordem do FEFO.
    public void preencherLotes(ArrayList<Lote> lotes) {
        caixaLote.removeAllItems();
        for (Lote lote : lotes) {
            caixaLote.addItem(lote);
        }
        atualizarLoteEscolhido();
    }
    // Mostra o saldo do lote escolhido e marca o sugerido.
    private void atualizarLoteEscolhido() {
        Lote lote = (Lote) caixaLote.getSelectedItem();
        if (lote == null) {
            rotuloSaldo.setText("");
            rotuloSugerido.setText("");
            return;
        }
        rotuloSaldo.setText(Utilidade.formatarQuantidade(lote.getSaldo()));
        rotuloSugerido.setText(caixaLote.getSelectedIndex() == 0 ? "Sugerido (vence primeiro)" : "");
    }

    // Informa os erros.
    public void informarErro(String mensagem) {
        mostrarErro(mensagem);
    }

    // Avisa quando tudo deu certo.
    public void informarSucesso(String mensagem) {
        mostrarSucesso(mensagem);
    }

    // Limpa o campo da quantidade.
    public void limparQuantidade() {
        campoQuantidade.setText("");
    }
}
