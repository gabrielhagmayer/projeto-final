package connections;

import exceptions.StokiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abre a conexão com o PostgreSQL usando os dados do arquivo banco.properties.
 * Esse arquivo fica fora do GitHub, porque cada computador tem a própria senha.
 */
public class ConexaoPostgreSQL {

    private static final String ARQUIVO = "banco.properties";
    private static Connection conexao;

    /** Devolve a conexão com o banco do STOKI, abrindo-a quando necessário. */
    public static Connection get() throws StokiException {
        try {
            if (conexao == null || conexao.isClosed()) {
                Properties dados = lerConfiguracao();
                conexao = abrir(dados, dados.getProperty("banco", "stoki"));
            }
            return conexao;
        } catch (SQLException e) {
            throw new StokiException("Não foi possível acessar o banco de dados.", e);
        }
    }

    /** Lê o arquivo banco.properties da pasta principal do projeto. */
    public static Properties lerConfiguracao() throws StokiException {
        Properties dados = new Properties();
        try (FileInputStream arquivo = new FileInputStream(ARQUIVO)) {
            dados.load(arquivo);
            return dados;
        } catch (IOException e) {
            throw new StokiException("Arquivo banco.properties não encontrado. "
                    + "Copie banco.properties.exemplo com esse nome e coloque a senha do seu PostgreSQL.", e);
        }
    }

    /** Abre uma conexão com o banco informado. */
    public static Connection abrir(Properties dados, String banco) throws SQLException {
        String url = "jdbc:postgresql://" + dados.getProperty("host", "localhost") + ":"
                + dados.getProperty("porta", "5432") + "/" + banco;
        return DriverManager.getConnection(url, dados.getProperty("usuario", "postgres"), dados.getProperty("senha", ""));
    }

    /** Fecha a conexão atual. Usado antes de recriar o banco. */
    public static void fechar() {
        try {
            if (conexao != null) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
        conexao = null;
    }
}
