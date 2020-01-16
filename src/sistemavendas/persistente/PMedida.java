/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NMedida;

/**
 *
 * @author rhuan
 */
public class PMedida extends Persistente<NMedida>{
    
    public PMedida(String table) {
        super(table);
    }
    
    public void inserir(NMedida medida) throws Erro {
        String sql = "INSERT INTO " + table + " (CODIGO, DESCRICAO) VALUES (?,?)";
        super.inserir(medida, sql);
    }
    
    public void alterar(NMedida medida) throws Erro {
        String sql = "UPDATE " + table + " SET CODIGO=?, DESCRICAO=? WHERE ID=?";
        super.alterar(medida, medida.getId(), sql);
    }
    
    public List<NMedida> getLista() throws Erro {
        String sql = "SELECT * FROM " + table + "";
        return super.getLista(sql);
    }

    @Override
    public NMedida recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }

    public void excluir(int codigo) throws Erro {
        super.excluir(codigo, "DELETE FROM " + table + " WHERE CODIGO = ?"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public NMedida buscarPorCodigo(int codigo) throws Erro {
        
        String sql = "SELECT * FROM " + table + " WHERE CODIGO = ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            NMedida obj;
            while (rs.next()) {
                obj = new NMedida();
                obj.setId(rs.getInt("ID"));
                obj.setCodigo(rs.getInt("CODIGO"));
                obj.setDescricao(rs.getString("DESCRICAO"));
                return obj;
            }   
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + "\n" + StackDebug.getLineNumber(this.getClass()));
        } catch (SQLException ex) {
            Logger.getLogger(PMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public NMedida buscarPorId(int codigo) throws Erro {
        
        String sql = "SELECT * FROM " + table + " WHERE ID = ?";
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            NMedida obj;
            while (rs.next()) {
                obj = new NMedida();
                obj.setId(rs.getInt("ID"));
                obj.setCodigo(rs.getInt("CODIGO"));
                obj.setDescricao(rs.getString("DESCRICAO"));
                return obj;
            }   
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + "\n" + StackDebug.getLineNumber(this.getClass()));
        } catch (SQLException ex) {
            Logger.getLogger(PMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }    
    
    
    
    public List<NMedida> pesquisar(NMedida medida) throws Erro {
        
        String sql = "SELECT * FROM " + table + " ";
        List<String> where = new ArrayList();
        if (medida.getCodigo() != null) {
            where.add(" CODIGO = " + medida.getCodigo());
        }
        if (medida.getDescricao() != null) {
            where.add(" DESCRICAO LIKE '%" + medida.getDescricao() + "%' ");
        }
        
        if (!where.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", where);
        }
        
        System.out.println(sql);
        List lista = new ArrayList<>();
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            NMedida obj;
            while (rs.next()) {
                obj = new NMedida();
                obj.setId(rs.getInt("ID"));
                obj.setCodigo(rs.getInt("CODIGO"));
                obj.setDescricao(rs.getString("DESCRICAO"));
                lista.add(obj);
            }
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + "\n" + StackDebug.getLineNumber(this.getClass()));
        } catch (SQLException ex) {
            Logger.getLogger(PMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
