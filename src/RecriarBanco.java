import connections.InicializadorBanco;
import exceptions.StokiException;

// ele apaga o que foi feito no banco atual e volta ao banco original, afetando somente o PostgreSQL do seu computador.
public class RecriarBanco {
    //Recria o banco e manda uma mensagem que foi executado.
    public static void main(String[] args) throws StokiException {
        InicializadorBanco.recriar();
        System.out.println("O banco foi recriado com os dados iniciais!");
    }
}
