/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.dashboard;

import infra.comunicacao.Erro;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import sistemavendas.configuracoes.ColorSystem;
import sistemavendas.controle.CProduto;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class PanelProduto extends PanelDashboard {
    
    public PanelProduto(int codigo) {
        super(codigo);
        
        titulo.setText("Produtos");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Leelawadee UI", Font.BOLD, 24));
        
        
        // clientes
        JLabel totalProdutosLabel = getTotalProdutos();
        totalProdutosLabel.setForeground(Color.WHITE);
        totalProdutosLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // ativos
        JLabel totalProdutosAtivosLabel = getTotalProdutosAtivos();
        totalProdutosAtivosLabel.setForeground(Color.WHITE);
        totalProdutosAtivosLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // inativos
        JLabel totalProdutosInativosLabel = getTotalProdutosInativos();
        totalProdutosInativosLabel.setForeground(Color.WHITE);
        totalProdutosInativosLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // estoque
        JLabel totalProdutosEstoque = getTotalProdutosEstoque();
        totalProdutosEstoque.setForeground(Color.WHITE);
        totalProdutosEstoque.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        // total em venda
        JLabel totalValorVendaEmEstoque = getTotalValorVendaEmEstoque();
        totalValorVendaEmEstoque.setForeground(Color.WHITE);
        totalValorVendaEmEstoque.setFont(new Font("Leelawadee UI", Font.PLAIN, 14));
        
        // terminar cadastro
        JLabel totalTerminarCad = getTotalCadastroRapido();
        totalTerminarCad.setForeground(Color.WHITE);
        totalTerminarCad.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        
        
        conteudo.add(totalProdutosLabel);
        conteudo.add(totalProdutosAtivosLabel);
        conteudo.add(totalProdutosInativosLabel);        
        conteudo.add(totalProdutosEstoque);
        conteudo.add(totalValorVendaEmEstoque);
        conteudo.add(totalTerminarCad);
        
        //conteudo.setLayout(new GridLayout(3, 0, 5, 5));
        conteudo.setBackground(ColorSystem.DEFAULT());
        titulo.setBackground(ColorSystem.DEFAULT());
        titulo.setOpaque(true);
        
    }
    
 
    
    
    private JLabel getTotalProdutos() {
        CProduto controllerProduto = new CProduto();
        
        // clientes
        Integer totalProdutos = controllerProduto.count();
        if (totalProdutos != null) {
            return new JLabel("Total: " + totalProdutos);
        } else {
            return new JLabel("Total: " + 0);
        }
    }
    
    
    private JLabel getTotalProdutosAtivos() {
        CProduto controllerProduto = new CProduto();
        NProduto c1 = new NProduto();
        c1.setAtivo(true);
        try {
            List<NProduto> cls = controllerProduto.pesquisar(c1);
            if (cls != null) {
                return new JLabel("Ativos: " + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("Ativos: " + 0); 
    }
    
    
    private JLabel getTotalProdutosInativos() {
        CProduto controllerProduto = new CProduto();
        NProduto c1 = new NProduto();
        c1.setAtivo(false);
        try {
            List<NProduto> cls = controllerProduto.pesquisar(c1);
            if (cls != null) {
                return new JLabel("Inativos: " + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("Inativos: " + 0); 
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
    
    
    private JLabel getTotalCadastroRapido() {
        CProduto controllerProduto = new CProduto();
        Integer cls = controllerProduto.totalProdutosCadastroRapido();
        if (cls != null) {
            return new JLabel("Concluir cadastro: " + cls);
        }
        return new JLabel("Concluir cadastro: " + 0); 
    }
}
