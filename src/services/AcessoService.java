package services;

import dao.AcessoDAO;
import exceptions.StokiException;
import models.Usuario;
import utils.Sessao;
import utils.Utilidade;

/**
 * Regras do login.
 */
public class AcessoService {

    private final AcessoDAO acessoDAO = new AcessoDAO();

    /** Confere login e senha. Em caso de sucesso, inicia a sessão. */
    public Usuario entrar(String login, String senha) throws StokiException {
        if (login == null || login.isBlank() || senha == null || senha.isEmpty()) {
            throw new StokiException("Informe o usuário e a senha.");
        }
        Usuario usuario = acessoDAO.buscarAtivoPorLogin(login.trim());
        if (usuario == null || !usuario.getSenha().equals(Utilidade.gerarHash(senha))) {
            throw new StokiException("Usuário ou senha incorretos.");
        }
        Sessao.iniciar(usuario);
        return usuario;
    }
}
