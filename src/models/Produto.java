package models;

import java.math.BigDecimal;

/**
 * Cadastro de um item do depósito. O saldo não fica aqui: ele vem dos lotes.
 */
public class Produto {

    private Integer id; // chave primária
    private String nome; // nome do produto
    private String codigoBarras; // opcional
    private Setor setor; // setor onde o produto fica
    private Integer prateleira; // número da prateleira
    private String unidadeEstoque; // UN, KG ou L
    private BigDecimal conteudo; // conteúdo de cada unidade, como 2 em 2 L; opcional
    private String unidadeConteudo; // mL, L, g ou kg; opcional
    private BigDecimal estoqueMinimo; // abaixo disso gera alerta
    private BigDecimal estoqueMaximo; // acima disso é excesso
    private Boolean temValidade; // true quando cada lote exige validade
    private Boolean ativo; // false quando foi inativado
    private Usuario cadastradoPor; // quem cadastrou

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
