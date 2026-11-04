package views;

import controllers.SetorController;
import models.Setor;
import utils.Tema;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Tela de exemplo já pronta. Mostra o caminho completo: tela, Controller, Service, DAO e banco.
 */
public class TelaSetor extends TelaBase {

    private final SetorController controller = new SetorController();
    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"Número", "Setor", "Cor"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };

    public TelaSetor() {
        super("Setores", "Organização do depósito por setores numerados.");
        JTable tabela = new JTable(modelo);
        Tema.estilizarTabela(tabela);
        conteudo.add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    @Override
    public void aoAbrir() {
        controller.listar(this);
    }

    /** Recebe os dados do Controller e atualiza a tabela. */
    public void preencherTabela(ArrayList<Setor> setores) {
        modelo.setRowCount(0);
        for (Setor setor : setores) {
            modelo.addRow(new Object[]{setor.getNumero(), setor.getNome(), setor.getCor()});
        }
    }

    public void informarErro(String mensagem) {
        mostrarErro(mensagem);
    }
}
