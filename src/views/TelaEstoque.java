package views;

/**
 * Responsável: Erick.
 * Lista completa do estoque, com busca e localização.
 */
public class TelaEstoque extends TelaBase {

    public TelaEstoque() {
        super("Estoque geral", "Lista completa do estoque, com busca e localização.");
        mostrarEmConstrucao("Erick");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
