/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.comunicacao;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author rhuan
 */
public class Erro extends Message {

    
    public Erro(String msg) {
        super(msg, "Erro");
        icon = new ImageIcon("src/imagens/35x35/error.png");
    }
    
    
    public Erro(String msg, String title) {
        super(msg, title);
        icon = new ImageIcon("src/imagens/35x35/error.png");
    }
    
    
    @Override
    public void show() {
        JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.PLAIN_MESSAGE, icon);
    }
        
}
