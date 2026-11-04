package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Perfil;
import models.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Consulta usada somente no login. O CRUD de usuários fica em UsuarioDAO.
 */
public class AcessoDAO {

    /** Busca um usuário ativo pelo login. Devolve null quando não encontra. */
    public Usuario buscarAtivoPorLogin(String login) throws StokiException {
        String sql = "SELECT id, nome, login, senha, perfil, ativo FROM usuario WHERE login = ? AND ativo = TRUE";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            preparacao.setString(1, login);
            try (ResultSet resultado = preparacao.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setNome(resultado.getString("nome"));
                usuario.setLogin(resultado.getString("login"));
                usuario.setSenha(resultado.getString("senha"));
                usuario.setPerfil(Perfil.valueOf(resultado.getString("perfil")));
                usuario.setAtivo(resultado.getBoolean("ativo"));
                return usuario;
            }
        } catch (SQLException e) {
            throw new StokiException("Não foi possível consultar o usuário.", e);
        }
    }
}
