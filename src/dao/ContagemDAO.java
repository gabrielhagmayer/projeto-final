package dao;

// Responsável: Gabriel.
//faz a execução do SQL com o PreparedStatement

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContagemDAO {

    public boolean existeContagemAberta() throws StokiException {
        String sql = "SELECT 1 FROM contagem WHERE status = 'ABERTA'";

        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = preparacao.executeQuery()){
            return resultado.next();
        } catch (SQLException e) {
            throw new StokiException("Não foi possivel encontrar uma contagem aberta!", e);
        }
    }
}
