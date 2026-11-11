package views;

import utils.Tema;
import utils.Utilidade;

import java.util.List;
import java.util.Map;

import exceptions.StokiException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.math.BigDecimal;

import controllers.RelatorioController;

/**
 * Responsável: Erick.
 * Alertas, promoções, valor em estoque e Dinheiro em risco.
 */
public class TelaPainel extends TelaBase {

    private final RelatorioController controller = new RelatorioController();
    private final JLabel lblValor = new JLabel("-");
    private final JLabel lblVencidos = new JLabel("-");
    private final JLabel lblPerto = new JLabel("-");
    private final JLabel lblAbaixo = new JLabel("-");
    private final DefaultTableModel modeloPromocoes = new DefaultTableModel(new String[]{"Produto", "Prioridade", "Motivo"}, 0) {
        @Override
        public boolean isCellEditable(int linha, int coluna) {
            return false;
        }
    };
    private final JLabel avisoContagem = new JLabel("A última contagem de estoque passou de 15 dias. Faça uma nova contagem.");

    public TelaPainel() {
        super("Painel", "Alertas, promoções, valor em estoque e Dinheiro em risco.");
        JPanel cartoes = new JPanel(new GridLayout(1, 4, 16, 0));
        cartoes.setOpaque(false);
        cartoes.add(criarCartao("Valor em estoque", lblValor));
        cartoes.add(criarCartao("Vencidos", lblVencidos));
        cartoes.add(criarCartao("Perto de vencer", lblPerto));
        cartoes.add(criarCartao("Abaixo do mínimo", lblAbaixo));

        avisoContagem.setOpaque(true);
        avisoContagem.setBackground(Tema.AMBAR);
        avisoContagem.setForeground(Tema.VERDE_ESCURO);
        avisoContagem.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        avisoContagem.setVisible(false);
        JPanel topo = new JPanel(new BorderLayout(0, 12));
        topo.setOpaque(false);
        topo.add(avisoContagem, BorderLayout.NORTH);
        topo.add(cartoes, BorderLayout.CENTER);
        conteudo.add(topo, BorderLayout.NORTH);

        JTable tabela = new JTable(modeloPromocoes);
        Tema.estilizarTabela(tabela);
        JLabel tituloTabela = new JLabel("Sugestões de promoção");
        tituloTabela.setFont(Tema.fonte(Font.BOLD, 18));
        tituloTabela.setForeground(Tema.VERDE_ESCURO);
        JPanel painelTabela = new JPanel(new BorderLayout(0, 8));
        painelTabela.setOpaque(false);
        painelTabela.add(tituloTabela, BorderLayout.NORTH);
        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
        conteudo.add(painelTabela, BorderLayout.CENTER);
    }

    /**
     * Chamado sempre que esta tela aparece. Recarregue os dados do banco aqui.
     */
    @Override
    public void aoAbrir() {
        try {
            Map<String, Object> painel = controller.montarPainel();
            lblValor.setText(Utilidade.formatarMoeda((BigDecimal) painel.get("valor")));
            lblVencidos.setText(String.valueOf(painel.get("vencidos")));
            lblPerto.setText(String.valueOf(painel.get("pertoDeVencer")));
            lblAbaixo.setText(String.valueOf(painel.get("abaixoDoMinimo")));
            avisoContagem.setVisible((boolean) painel.get("contagemAtrasada"));
            modeloPromocoes.setRowCount(0);
            List<Map<String, Object>> promocoes = (List<Map<String, Object>>) painel.get("promocoes");
            for (Map<String, Object> promocao : promocoes) {
                modeloPromocoes.addRow(new Object[]{promocao.get("produto"), promocao.get("prioridade"), promocao.get("motivo")});
            }
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
    }

    private JPanel criarCartao(String titulo, JLabel numero) {
        JPanel cartao = new JPanel(new BorderLayout(0, 8));
        cartao.setBackground(Tema.BRANCO);
        cartao.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel rotulo = new JLabel(titulo);
        rotulo.setForeground(Tema.TEXTO_SUAVE);
        numero.setFont(Tema.fonte(Font.BOLD, 28));
        numero.setForeground(Tema.VERDE_ESCURO);

        cartao.add(rotulo, BorderLayout.NORTH);
        cartao.add(numero, BorderLayout.CENTER);
        return cartao;
    }
}

