package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registro de tudo que muda o saldo de um lote.
 */
public class Movimentacao {

    private Integer id; // chave primária
    private Lote lote; // lote movimentado
    private Usuario usuario; // quem registrou
    private Integer contagemId; // contagem que gerou o ajuste; opcional
    private String tipo; // ENTRADA, SAIDA, PERDA ou AJUSTE
    private BigDecimal quantidade; // positiva aumenta o saldo; negativa diminui
    private String motivoPerda; // VENCIMENTO, AVARIA ou OUTRO; só em perda
    private String observacao; // opcional
    private LocalDateTime dataHora; // quando aconteceu

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
