package views;

import controllers.UsuarioController;

import exceptions.StokiException;

import models.Perfil;
import models.Usuario;

import utils.Tema;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

/**
 * Responsável: Erick.
 * CRUD de usuários.
 */
public class TelaUsuario extends TelaBase {

    private UsuarioController usuarioController = new UsuarioController();
    private JTable tabelaUsuarios = new JTable();

    // ID do usuário selecionado na tabela (null se nenhum)
    private Integer usuarioIdSelecionado = null;

    private JTextField txtNome = new JTextField(20);
    private JTextField txtLogin = new JTextField(15);
    private JPasswordField txtSenha = new JPasswordField(15);
    private JComboBox<Perfil> cbPerfil = new JComboBox<>(Perfil.values());

    private JButton btnCadastrar = Tema.botaoPrimario("Cadastrar");
    private JButton btnAlterar = Tema.botaoSecundario("Alterar");
    private JButton btnExcluir = Tema.botaoSecundario("Excluir");
    private JButton btnLimpar = Tema.botaoSecundario("Limpar");

    public TelaUsuario() {
        super("Usuários", "Cadastro de usuários do sistema.");
        // Formulário
        JPanel painelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        painelFormulario.setOpaque(false);
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(txtNome);
        painelFormulario.add(new JLabel("Login:"));
        painelFormulario.add(txtLogin);
        painelFormulario.add(new JLabel("Senha:"));
        painelFormulario.add(txtSenha);
        painelFormulario.add(new JLabel("Perfil:"));
        painelFormulario.add(cbPerfil);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnCadastrar);

        // Formulário + botões na parte de cima
        JPanel painelTopo = new JPanel(new BorderLayout(0, 8));
        painelTopo.setOpaque(false);
        painelTopo.add(painelFormulario, BorderLayout.CENTER);
        painelTopo.add(painelBotoes, BorderLayout.SOUTH);

        // Tudo dentro de "conteudo" (da TelaBase)
        Tema.estilizarTabela(tabelaUsuarios);
        conteudo.add(painelTopo, BorderLayout.NORTH);
        conteudo.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> aoClicarCadastrar());
        btnAlterar.addActionListener(e -> aoClicarAlterar());
        btnExcluir.addActionListener(e -> aoClicarExcluir());
        btnLimpar.addActionListener(e -> limparCampos());

        // Clique na linha: preenche o formulário, senha em branco
        tabelaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaUsuarios.getSelectedRow() != -1) {
                int linha = tabelaUsuarios.getSelectedRow();
                usuarioIdSelecionado = (Integer) tabelaUsuarios.getValueAt(linha, 0);
                txtNome.setText((String) tabelaUsuarios.getValueAt(linha, 1));
                txtLogin.setText((String) tabelaUsuarios.getValueAt(linha, 2));
                cbPerfil.setSelectedItem(tabelaUsuarios.getValueAt(linha, 3));
                txtSenha.setText("");
            }
        });
    }

    /**
     * Chamado sempre que esta tela aparece. Recarrega a tabela.
     */
    @Override
    public void aoAbrir() {
        carregarTabela();
        limparCampos();
    }

    private void carregarTabela() {
        try {
            List<Usuario> lista = usuarioController.listar();
            String[] colunas = {"ID", "Nome", "Login", "Perfil", "Ativo"};

            DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Usuario u : lista) {
                Object[] linha = {
                        u.getId(),
                        u.getNome(),
                        u.getLogin(),
                        u.getPerfil(),
                        Boolean.TRUE.equals(u.getAtivo()) ? "Sim" : "Não"
                };
                modelo.addRow(linha);
            }

            tabelaUsuarios.setModel(modelo);
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    // Monta o Usuario com o que está no formulário
    private Usuario lerFormulario() {
        Usuario usuario = new Usuario();
        usuario.setNome(txtNome.getText().trim());
        usuario.setLogin(txtLogin.getText().trim());
        String senha = new String(txtSenha.getPassword());
        usuario.setSenha(senha.isEmpty() ? null : senha);
        usuario.setPerfil((Perfil) cbPerfil.getSelectedItem());
        return usuario;
    }

    private void aoClicarCadastrar() {
        try {
            usuarioController.cadastrar(lerFormulario());
            mostrarSucesso("Usuário cadastrado com sucesso!");
            aoAbrir();
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void aoClicarAlterar() {
        if (usuarioIdSelecionado == null) {
            mostrarErro("Selecione um usuário na tabela para alterar.");
            return;
        }
        try {
            Usuario usuario = lerFormulario();
            usuario.setId(usuarioIdSelecionado);
            usuarioController.alterar(usuario);
            mostrarSucesso("Usuário alterado com sucesso!");
            aoAbrir();
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void aoClicarExcluir() {
        if (usuarioIdSelecionado == null) {
            mostrarErro("Selecione um usuário na tabela para excluir.");
            return;
        }
        if (!confirmar("Tem certeza que deseja excluir este usuário?")) {
            return;
        }
        try {
            if (usuarioController.verificarHistorico(usuarioIdSelecionado)) {
                if (confirmar("Este usuário tem histórico e não pode ser excluído. Deseja inativar?")) {
                    usuarioController.inativar(usuarioIdSelecionado);
                    mostrarSucesso("Usuário inativado com sucesso!");
                }
            } else {
                usuarioController.excluir(usuarioIdSelecionado);
                mostrarSucesso("Usuário excluído com sucesso!");
            }
            aoAbrir();
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        cbPerfil.setSelectedIndex(0);
        usuarioIdSelecionado = null;
        tabelaUsuarios.clearSelection();
    }
}
