package models;

/**
 * Categoria e localização no depósito. Exemplo: setor 3, Laticínios.
 */
public class Setor {

    private Integer id; // chave primária
    private Integer numero; // número do setor
    private String nome; // nome do setor
    private String cor; // cor em hexadecimal, como #2E7D32

    public Setor() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
