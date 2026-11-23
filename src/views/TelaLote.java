package views;

import controllers.LoteController;
import controllers.ProdutoController;
import exceptions.StokiException;
import models.Fornecedor;
import models.Lote;
import models.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//Responsável: Erick.
//Consulta dos lotes recebidos.

public class TelaLote extends TelaBase {

    private LoteController loteController = new LoteController();
    private JTable tabelaLotes = new JTable();

    public TelaLote() {
        super("Lotes", "Consulta dos lotes recebidos. A mercadoria entra pela tela Entrada.");
        conteudo.add(new JScrollPane(tabelaLotes), BorderLayout.CENTER);
    }

    @Override
    public void aoAbrir() {
        try {
            List<Lote> listaLotes = loteController.listar();
            String[] colunas = {"ID", "Produto", "Fornecedor", "Nº Lote", "Validade", "Custo Unitário", "Data Entrada"};

            DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Lote l : listaLotes) {
                Object[] linha = {
                        l.getId(),
                        l.getProduto() != null ? l.getProduto().getNome() : "-",
                        l.getFornecedor() != null ? l.getFornecedor().getRazaoSocial() : "-",
                        l.getNumeroLote(),
                        l.getValidade() != null ? l.getValidade().toString() : "Não se aplica",
                        l.getCustoUnitario(),
                        l.getDataEntrada() != null ? l.getDataEntrada().format(formatter) : "-"
                };
                modeloTabela.addRow(linha);
            }

            tabelaLotes.setModel(modeloTabela);

        } catch (StokiException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro ao carregar lotes", JOptionPane.ERROR_MESSAGE);
        }
    }
}
