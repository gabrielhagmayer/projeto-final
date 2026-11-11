package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Registra toda a movimentação dos lotes.

public class Movimentacao {

    private Integer id;
    private Lote lote;
    private Usuario usuario;
    private Integer contagemId; // número da contagem que gerou o ajuste; null nas outras movimentações.
    private String tipo;
    private BigDecimal quantidade;
    private String motivoPerda;
    private String observacao;
    private LocalDateTime dataHora;

    public Movimentacao() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getContagemId() {
        return contagemId;
    }

    public void setContagemId(Integer contagemId) {
        this.contagemId = contagemId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivoPerda() {
        return motivoPerda;
    }

    public void setMotivoPerda(String motivoPerda) {
        this.motivoPerda = motivoPerda;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
