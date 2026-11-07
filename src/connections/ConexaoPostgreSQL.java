package connections;

import exceptions.StokiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/* aqui acontece as conexões do banco PostgreSQL
 e tambem a parte banco.properties fica fora do github pois cada um tem sua propria senha*/

public class ConexaoPostgreSQL {
    private static final String ARQUIVO = "banco.properties";

    private static Connection conexao;

    //vai ler as configurações do banco
    public static Properties lerConfiguracao() throws StokiException {
        Properties dados = new Properties();
        try (FileInputStream arquivo = new FileInputStream(ARQUIVO)) {
            dados.load(arquivo);
            return dados;
        } catch (IOException e) {
            throw new StokiException("Arquivo chamado banco.properties não foi encontrado. "
                    + "Copie o exemplo do banco.properties.exemplo com esse nome e mude para a senha do seu PostgreSQL.", e);
        }
    }

    // Vai inicializar uma conexão com o banco cadastrado.
    public static Connection abrir(Properties dados, String banco) throws SQLException {
        String url = "jdbc:postgresql://" + dados.getProperty("host", "localhost") + ":"
                + dados.getProperty("porta", "5432") + "/" + banco;
        return DriverManager.getConnection(url, dados.getProperty("usuario", "postgres"), dados.getProperty("senha", ""));
    }


    // Devolve a conexão com o banco do STOKI, abrindo-a quando necessário.
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

    // Aqui a conexão atual é fechada.
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

