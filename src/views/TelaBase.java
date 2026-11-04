package views;

import utils.Tema;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Modelo de todas as telas. Cada tela coloca seus componentes no painel "conteudo".
 */
public abstract class TelaBase extends JPanel {

    protected final JPanel conteudo = new JPanel(new BorderLayout(0, 16));

    protected TelaBase(String titulo, String descricao) {
        setLayout(new BorderLayout());
        setBackground(Tema.FUNDO);
        setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel rotuloTitulo = new JLabel(titulo);
        rotuloTitulo.setFont(Tema.fonte(Font.BOLD, 26));
        rotuloTitulo.setForeground(Tema.VERDE_ESCURO);

        JLabel rotuloDescricao = new JLabel(descricao);
        rotuloDescricao.setFont(Tema.fonte(Font.PLAIN, 14));
        rotuloDescricao.setForeground(Tema.TEXTO_SUAVE);

        JPanel cabecalho = new JPanel(new BorderLayout(0, 4));
        cabecalho.setOpaque(false);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        cabecalho.add(rotuloTitulo, BorderLayout.NORTH);
        cabecalho.add(rotuloDescricao, BorderLayout.SOUTH);

        conteudo.setOpaque(false);
        add(cabecalho, BorderLayout.NORTH);
        add(conteudo, BorderLayout.CENTER);
    }

    /** A janela principal chama este método sempre que a tela aparece. */
    public abstract void aoAbrir();

    /** Mensagem temporária das telas ainda não implementadas. Apague ao construir a tela. */
    protected final void mostrarEmConstrucao(String responsavel) {
        JLabel aviso = new JLabel("Em construção. Responsável: " + responsavel + ".", SwingConstants.CENTER);
        aviso.setForeground(Tema.TEXTO_SUAVE);
        conteudo.add(aviso, BorderLayout.CENTER);
    }

    protected final void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "STOKI", JOptionPane.INFORMATION_MESSAGE);
    }

    protected final void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.ERROR_MESSAGE);
    }

    /** Pede confirmação antes de uma ação importante. */
    protected final boolean confirmar(String mensagem) {
        int resposta = JOptionPane.showConfirmDialog(this, mensagem, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return resposta == JOptionPane.YES_OPTION;
    }
}
