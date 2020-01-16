/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.abstratas;

import infra.comunicacao.Erro;

/**
 *
 * @author rhuan
 */
public abstract class NegocioSimples {
    
    protected Integer id;
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

}
