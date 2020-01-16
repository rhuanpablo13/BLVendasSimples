/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import sistemavendas.negocio.NProduto;
import sistemavendas.vendas.NCarrinhoCompras;

/**
 *
 * @author rhuan
 */
public class DefaultTableModelProdutosVenda extends DefaultTableModel {

    
    private HashMap<NProduto, Integer> linhas = new HashMap();
    private NCarrinhoCompras carrinho = new NCarrinhoCompras();
    private javax.swing.JTable table;
    
    
    /**
     * Construtores
     */
    public DefaultTableModelProdutosVenda() {}

    public DefaultTableModelProdutosVenda(JTable table) {
        this.table = table;        
    }
    
    public DefaultTableModelProdutosVenda(HashMap<NProduto, Integer> linhas) {
        this.linhas = linhas;
    }

    

    
    
    public void adicionarProduto(NProduto produto, Integer quantidade) throws IllegalArgumentException {
        try {
            if(carrinho.adicionarProduto(produto, quantidade)) {
                linhas = carrinho.buscarTodosProdutos();
                int ultimoIndice = getRowCount() - 1;
                fireTableRowsInserted(ultimoIndice, ultimoIndice);
                refresh();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        //System.out.println("Adicionar: Qtd no carrinho produto: " + produto.getNome() + " > " + getCarrinho().quatidadeItens(produto));
    }
    
    
    public void removeProduto(NProduto produto) {
        int linha = procuraLinhaRegistro(produto);
        if (linha > -1) {
            carrinho.removerTodos(produto);
            removeProduto(linha);
            refresh();
        }
    }
    
    
    public void removeProduto(Integer linha) {
        linhas = carrinho.buscarTodosProdutos();
        fireTableRowsDeleted(linha, linha);
        refresh();
    }
    

    
    public void alteraQuantidadeProduto(NProduto produto, Integer quantidade) {
        
        int linha = procuraLinhaRegistro(produto);
        if (quantidade <= 0) {
            removeProduto(produto);
            linhas = carrinho.buscarTodosProdutos();
            //fireTableRowsDeleted(linha, linha);
        } else {
            linhas = carrinho.buscarTodosProdutos();
            //fireTableRowsUpdated(linha, linha);
        }
        carrinho.alteraQuantidade(produto, quantidade);
        //System.out.println("Altra: Qtd no carrinho produto: " + produto.getNome() + " > " + getCarrinho().quatidadeItens(produto));
        refresh();
    }
    
    
    private int procuraLinhaRegistro(NProduto produto) {
        if (produto == null) return  -1;
        
        int linha = 0;
        for (java.util.Map.Entry<NProduto, Integer> entrySet : linhas.entrySet()) {
            NProduto key = entrySet.getKey();
            if (key.equals(produto)) {
                return linha;
            }
            linha++;            
        }
        return -1;
    }
    
    
    public void refresh() {
        try {
            linhas = carrinho.buscarTodosProdutos();
            this.setNumRows(0);
            for (java.util.Map.Entry<NProduto, Integer> entrySet : linhas.entrySet()) {
                NProduto nProduto = entrySet.getKey();
                Integer quantidade = entrySet.getValue();
                
                //System.out.println(nProduto.getCodigo() + " - " + nProduto.getNome() + " = " + quantidade);
                
                this.addRow(new Object[] {
                    nProduto.getId(),
                    nProduto.getCodigo(),
                    nProduto.getNome(),
                    nProduto.getValorVenda(),
                    quantidade.toString()
                });                
            }            
        } catch (Exception e) {}
    }
    
    
    
    
    
    public void setValueAt(Integer aValue, int row, int column) {        
        super.setValueAt((int)aValue, row, column);
    }
    
    public void setValueAt(String aValue, int row, int column) {        
        super.setValueAt(Integer.parseInt(aValue), row, column);
    }

    
    public int getValorColunaQuantidade(NProduto produto) {
        if (! carrinho.isEmpty()) {
            int linha = procuraLinhaRegistro(produto);
            int coluna = findColumn("Quantidade");
            if (linha > -1 && coluna > -1) {
                String value = (String) getValueAt(linha, coluna);
                return Integer.parseInt(value);
            }            
        }
        return -1;
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

    public NCarrinhoCompras getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(NCarrinhoCompras carrinho) {
        this.carrinho = carrinho;
    }
    
    public boolean isEmpty() {
        return carrinho.isEmpty();
    }
    
    public void limparCarrinho() {
        this.carrinho = new NCarrinhoCompras();
        this.linhas = new HashMap<>();
    }
   
    
    
    public void configure() {
        addColumn("#");
        addColumn("Código");
        addColumn("Descrição");
        addColumn("Valor (R$)");
        addColumn("Quantidade");        
    }
    
    
    
}
