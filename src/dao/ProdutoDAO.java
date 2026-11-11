package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Produto;
import models.Setor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 - Responsável: Erick.

 - Executa o SQL com PreparedStatement.

 */
public class ProdutoDAO {

    public List<Produto> listar() throws SQLException, StokiException {
        String sql = "SELECT p.id, p.nome, p.codigo_barras, p.estoque_minimo, p.estoque_maximo, " +
                "p.tem_validade, p.ativo, p.prateleira, p.unidade_estoque, p.conteudo, p.unidade_conteudo, " +
                "s.id as setor_id, s.nome as setor_nome " +
                "FROM produto p LEFT JOIN setor s ON p.setor_id = s.id ORDER BY p.nome";
        List<Produto> produtos = new ArrayList<>();

        try (
                PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setEstoqueMinimo(rs.getBigDecimal("estoque_minimo"));
                produto.setEstoqueMaximo(rs.getBigDecimal("estoque_maximo"));
                produto.setTemValidade(rs.getBoolean("tem_validade"));
                produto.setAtivo(rs.getBoolean("ativo"));
                produto.setPrateleira(rs.getObject("prateleira") != null ? rs.getInt("prateleira") : null);
                produto.setUnidadeEstoque(rs.getString("unidade_estoque"));
                produto.setConteudo(rs.getBigDecimal("conteudo"));
                produto.setUnidadeConteudo(rs.getString("unidade_conteudo"));

                if (rs.getObject("setor_id") != null) {
                    Setor setor = new Setor();
                    setor.setId(rs.getInt("setor_id"));
                    setor.setNome(rs.getString("setor_nome"));
                    produto.setSetor(setor);
                }

                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new StokiException("Erro ao listar produtos no banco de dados: " + e.getMessage());
        }

        return produtos;
    }

    public boolean temLotes(long produtoId) throws StokiException {
        String sql = "SELECT COUNT(*) FROM lote WHERE produto_id = ?";
        try (PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql)) {

            stmt.setLong(1, produtoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            throw new StokiException("Erro ao verificar lotes: " + e.getMessage());
        }
        return false;
    }

    public void cadastrar(Produto produto) throws SQLException, StokiException {
        String sql = "INSERT INTO produto (nome, codigo_barras, estoque_minimo, estoque_maximo, tem_validade, ativo, prateleira, unidade_estoque, conteudo, unidade_conteudo, setor_id, cadastrado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigoBarras());
            stmt.setBigDecimal(3, produto.getEstoqueMinimo());
            stmt.setBigDecimal(4, produto.getEstoqueMaximo());
            stmt.setBoolean(5, produto.getTemValidade() != null ? produto.getTemValidade() : true);
            stmt.setBoolean(6, produto.getAtivo() != null ? produto.getAtivo() : true);

            if (produto.getPrateleira() != null) {
                stmt.setInt(7, produto.getPrateleira());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            stmt.setString(8, produto.getUnidadeEstoque());
            stmt.setBigDecimal(9, produto.getConteudo());
            stmt.setString(10, produto.getUnidadeConteudo());

            if (produto.getSetor() != null && produto.getSetor().getId() != null) {
                stmt.setInt(11, produto.getSetor().getId());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            if (produto.getCadastradoPor() != null && produto.getCadastradoPor().getId() != null) {
                stmt.setInt(12, produto.getCadastradoPor().getId());
            } else {
                throw new StokiException("Erro: É necessário estar logado para cadastrar um produto.");
            }

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new StokiException("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public void atualizar(Produto produto) throws SQLException, StokiException {
        String sql = "UPDATE produto SET nome = ?, codigo_barras = ?, estoque_minimo = ?, estoque_maximo = ?, " +
                "tem_validade = ?, ativo = ?, prateleira = ?, unidade_estoque = ?, conteudo = ?, unidade_conteudo = ?, setor_id = ? WHERE id = ?";

        try (PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigoBarras());
            stmt.setBigDecimal(3, produto.getEstoqueMinimo());
            stmt.setBigDecimal(4, produto.getEstoqueMaximo());
            stmt.setBoolean(5, produto.getTemValidade() != null ? produto.getTemValidade() : true);
            stmt.setBoolean(6, produto.getAtivo() != null ? produto.getAtivo() : true);

            if (produto.getPrateleira() != null) {
                stmt.setInt(7, produto.getPrateleira());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            stmt.setString(8, produto.getUnidadeEstoque());
            stmt.setBigDecimal(9, produto.getConteudo());
            stmt.setString(10, produto.getUnidadeConteudo());

            if (produto.getSetor() != null && produto.getSetor().getId() != null) {
                stmt.setInt(11, produto.getSetor().getId());
            } else {
                stmt.setNull(11, java.sql.Types.INTEGER);
            }

            stmt.setInt(12, produto.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new StokiException("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void inativar(Integer id) throws SQLException, StokiException {
        String sql = "UPDATE produto SET ativo = false WHERE id = ?";
        try (PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StokiException("Erro ao inativar produto: " + e.getMessage());
        }
    }

    public void excluir(Integer id) throws SQLException, StokiException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StokiException("Erro ao excluir produto: " + e.getMessage());
        }
    }

    public BigDecimal saldo(Integer id) throws StokiException {
        String sql = "SELECT COALESCE(SUM(saldo), 0) FROM vw_saldo_lote WHERE produto_id = ?";
        try (PreparedStatement stmt = ConexaoPostgreSQL.get().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (Exception e) {
            throw new StokiException("Erro ao consultar saldo: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}