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
public class Sucess extends Message {

    
    public Sucess(String msg, String barMessage) {
        super(msg, barMessage);
        super.sucess = true;
        icon = new ImageIcon("src/imagens/35x35/success.png");
    }
    
    @Override
    public void show() {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE, icon);
    }

}
