package utils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

/**
 * Cores e componentes visuais do STOKI. Use esta classe para todas as telas ficarem iguais.
 */
public class Tema {

    public static final Color VERDE = Color.decode("#0F766E");
    public static final Color VERDE_ESCURO = Color.decode("#12312E");
    public static final Color AMBAR = Color.decode("#F59E0B");
    public static final Color FUNDO = Color.decode("#F6F8F7");
    public static final Color BRANCO = Color.WHITE;
    public static final Color TEXTO_SUAVE = Color.decode("#4B635F");
    public static final Color BORDA = Color.decode("#D5DEDB");

    private static final String FONTE = escolherFonte();

    public static Font fonte(int estilo, int tamanho) {
        return new Font(FONTE, estilo, tamanho);
    }

    /** Usa a Inter quando instalada. Sem ela, usa a fonte padrão do computador. */
    private static String escolherFonte() {
        String[] fontes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return Arrays.asList(fontes).contains("Inter") ? "Inter" : Font.SANS_SERIF;
    }

    public static void aplicarPadroes() {
        UIManager.put("Button.font", fonte(Font.BOLD, 14));
        UIManager.put("Label.font", fonte(Font.PLAIN, 14));
        UIManager.put("TextField.font", fonte(Font.PLAIN, 14));
        UIManager.put("PasswordField.font", fonte(Font.PLAIN, 14));
        UIManager.put("ComboBox.font", fonte(Font.PLAIN, 14));
        UIManager.put("Table.font", fonte(Font.PLAIN, 14));
        UIManager.put("TableHeader.font", fonte(Font.BOLD, 14));
        UIManager.put("OptionPane.messageFont", fonte(Font.PLAIN, 14));
    }

    /** Botão da ação principal da tela. */
    public static JButton botaoPrimario(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(VERDE);
        botao.setForeground(BRANCO);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return botao;
    }

    /** Botão das demais ações. */
    public static JButton botaoSecundario(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(BRANCO);
        botao.setForeground(VERDE_ESCURO);
        botao.setFocusPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }

    /** Deixa todas as tabelas com a mesma aparência. */
    public static void estilizarTabela(JTable tabela) {
        tabela.setRowHeight(32);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(BORDA);
        tabela.setSelectionBackground(Color.decode("#D7EFEA"));
        tabela.setSelectionForeground(VERDE_ESCURO);
        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setBackground(VERDE_ESCURO);
        cabecalho.setForeground(BRANCO);
        cabecalho.setReorderingAllowed(false);
    }
}
