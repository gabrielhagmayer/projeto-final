package controllers;

import exceptions.StokiException;
import services.MapaService;

import java.util.List;
import java.util.Map;

/**
 * Responsável: Gabriel.
 * Liga a TelaMapa ao MapaService.
 */
public class MapaController {

    private final MapaService mapaService = new MapaService();

    // Pede ao Service o mapa já agrupado por setor e prateleira.
    public Map<Integer, Map<Integer, List<Map<String, Object>>>> montarMapa() throws StokiException {
        return mapaService.montarMapa();
    }

    // Pede ao Service o pior alerta de uma prateleira.
    public String alertaDaPrateleira(List<Map<String, Object>> produtos) {
        return mapaService.alertaDaPrateleira(produtos);
    }
}