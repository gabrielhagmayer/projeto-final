package views;

import controllers.MapaController;
import exceptions.StokiException;
import services.MapaService;
import utils.Tema;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

//Responsável: Gabriel. Setores, prateleiras e alertas. */
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
        return botao;
    }
}