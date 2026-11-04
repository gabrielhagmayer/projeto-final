package models;

/**
 * Empresa que entrega mercadorias.
 */
public class Fornecedor {

    private Integer id; // chave primária
    private String razaoSocial; // nome da empresa
    private String cnpj; // 14 números, sem pontuação
    private String endereco; // endereço
    private Boolean ativo; // false quando foi inativado

    public Fornecedor() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
