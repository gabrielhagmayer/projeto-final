package tests;

import controllers.UsuarioController;

import models.Usuario;
import models.Perfil;

import services.AcessoService;

/**
 * Responsável: Erick.
 * Cada teste cria e apaga os próprios dados, sem depender de um id fixo.
 */
public class UsuarioTest {

    public void iniciar() {
        System.out.println("=== USUARIO ===");
        try {
            new AcessoService().entrar("gerente", "123");
        } catch (Exception e) {
            System.err.println("Login: Falhou. " + e.getMessage());
        }
        cadastrar();
        listar();
        alterar();
        excluir();
    }

    private final UsuarioController usuarioController = new UsuarioController();

    private Usuario montarUsuario(String login) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario " + login);
        usuario.setLogin(login);
        usuario.setSenha("123");
        usuario.setPerfil(Perfil.REPOSITOR);
        return usuario;
    }

    private Usuario buscarPorLogin(String login) throws Exception {
        for (Usuario u : usuarioController.listar()) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    private void cadastrar() {
        try {
            String login = "teste" + System.currentTimeMillis();
            usuarioController.cadastrar(montarUsuario(login));
            Usuario achado = buscarPorLogin(login);
            if (achado != null) {
                System.out.println("Cadastrar: Passou");
                usuarioController.excluir(achado.getId());
            } else {
                System.err.println("Cadastrar: Falhou. Usuário não encontrado na lista.");
            }
        } catch (Exception e) {
            System.err.println("Cadastrar: Falhou. " + e.getMessage());
        }
    }

    private void listar() {
        try {
            boolean temUsuarios = !usuarioController.listar().isEmpty();
            System.out.println(temUsuarios ? "Listar: Passou" : "Listar: Falhou.");
        } catch (Exception e) {
            System.err.println("Listar: Falhou. " + e.getMessage());
        }
    }

    private void alterar() {
        try {
            String login = "teste" + System.currentTimeMillis();
            usuarioController.cadastrar(montarUsuario(login));
            Usuario usuario = buscarPorLogin(login);

            String novoNome = usuario.getNome() + " alterado";
            usuario.setNome(novoNome);
            usuario.setSenha("123");
            usuarioController.alterar(usuario);

            Usuario alterado = buscarPorLogin(login);
            if (alterado != null && novoNome.equals(alterado.getNome())) {
                System.out.println("Alterar: Passou");
            } else {
                System.err.println("Alterar: Falhou. Nome novo não encontrado.");
            }
            usuarioController.excluir(usuario.getId());
        } catch (Exception e) {
            System.err.println("Alterar: Falhou. " + e.getMessage());
        }
    }

    private void excluir() {
        try {
            String login = "teste" + System.currentTimeMillis();
            usuarioController.cadastrar(montarUsuario(login));
            Usuario usuario = buscarPorLogin(login);

            usuarioController.excluir(usuario.getId());

            System.out.println(buscarPorLogin(login) == null ? "Excluir: Passou" : "Excluir: Falhou.");
        } catch (Exception e) {
            System.err.println("Excluir: Falhou. " + e.getMessage());
        }
    }
}