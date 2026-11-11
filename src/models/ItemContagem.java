package models;

import java.math.BigDecimal;

//Contagem feita sempre ligada a um lote

public class ItemContagem {

    private Integer id;
    private Integer contagemId;
    private Lote lote;
    private BigDecimal saldoSistema;
    private BigDecimal saldoContado;
    private Boolean conferido;
    private String motivo;

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
