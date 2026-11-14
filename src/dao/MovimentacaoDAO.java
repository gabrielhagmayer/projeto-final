package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Movimentacao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Responsável: Gabriel.
 * Executa o SQL com PreparedStatement.
 */
public class MovimentacaoDAO {

    //Registar as movimetações do Stoki
    public void registrar(Movimentacao movimentacao) throws StokiException {
        String sql = "INSERT INTO movimentacao (lote_id, usuario_id, tipo, quantidade, motivo_perda, observacao) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement inserir = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            inserir.setInt(1, movimentacao.getLote().getId());
            inserir.setInt(2, movimentacao.getUsuario().getId());
            inserir.setString(3, movimentacao.getTipo());
            inserir.setBigDecimal(4, movimentacao.getQuantidade());
            inserir.setString(5, movimentacao.getMotivoPerda());
            inserir.setString(6, movimentacao.getObservacao());
            inserir.executeUpdate();
        } catch (SQLException e) {
            throw new StokiException("Não foi possível registrar a movimentação.", e);
        }
    }
}
