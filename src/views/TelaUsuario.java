package views;

/**
 * Responsável: Renan.
 * CRUD de usuários.
 */
public class TelaUsuario extends TelaBase {

    public TelaUsuario() {
        super("Usuários", "CRUD de usuários.");
        mostrarEmConstrucao("Renan");
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
    }
}
