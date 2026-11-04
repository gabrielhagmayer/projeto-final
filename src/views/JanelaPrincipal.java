package views;

import controllers.AcessoController;
import models.Perfil;
import utils.Sessao;
import utils.Tema;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

/**
 * Janela única do sistema: menu lateral à esquerda e a tela escolhida à direita.
 * Mostra somente os itens permitidos ao perfil. Este arquivo pertence à base.
 */
public class JanelaPrincipal extends JFrame {

    private final CardLayout troca = new CardLayout();
    private final JPanel areaTelas = new JPanel(troca);
    private final JPanel menu = new JPanel();
    private final ArrayList<JButton> botoes = new ArrayList<>();

    public JanelaPrincipal(AcessoController controller) {
        setTitle("STOKI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 680));
        setSize(1280, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Tema.VERDE_ESCURO);
        menu.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        JLabel marca = new JLabel("STOKI");
        marca.setFont(Tema.fonte(Font.BOLD, 26));
        marca.setForeground(Tema.BRANCO);
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel perfil = new JLabel(Sessao.getUsuario().getPerfil().getDescricao());
        perfil.setForeground(Color.decode("#BFD6D1"));
        perfil.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(marca);
        menu.add(Box.createVerticalStrut(4));
        menu.add(perfil);
        menu.add(Box.createVerticalStrut(24));

        montarMenu();

        menu.add(Box.createVerticalGlue());
        JButton sair = criarBotaoMenu("Sair");
        sair.addActionListener(e -> controller.sair(this));
        menu.add(sair);

        JScrollPane rolagem = new JScrollPane(menu);
        rolagem.setBorder(null);
        rolagem.setPreferredSize(new Dimension(232, 0));
        rolagem.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(rolagem, BorderLayout.WEST);
        add(areaTelas, BorderLayout.CENTER);

        abrirPrimeiraTela();
    }

    /** Define quais telas cada perfil pode ver. */
    private void montarMenu() {
        boolean gerente = Sessao.temPerfil(Perfil.GERENTE);
        boolean gestao = Sessao.temPerfil(Perfil.GERENTE, Perfil.RESPONSAVEL);

        if (gestao) adicionar("Painel", new TelaPainel());
        adicionar("Estoque geral", new TelaEstoque());
        adicionar("Mapa do depósito", new TelaMapa());
        adicionar("Produtos", new TelaProduto());
        if (gestao) adicionar("Fornecedores", new TelaFornecedor());
        if (gerente) adicionar("Usuários", new TelaUsuario());
        if (gestao) adicionar("Entrada", new TelaEntrada());
        adicionar("Saída", new TelaSaida());
        adicionar("Perdas", new TelaPerda());
        if (gestao) adicionar("Contagem", new TelaContagem());
        if (gestao) adicionar("Relatórios", new TelaRelatorios());
        adicionar("Setores", new TelaSetor());
    }

    private void adicionar(String nome, TelaBase tela) {
        areaTelas.add(tela, nome);
        JButton botao = criarBotaoMenu(nome);
        botao.addActionListener(e -> abrir(nome, tela, botao));
        botao.putClientProperty("tela", tela);
        botoes.add(botao);
        menu.add(botao);
        menu.add(Box.createVerticalStrut(4));
    }

    private JButton criarBotaoMenu(String nome) {
        JButton botao = new JButton(nome);
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setAlignmentX(Component.LEFT_ALIGNMENT);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        botao.setBackground(Tema.VERDE_ESCURO);
        botao.setForeground(Tema.BRANCO);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }

    /** Troca a tela visível e pede para ela recarregar os dados. */
    private void abrir(String nome, TelaBase tela, JButton botaoAtivo) {
        for (JButton botao : botoes) {
            botao.setBackground(botao == botaoAtivo ? Tema.VERDE : Tema.VERDE_ESCURO);
        }
        troca.show(areaTelas, nome);
        tela.aoAbrir();
    }

    private void abrirPrimeiraTela() {
        JButton primeiro = botoes.get(0);
        abrir(primeiro.getText(), (TelaBase) primeiro.getClientProperty("tela"), primeiro);
    }
}
