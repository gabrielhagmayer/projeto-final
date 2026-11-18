package views;

import controllers.MapaController;
import exceptions.StokiException;
import services.MapaService;
import utils.Tema;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Responsável: Gabriel. Setores, prateleiras e alertas. */
public class TelaMapa extends TelaBase {

    // Vermelho usado só no mapa, para prateleira com lote vencido.
    private static final Color VERMELHO = Color.decode("#DC2626");

    private final MapaController controller = new MapaController();

    // Grade com 3 setores por linha. O 0 quer dizer "quantas linhas forem necessárias".
    private final JPanel painelSetores = new JPanel(new GridLayout(0, 3, 16, 16));

    public TelaMapa() {
        super("Mapa do depósito", "Setores, prateleiras e alertas.");
        painelSetores.setOpaque(false);

        JScrollPane rolagem = new JScrollPane(painelSetores);
        rolagem.setBorder(null);
        rolagem.setOpaque(false);
        rolagem.getViewport().setOpaque(false);
        conteudo.add(rolagem, BorderLayout.CENTER);
        conteudo.add(criarLegenda(), BorderLayout.SOUTH);
    }

    /** Chamado sempre que esta tela aparece. Recarrega o mapa do banco. */
    @Override
    public void aoAbrir() {
        painelSetores.removeAll();
        try {
            Map<Integer, Map<Integer, List<Map<String, Object>>>> mapa = controller.montarMapa();
            for (Map<Integer, List<Map<String, Object>>> prateleiras : mapa.values()) {
                painelSetores.add(criarPainelSetor(prateleiras));
            }
        } catch (StokiException e) {
            mostrarErro(e.getMessage());
        }
        painelSetores.revalidate();
        painelSetores.repaint();
    }

    // Monta o quadro de um setor: borda e título na cor do setor, e um botão por prateleira.
    private JPanel criarPainelSetor(Map<Integer, List<Map<String, Object>>> prateleiras) {
        // Número, nome e cor do setor vêm do primeiro produto do setor.
        Map<String, Object> primeiro = prateleiras.values().iterator().next().get(0);
        Color corSetor = Color.decode((String) primeiro.get("setorCor"));

        JLabel titulo = new JLabel("Setor " + primeiro.get("setorNumero") + " · " + primeiro.get("setorNome"));
        titulo.setFont(Tema.fonte(Font.BOLD, 16));
        titulo.setForeground(corSetor);

        JPanel botoes = new JPanel(new GridLayout(0, 1, 0, 8));
        botoes.setOpaque(false);
        for (Integer numero : prateleiras.keySet()) {
            botoes.add(criarBotaoPrateleira(numero, prateleiras.get(numero)));
        }

        // O NORTH respeita a altura natural, então os botões não esticam.
        JPanel coluna = new JPanel(new BorderLayout(0, 10));
        coluna.setOpaque(false);
        coluna.add(titulo, BorderLayout.NORTH);
        coluna.add(botoes, BorderLayout.CENTER);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Tema.BRANCO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corSetor, 4),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        painel.add(coluna, BorderLayout.NORTH);
        return painel;
    }

    // Cria o botão de uma prateleira, pintado conforme o pior alerta dela.
    private JButton criarBotaoPrateleira(Integer numero, List<Map<String, Object>> produtos) {
        String alerta = controller.alertaDaPrateleira(produtos);
        String texto = "Prateleira " + numero + " (" + produtos.size()
                + (produtos.size() == 1 ? " produto)" : " produtos)");

        JButton botao = new JButton(texto);
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setBackground(Tema.BRANCO);
        botao.setForeground(Tema.VERDE_ESCURO);
        Color corBorda = Tema.BORDA;

        if (MapaService.VENCIDO.equals(alerta)) {
            botao.setBackground(VERMELHO);
            botao.setForeground(Tema.BRANCO);
            corBorda = VERMELHO;
        } else if (MapaService.PERTO.equals(alerta)) {
            botao.setBackground(Tema.AMBAR);
            corBorda = Tema.AMBAR;
        } else if (MapaService.ABAIXO.equals(alerta)) {
            botao.setText("<html><center>" + texto + "<br>Abaixo do mínimo</center></html>");
            corBorda = Tema.AMBAR;
        }

        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corBorda, 3),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // Ao clicar, abre a lista de produtos desta prateleira.
        botao.addActionListener(e -> abrirPrateleira(numero, produtos));
        return botao;
    }

    // Mostra os produtos da prateleira em uma tabela: produto, saldo válido e alerta.
    private void abrirPrateleira(Integer numero, List<Map<String, Object>> produtos) {
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Produto", "Saldo válido", "Alerta"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        for (Map<String, Object> produto : produtos) {
            BigDecimal saldo = (BigDecimal) produto.get("saldoValido");
            modelo.addRow(new Object[]{
                    produto.get("produtoNome"),
                    saldo.stripTrailingZeros().toPlainString(),
                    produto.get("alerta")
            });
        }

        JTable tabela = new JTable(modelo);
        Tema.estilizarTabela(tabela);
        JScrollPane rolagem = new JScrollPane(tabela);
        rolagem.setPreferredSize(new Dimension(520, 40 + produtos.size() * 32));

        String titulo = produtos.get(0).get("setorNome") + " · Prateleira " + numero;
        JOptionPane.showMessageDialog(this, rolagem, titulo, JOptionPane.PLAIN_MESSAGE);
    }

    // Monta a legenda que explica as cores das prateleiras.
    private JPanel criarLegenda() {
        JPanel legenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        legenda.setOpaque(false);
        legenda.add(criarItemLegenda(VERMELHO, VERMELHO, "Lote vencido"));
        legenda.add(criarItemLegenda(Tema.AMBAR, Tema.AMBAR, "Vence em até 30 dias"));
        legenda.add(criarItemLegenda(Tema.BRANCO, Tema.AMBAR, "Abaixo do mínimo"));
        legenda.add(criarItemLegenda(Tema.BRANCO, Tema.BORDA, "Sem alerta"));
        return legenda;
    }

    // Um item da legenda: um quadradinho colorido e o texto ao lado.
    private JPanel criarItemLegenda(Color fundo, Color borda, String texto) {
        JPanel quadrado = new JPanel();
        quadrado.setPreferredSize(new Dimension(18, 18));
        quadrado.setBackground(fundo);
        quadrado.setBorder(BorderFactory.createLineBorder(borda, 3));

        JLabel rotulo = new JLabel(texto);
        rotulo.setForeground(Tema.TEXTO_SUAVE);

        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setOpaque(false);
        item.add(quadrado);
        item.add(rotulo);
        return item;
    }
}