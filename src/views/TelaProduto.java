package views;

/**
 * Responsável: Erick.
 * CRUD de produtos.
 */
public class TelaProduto extends TelaBase {

    public TelaProduto() {
        super("Produtos", "CRUD de produtos.");
        mostrarEmConstrucao("Erick");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
