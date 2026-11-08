package models;

// nome e descrição dos acessos permitidos.

public enum Perfil {
    GERENTE("Gerente/Dono"),
    RESPONSAVEL("Responsável pelo estoque"),
    REPOSITOR("Repositor");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
