/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import infra.utilitarios.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import sistemavendas.negocio.NCliente;


/**
 *
 * @author rhuan
 */
public class DefaultTableModelClientes extends DefaultTableModel {

    private static final int COL_ID = 0;
    private static final int COL_CODIGO = 1;
    private static final int COL_NOME = 2;
    private static final int COL_CPF = 3;
    private static final int COL_DT_NASC = 4;
    private static final int COL_EMAIL = 5;
    
    private List<NCliente> linhas = new ArrayList();
    private String[] colunas = new String[]{"#", "Código", "Nome", "CPF", "Dt. Nasc.", "E-mail"};

    
    
    
    public DefaultTableModelClientes() {
        linhas = new ArrayList();
    }

    public DefaultTableModelClientes(List<NCliente> linhas) {
        this.linhas = linhas;
    }
    
    
    private void configurarTabela() {
        //jTableListagem.getColumnModel().getColumn(0).setPreferredWidth(40);
        
    }
 
    
    
    public NCliente getCliente(int rowIndex) {
        try {
            return linhas.get(rowIndex);
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    
    public void addCliente(NCliente cliente) {
        linhas.add(cliente);
        int ultimoIndice = getRowCount() - 1;
        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }
    
    
    public void updateCliente(int indiceLinha, NCliente marca) {
        linhas.set(indiceLinha, marca);
        fireTableRowsUpdated(indiceLinha, indiceLinha);
    }
    
    
    public void removeCliente(int indiceLinha) {
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


    
    
//    
//    @Override
//    public int getRowCount() {
//        return linhas == null || linhas.isEmpty() ? 0 : linhas.size();
//    }
//
//    @Override
//    public int getColumnCount() {
//        return colunas == null ? 0 : colunas.length;
//    }
//
//    @Override
//    public String getColumnName(int columnIndex) {
//        if (colunas.length < columnIndex) return "";
//        return colunas[columnIndex];
//    }


//    
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        
//        try {
//            NCliente c = linhas.get(rowIndex);
//            switch (columnIndex) {
//                case COL_ID:
//                    return c.getId();
//                case COL_CODIGO:
//                    return c.getCodigo();
//                case COL_CPF:
//                    c.getCpf();
//                case COL_DT_NASC:
//                    Utils.date2View(c.getDtNascimento());
//                case COL_EMAIL:
//                    c.getEmail();
//                case COL_NOME:
//                    c.getNome();
//                default:
//                    return "";
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            return null;
//        }
//    }
//
//    @Override
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        
//        try {
//            NCliente c = linhas.get(rowIndex);
//            switch (columnIndex) {
//                case COL_ID:
//                    c.setId((Integer) aValue);
//                    break;
//                case COL_CODIGO:
//                    c.setCodigo((Integer) aValue);
//                    break;
//                case COL_CPF:
//                    c.setCpf((String) aValue);
//                    break;
//                case COL_DT_NASC:
//                    c.setDtNascimento(Utils.view2Date((String) aValue));
//                    break;
//                case COL_EMAIL:
//                    c.setEmail((String) aValue);
//                    break;
//                case COL_NOME:
//                    c.setNome((String) aValue);
//            }
//        } catch(IndexOutOfBoundsException e) {
//        }
//    }
    

    public void addClientes(List<NCliente> clientes) {
        this.linhas = clientes;
    }
}
