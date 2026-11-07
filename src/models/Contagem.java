package models;

import java.time.LocalDateTime;

//Confere a contagem do estoque.

public class Contagem {

    private Integer id;
    private Usuario usuario;
    private LocalDateTime abertaEm;
    private LocalDateTime finalizadaEm;
    private String status;

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
