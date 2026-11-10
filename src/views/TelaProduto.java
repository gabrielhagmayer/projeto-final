package views;

import controllers.ProdutoController;
import controllers.SetorController;
import exceptions.StokiException;
import models.Produto;
import models.Perfil;
import models.Setor;
import utils.Sessao;
import utils.Tema;
import utils.Utilidade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Responsável: Erick.
 * CRUD de produtos
 */
public class TelaProduto extends TelaBase {

    private ProdutoController produtoController = new ProdutoController();
    private JTable tabelaProdutos = new JTable();

    // ID do produto selecionado para edição (null se for novo cadastro)
    private Integer produtoIdSelecionado = null;

    // Campos visuais do formulário cobrindo os atributos da classe Produto
    private JTextField txtNome = new JTextField(20);
    private JTextField txtCodigoBarras = new JTextField(15);
    private JTextField txtEstoqueMinimo = new JTextField(10);
    private JTextField txtEstoqueMaximo = new JTextField(10);
    private JTextField txtConteudo = new JTextField(10);
    private JTextField txtPrateleira = new JTextField(10);

    // Campos obrigatórios mapeados da classe Produto
    private JComboBox<Setor> cbSetor = new JComboBox<>();
    private JComboBox<String> cbUnidadeEstoque = new JComboBox<>(new String[]{"UN", "KG", "L"});
    private JCheckBox chkTemValidade = new JCheckBox("Exige controle de validade por lote");
    private JComboBox<String> cbUnidadeConteudo = new JComboBox<>(new String[]{"","mL", "L", "g", "kg"});

    private JButton btnSalvar = Tema.botaoPrimario("Salvar");

