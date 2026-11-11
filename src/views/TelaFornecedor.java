package views;

import controllers.FornecedorController;
import models.Fornecedor;
import exceptions.StokiException;
import utils.Tema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaFornecedor extends TelaBase {

    private final FornecedorController controller;

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtCnpj;
    private JTextField txtEndereco;
    private JCheckBox chkAtivo;

    private JTable tabela;
    private DefaultTableModel modelTabela;

    private JButton btnCadastrar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public TelaFornecedor() {
        super("fornecedores", "Controle dos fornecedores da loja para manter tudo em ordem.");
        this.controller = new FornecedorController();

        initCustomComponents();
        carregarTabela();
    }

    @Override
    public void aoAbrir() {
        carregarTabela();
    }

    private void initCustomComponents() {
        // Painel Principal dividido em Formulario (Norte/Centro) e Tabela (Sul)
        JPanel painelConteudo = new JPanel(new BorderLayout(10, 10));
        painelConteudo.setBackground(Tema.FUNDO);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- PAINEL FORMULÁRIO E BOTÕES ---
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.setOpaque(false);

        // Formulario (GridBagLayout para alinhamento limpo)
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setOpaque(false);
        painelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Tema.BORDA),
                "Dados do Fornecedor",
                0, 0, Tema.fonte(Font.BOLD, 15), Tema.VERDE_ESCURO
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID (Desabilitado)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(lblId, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        txtId = new JTextField(5);
        txtId.setEditable(false);
        txtId.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(txtId, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(lblNome, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3;
        txtNome = new JTextField(30);
        txtNome.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(txtNome, gbc);

        // CNPJ
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblCnpj = new JLabel("CNPJ:");
        lblCnpj.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(lblCnpj, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        txtCnpj = new JTextField(15);
        txtCnpj.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(txtCnpj, gbc);


        // Endereco
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblEndereco = new JLabel("Endereco:");
        lblEndereco.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(lblEndereco, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        txtEndereco = new JTextField(25);
        txtEndereco.setFont(Tema.fonte(Font.PLAIN,15));
        painelForm.add(txtEndereco, gbc);

        // Ativo
        gbc.gridwidth = 1;
        gbc.gridx = 3; gbc.gridy = 3;
        chkAtivo = new JCheckBox("Ativo");
        chkAtivo.setSelected(true);
        chkAtivo.setFont(Tema.fonte(Font.PLAIN,15));
        chkAtivo.setOpaque(false);
        painelForm.add(chkAtivo, gbc);

        painelSuperior.add(painelForm, BorderLayout.CENTER);

        // Painel de Botões da Ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setOpaque(false);

        btnCadastrar = Tema.botaoPrimario("Cadastrar");
        btnAlterar = Tema.botaoSecundario("Alterar");
        btnExcluir = Tema.botaoPrimario("Excluir");
        btnLimpar = Tema.botaoSecundario("Limpar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);
        painelConteudo.add(painelSuperior, BorderLayout.NORTH);

        // --- PAINEL TABELA ---
        modelTabela = new DefaultTableModel(
                new Object[]{"ID", "Razao_Social", "CNPJ", "Endereco", "Ativo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desabilita edição direta nas células
            }
        };

        tabela = new JTable(modelTabela);
        Tema.estilizarTabela(tabela);

        JScrollPane scrollPane = new JScrollPane(tabela);
        painelConteudo.add(scrollPane, BorderLayout.CENTER);

        // Adicionar painel estilizado ao container principal da TelaBase
        add(painelConteudo);

        // --- EVENTOS ---
        configurarEventos();
    }

    private void configurarEventos() {
        // Seleção de linha na tabela preenche os campos
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int row = tabela.getSelectedRow();
                txtId.setText(modelTabela.getValueAt(row, 0).toString());
                txtNome.setText(modelTabela.getValueAt(row, 1).toString());
                txtCnpj.setText(modelTabela.getValueAt(row, 2).toString());
                txtEndereco.setText(modelTabela.getValueAt(row, 3).toString());
                chkAtivo.setSelected((Boolean) modelTabela.getValueAt(row, 4));
            }
        });

        btnCadastrar.addActionListener(e -> acaoCadastrar());
        btnAlterar.addActionListener(e -> acaoAlterar());
        btnExcluir.addActionListener(e -> acaoExcluir());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void carregarTabela() {
        modelTabela.setRowCount(0);
        try {
            List<Fornecedor> lista = controller.listar();
            for (Fornecedor f : lista) {
                modelTabela.addRow(new Object[]{
                        f.getId(),
                        f.getRazaoSocial(),
                        f.getCnpj(),
                        f.getEndereco(),
                        f.getAtivo()
                });
            }
        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoCadastrar() {
        try {
            Fornecedor f = montarObjeto();
            controller.cadastrar(f);
            JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoAlterar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Fornecedor f = montarObjeto();
            f.setId(Integer.parseInt(txtId.getText()));
            controller.alterar(f);
            JOptionPane.showMessageDialog(this, "Fornecedor alterado com sucesso!");
            limparCampos();
            carregarTabela();
        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void acaoExcluir() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        boolean temLotes;

        try {
            temLotes = controller.temLotes(id);
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
            return;
        }

        if (temLotes) {

            String mensagem = "Este fornecedor possui lotes vinculados e não pode ser excluído. Deseja inativá-lo?";

            if (confirmar(mensagem)) {
                try {
                    controller.inativar(id);
                    mostrarSucesso("Fornecedor inativado com sucesso!");
                    limparCampos();
                    carregarTabela();
                } catch (StokiException e) {
                    mostrarErro(e.getMessage());
                }
            }

        } else {

            if (confirmar("Deseja realmente excluir este fornecedor?")) {
                try {
                    controller.excluir(id);
                    mostrarSucesso("Fornecedor excluído com sucesso!");
                    limparCampos();
                    carregarTabela();
                } catch (StokiException e) {
                    mostrarErro(e.getMessage());
                }
            }

        }

        int confirma = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este fornecedor?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirma == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(id);
                JOptionPane.showMessageDialog(this, "Fornecedor excluído com sucesso!");
                limparCampos();
                carregarTabela();
            } catch (StokiException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            try {
               controller.inativar(id);
                JOptionPane.showMessageDialog(this, "Fornecedor inativado com sucesso!");
                limparCampos();
                carregarTabela();
           } catch (StokiException ex) {
              JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Fornecedor montarObjeto() {
        Fornecedor f = new Fornecedor();
        f.setRazaoSocial(txtNome.getText());
        f.setCnpj(txtCnpj.getText());
        f.setEndereco(txtEndereco.getText());
        f.setAtivo(chkAtivo.isSelected());
        return f;
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtCnpj.setText("");
        txtEndereco.setText("");
        chkAtivo.setSelected(true);
        tabela.clearSelection();
    }
}