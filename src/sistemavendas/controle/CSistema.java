/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.ControleSimples;
import sistemavendas.negocio.NSistema;
import sistemavendas.persistente.PSistema;

/**
 *
 * @author rhuan
 */
public class CSistema extends ControleSimples<NSistema> {

    
    private final PSistema dao = new PSistema("SISTEMA_CONFIG");
    
    
    @Override
    public void alterar(NSistema model) throws Exception {
        dao.alterar(model);
    }

    @Override
    public void inserir(NSistema model) throws Exception {
        dao.inserir(model);
    }
    
    public NSistema buscarConfiguracaoVigente() {
        return dao.buscarConfiguracaoVigente();
    }
}
