package tests;

import services.AcessoService;
import services.FornecedorService;

import models.Fornecedor;

import java.util.List;
import java.util.Random;

/**
 * Responsável: Renan.
 * Cada teste cria e apaga os próprios dados, sem depender de um id fixo.
 */
public class FornecedorTest {

    private final FornecedorService fornecedorService = new FornecedorService();

    public void iniciar() {
        System.out.println("=== FORNECEDOR ===");
        try {
            new AcessoService().entrar("gerente", "123");
        } catch (Exception e) {
            System.err.println("Login: Falhou. " + e.getMessage());
        }
        listar();
        cadastrar();
        alterar();
        excluir();
    }

    private int calcularDigito(int[] numeros, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < numeros.length; i++) {
            soma += numeros[i] * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }

    private String gerarCnpj() {
        Random aleatorio = new Random();
        int[] base = new int[12];
        for (int i = 0; i < 12; i++) {
            base[i] = aleatorio.nextInt(10); // Sorteia de 0 a 9
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int dig1 = calcularDigito(base, pesos1);

        int[] comDig1 = new int[13];
        for (int i = 0; i < 12; i++) {
            comDig1[i] = base[i];
        }
        comDig1[12] = dig1;

        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int dig2 = calcularDigito(comDig1, pesos2);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(base[i]);
        }
        sb.append(dig1);
        sb.append(dig2);

        return sb.toString();
    }

    private Fornecedor montarFornecedor(String cnpj) throws Exception {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste " + System.currentTimeMillis());
        fornecedor.setCnpj(cnpj);
        fornecedor.setEndereco("Rua Teste, 1");
        fornecedor.setAtivo(true);
        return fornecedor;
    }

    private Fornecedor buscarPorCnpj(String cnpj) throws Exception {
        for (Fornecedor f : fornecedorService.listarTodos()) {
            if (cnpj.equals(f.getCnpj())) {
                return f;
            }
        }
        return null;
    }

    private void listar() {
        try {
            String cnpj = gerarCnpj();
            fornecedorService.cadastrar(montarFornecedor(cnpj));

            Fornecedor achado = buscarPorCnpj(cnpj);
            List<Fornecedor> lista = fornecedorService.listarTodos();

            if (achado != null && !lista.isEmpty()) {
                System.out.println("Listar: Passou");
            } else {
                System.err.println("Listar: Falhou. Fornecedor criado não apareceu na lista.");
            }

            if (achado != null) {
                fornecedorService.excluir(achado.getId());
            }
        } catch (Exception e) {
            System.err.println("Listar: Falhou. " + e.getMessage());
        }
    }

    private void cadastrar() {
        try {
            String cnpj = gerarCnpj();
            fornecedorService.cadastrar(montarFornecedor(cnpj));
            Fornecedor achado = buscarPorCnpj(cnpj);
            if (achado != null) {
                System.out.println("Cadastrar: Passou");
                fornecedorService.excluir(achado.getId());
            } else {
                System.err.println("Cadastrar: Falhou. Fornecedor não encontrado na lista.");
            }
        } catch (Exception e) {
            System.err.println("Cadastrar: Falhou. " + e.getMessage());
        }
    }

    private void alterar() {
        try {
            String cnpj = gerarCnpj();
            fornecedorService.cadastrar(montarFornecedor(cnpj));
            Fornecedor fornecedor = buscarPorCnpj(cnpj);

            String novaRazao = fornecedor.getRazaoSocial() + " alterado";
            fornecedor.setRazaoSocial(novaRazao);
            fornecedorService.alterar(fornecedor);

            Fornecedor alterado = buscarPorCnpj(cnpj);
            if (alterado != null && novaRazao.equals(alterado.getRazaoSocial())) {
                System.out.println("Alterar: Passou");
            } else {
                System.err.println("Alterar: Falhou. Razão social nova não encontrada.");
            }
            fornecedorService.excluir(fornecedor.getId());
        } catch (Exception e) {
            System.err.println("Alterar: Falhou. " + e.getMessage());
        }
    }

    private void excluir() {
        try {
            String cnpj = gerarCnpj();
            fornecedorService.cadastrar(montarFornecedor(cnpj));
            Fornecedor fornecedor = buscarPorCnpj(cnpj);

            fornecedorService.excluir(fornecedor.getId());

            System.out.println(buscarPorCnpj(cnpj) == null ? "Excluir: Passou" : "Excluir: Falhou");
        } catch (Exception e) {
            System.err.println("Excluir: Falhou. " + e.getMessage());
        }
    }
}