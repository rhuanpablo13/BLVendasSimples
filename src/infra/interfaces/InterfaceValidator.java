/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.interfaces;

import infra.operacoes.Operacao;
import java.awt.Color;
import javax.swing.JTextField;

/**
 *
 * @author rhuan
 */
public interface InterfaceValidator {

    
    
    /**
     * Define uma interface para validação de um JTextField
     * @param field
     * @param operacao 
     */
    public void required(final JTextField field, final Operacao operacao);
    
    
    /**
     * Define uma interface para validação de um JTextField
     * @param field
     * @param message
     * @param operacao 
     */
    public void required(final JTextField field, final String message, final Operacao operacao);
    
}
