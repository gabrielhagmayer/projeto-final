import connections.InicializadorBanco;
import exceptions.StokiException;

/**
 * Apaga os dados do banco local e volta ao estado inicial do projeto.
 * Afeta somente o PostgreSQL do seu computador.
 */
public class RecriarBanco {

    public static void main(String[] args) throws StokiException {
        InicializadorBanco.recriar();
        System.out.println("Banco recriado com os dados iniciais.");
    }
}
