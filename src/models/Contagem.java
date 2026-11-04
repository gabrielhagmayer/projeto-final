package models;

import java.time.LocalDateTime;

/**
 * Conferência do depósito, feita a cada 15 dias.
 */
public class Contagem {

    private Integer id; // chave primária
    private Usuario usuario; // responsável pela contagem
    private LocalDateTime abertaEm; // quando começou
    private LocalDateTime finalizadaEm; // quando terminou
    private String status; // ABERTA ou FINALIZADA

    public Contagem() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getAbertaEm() {
        return abertaEm;
    }

    public void setAbertaEm(LocalDateTime abertaEm) {
        this.abertaEm = abertaEm;
    }

    public LocalDateTime getFinalizadaEm() {
        return finalizadaEm;
    }

    public void setFinalizadaEm(LocalDateTime finalizadaEm) {
        this.finalizadaEm = finalizadaEm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
