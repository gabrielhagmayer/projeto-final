package controllers;

import exceptions.StokiException;
import services.AcessoService;
import utils.Sessao;
import views.JanelaPrincipal;
import views.TelaLogin;

/**
 * Controla a entrada e a saída do sistema.
 */
public class AcessoController {

    private final AcessoService acessoService = new AcessoService();

    /** Faz o login e abre a janela principal. */
    public void entrar(TelaLogin tela, String login, String senha) {
        try {
            acessoService.entrar(login, senha);
            tela.dispose();
            new JanelaPrincipal(this).setVisible(true);
        } catch (StokiException e) {
            tela.mostrarErro(e.getMessage());
        }
    }

    /** Encerra a sessão e volta para o login. */
    public void sair(JanelaPrincipal janela) {
        Sessao.encerrar();
        janela.dispose();
        new TelaLogin(this).setVisible(true);
    }
}
