package connections;

import exceptions.StokiException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Prepara o banco na primeira execução: cria o banco stoki, as tabelas e os dados iniciais.
 */
public class InicializadorBanco {

    private static final String TABELAS = "database/01_tabelas.sql";
    private static final String DADOS = "database/02_dados_iniciais.sql";

    /** Cria somente o que ainda não existe. Nunca apaga dados. */
    public static void preparar() throws StokiException {
        Properties dados = ConexaoPostgreSQL.lerConfiguracao();
        criarBancoSeNecessario(dados);
        try {
            Connection conexao = ConexaoPostgreSQL.get();
            if (!existemTabelas(conexao)) {
                executarScripts(conexao);
            }
        } catch (SQLException e) {
            throw new StokiException("Não foi possível criar as tabelas do banco.", e);
        }
    }

    /** Apaga tudo e volta aos dados iniciais. Usado pela classe RecriarBanco. */
    public static void recriar() throws StokiException {
        preparar();
        try {
            Connection conexao = ConexaoPostgreSQL.get();
            try (Statement comando = conexao.createStatement()) {
                comando.execute("DROP SCHEMA public CASCADE; CREATE SCHEMA public;");
            }
            executarScripts(conexao);
        } catch (SQLException e) {
            throw new StokiException("Não foi possível recriar o banco.", e);
        }
    }

    private static void criarBancoSeNecessario(Properties dados) throws StokiException {
        String nome = dados.getProperty("banco", "stoki");
        if (!nome.matches("[a-z][a-z0-9_]*")) {
            throw new StokiException("O nome do banco em banco.properties é inválido.");
        }
        try (Connection conexao = ConexaoPostgreSQL.abrir(dados, "postgres");
             PreparedStatement consulta = conexao.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?")) {
            consulta.setString(1, nome);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) {
                    try (Statement comando = conexao.createStatement()) {
                        comando.execute("CREATE DATABASE " + nome);
                    }
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("No suitable driver")) {
                throw new StokiException("Driver do PostgreSQL não encontrado. Confira o arquivo .jar na pasta libs.", e);
            }
            throw new StokiException("Não foi possível conectar ao PostgreSQL. "
                    + "Confira se ele está ligado e se a senha de banco.properties está certa.", e);
        }
    }

    private static boolean existemTabelas(Connection conexao) throws SQLException {
        try (Statement comando = conexao.createStatement();
             ResultSet resultado = comando.executeQuery("SELECT to_regclass('public.usuario')")) {
            resultado.next();
            return resultado.getString(1) != null;
        }
    }

    /** Executa os dois scripts em uma transação: ou tudo dá certo, ou nada fica salvo. */
    private static void executarScripts(Connection conexao) throws StokiException, SQLException {
        conexao.setAutoCommit(false);
        try (Statement comando = conexao.createStatement()) {
            comando.execute(lerScript(TABELAS));
            comando.execute(lerScript(DADOS));
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    private static String lerScript(String caminho) throws StokiException {
        try {
            return Files.readString(Path.of(caminho), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new StokiException("Script não encontrado: " + caminho
                    + ". Rode o programa a partir da pasta principal do projeto.", e);
        }
    }
}
