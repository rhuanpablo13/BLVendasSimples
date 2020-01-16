/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class DefaultTableProdutosVenda extends DefaultTableModel {
    
    private List<NProduto> linhas = new ArrayList();

    public DefaultTableProdutosVenda() {
    }
    
    public DefaultTableProdutosVenda(List<NProduto> linhas) {
        this.linhas = linhas;
    }
    
    
    
}
