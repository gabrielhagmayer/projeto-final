package services;

import dao.UsuarioDAO;
import exceptions.StokiException;
import models.Perfil;
import models.Usuario;
import utils.Sessao;
import utils.Utilidade;

import java.util.List;

//Responsável: Renan.
//Valida os dados e aplica as regras de negócio.

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Confere se quem está logado é o gerente.
    private void validarPermissao() throws StokiException {
        if (!Sessao.temPerfil(Perfil.GERENTE)) {
            throw new StokiException("Somente o gerente pode gerenciar usuários.");
        }

    }

    // Diz se o usuário é o único gerente ativo do sistema.
    private boolean ehUltimoGerenteAtivo(int id) throws StokiException {
        for (Usuario usuario : usuarioDAO.listar()) {
            if (usuario.getId() == id) {
                return usuario.getPerfil() == Perfil.GERENTE
                        && Boolean.TRUE.equals(usuario.getAtivo())
                        && usuarioDAO.contarGerentesAtivos() <= 1;
            }
        }
        return false;
    }

    public List<Usuario> listar() throws StokiException {
        validarPermissao();
        return usuarioDAO.listar();
    }

    public void cadastrar(Usuario usuario) throws StokiException {
        validarPermissao();
        if (usuario == null) {
            throw new StokiException("Usuário inválido.");
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new StokiException("Informe o nome do usuário.");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new StokiException("Informe o login do usuário.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new StokiException("Informe a senha do usuário.");
        }

        if (usuario.getPerfil() == null) {
            throw new StokiException("Selecione o perfil do usuário.");
        }
        usuario.setSenha(Utilidade.gerarHash(usuario.getSenha()));
        usuarioDAO.cadastrar(usuario);
    }

    public void alterar(Usuario usuario) throws StokiException {
        validarPermissao();
        if (usuario == null) {
            throw new StokiException("Usuário inválido.");
        }

        if (usuario.getId() <= 0) {
            throw new StokiException("ID do usuário inválido.");
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new StokiException("Informe o nome do usuário.");
        }

        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new StokiException("Informe o login do usuário.");
        }

        if (usuario.getPerfil() == null) {
            throw new StokiException("Selecione o perfil do usuário.");
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            usuarioDAO.alterarSemSenha(usuario);
        } else {
            usuario.setSenha(Utilidade.gerarHash(usuario.getSenha()));
            usuarioDAO.alterar(usuario);
        }
    }

    public boolean verificarHistorico(int id) throws StokiException {
        validarPermissao();
        return usuarioDAO.verificarHistorico(id);
    }

    public void excluir(int id) throws StokiException {
        validarPermissao();
        if (id <= 0) {
            throw new StokiException("ID do usuário invalido.");
        }
        if (id == Sessao.getUsuario().getId()) {
            throw new StokiException("Você não pode excluir a própria conta.");
        }
        if (ehUltimoGerenteAtivo(id)) {
            throw new StokiException("Não é possível excluir o último gerente ativo.");
        }
        if (usuarioDAO.verificarHistorico(id)) {
            throw new StokiException("Este usuário tem histórico no sistema. Inative em vez de excluir.");
        }

        usuarioDAO.excluir(id);
    }

    public void inativar(int id) throws StokiException {
        validarPermissao();
        if (id <= 0) {
            throw new StokiException("ID do usuário invalido.");
        }
        if (id == Sessao.getUsuario().getId()) {
            throw new StokiException("Você não pode inativar a própria conta.");
        }
        if (ehUltimoGerenteAtivo(id)) {
            throw new StokiException("Não é possível inativar o último gerente ativo.");
        }
        usuarioDAO.inativar(id);
    }
}

