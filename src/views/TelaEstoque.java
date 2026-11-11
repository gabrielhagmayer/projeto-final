package views;

import controllers.RelatorioController;

import exceptions.StokiException;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import java.math.BigDecimal;

import java.sql.Date;

import models.Perfil;

import utils.Sessao;
import utils.Tema;
import utils.Utilidade;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.BorderLayout;

/**
 * Responsável: Erick.
 * Lista completa do estoque, com busca e localização.
 */
public class TelaEstoque extends TelaBase {

    private final DefaultTableModel modelo = new DefaultTableModel(new String[]{"Produto", "Setor", "Prateleira", "Saldo", "Medida total", "Validade mais próxima", "Valor"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final RelatorioController controller = new RelatorioController();
    private final JTextField campoBusca = new JTextField(20);
    private final JButton btnBuscar = Tema.botaoPrimario("Buscar");

    private List<Map<String, Object>> todasAsLinhas = new ArrayList<>();

    public TelaEstoque() {
        super("Estoque geral", "Lista completa do estoque, com busca e localização.");
        JTable tabela = new JTable(modelo);
        Tema.estilizarTabela(tabela);
        if (Sessao.temPerfil(Perfil.REPOSITOR)) {
            tabela.removeColumn(tabela.getColumnModel().getColumn(6));
        }
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setOpaque(false);

        painelBusca.add(campoBusca);
        painelBusca.add(btnBuscar);
        btnBuscar.addActionListener(e -> buscar());

        conteudo.add(painelBusca, BorderLayout.NORTH);
        conteudo.add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    /**
     * Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui.
     */
    @Override
    public void aoAbrir() {
        try {
            List<Map<String, Object>> linhas = controller.listarEstoqueGeral();
            todasAsLinhas = linhas;
            preencherTabela(linhas);
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    private void buscar() {
        String texto = campoBusca.getText().trim().toLowerCase();
        List<Map<String, Object>> filtradas = new ArrayList<>();
        for (Map<String, Object> linha : todasAsLinhas) {
            String nome = ((String) linha.get("produtoNome")).toLowerCase();
            if (nome.contains(texto)) {
                filtradas.add(linha);
            }
        }
        preencherTabela(filtradas);
    }

    private void preencherTabela(List<Map<String, Object>> linhas) {
        modelo.setRowCount(0);
        for (Map<String, Object> linha : linhas) {
            modelo.addRow(new Object[]{
                    linha.get("produtoNome"),
                    linha.get("setorNumero") + " - " + linha.get("setorNome"),
                    linha.get("prateleira"),
                    Utilidade.formatarQuantidade((BigDecimal) linha.get("saldo")),
                    calcularMedidaTotal(linha),
                    formatarValidade(linha),
                    Utilidade.formatarMoeda((BigDecimal) linha.get("valor"))
            });
        }
    }

    private String calcularMedidaTotal(Map<String, Object> linha) {
        BigDecimal conteudo = (BigDecimal) linha.get("conteudo");
        if (conteudo == null) {
            return "-";
        }
        BigDecimal saldo = (BigDecimal) linha.get("saldo");
        return Utilidade.formatarQuantidade(saldo.multiply(conteudo)) + " " + linha.get("unidadeConteudo");
    }

    private String formatarValidade(Map<String, Object> linha) {
        Date validade = (Date) linha.get("validade");
        if (validade == null) {
            return "-";
        }
        return Utilidade.formatarData(validade.toLocalDate());
    }
}

