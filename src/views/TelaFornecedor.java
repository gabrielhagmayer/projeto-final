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
}