package views;

/**
 * Responsável: Gabriel.
 * Baixa com indicação do lote que vence primeiro.
 */
public class TelaSaida extends TelaBase {

    public TelaSaida() {
        super("Saída", "Baixa com indicação do lote que vence primeiro.");
        mostrarEmConstrucao("Gabriel");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
