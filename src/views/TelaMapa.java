package views;

/**
 * Responsável: Gabriel.
 * Setores, prateleiras e alertas.
 */
public class TelaMapa extends TelaBase {

    public TelaMapa() {
        super("Mapa do depósito", "Setores, prateleiras e alertas.");
        mostrarEmConstrucao("Gabriel");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
