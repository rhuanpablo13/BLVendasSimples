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
public enum FormaPagamento {
    A_VISTA("À Vista"), CARTAO_DEBITO("Débito"), CARTAO_CREDITO("Crédito");
    
    private String formaPagamento;
    
    FormaPagamento(String formaPag) {
        formaPagamento = formaPag;
    }

    
    public String getFormaPagamento() {
        return formaPagamento;
    }
    
    public static FormaPagamento getFormaPagamento(String formaPag) {
        if (formaPag.equalsIgnoreCase("A_VISTA")) {
            return A_VISTA;
        }
        if (formaPag.equalsIgnoreCase("CARTAO_DEBITO")) {
            return CARTAO_DEBITO;
        }                
        return CARTAO_CREDITO;
    }
}
