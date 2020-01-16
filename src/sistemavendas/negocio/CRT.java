/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.negocio;

/**
 *
 * @author rhuan
 */
public enum CRT {
    
    SIMPLES_NACIONAL(1),
    SIMPLES_NACIONAL_EXCESSO_RECEITA_BRUTA(2),
    REGIME_NORMAL(3);
    
    private final Integer crt;
    
    CRT (Integer crt) {
        this.crt = crt;
    }

    public Integer getCrt() {
        return crt;
    }
    
    
}
