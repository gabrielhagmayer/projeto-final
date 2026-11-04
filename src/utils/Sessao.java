package utils;

import models.Perfil;
import models.Usuario;

/**
 * Guarda quem está usando o sistema depois do login.
 */
public class Sessao {

    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void iniciar(Usuario usuarioLogado) {
        usuario = usuarioLogado;
    }

    public static void encerrar() {
        usuario = null;
    }

    /** Informa se a pessoa logada tem um dos perfis indicados. */
    public static boolean temPerfil(Perfil... perfis) {
        if (usuario == null) {
            return false;
        }
        for (Perfil perfil : perfis) {
            if (usuario.getPerfil() == perfil) {
                return true;
            }
        }
        return false;
    }
}
