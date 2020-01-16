/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.comunicacao.DatabaseException;
import infra.comunicacao.PersistenteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NSistema;

/**
 *
 * @author rhuan
 */
public class PSistema extends PersistenteSimples <NSistema> {

    public PSistema(String table) {
        super(table);
    }
    
    
    public void inserir(NSistema model) throws DatabaseException {
        String sql = "INSERT INTO " + table + " (EMAIL, SENHA) VALUES ('" + model.getEmail() + "', '" + model.getSenha() + "')";        
        System.out.println(sql);
        save(sql);
        commit();
    }
   
    
    public void alterar(NSistema model) throws DatabaseException {
        String sql = "UPDATE " + table + " SET EMAIL = '" + model.getEmail() + "', SENHA = '" + model.getSenha() + "'";
        System.out.println(sql);
        update(sql);
        commit();
    }
    
    
    public NSistema buscarConfiguracaoVigente() {
        try {
            String sql = "SELECT ID, EMAIL, SENHA FROM " + table;
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                NSistema s = new NSistema();
                s.setId(rs.getInt(1));
                s.setEmail(rs.getString(2));
                s.setSenha(rs.getString(3));
                return s;
            }
        } catch (DatabaseException ex) {
            Logger.getLogger(PSistema.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
