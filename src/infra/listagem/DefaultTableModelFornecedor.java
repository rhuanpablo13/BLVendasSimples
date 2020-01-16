/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import sistemavendas.negocio.NFornecedor;

/**
 *
 * @author rhuan
 */
public class DefaultTableModelFornecedor extends DefaultTableModel {

    private static final int COL_ID = 0;
    private static final int COL_CODIGO = 1;
    private static final int COL_NOME = 2;
    private static final int COL_CPF = 3;
    private static final int COL_DT_NASC = 4;
    private static final int COL_EMAIL = 5;
    
    private List<NFornecedor> linhas = new ArrayList();
    private String[] colunas = new String[]{"ID", "Código", "Nome", "CNPJ", "E-mail"};
    
    
    public DefaultTableModelFornecedor() {
        linhas = new ArrayList();
    }

    public DefaultTableModelFornecedor(List<NFornecedor> linhas) {
        this.linhas = linhas;
    }
    
}
