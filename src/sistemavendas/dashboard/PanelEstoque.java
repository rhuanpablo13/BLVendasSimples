/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.dashboard;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import sistemavendas.configuracoes.ColorSystem;
import sistemavendas.controle.CProduto;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class PanelEstoque extends PanelDashboard {
    
    
    public PanelEstoque(int codigo) {
        super(codigo);
        
        titulo.setText("Estoque");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Leelawadee UI", Font.BOLD, 24));
        
        
        // estoque
        JLabel totalProdutosEstoque = getTotalProdutosEstoque();
        totalProdutosEstoque.setForeground(Color.WHITE);
        totalProdutosEstoque.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // total em venda
        JLabel totalValorVendaEmEstoque = getTotalValorVendaEmEstoque();
        totalValorVendaEmEstoque.setForeground(Color.WHITE);
        totalValorVendaEmEstoque.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // mais vendido
        JLabel maisVendido = getProdutoMaisVendido();
        maisVendido.setForeground(Color.WHITE);
        maisVendido.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // menos vendido
        JLabel menosVendido = getProdutoMenosVendido();
        menosVendido.setForeground(Color.WHITE);
        menosVendido.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        
        conteudo.add(totalProdutosEstoque);
        conteudo.add(totalValorVendaEmEstoque);
        conteudo.add(maisVendido);
        conteudo.add(menosVendido);
        
        //conteudo.setLayout(new GridLayout(3, 0, 5, 5));
        conteudo.setBackground(ColorSystem.DEFAULT());
        titulo.setBackground(ColorSystem.DEFAULT());
        titulo.setOpaque(true);
        
    }
    
    
    
    private JLabel getTotalProdutosEstoque() {
        CProduto controllerProduto = new CProduto();
        Integer cls = controllerProduto.totalProdutosEmEstoque();
        if (cls != null) {
            return new JLabel("Em Estoque: " + cls);
        }
        return new JLabel("Em Estoque: " + 0); 
    }
    
    
    
    private JLabel getTotalValorVendaEmEstoque() {
        CProduto controllerProduto = new CProduto();
        Double cls = controllerProduto.totalValorVendaEmEstoque();
        if (cls != null) {
            return new JLabel("(R$) Total: R$" + cls);
        }
        return new JLabel("(R$) Total: R$ 0,00");
    }

    
    private JLabel getProdutoMaisVendido() {
        CProduto controllerProduto = new CProduto();
        NProduto cls = controllerProduto.maisVendido();
        if (cls != null) {
            return new JLabel("Mais Vendido: " + cls.getCodigo() + "-" + cls.getNome());
        }
        return new JLabel("Mais Vendido: Nenhum");
    }
    
    
    private JLabel getProdutoMenosVendido() {
        CProduto controllerProduto = new CProduto();
        NProduto cls = controllerProduto.menosVendido();
        if (cls != null) {
            return new JLabel("Menos Vendido: " + cls.getCodigo() + "-" + cls.getNome());
        }
        return new JLabel("Menos Vendido: Nenhum");
    }
    
    
}
