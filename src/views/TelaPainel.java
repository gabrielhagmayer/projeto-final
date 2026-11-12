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
    private final JLabel lblRiscoTotal = new JLabel("-");
    private final JLabel lblRiscoVencido = new JLabel("-");
    private final JLabel lblRiscoPerto = new JLabel("-");
    private final JLabel lblRiscoExcesso = new JLabel("-");
    private final JLabel lblRiscoPerdido = new JLabel("-");

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
        topo.add(criarCartaoRisco(), BorderLayout.SOUTH);
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
            Map<String, BigDecimal> risco = controller.calcularDinheiroEmRisco();
            lblRiscoTotal.setText(Utilidade.formatarMoeda(risco.get("total")));
            lblRiscoVencido.setText(Utilidade.formatarMoeda(risco.get("vencido")));
            lblRiscoPerto.setText(Utilidade.formatarMoeda(risco.get("perto")));
            lblRiscoExcesso.setText(Utilidade.formatarMoeda(risco.get("excesso")));
            lblRiscoPerdido.setText(Utilidade.formatarMoeda(risco.get("perdido")));
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

    private JPanel criarDetalhe(String titulo, JLabel valor) {
        JPanel detalhe = new JPanel(new BorderLayout(0, 4));
        detalhe.setOpaque(false);
        JLabel rotulo = new JLabel(titulo);
        rotulo.setForeground(Tema.BRANCO);
        valor.setFont(Tema.fonte(Font.BOLD, 18));
        valor.setForeground(Tema.BRANCO);
        detalhe.add(rotulo, BorderLayout.NORTH);
        detalhe.add(valor, BorderLayout.CENTER);
        return detalhe;
    }

    private JPanel criarCartaoRisco() {
        JPanel cartao = new JPanel(new BorderLayout(0, 12));
        cartao.setBackground(Tema.VERDE_ESCURO);
        cartao.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel rotulo = new JLabel("Dinheiro em risco");
        rotulo.setForeground(Tema.BRANCO);
        lblRiscoTotal.setFont(Tema.fonte(Font.BOLD, 32));
        lblRiscoTotal.setForeground(Tema.AMBAR);
        JPanel cabecalho = new JPanel(new BorderLayout(0, 4));
        cabecalho.setOpaque(false);
        cabecalho.add(rotulo, BorderLayout.NORTH);
        cabecalho.add(lblRiscoTotal, BorderLayout.CENTER);
        cartao.add(cabecalho, BorderLayout.NORTH);
        cartao.add(criarDetalhes(), BorderLayout.CENTER);
        return cartao;
    }

    private JPanel criarDetalhes() {
        JPanel detalhes = new JPanel(new GridLayout(1, 4, 16, 0));
        detalhes.setOpaque(false);
        detalhes.add(criarDetalhe("Vencido", lblRiscoVencido));
        detalhes.add(criarDetalhe("Perto de vencer", lblRiscoPerto));
        detalhes.add(criarDetalhe("Em excesso", lblRiscoExcesso));
        detalhes.add(criarDetalhe("Perdido no mês", lblRiscoPerdido));
        return detalhes;
    }
}

