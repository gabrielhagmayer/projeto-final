package views;

import controllers.RelatorioController;

import exceptions.StokiException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Responsável: Erick.
 * Relatórios em tabelas.
 */
public class TelaRelatorios extends TelaBase {

    private final RelatorioController relatorioController = new RelatorioController();
    private final JComboBox<String> comboRelatorio = new JComboBox<>(new String[]{
            "Lotes", "Vencimentos", "Abaixo do mínimo", "Maior valor parado", "Perdas"});
    private final DefaultTableModel modeloTabela = new DefaultTableModel();
    private final JTable tabelaRelatorio = new JTable(modeloTabela);

    public TelaRelatorios() {
        super("Relatórios", "Relatórios de lotes e stock.");
        initComponents();
        aoAbrir();
    }

    private void initComponents() {
        conteudo.add(comboRelatorio, BorderLayout.NORTH);
        conteudo.add(new JScrollPane(tabelaRelatorio), BorderLayout.CENTER);
        comboRelatorio.addActionListener(e -> carregarRelatorio());
    }

    private void carregarRelatorio() {
        try {
            Map<String, Object> relatorio = relatorioController.gerarRelatorio(comboRelatorio.getSelectedIndex());
            String[] colunas = (String[]) relatorio.get("colunas");
            List<Object[]> linhas = (List<Object[]>) relatorio.get("linhas");
            modeloTabela.setRowCount(0);
            modeloTabela.setColumnIdentifiers(colunas);
            for (Object[] linha : linhas) {
                modeloTabela.addRow(linha);
            }
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    /**
     * Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui.
     */
    @Override
    public void aoAbrir() {
        carregarRelatorio();
    }
}
