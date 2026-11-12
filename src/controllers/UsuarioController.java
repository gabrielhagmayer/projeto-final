package controllers;

import exceptions.StokiException;
import models.Usuario;
import services.UsuarioService;

import java.util.List;

/**
 * Responsável: Renan.
 * Liga a tela ao UsuarioService e mostra sucesso ou erro.
 */
public class UsuarioController {

    private final UsuarioService usuarioService = new UsuarioService();


    /**
     * Devolve todos os usuarios cadastrados
     */
    public List<Usuario> listar() throws StokiException {
        return usuarioService.listar();
    }

    /**
     * Cadastrar um novo usuario
     */
    public void cadastrar(Usuario usuario) throws StokiException {
        usuarioService.cadastrar(usuario);
    }

    /**
     * Alterar um usuario
     */
    public void alterar(Usuario usuario) throws StokiException {
        usuarioService.alterar(usuario);
    }

    /** Remove um usuário */
    public void excluir(int id) throws StokiException {
        usuarioService.excluir(id);
    }

    /** Inativa um usuário */
    public void inativar(int id) throws StokiException {
        usuarioService.inativar(id);
    }
    /** “Informa se o usuário já tem movimentações registradas”*/
    public boolean verificarHistorico(int id) throws StokiException {
        return usuarioService.verificarHistorico(id);
    }
}

