/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import java.awt.Component;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author RHUAN
 */
public class MyLookAndFeel {

    private Component component;
    public MyLookAndFeel(Component component) {
        this.component = component;
    }

    
    
    public void setWindowsApperance(boolean set) {
        if (set)  {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                //UIManager.setUIFont(new javax.swing.plaf.FontUIResource("Sans", Font.PLAIN, 24));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }
            SwingUtilities.updateComponentTreeUI(component);
        }
    }
}
