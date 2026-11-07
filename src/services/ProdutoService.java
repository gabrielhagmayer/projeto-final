package services;

import dao.ProdutoDAO;
import exceptions.StokiException;
import models.Produto;
import utils.Sessao;

import java.util.List;

/**
 * Responsável: Erick.
 * Valida os dados e aplica as regras de negócio.
 */

public class ProdutoService {

        private ProdutoDAO produtoDAO = new ProdutoDAO();

        /**
         * Valida as regras de negócio e cadastra um novo produto no sistema.
         */
        public void cadastrar(Produto produto) throws StokiException {
            // 1. Validar campos obrigatórios (usando getSetor() e prateleira convertida para String)
            if (produto.getNome() == null || produto.getNome().trim().isEmpty() ||
                    produto.getSetor() == null ||
                    produto.getPrateleira() == null || produto.getPrateleira().toString().trim().isEmpty() ||
                    produto.getUnidadeEstoque() == null ||
                    produto.getEstoqueMinimo() == null ||
                    produto.getEstoqueMaximo() == null ||
                    produto.getTemValidade() == null) {

                throw new StokiException("Preencha todos os campos obrigatórios do produto.");
            }

            // 2. Validar regras de valores (máximo deve ser maior que mínimo e sem negativos)
            if (produto.getEstoqueMinimo().signum() < 0 || produto.getEstoqueMaximo().signum() < 0) {
                throw new StokiException("Os valores de estoque não podem ser negativos.");
            }

            if (produto.getEstoqueMaximo().compareTo(produto.getEstoqueMinimo()) <= 0) {
                throw new StokiException("O estoque máximo deve ser maior que o estoque mínimo.");
            }

            // 3. Tratar código de barras opcional (se vazio, define como null)
            if (produto.getCodigoBarras() == null || produto.getCodigoBarras().trim().isEmpty()) {
                produto.setCodigoBarras(null);
            }

            // 4. Se passou pelas validações, chama o DAO para salvar no banco
            produtoDAO.cadastrar(produto);
        }

        public List<Produto> listar() throws StokiException {
            try {
                return produtoDAO.listar();
            } catch (Exception e) {
                throw new StokiException("Erro ao listar produtos no serviço: " + e.getMessage());
            }
        }
    }


