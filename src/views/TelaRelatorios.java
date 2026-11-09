package views;

import controllers.RelatorioController;

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
    private JTable tabelaRelatorio;
    private DefaultTableModel modeloTabela;

    public TelaRelatorios() {
        super("Relatórios", "Relatórios de lotes e stock.");
        initComponents();
        aoAbrir();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Configuração das colunas do relatório
        String[] colunas = {"ID Lote", "Nº Lote", "Produto", "Fornecedor", "Validade", "Custo Unitário", "Data Entrada"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Apenas leitura
            }
        };

        tabelaRelatorio = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaRelatorio);
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com botão de atualizar
        JPanel painelInferior = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar Relatório");
        btnAtualizar.addActionListener(e -> aoAbrir());
        painelInferior.add(btnAtualizar);

        add(painelInferior, BorderLayout.SOUTH);
    }

    /** Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui. */
    @Override
    public void aoAbrir() {
        modeloTabela.setRowCount(0); // Limpa os dados anteriores
        List dados = relatorioController.buscarRelatorioSaldoLotes();

        if (dados != null) {
            for (Object obj : dados) {
                Map linha = (Map) obj;
                Object[] rowData = {
                        linha.get("loteId"),
                        linha.get("numeroLote"),
                        linha.get("produtoNome"),
                        linha.get("fornecedorNome"),
                        linha.get("validade"),
                        linha.get("custoUnitario"),
                        linha.get("dataEntrada")
                };
                modeloTabela.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar os dados do relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}