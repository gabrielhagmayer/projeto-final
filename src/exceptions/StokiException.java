package exceptions;

// Erro descrito do problema, onde vai aparecer na tela os erros que aconteceram

public class StokiException extends Exception {

    public StokiException(String mensagem) {
        super(mensagem);
    }

    public StokiException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
