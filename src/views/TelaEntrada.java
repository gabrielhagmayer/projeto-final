package views;

import controllers.EstoqueController;
import models.Fornecedor;
import models.Produto;
import utils.Tema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Responsável: Gabriel.
 * Recebimento de mercadorias.
 */
public class TelaEntrada extends TelaBase {

    private final EstoqueController estoqueController = new EstoqueController();
    private final JComboBox<Produto> caixaProduto = new JComboBox<>();
    private final JComboBox<Fornecedor> caixaFornecedor = new JComboBox<>();
    private final JTextField campoNumeroLote = new JTextField();
    private final JTextField campoValidade = new JTextField();
    private final JTextField campoCusto = new JTextField();
    private final JTextField campoQuantidade = new JTextField();

    public TelaEntrada() {
        super("Entrada de lote", "Recebimento de mercadorias.");

        JPanel formulario = new JPanel(new GridLayout(0, 2, 12, 12));
        formulario.add(new JLabel("Produto:"));
        formulario.add(caixaProduto);
        formulario.add(new JLabel("Fornecedor:"));
        formulario.add(caixaFornecedor);
        formulario.add(new JLabel("Número do lote:"));
        formulario.add(campoNumeroLote);
        formulario.add(new JLabel("Validade (dd/mm/aaaa):"));
        formulario.add(campoValidade);
        formulario.add(new JLabel("Custo unitário (R$):"));
        formulario.add(campoCusto);
        formulario.add(new JLabel("Quantidade:"));
        formulario.add(campoQuantidade);

        conteudo.add(formulario, BorderLayout.NORTH);

        JButton botaoRegistrar = Tema.botaoPrimario("Registrar entrada");
        botaoRegistrar.addActionListener(evento -> estoqueController.registrarEntrada(
                this,
                (Produto) caixaProduto.getSelectedItem(),
                (Fornecedor) caixaFornecedor.getSelectedItem(),
                campoNumeroLote.getText(),
                campoValidade.getText(),
                campoCusto.getText(),
                campoQuantidade.getText()));

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acoes.setOpaque(false);
        acoes.add(botaoRegistrar);
        conteudo.add(acoes, BorderLayout.CENTER);
    }

    //Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui.
    @Override
    public void aoAbrir() {
        estoqueController.carregarListas(this);
    }

    // Adiciona os Produtos na caixa de seleção.
    public void preencherProdutos(ArrayList<Produto> produtos) {
        caixaProduto.removeAllItems();
        for (Produto produto : produtos) {
            caixaProduto.addItem(produto);
        }
    }

    // Adiciona os Fornecedores na caixa de seleção.
    public void preencherFornecedores(ArrayList<Fornecedor> fornecedores) {
        caixaFornecedor.removeAllItems();
        for (Fornecedor fornecedor : fornecedores) {
            caixaFornecedor.addItem(fornecedor);
        }
    }

    // informa os erros.
    public void informarErro(String mensagem) {
        mostrarErro(mensagem);
    }

    // Manda uma mensagem se tudo deu certo.
    public void informarSucesso(String mensagem) {
        mostrarSucesso(mensagem);
    }

    // Limpa os campos preenchidos.
    public void limparCampos() {
        campoNumeroLote.setText("");
        campoValidade.setText("");
        campoCusto.setText("");
        campoQuantidade.setText("");
    }
}
