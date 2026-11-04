package dao;

import connections.ConexaoPostgreSQL;
import exceptions.StokiException;
import models.Fornecedor;
import models.Produto;
import models.Setor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Consultas prontas que qualquer módulo pode usar sem depender do trabalho de outro dev.
 * Este arquivo pertence à base: não altere sem falar com o Gabriel.
 */
public class CatalogoDAO {

    public ArrayList<Setor> listarSetores() throws StokiException {
        String sql = "SELECT id, numero, nome, cor FROM setor ORDER BY numero";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = preparacao.executeQuery()) {
            ArrayList<Setor> setores = new ArrayList<>();
            while (resultado.next()) {
                setores.add(lerSetor(resultado, "id", "numero", "nome", "cor"));
            }
            return setores;
        } catch (SQLException e) {
            throw new StokiException("Não foi possível listar os setores.", e);
        }
    }

    public ArrayList<Fornecedor> listarFornecedoresAtivos() throws StokiException {
        String sql = "SELECT id, razao_social, cnpj, endereco, ativo FROM fornecedor WHERE ativo = TRUE ORDER BY razao_social";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = preparacao.executeQuery()) {
            ArrayList<Fornecedor> fornecedores = new ArrayList<>();
            while (resultado.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setId(resultado.getInt("id"));
                fornecedor.setRazaoSocial(resultado.getString("razao_social"));
                fornecedor.setCnpj(resultado.getString("cnpj"));
                fornecedor.setEndereco(resultado.getString("endereco"));
                fornecedor.setAtivo(resultado.getBoolean("ativo"));
                fornecedores.add(fornecedor);
            }
            return fornecedores;
        } catch (SQLException e) {
            throw new StokiException("Não foi possível listar os fornecedores.", e);
        }
    }

    public ArrayList<Produto> listarProdutosAtivos() throws StokiException {
        String sql = "SELECT p.id, p.nome, p.codigo_barras, p.prateleira, p.unidade_estoque, p.conteudo, "
                + "p.unidade_conteudo, p.estoque_minimo, p.estoque_maximo, p.tem_validade, p.ativo, "
                + "s.id AS setor_id, s.numero AS setor_numero, s.nome AS setor_nome, s.cor AS setor_cor "
                + "FROM produto p JOIN setor s ON s.id = p.setor_id WHERE p.ativo = TRUE ORDER BY p.nome";
        try (PreparedStatement preparacao = ConexaoPostgreSQL.get().prepareStatement(sql);
             ResultSet resultado = preparacao.executeQuery()) {
            ArrayList<Produto> produtos = new ArrayList<>();
            while (resultado.next()) {
                Produto produto = new Produto();
                produto.setId(resultado.getInt("id"));
                produto.setNome(resultado.getString("nome"));
                produto.setCodigoBarras(resultado.getString("codigo_barras"));
                produto.setPrateleira(resultado.getInt("prateleira"));
                produto.setUnidadeEstoque(resultado.getString("unidade_estoque"));
                produto.setConteudo(resultado.getBigDecimal("conteudo"));
                produto.setUnidadeConteudo(resultado.getString("unidade_conteudo"));
                produto.setEstoqueMinimo(resultado.getBigDecimal("estoque_minimo"));
                produto.setEstoqueMaximo(resultado.getBigDecimal("estoque_maximo"));
                produto.setTemValidade(resultado.getBoolean("tem_validade"));
                produto.setAtivo(resultado.getBoolean("ativo"));
                produto.setSetor(lerSetor(resultado, "setor_id", "setor_numero", "setor_nome", "setor_cor"));
                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException e) {
            throw new StokiException("Não foi possível listar os produtos.", e);
        }
    }

    private Setor lerSetor(ResultSet resultado, String id, String numero, String nome, String cor) throws SQLException {
        Setor setor = new Setor();
        setor.setId(resultado.getInt(id));
        setor.setNumero(resultado.getInt(numero));
        setor.setNome(resultado.getString(nome));
        setor.setCor(resultado.getString(cor));
        return setor;
    }
}
