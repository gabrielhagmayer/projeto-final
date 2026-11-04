package views;

/**
 * Responsável: Erick.
 * Alertas, promoções, valor em estoque e Dinheiro em risco.
 */
public class TelaPainel extends TelaBase {

    public TelaPainel() {
        super("Painel", "Alertas, promoções, valor em estoque e Dinheiro em risco.");
        mostrarEmConstrucao("Erick");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
