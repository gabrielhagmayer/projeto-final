package tests;

import services.AcessoService;
import models.Produto;
import services.CatalogoService;
import services.ProdutoService;
import utils.Sessao;
import java.math.BigDecimal;

/**
 * Responsável: Erick.
 * Cada teste cria e apaga os próprios dados, sem depender de um id fixo.
 */
public class ProdutoTest {

    public void iniciar() {
        System.out.println("=== PRODUTO ===");
        try {
            new AcessoService().entrar("gerente", "123");
        } catch (Exception e) {
            System.err.println("Login: Falhou. " + e.getMessage());
        }
        cadastrar();
        listar();
        alterar();
        excluir();
    }

    private final ProdutoService produtoService = new ProdutoService();

    private Produto montarProduto(String nome) throws Exception {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setSetor(new CatalogoService().listarSetores().get(0));
        produto.setPrateleira(1);
        produto.setUnidadeEstoque("UN");
        produto.setEstoqueMinimo(new BigDecimal("1"));
        produto.setEstoqueMaximo(new BigDecimal("10"));
        produto.setTemValidade(false);
        produto.setCadastradoPor(Sessao.getUsuario());
        return produto;
    }

    private Produto buscarPorNome(String nome) throws Exception {
        for (Produto p : produtoService.listar()) {
            if (p.getNome().equals(nome)) {
                return p;
            }
        }
        return null;
    }

    private void cadastrar() {
        try {
            String nome = "Teste " + System.currentTimeMillis();
            produtoService.cadastrar(montarProduto(nome));
            Produto achado = buscarPorNome(nome);
            if (achado != null) {
                System.out.println("Cadastrar: Passou");
                produtoService.excluir(achado.getId());
            } else {
                System.err.println("Cadastrar: Falhou. Produto não encontrado na lista.");
            }
        } catch (Exception e) {
            System.err.println("Cadastrar: Falhou. " + e.getMessage());
        }
    }

    private void listar() {
        try {
            boolean temProdutos = !produtoService.listar().isEmpty();
            System.out.println(temProdutos ? "Listar: Passou" : "Listar: Falhou");
        } catch (Exception e) {
            System.err.println("Listar: Falhou. " + e.getMessage());
        }
    }

    private void alterar() {
        try {
            String nome = "Teste " + System.currentTimeMillis();
            produtoService.cadastrar(montarProduto(nome));
            Produto produto = buscarPorNome(nome);

            String novoNome = nome + " alterado";
            produto.setNome(novoNome);
            produtoService.atualizar(produto);

            Produto alterado = buscarPorNome(novoNome);
            if (alterado != null) {
                System.out.println("Alterar: Passou");
            } else {
                System.err.println("Alterar: Falhou. Nome novo não encontrado.");
            }
            produtoService.excluir(produto.getId());
        } catch (Exception e) {
            System.err.println("Alterar: Falhou. " + e.getMessage());
        }
    }

    private void excluir() {
        try {
            String nome = "Teste " + System.currentTimeMillis();
            produtoService.cadastrar(montarProduto(nome));
            Produto produto = buscarPorNome(nome);

            produtoService.excluir(produto.getId());

            System.out.println(buscarPorNome(nome) == null ? "Excluir: Passou" : "Excluir: Falhou");
        } catch (Exception e) {
            System.err.println("Excluir: Falhou. " + e.getMessage());
        }
    }
}