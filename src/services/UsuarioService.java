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

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new StokiException("Informe a senha do usuário.");
        }

        if (usuario.getPerfil() == null) {
            throw new StokiException("Selecione o perfil do usuário.");
        }

        usuarioDAO.alterar(usuario);
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

        usuarioDAO.excluir(id);
    }

    public void inativar(int id) throws StokiException {
        validarPermissao();
        if (id <= 0) {
            throw new StokiException("ID do usuário invalido.");
        }

        usuarioDAO.inativar(id);

    }
}

