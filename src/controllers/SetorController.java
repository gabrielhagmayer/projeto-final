package controllers;

import exceptions.StokiException;
import models.Setor;
import services.CatalogoService;
import views.TelaSetor;

import java.util.ArrayList;

/**
 * Exemplo pronto de Controller: chama o Service e devolve o resultado ou o erro para a tela.
 */
public class SetorController {

    private final CatalogoService catalogoService = new CatalogoService();

    public void listar(TelaSetor tela) {
        try {
            ArrayList<Setor> setores = catalogoService.listarSetores();
            tela.preencherTabela(setores);
        } catch (StokiException e) {
            tela.informarErro(e.getMessage());
        }
    }
}
