package views;

import controllers.AcessoController;
import utils.Tema;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

/**
 * Tela de entrada. Contas novas são criadas somente pelo gerente, dentro do sistema.
 */
public class TelaLogin extends JFrame {

    private final JTextField campoLogin = new JTextField();
    private final JPasswordField campoSenha = new JPasswordField();

    public TelaLogin(AcessoController controller) {
        setTitle("STOKI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 520);
        setLocationRelativeTo(null);
        URL arquivoLogo = getClass().getResource("/recursos/logo.png");

        JPanel fundo = new JPanel(new GridBagLayout());
        fundo.setBackground(Tema.FUNDO);

        JPanel cartao = new JPanel(new GridLayout(0, 1, 0, 8));
        cartao.setBackground(Tema.BRANCO);
        cartao.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));
        cartao.setPreferredSize(new Dimension(360, 400));

        JLabel logo = new JLabel("STOKI", SwingConstants.CENTER);
        logo.setFont(Tema.fonte(Font.BOLD, 30));
        logo.setForeground(Tema.VERDE_ESCURO);
        if (arquivoLogo != null) {
            setIconImage(new ImageIcon(arquivoLogo).getImage());
            logo.setIcon(new ImageIcon(new ImageIcon(arquivoLogo).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH)));
        }

        JButton botaoEntrar = Tema.botaoPrimario("Entrar");
        botaoEntrar.addActionListener(e -> controller.entrar(this, campoLogin.getText(), new String(campoSenha.getPassword())));
        getRootPane().setDefaultButton(botaoEntrar);

        cartao.add(logo);
        cartao.add(new JLabel("Usuário"));
        cartao.add(campoLogin);
        cartao.add(new JLabel("Senha"));
        cartao.add(campoSenha);
        cartao.add(new JLabel());
        cartao.add(botaoEntrar);

        fundo.add(cartao, new GridBagConstraints());
        add(fundo);
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.ERROR_MESSAGE);
    }
}
