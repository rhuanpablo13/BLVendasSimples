/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import sistemavendas.negocio.NPrograma;

/**
 *
 * @author rhuan
 */
public class NProgramaCellRenderer  extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
        if (value instanceof NPrograma) {
            NPrograma cliente = (NPrograma) value;
            setText(cliente.getNome());
        }        
        return this;
    }
    
}
