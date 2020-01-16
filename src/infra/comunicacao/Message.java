/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.comunicacao;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author rhuan
 */
public class Message extends AbstractMessage {

    protected ImageIcon icon;
    protected boolean sucess = false;

    
    public Message() {
    }

    
    public Message(String msg, String title) {
        super(msg, title);
        icon = new ImageIcon("src/imagens/35x35/message.png");
    }

    
    static {
        init();
    }
    
    
    @Override
    public void show() {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE, icon);
        JOptionPane.setDefaultLocale(null);
    }


    public boolean isSucess() {
        return sucess;
    }



    
    
    private static void init() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } 
        SwingUtilities.updateComponentTreeUI(JOptionPane.getRootFrame());
    }

    
    
    
}
