package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// classe que recebe cada recebimento de um produto

public class Lote {

    private Integer id;
    private Produto produto;
    private Fornecedor fornecedor;
    private String numeroLote;
    private LocalDate validade;//mostra o dia
    private BigDecimal custoUnitario;
    private LocalDateTime dataEntrada;//mostra a data e a hora
    private BigDecimal saldo; // o valor vem do vw_saldo_lote, programado no banco de dados e não é coluna da tabela teste.

    public Lote() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
