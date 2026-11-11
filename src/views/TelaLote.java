package views;

import controllers.LoteController;
import controllers.ProdutoController;
import exceptions.StokiException;
import models.Fornecedor;
import models.Lote;
import models.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Responsável: Erick.
 * Gestão de Lotes e Entradas de Stock.
 */

public class TelaLote extends TelaBase {

    private LoteController loteController = new LoteController();
    private ProdutoController produtoController = new ProdutoController();
    private JTable tabelaLotes = new JTable();

    // Campos visuais do formulário de lote
    private JComboBox<Produto> cbProduto = new JComboBox<>();
    private JComboBox<Fornecedor> cbFornecedor = new JComboBox<>();
    private JTextField txtNumeroLote = new JTextField(15);
    private JTextField txtValidade = new JTextField(10); // Formato: AAAA-MM-DD
    private JTextField txtCustoUnitario = new JTextField(10);

    private JButton btnSalvar = new JButton("Registar Entrada");

    public TelaLote() {
        super("Lotes", "Gestão de Lotes e Entradas de Stock.");

        setLayout(new BorderLayout());

        // Carregar os comboboxes iniciais
        carregarProdutos();
        carregarFornecedores();

        // Painel de formulário organizado em grelha
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
        painelFormulario.add(new JLabel("Produto:"));
        painelFormulario.add(cbProduto);

        painelFormulario.add(new JLabel("Fornecedor:"));
        painelFormulario.add(cbFornecedor);

        painelFormulario.add(new JLabel("Número do Lote:"));
        painelFormulario.add(txtNumeroLote);

        painelFormulario.add(new JLabel("Data de Validade (AAAA-MM-DD):"));
        painelFormulario.add(txtValidade);

        painelFormulario.add(new JLabel("Custo Unitário (R$):"));
        painelFormulario.add(txtCustoUnitario);

        // Painel inferior para o botão Salvar
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotao.add(btnSalvar);

        // Painel central unindo formulário e tabela de listagem
        JPanel painelCentral = new JPanel(new BorderLayout(5, 5));
        painelCentral.add(painelFormulario, BorderLayout.NORTH);
        painelCentral.add(new JScrollPane(tabelaLotes), BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);

        // Ação do botão salvar
        btnSalvar.addActionListener(e -> aoClicarSalvar());

        revalidate();
        repaint();
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoController.listar();
            cbProduto.removeAllItems();
            for (Produto p : produtos) {
                cbProduto.addItem(p);
            }
        } catch (Exception e) {
            // Ignora ou trata erro de carregamento inicial
        }
    }

    private void carregarFornecedores() {
        try {
            // Caso utilize um FornecedorService/Controller, pode carregar aqui.
            // Exemplo:
            // List<Fornecedor> fornecedores = fornecedorController.listar();
            // Para já, mantemos preparado para receber os dados.
        } catch (Exception e) {
            // Ignora caso ainda não tenha a listagem de fornecedores ativa
        }
    }

    @Override
    public void aoAbrir() {
        try {
            List<Lote> listaLotes = loteController.listar();
            String[] colunas = {"ID", "Produto", "Fornecedor", "Nº Lote", "Validade", "Custo Unitário", "Data Entrada"};

            DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Lote l : listaLotes) {
                Object[] linha = {
                        l.getId(),
                        l.getProduto() != null ? l.getProduto().getNome() : "-",
                        l.getFornecedor() != null ? l.getFornecedor().getRazaoSocial() : "-",
                        l.getNumeroLote(),
                        l.getValidade() != null ? l.getValidade().toString() : "Não se aplica",
                        l.getCustoUnitario(),
                        l.getDataEntrada() != null ? l.getDataEntrada().format(formatter) : "-"
                };
                modeloTabela.addRow(linha);
            }

            tabelaLotes.setModel(modeloTabela);
            limparCampos();

        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao carregar lotes", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aoClicarSalvar() {
        try {
            Lote lote = new Lote();
            lote.setProduto((Produto) cbProduto.getSelectedItem());
            lote.setFornecedor((Fornecedor) cbFornecedor.getSelectedItem());
            lote.setNumeroLote(txtNumeroLote.getText().trim());

            String validadeStr = txtValidade.getText().trim();
            if (!validadeStr.isEmpty()) {
                lote.setValidade(LocalDate.parse(validadeStr));
            }

            String custoStr = txtCustoUnitario.getText().trim().replace(",", ".");
            lote.setCustoUnitario(custoStr.isEmpty() ? null : new BigDecimal(custoStr));

            loteController.cadastrar(lote);
            JOptionPane.showMessageDialog(this, "Lote registado com sucesso!");

            aoAbrir();
            limparCampos();

        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Preencha os campos corretamente (Validade no formato AAAA-MM-DD).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNumeroLote.setText("");
        txtValidade.setText("");
        txtCustoUnitario.setText("");
        if (cbProduto.getItemCount() > 0) cbProduto.setSelectedIndex(0);
        if (cbFornecedor.getItemCount() > 0) cbFornecedor.setSelectedIndex(0);
    }
}
