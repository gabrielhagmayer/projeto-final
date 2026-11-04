package views;

/**
 * Responsável: Gabriel.
 * Conferência do depósito.
 */
public class TelaContagem extends TelaBase {

    public TelaContagem() {
        super("Contagem", "Conferência do depósito.");
        mostrarEmConstrucao("Gabriel");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
