/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.cadastrosrapidos;

import infra.validadores.ViewValidator;
import infra.visualizacao.MessageOutPut;
import java.awt.HeadlessException;

/**
 *
 * @author rhuan
 */
public abstract class CadastroRapido extends javax.swing.JFrame {
    
    protected ViewValidator validator;
    protected abstract void initMasks();
    protected abstract void initActions();
    protected abstract void cadastrar();

    public CadastroRapido() throws HeadlessException {
        setLocationRelativeTo(null); 
    }
    
    
    public void showException(Exception ex) {
        MessageOutPut out = new MessageOutPut();
        out.show(ex);
    }
}
