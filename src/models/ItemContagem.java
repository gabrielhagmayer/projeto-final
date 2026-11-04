package models;

import java.math.BigDecimal;

/**
 * Uma linha da contagem, sempre ligada a um lote.
 */
public class ItemContagem {

    private Integer id; // chave primária
    private Integer contagemId; // contagem a que pertence
    private Lote lote; // lote conferido
    private BigDecimal saldoSistema; // saldo no início da contagem
    private BigDecimal saldoContado; // quantidade encontrada
    private Boolean conferido; // true quando a linha foi conferida
    private String motivo; // obrigatório quando houver diferença

    public ItemContagem() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContagemId() {
        return contagemId;
    }

    public void setContagemId(Integer contagemId) {
        this.contagemId = contagemId;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public BigDecimal getSaldoSistema() {
        return saldoSistema;
    }

    public void setSaldoSistema(BigDecimal saldoSistema) {
        this.saldoSistema = saldoSistema;
    }

    public BigDecimal getSaldoContado() {
        return saldoContado;
    }

    public void setSaldoContado(BigDecimal saldoContado) {
        this.saldoContado = saldoContado;
    }

    public Boolean getConferido() {
        return conferido;
    }

    public void setConferido(Boolean conferido) {
        this.conferido = conferido;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
