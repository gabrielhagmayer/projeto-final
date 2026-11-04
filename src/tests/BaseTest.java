package tests;

import connections.InicializadorBanco;
import models.Usuario;
import services.AcessoService;
import services.CatalogoService;
import utils.Utilidade;

import java.math.BigDecimal;

/**
 * Confere se a base funciona no computador de cada dev.
 */
public class BaseTest {

    public void iniciar() {
        System.out.println("=== BASE ===");

        banco();
        login();
        catalogo();
        conversoes();
    }

    private void banco() {
        try {
            InicializadorBanco.preparar();
            System.out.println("Banco: Passou");
        } catch (Exception e) {
            System.err.println("Banco: Falhou. " + e.getMessage());
        }
    }

    private void login() {
        try {
            Usuario usuario = new AcessoService().entrar("gerente", "123");
            System.out.println(usuario != null ? "Login: Passou" : "Login: Falhou");
        } catch (Exception e) {
            System.err.println("Login: Falhou. " + e.getMessage());
        }
    }

    private void catalogo() {
        try {
            CatalogoService catalogo = new CatalogoService();
            boolean preenchido = !catalogo.listarSetores().isEmpty()
                    && !catalogo.listarFornecedoresAtivos().isEmpty()
                    && !catalogo.listarProdutosAtivos().isEmpty();
            System.out.println(preenchido ? "Catálogo: Passou" : "Catálogo: Falhou");
        } catch (Exception e) {
            System.err.println("Catálogo: Falhou. " + e.getMessage());
        }
    }

    private void conversoes() {
        boolean decimal = new BigDecimal("6.84").compareTo(Utilidade.paraDecimal("6,84")) == 0;
        boolean anoCurto = Utilidade.paraData("12/04/27") == null;
        boolean dataInvalida = Utilidade.paraData("31/02/2027") == null;
        boolean dataValida = Utilidade.paraData("12/04/2027") != null;
        boolean tudoCerto = decimal && anoCurto && dataInvalida && dataValida;
        System.out.println(tudoCerto ? "Conversões: Passou" : "Conversões: Falhou");
    }
}
