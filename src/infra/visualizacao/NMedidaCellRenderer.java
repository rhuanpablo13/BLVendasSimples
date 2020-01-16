/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import sistemavendas.negocio.NMedida;

/**
 *
 * @author rhuan
 */
public class NMedidaCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof NMedida) {
            NMedida medida = (NMedida) value;
            setText(medida.getDescricao());
        }
        return this;
    }
    
}
