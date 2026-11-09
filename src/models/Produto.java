package models;

import java.math.BigDecimal;

// classe que cadastra os produtos, mas não os valores deles
public class Produto {

    private Integer id;
    private String nome;
    private String codigoBarras;
    private Setor setor;
    private Integer prateleira;
    private String unidadeEstoque;
    private BigDecimal conteudo; // usamos BigDecimal, do pacote math do java, para combinar com numeric no banco e tambem fazer contas precisas.
    private String unidadeConteudo;
    private BigDecimal estoqueMinimo;
    private BigDecimal estoqueMaximo;
    private Boolean temValidade;
    private Boolean ativo;
    private Usuario cadastradoPor;

    public Produto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Integer getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(Integer prateleira) {
        this.prateleira = prateleira;
    }

    public String getUnidadeEstoque() {
        return unidadeEstoque;
    }

    public void setUnidadeEstoque(String unidadeEstoque) {
        this.unidadeEstoque = unidadeEstoque;
    }

    public BigDecimal getConteudo() {
        return conteudo;
    }

    public void setConteudo(BigDecimal conteudo) {
        this.conteudo = conteudo;
    }

    public String getUnidadeConteudo() {
        return unidadeConteudo;
    }

    public void setUnidadeConteudo(String unidadeConteudo) {
        this.unidadeConteudo = unidadeConteudo;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public BigDecimal getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(BigDecimal estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public Boolean getTemValidade() {
        return temValidade;
    }

    public void setTemValidade(Boolean temValidade) {
        this.temValidade = temValidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Usuario getCadastradoPor() {
        return cadastradoPor;
    }

    public void setCadastradoPor(Usuario cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }
}
