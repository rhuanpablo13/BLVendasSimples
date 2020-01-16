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
import sistemavendas.controle.CCliente;
import sistemavendas.negocio.NCliente;

/**
 *
 * @author rhuan
 */
public class PanelCliente extends PanelDashboard {

    public PanelCliente(int codigo) {
        super(codigo);
        
        titulo.setText("Clientes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Leelawadee UI", Font.BOLD, 24));
        
        
        // clientes
        JLabel totalClientesLabel = getTotalClientes();
        totalClientesLabel.setForeground(Color.WHITE);
        totalClientesLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 18));
        
        // ativos
        JLabel totalClientesAtivosLabel = getTotalClientesAtivos();
        totalClientesAtivosLabel.setForeground(Color.WHITE);
        totalClientesAtivosLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 18));
        
        // ativos
        JLabel totalClientesInativosLabel = getTotalClientesInativos();
        totalClientesInativosLabel.setForeground(Color.WHITE);
        totalClientesInativosLabel.setFont(new Font("Leelawadee UI", Font.PLAIN, 18));
        
        conteudo.add(totalClientesLabel);
        conteudo.add(totalClientesAtivosLabel);
        conteudo.add(totalClientesInativosLabel);
        
        //conteudo.setLayout(new GridLayout(3, 0, 5, 5));
        conteudo.setBackground(ColorSystem.DEFAULT());
        titulo.setBackground(ColorSystem.DEFAULT());
        titulo.setOpaque(true);
    }
    
    
    private JLabel getTotalClientes() {
        CCliente controllerCliente = new CCliente();
        
        // clientes
        Integer totalClientes = controllerCliente.count();
        if (totalClientes != null) {
            return new JLabel("Total: " + totalClientes);
        } else {
            return new JLabel("Total: " + 0);
        }
    }
    
    
    
    private JLabel getTotalClientesAtivos() {
        CCliente controllerCliente = new CCliente();
        NCliente c1 = new NCliente();
        c1.setAtivo(true);
        try {
            List<NCliente> cls = controllerCliente.pesquisar(c1);
            if (cls != null) {
                return new JLabel("Ativos: " + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("Ativos: " + 0); 
    }
    
    
    private JLabel getTotalClientesInativos() {
        CCliente controllerCliente = new CCliente();
        NCliente c1 = new NCliente();
        c1.setAtivo(false);
        try {
            List<NCliente> cls = controllerCliente.pesquisar(c1);
            if (cls != null) {
                return new JLabel("Inativos: " + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("Inativos: " + 0); 
    }
}
