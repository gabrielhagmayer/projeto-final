package services;

import dao.MapaDAO;
import exceptions.StokiException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsável: Gabriel.
 * Organiza os produtos do mapa por setor e prateleira e decide o alerta de cada um.
 */
public class MapaService {

    // Textos dos alertas, do mais grave para o menos grave.
    public static final String VENCIDO = "Vencido";
    public static final String PERTO = "Perto de vencer";
    public static final String ABAIXO = "Abaixo do mínimo";
    public static final String OK = "OK";

    private final MapaDAO mapaDAO = new MapaDAO();

    // Devolve setor -> prateleira -> produtos, e cada produto já leva o seu alerta.
    public Map<Integer, Map<Integer, List<Map<String, Object>>>> montarMapa() throws StokiException {
        Map<Integer, Map<Integer, List<Map<String, Object>>>> mapa = new LinkedHashMap<>();

        for (Map<String, Object> produto : mapaDAO.listarProdutosDoMapa()) {
            produto.put("alerta", calcularAlerta(produto));

            Integer setor = (Integer) produto.get("setorNumero");
            Integer prateleira = (Integer) produto.get("prateleira");

            // Se o setor ainda não está no mapa, cria a "gaveta" dele.
            Map<Integer, List<Map<String, Object>>> prateleiras = mapa.get(setor);
            if (prateleiras == null) {
                prateleiras = new LinkedHashMap<>();
                mapa.put(setor, prateleiras);
            }

            // Se a prateleira ainda não existe nesse setor, cria a lista dela.
            List<Map<String, Object>> produtos = prateleiras.get(prateleira);
            if (produtos == null) {
                produtos = new ArrayList<>();
                prateleiras.put(prateleira, produtos);
            }

            produtos.add(produto);
        }
        return mapa;
    }

    // Decide a cor da prateleira: vale o pior alerta entre os produtos dela.
    public String alertaDaPrateleira(List<Map<String, Object>> produtos) {
        boolean temPerto = false;
        boolean temAbaixo = false;

        for (Map<String, Object> produto : produtos) {
            String alerta = (String) produto.get("alerta");
            if (VENCIDO.equals(alerta)) {
                return VENCIDO;
            }
            if (PERTO.equals(alerta)) {
                temPerto = true;
            }
            if (ABAIXO.equals(alerta)) {
                temAbaixo = true;
            }
        }

        if (temPerto) {
            return PERTO;
        }
        if (temAbaixo) {
            return ABAIXO;
        }
        return OK;
    }

    // Decide o alerta de um produto, na ordem de gravidade.
    private String calcularAlerta(Map<String, Object> produto) {
        int vencidos = (Integer) produto.get("lotesVencidos");
        int perto = (Integer) produto.get("lotesPerto");
        BigDecimal saldo = (BigDecimal) produto.get("saldoValido");
        BigDecimal minimo = (BigDecimal) produto.get("estoqueMinimo");

        if (vencidos > 0) {
            return VENCIDO;
        }
        if (perto > 0) {
            return PERTO;
        }
        if (saldo.compareTo(minimo) < 0) {
            return ABAIXO;
        }
        return OK;
    }
}