    public TelaProduto() {
        super("Produtos", "CRUD de produtos.");

        // Carrega os setores no JComboBox ao iniciar a tela
        carregarSetores();

        // Criação e organização do painel de formulário ajustado para os novos campos
        JPanel painelFormulario = new JPanel(new GridLayout(10, 2, 5, 5));
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Código de Barras:"));
        painelFormulario.add(txtCodigoBarras);

        painelFormulario.add(new JLabel("Setor:"));
        painelFormulario.add(cbSetor);

        painelFormulario.add(new JLabel("Unidade de Estoque:"));
        painelFormulario.add(cbUnidadeEstoque);

        painelFormulario.add(new JLabel("Estoque Mínimo:"));
        painelFormulario.add(txtEstoqueMinimo);

        painelFormulario.add(new JLabel("Estoque Máximo:"));
        painelFormulario.add(txtEstoqueMaximo);

        painelFormulario.add(new JLabel("Conteúdo:"));
        painelFormulario.add(txtConteudo);

        painelFormulario.add(new JLabel("Validade:"));
        painelFormulario.add(chkTemValidade);

        painelFormulario.add(new JLabel("Prateleira:"));
        painelFormulario.add(txtPrateleira);

        painelFormulario.add(new JLabel("Unidade de Conteúdo:"));
        painelFormulario.add(cbUnidadeConteudo);

        // Painel inferior para o botão Salvar não ficar espremido
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotao.add(btnSalvar);

        // Criação de um painel central para dividir o formulário e a listagem em tabela
        JPanel painelCentral = new JPanel(new BorderLayout(5, 5));
        painelCentral.add(painelFormulario, BorderLayout.NORTH);
        painelCentral.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        Tema.estilizarTabela(tabelaProdutos);

        // Adiciona os componentes na tela principal
        conteudo.add(painelCentral, BorderLayout.CENTER);
        conteudo.add(painelBotao, BorderLayout.SOUTH);

        if (Sessao.temPerfil(Perfil.REPOSITOR)) {
            painelFormulario.setVisible(false);
            painelBotao.setVisible(false);
        }

        // Associa o clique do botão ao método de salvar
        btnSalvar.addActionListener(e -> aoClicarSalvar());

        // Evento para selecionar um produto da tabela e preencher para edição
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaProdutos.getSelectedRow() != -1) {
                int linhaSelecionada = tabelaProdutos.getSelectedRow();
                produtoIdSelecionado = (Integer) tabelaProdutos.getValueAt(linhaSelecionada, 0);

                try {
                    List<Produto> lista = produtoController.listar();
                    for (Produto p : lista) {
                        if (p.getId().equals(produtoIdSelecionado)) {
                            txtNome.setText(p.getNome());
                            txtCodigoBarras.setText(p.getCodigoBarras());
                            txtEstoqueMinimo.setText(p.getEstoqueMinimo() != null ? p.getEstoqueMinimo().toString() : "");
                            txtEstoqueMaximo.setText(p.getEstoqueMaximo() != null ? p.getEstoqueMaximo().toString() : "");
                            txtConteudo.setText(p.getConteudo() != null ? p.getConteudo().toString() : "");

                            // Preenchendo os novos campos na edição
                            txtPrateleira.setText(p.getPrateleira() != null ? p.getPrateleira().toString() : "");
                            cbUnidadeConteudo.setSelectedItem(p.getUnidadeConteudo());
                            cbUnidadeEstoque.setSelectedItem(p.getUnidadeEstoque());
                            chkTemValidade.setSelected(p.getTemValidade() != null ? p.getTemValidade() : false);

                            if (p.getSetor() != null) {
                                for (int i = 0; i < cbSetor.getItemCount(); i++) {
                                    Setor s = cbSetor.getItemAt(i);
                                    if (s.getId().equals(p.getSetor().getId())) {
                                        cbSetor.setSelectedIndex(i);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                } catch (StokiException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao selecionar", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        revalidate();
        repaint();
    }

    private void carregarSetores() {
        try {
            services.CatalogoService catalogoService = new services.CatalogoService();
            java.util.ArrayList<Setor> setores = catalogoService.listarSetores();

            cbSetor.removeAllItems();
            for (Setor s : setores) {
                cbSetor.addItem(s);
            }
        } catch (Exception e) {
           mostrarErro(e.getMessage());
        }
    }

    /**
     * Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui.
     */
    @Override
    public void aoAbrir() {
        try {
            List<Produto> listaProdutos = produtoController.listar();
            String[] colunas = {"ID", "Nome", "Código de Barras", "Setor", "Est. Mínimo", "Est. Máximo"};

            DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Produto p : listaProdutos) {
                Object[] linha = {
                        p.getId(),
                        p.getNome(),
                        p.getCodigoBarras() != null ? p.getCodigoBarras() : "-",
                        p.getSetor() != null ? p.getSetor().getNome() : "-",
                        Utilidade.formatarQuantidade(p.getEstoqueMinimo()),
                        Utilidade.formatarQuantidade(p.getEstoqueMaximo())
                };
                modeloTabela.addRow(linha);
            }

            tabelaProdutos.setModel(modeloTabela);
            limparCampos();

        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao carregar produtos", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método acionado ao clicar no botão Salvar (Cadastra ou Atualiza)
    private void aoClicarSalvar() {
        try {
            Produto produto = new Produto();

            if (produtoIdSelecionado != null) {
                produto.setId(produtoIdSelecionado);
            }

            produto.setNome(txtNome.getText());
            produto.setCodigoBarras(txtCodigoBarras.getText());

            produto.setCadastradoPor(utils.Sessao.getUsuario());

            // Preenchendo todos os campos obrigatórios e opcionais da classe Produto
            produto.setSetor((Setor) cbSetor.getSelectedItem());

            String unidadeEstoque = (String) cbUnidadeEstoque.getSelectedItem();
            produto.setUnidadeEstoque(unidadeEstoque);
            if (txtConteudo.getText().isBlank() || unidadeEstoque.equals("KG") || unidadeEstoque.equals("L")) {
                produto.setConteudo(null);
                produto.setUnidadeConteudo(null);
            } else {
                BigDecimal conteudo = Utilidade.paraDecimal(txtConteudo.getText());
                if (conteudo == null) {
                    mostrarErro("Informe um conteúdo válido.");
                    return;
                }
                produto.setConteudo(conteudo);
                produto.setUnidadeConteudo((String) cbUnidadeConteudo.getSelectedItem());
            }

            produto.setTemValidade(chkTemValidade.isSelected());

            // Tratamento seguro para prateleira (inteiro)
            Integer prateleira = Utilidade.paraInteiro(txtPrateleira.getText());
            if (prateleira == null) {
                mostrarErro("Informe uma prateleira válida");
                return;
            }
            produto.setPrateleira(prateleira);

            // Tratamento seguro para campos numéricos BigDecimal (substituindo vírgula por ponto para evitar erros)
            BigDecimal minimo = Utilidade.paraDecimal(txtEstoqueMinimo.getText());
            if (minimo == null) {
                mostrarErro("Informe um estoque mínimo válido.");
                return;
            }
            produto.setEstoqueMinimo(minimo);

            BigDecimal maximo = Utilidade.paraDecimal(txtEstoqueMaximo.getText());
            if (maximo == null) {
                mostrarErro("Informe um estoque máximo válido.");
                return;
            }
            produto.setEstoqueMaximo(maximo);

            if (produtoIdSelecionado == null) {
                produtoController.cadastrar(produto);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            } else {
                produtoController.atualizar(produto);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            }

            aoAbrir();
            limparCampos();

        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preencha os campos numéricos com valores válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCodigoBarras.setText("");
        txtEstoqueMinimo.setText("");
        txtEstoqueMaximo.setText("");
        txtConteudo.setText("");
        txtPrateleira.setText("");
        chkTemValidade.setSelected(false);
        if (cbSetor.getItemCount() > 0) cbSetor.setSelectedIndex(0);
        if (cbUnidadeEstoque.getItemCount() > 0) cbUnidadeEstoque.setSelectedIndex(0);
        if (cbUnidadeConteudo.getItemCount() > 0) cbUnidadeConteudo.setSelectedIndex(0);
        produtoIdSelecionado = null;
    }

    private void aoClicarInativar() {
        if (produtoIdSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para inativar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja inativar este produto?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                produtoController.inativar(produtoIdSelecionado);
                JOptionPane.showMessageDialog(this, "Produto inativado com sucesso!");
                aoAbrir();
                limparCampos();
            } catch (StokiException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao inativar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}