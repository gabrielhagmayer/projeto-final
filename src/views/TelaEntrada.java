package views;

/**
 * Responsável: Gabriel.
 * Recebimento de mercadorias.
 */
public class TelaEntrada extends TelaBase {

    public TelaEntrada() {
        super("Entrada de lote", "Recebimento de mercadorias.");
        mostrarEmConstrucao("Gabriel");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
