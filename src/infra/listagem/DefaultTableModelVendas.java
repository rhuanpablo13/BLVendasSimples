/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import sistemavendas.negocio.NVendas;

/**
 *
 * @author rhuan
 */
public class DefaultTableModelVendas extends DefaultTableModel {
    
    private List<NVendas> linhas;

    public DefaultTableModelVendas() {
    }

    public DefaultTableModelVendas(List<NVendas> linhas) {
        this.linhas = linhas;
    }
    
    public NVendas getVenda(int rowIndex) {
        try {
            return linhas.get(rowIndex);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    
    public void addVenda(NVendas cliente) {
        linhas.add(cliente);
        int ultimoIndice = getRowCount() - 1;
        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }
    
    
    public void updateVenda(int indiceLinha, NVendas marca) {
        linhas.set(indiceLinha, marca);
        fireTableRowsUpdated(indiceLinha, indiceLinha);
    }
    
    
    public void removeVenda(int indiceLinha) {
        linhas.remove(indiceLinha);
        fireTableRowsDeleted(indiceLinha, indiceLinha);
    }
    
    
    List<Color> rowColours = Arrays.asList(
        Color.RED,
        Color.GREEN,
        Color.CYAN
    );

    public void setRowColour(int row, Color c) {
        rowColours.set(row, c);
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours.get(row);
    }    
    
}
