import connections.InicializadorBanco;
import controllers.AcessoController;
import exceptions.StokiException;
import utils.Tema;
import views.TelaLogin;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Início do sistema: prepara o banco e abre o login.
 */
public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tema.aplicarPadroes();
            try {
                InicializadorBanco.preparar();
                new TelaLogin(new AcessoController()).setVisible(true);
            } catch (StokiException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "STOKI", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
