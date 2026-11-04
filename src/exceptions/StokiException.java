package exceptions;

/**
 * Erro previsto do sistema. A mensagem deve ser clara, porque aparece na tela.
 */
public class StokiException extends Exception {

    public StokiException(String mensagem) {
        super(mensagem);
    }

    public StokiException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
