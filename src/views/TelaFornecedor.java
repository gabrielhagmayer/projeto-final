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
        super("Fornecedores", "Controle dos fornecedores da loja para manter tudo em ordem.");
        this.controller = new FornecedorController();

        initCustomComponents();
        carregarTabela();
    }

    @Override
    public void aoAbrir() {
        carregarTabela();
    }

    private void initCustomComponents() {
        // Painel Principal
        JPanel painelConteudo = new JPanel(new BorderLayout(10, 10));
        painelConteudo.setBackground(Tema.FUNDO);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- PAINEL FORMULÁRIO E BOTÕES ---
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.setOpaque(false);

        // Formulário
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblId = new JLabel("ID:");
        lblId.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(lblId, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtId = new JTextField(5);
        txtId.setEditable(false);
        txtId.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(txtId, gbc);

        // Nome / Razão Social
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(lblNome, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        txtNome = new JTextField(30);
        txtNome.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(txtNome, gbc);

        // CNPJ
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblCnpj = new JLabel("CNPJ:");
        lblCnpj.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(lblCnpj, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        txtCnpj = new JTextField(15);
        txtCnpj.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(txtCnpj, gbc);

        // Endereço
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(lblEndereco, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        txtEndereco = new JTextField(25);
        txtEndereco.setFont(Tema.fonte(Font.PLAIN, 15));
        painelForm.add(txtEndereco, gbc);

        // Ativo
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        gbc.gridy = 3;
        chkAtivo = new JCheckBox("Ativo");
        chkAtivo.setSelected(true);
        chkAtivo.setFont(Tema.fonte(Font.PLAIN, 15));
        chkAtivo.setOpaque(false);
        painelForm.add(chkAtivo, gbc);

        painelSuperior.add(painelForm, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setOpaque(false);

        btnCadastrar = Tema.botaoPrimario("Cadastrar");
        btnAlterar = Tema.botaoSecundario("Alterar");
        btnExcluir = Tema.botaoSecundario("Excluir");
        btnLimpar = Tema.botaoSecundario("Limpar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);
        painelConteudo.add(painelSuperior, BorderLayout.NORTH);

    }

    }