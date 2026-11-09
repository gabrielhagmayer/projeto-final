package utils;

import models.Perfil;
import models.Usuario;

// ele vai sinalizar quem etá usando o programa, o login que entrou.
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

    // Verifica se a pessoa logada corresponde com as exigencias de perfil.
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


