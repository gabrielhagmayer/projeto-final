import tests.BaseTest;
import tests.EstoqueTest;
import tests.FornecedorTest;
import tests.ProdutoTest;
import tests.UsuarioTest;

/**
 * Roda todos os testes automatizados. Este arquivo já chama os testes de todos os devs.
 */
public class Teste {

    public static void main(String[] args) {
        new BaseTest().iniciar();
        new ProdutoTest().iniciar();
        new FornecedorTest().iniciar();
        new UsuarioTest().iniciar();
        new EstoqueTest().iniciar();
    }
}
