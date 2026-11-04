package utils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HexFormat;
import java.util.Locale;

/**
 * Conversões usadas por todo o sistema. Quando o texto é inválido, os métodos devolvem null.
 */
public class Utilidade {

    private static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final Locale BRASIL = Locale.of("pt", "BR");

    /** Aceita 1,5 e 1.5, além de 1.234,56. */
    public static BigDecimal paraDecimal(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        String valor = texto.trim();
        if (valor.contains(",")) {
            valor = valor.replace(".", "").replace(",", ".");
        }
        try {
            return new BigDecimal(valor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer paraInteiro(String texto) {
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    /** Aceita somente dd/MM/aaaa, com ano de quatro dígitos e data que exista no calendário. */
    public static LocalDate paraData(String texto) {
        try {
            return LocalDate.parse(texto.trim(), DATA);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public static String formatarData(LocalDate data) {
        return data == null ? "" : data.format(DATA);
    }

    /** Mostra um valor em reais, como R$ 1.234,56. */
    public static String formatarMoeda(BigDecimal valor) {
        DecimalFormat formato = new DecimalFormat("R$ #,##0.00", DecimalFormatSymbols.getInstance(BRASIL));
        return formato.format(valor == null ? BigDecimal.ZERO : valor);
    }

    /** Mostra uma quantidade sem zeros desnecessários, como 7 ou 1,5. */
    public static String formatarQuantidade(BigDecimal valor) {
        DecimalFormat formato = new DecimalFormat("#,##0.###", DecimalFormatSymbols.getInstance(BRASIL));
        return formato.format(valor == null ? BigDecimal.ZERO : valor);
    }

    /** Gera o hash SHA-256 usado para guardar senhas. */
    public static String gerarHash(String texto) {
        try {
            MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(algoritmo.digest(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível.", e);
        }
    }
}
