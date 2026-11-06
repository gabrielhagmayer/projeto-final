package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Produto;
import utils.Sessao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável: Erick.
 * Executa o SQL com PreparedStatement.
 */
public class ProdutoDAO {

    // Lista todos os produtos ativos cadastrados no banco de dados.
    public List<Produto> listar() throws StokiException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE ativo = true";

        try (Connection con = ConexaoPostgreSQL.get();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setPrateleira(rs.getObject("prateleira") != null ? rs.getInt("prateleira") : null);
                produto.setUnidadeEstoque(rs.getString("unidade_estoque"));
                produto.setConteudo(rs.getBigDecimal("conteudo"));
                produto.setUnidadeConteudo(rs.getString("unidade_conteudo"));
                produto.setEstoqueMinimo(rs.getBigDecimal("estoque_minimo"));
                produto.setEstoqueMaximo(rs.getBigDecimal("estoque_maximo"));
                produto.setTemValidade(rs.getBoolean("tem_validade"));
                produto.setAtivo(rs.getBoolean("ativo"));

                lista.add(produto);
            }

        } catch (Exception e) {
            throw new StokiException("Erro ao listar produto: " + e.getMessage());
        }

        return lista;
    }

    // Verifica se um produto possui lotes vinculados no estoque.
    public boolean temLotes(long produtoId) throws StokiException {
        String sql = "SELECT COUNT(*) FROM lote WHERE produto_id = ?";

        try (Connection con = ConexaoPostgreSQL.get();
             PreparedStatement stmt = con.prepareStatement(sql)) {

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

    public void cadastrar(Produto produto) throws StokiException {
        // Ordem exata garantida para evitar violação de restrições no banco
        String sql = "INSERT INTO produto (nome, codigo_barras, setor_id, prateleira, unidade_estoque, conteudo, unidade_conteudo, estoque_minimo, estoque_maximo, tem_validade, ativo, cadastrado_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, ?)";

        try (Connection con = ConexaoPostgreSQL.get();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigoBarras());

            if (produto.getSetor() != null && produto.getSetor().getId() != null) {
                stmt.setLong(3, produto.getSetor().getId());
            } else {
                stmt.setNull(3, java.sql.Types.BIGINT);
            }

            // 4. Prateleira (Integer)
            if (produto.getPrateleira() != null) {
                stmt.setInt(4, produto.getPrateleira());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            // 5. Unidade de Estoque
            stmt.setString(5, produto.getUnidadeEstoque());

            // 6. Conteúdo
            stmt.setBigDecimal(6, produto.getConteudo());

            // 7. Unidade de Conteúdo
            if (produto.getUnidadeConteudo() != null && !produto.getUnidadeConteudo().trim().isEmpty()) {
                stmt.setString(7, produto.getUnidadeConteudo());
            } else {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }

            // 8. Estoque Mínimo
            stmt.setBigDecimal(8, produto.getEstoqueMinimo());

            // 9. Estoque Máximo
            stmt.setBigDecimal(9, produto.getEstoqueMaximo());

            // 10. Tem Validade
            stmt.setBoolean(10, produto.getTemValidade() != null ? produto.getTemValidade() : false);

            // 11. Cadastrado Por
            if (Sessao.getUsuario() != null) {
                stmt.setLong(11, Sessao.getUsuario().getId());
            } else {
                stmt.setLong(11, 1L); // Fallback seguro caso a sessão esteja vazia em testes rápidos
            }

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new StokiException("Erro ao cadastrar produto no banco: " + e.getMessage());
        }
    }
}