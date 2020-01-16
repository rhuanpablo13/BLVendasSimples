/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import infra.utilitarios.Mask;
import infra.utilitarios.Utils;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author rhuan
 */
public class CellRenderContato implements TableCellRenderer {
    
    private Mask contatoMask;

    public CellRenderContato(Mask contatoMask) {
        this.contatoMask = contatoMask;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String contato = (String) value;
        return new JTextField(Utils.setMask(contato, contatoMask));
    }
    
}
