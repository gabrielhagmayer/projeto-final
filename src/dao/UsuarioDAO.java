package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Perfil;
import models.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável: Renan.
 * Executa o SQL com PreparedStatement.
 */
public class UsuarioDAO {

    public List<Usuario> listar() throws StokiException {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id, nome, login, perfil, ativo FROM usuario ORDER BY nome";

        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql)) {

            try (ResultSet resultado = preparacao.executeQuery()) {

                while (resultado.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setId(resultado.getInt("id"));
                    usuario.setNome(resultado.getString("nome"));
                    usuario.setLogin(resultado.getString("login"));
                    usuario.setPerfil(Perfil.valueOf(resultado.getString("perfil")));
                    usuario.setAtivo(resultado.getBoolean("ativo"));

                    usuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            throw new StokiException("Não foi possível listar os usuários.", e);
        }

        return usuarios;
    }


    public void cadastrar(Usuario usuario) throws StokiException {

        String sql = "INSERT INTO usuario (nome, login, senha, perfil, ativo) "
                + "VALUES (?, ?, ?, ?, TRUE)";

        try (PreparedStatement preparacao =
                     ConexaoPostgreSQL.get().prepareStatement(sql)) {

            preparacao.setString(1, usuario.getNome());
            preparacao.setString(2, usuario.getLogin());
            preparacao.setString(3, usuario.getSenha());
            preparacao.setString(4, usuario.getPerfil().name());

            preparacao.executeUpdate();

        } catch (SQLException e) {
            throw new StokiException("Não foi possível cadastrar o usuário.", e);
        }
    }

    public void alterar(Usuario usuario) throws StokiException{
        String sql = "UPDATE usuario SET nome = ?, login = ?, "
                + "senha = ?, perfil = ? WHERE id = ?";

        try (PreparedStatement preparacao =
                ConexaoPostgreSQL.get().prepareStatement(sql)) {

            preparacao.setString(1, usuario.getNome());
            preparacao.setString(2, usuario.getLogin());
            preparacao.setString(3, usuario.getSenha());
            preparacao.setString(4, usuario.getPerfil().name());
            preparacao.setInt(5, usuario.getId());

            preparacao.executeUpdate();

        } catch (SQLException e) {
            throw new StokiException("Não foi possível alterar o usuário.", e);
        }

        }

        public boolean verificarHistorico(int id) throws StokiException {

            String sql = "SELECT EXISTS ("
                    + "SELECT 1 FROM movimentacao WHERE usuario_id = ? "
                    + "UNION ALL "
                    + "SELECT 1 FROM contagem WHERE usuario_id = ? "
                    + "UNION ALL "
                    + "SELECT 1 FROM produto WHERE cadastrado_por = ?"
                    + ")";

            try (PreparedStatement preparacao =
                         ConexaoPostgreSQL.get().prepareStatement(sql)) {

                preparacao.setInt(1, id);
                preparacao.setInt(2, id);
                preparacao.setInt(3, id);

                try (ResultSet resultado = preparacao.executeQuery()) {
                    if (resultado.next()) {
                        return resultado.getBoolean(1);
                    }

                }

            } catch (SQLException e) {
                throw new StokiException(
                        "Não foi possível verificar o histórico do usuário.", e);
            }

            return false;
        }

    }


