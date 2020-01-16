/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.dashboard;

import infra.comunicacao.Erro;
import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import sistemavendas.configuracoes.ColorSystem;
import sistemavendas.controle.CVendas;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NVendas;

/**
 *
 * @author rhuan
 */
public class PanelVendas extends PanelDashboard {
    
    public PanelVendas(int codigo) {
        super(codigo);
        
        titulo.setText("Vendas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Leelawadee UI", Font.BOLD, 24));
        
        
        JLabel maisFrequencia = getClienteCompraMaisFrequencia();
        maisFrequencia.setForeground(Color.WHITE);
        maisFrequencia.setFont(new Font("Leelawadee UI", Font.PLAIN, 15));
        
        
        JLabel maiorValor = getClienteCompraMaiorValor();
        maiorValor.setForeground(Color.WHITE);
        maiorValor.setFont(new Font("Leelawadee UI", Font.PLAIN, 16));
        
        
        JLabel menosFrequencia = getClienteCompraMenosFrequencia();
        menosFrequencia.setForeground(Color.WHITE);
        menosFrequencia.setFont(new Font("Leelawadee UI", Font.PLAIN, 16));
        
        
        JLabel ultimaVenda = ultimaVendaRegistrada();
        ultimaVenda.setForeground(Color.WHITE);
        ultimaVenda.setFont(new Font("Leelawadee UI", Font.PLAIN, 16));
        
        conteudo.add(maisFrequencia);
        conteudo.add(maiorValor);
        conteudo.add(menosFrequencia);
        conteudo.add(ultimaVenda);
        
        
        conteudo.setBackground(ColorSystem.DEFAULT());
        titulo.setBackground(ColorSystem.DEFAULT());
        titulo.setOpaque(true);
        
    }
    
    
    
    
    public JLabel getClienteCompraMaisFrequencia() {
        CVendas controller = new CVendas();
        NCliente cliente = controller.getClienteCompraMaisFrequencia();
        if (!cliente.isEmpty()) {
            return new JLabel("Cliente mais frequente: " + cliente.getCodigo() + " - " + cliente.getNome());
        }
        return new JLabel("Cliente mais frequente: Nenhum");
    }
    
    
    public JLabel getClienteCompraMaiorValor() {
        CVendas controller = new CVendas();
        NCliente cliente = controller.getClienteCompraMaiorValor();
        if (!cliente.isEmpty()) {
            return new JLabel("Compra maior valor: " + cliente.getCodigo() + " - " + cliente.getNome());
        }
        return new JLabel("Compra maior valor: Nenhum");
    }
    
    
    public JLabel getClienteCompraMenosFrequencia() {
        CVendas controller = new CVendas();
        NCliente cliente = controller.getClienteCompraMenosFrequencia();
        if (!cliente.isEmpty()) {
            return new JLabel("Cliente menos frequente: " + cliente.getCodigo() + " - " + cliente.getNome());
        }
        return new JLabel("Cliente menos frequente: Nenhum");
    }
    
    
    public JLabel ultimaVendaRegistrada() {
        CVendas controller = new CVendas();
        try {
            NVendas venda = controller.recuperaUltimoRegistro();
            if (!venda.isEmpty()) {
                return new JLabel("Ultima venda registrada: " + venda.getCodigo() + " - R$" + venda.getValorTotal());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelVendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("Ultima venda registrada: Nenhum");
    }
}
