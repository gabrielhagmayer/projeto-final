package views;

/**
 * Responsável: Renan.
 * CRUD de fornecedores.
 */
public class TelaFornecedor extends TelaBase {

    public TelaFornecedor() {
        super("Fornecedores", "CRUD de fornecedores.");
        mostrarEmConstrucao("Renan");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
