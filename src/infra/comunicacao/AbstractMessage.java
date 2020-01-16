/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.comunicacao;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 
 * @author RHUAN
 */
public abstract class AbstractMessage extends Exception {

    protected String msg;
    protected String title;

    public AbstractMessage() {
        init();        
    }
    
    
    public AbstractMessage(String msg, String title) {
        super(msg);
        this.msg = msg;
        this.title = title;
    }
    
    
    public abstract void show();
    
    
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
    
    
    
    private void init() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } 
        SwingUtilities.updateComponentTreeUI(JOptionPane.getRootFrame());
    }
}
