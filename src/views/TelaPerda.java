package views;

/**
 * Responsável: Gabriel.
 * Registro de perdas no depósito.
 */
public class TelaPerda extends TelaBase {

    public TelaPerda() {
        super("Perdas", "Registro de perdas no depósito.");
        mostrarEmConstrucao("Gabriel");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
