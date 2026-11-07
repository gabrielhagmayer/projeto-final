package views;

import controllers.FornecedorController;
import exceptions.StokiException;
import models.Fornecedor;
import utils.Tema;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Responsável: Renan.
 * CRUD de fornecedores.
 */
public class TelaFornecedor extends TelaBase {

    private final FornecedorController fornecedorController = new FornecedorController();

    public TelaFornecedor() {
        super("Fornecedores", "CRUD de fornecedores.");
        // Removemos a linha mostrarEmConstrucao
    }

    @Override
    public void aoAbrir() {
        carregarTabela();
    }

    /** Carrega a lista de fornecedores na tabela da interface */
    private void carregarTabela() {
        try {
            ArrayList<Fornecedor> lista = fornecedorController.listar();

            // Aqui carregas a tua DefaultTableModel da tabela da TelaBase
            // Exemplo de preenchimento dos dados:
            /*
            DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
            modelo.setRowCount(0);

            for (Fornecedor f : lista) {
                modelo.addRow(new Object[]{
                    f.getId(),
                    f.getRazaoSocial(),
                    f.getCnpj(),
                    f.isAtivo() ? "Sim" : "Não"
                });
            }
            */
        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Ação do botão Novo / Cadastrar */
    public void acaoCadastrar() {
        String razaoSocial = JOptionPane.showInputDialog(this, "Informe a Razão Social:");
        if (razaoSocial == null) return;

        String cnpj = JOptionPane.showInputDialog(this, "Informe o CNPJ:");
        if (cnpj == null) return;

        String endereco = JOptionPane.showInputDialog(this, "Informe o Endereço:");

        try {
            Fornecedor f = new Fornecedor();
            f.setRazaoSocial(razaoSocial);
            f.setCnpj(cnpj);
            f.setEndereco(endereco);

            fornecedorController.cadastrar(f);
            JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
            carregarTabela();
        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Ação do botão Alterar */
    public void acaoAlterar(Fornecedor fornecedorSelecionado) {
        if (fornecedorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para alterar.");
            return;
        }

        String novaRazao = JOptionPane.showInputDialog(this, "Razão Social:", fornecedorSelecionado.getRazaoSocial());
        if (novaRazao == null) return;

        String novoCnpj = JOptionPane.showInputDialog(this, "CNPJ:", fornecedorSelecionado.getCnpj());
        if (novoCnpj == null) return;

        String novoEndereco = JOptionPane.showInputDialog(this, "Endereço:", fornecedorSelecionado.getEndereco());

        try {
            fornecedorSelecionado.setRazaoSocial(novaRazao);
            fornecedorSelecionado.setCnpj(novoCnpj);
            fornecedorSelecionado.setEndereco(novoEndereco);

            fornecedorController.alterar(fornecedorSelecionado);
            JOptionPane.showMessageDialog(this, "Fornecedor atualizado com sucesso!");
            carregarTabela();
        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Ação do botão Excluir / Inativar */
    public void acaoExcluir(int id) {
        try {
            fornecedorController.excluir(id);
            JOptionPane.showMessageDialog(this, "Fornecedor excluído com sucesso!");
            carregarTabela();
        } catch (StokiException e) {
            // Caso o Service avise que o fornecedor possui lotes vinculados
            if (e.getMessage().contains("lotes vinculados")) {
                int opcao = JOptionPane.showConfirmDialog(
                        this,
                        e.getMessage(),
                        "Inativar Fornecedor",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcao == JOptionPane.YES_OPTION) {
                    try {
                        fornecedorController.inativar(id);
                        JOptionPane.showMessageDialog(this, "Fornecedor inativado com sucesso!");
                        carregarTabela();
                    } catch (StokiException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}