/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import infra.comunicacao.StackDebug;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NPrograma;

/**
 *
 * @author rhuan
 */
public class PPrograma extends PersistenteSimples<NPrograma> {

    public PPrograma(String table) {
        super(table);
    }
    
    
    private List<NOperacao> getOperacoes(int idPrograma, int idUsuario) {
        String sql = "SELECT O.ID, O.CS_OPERACAO FROM OPERACAO O, PROGRAMA_OPERACAO PO"
                + " WHERE O.ID = PO.ID_OPERACAO AND PO.ID_PROGRAMA = " + idPrograma + ""
                + " AND PO.ID_USUARIO = " + idUsuario;
        System.out.println("getOperacoes: " + sql);
        
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<NOperacao> oprs = new ArrayList();
            NOperacao op;
            while(rs.next()) {
                op = new NOperacao();
                op.setId(rs.getInt(1));
                op.setDescricao(rs.getString(2));
                oprs.add(op);
            }            
            return oprs;
        } catch (DatabaseException | SQLException ex) {
            new Erro(ex.getMessage()).show();
        }
        return new ArrayList();
    }
    
    
    
    public List<NPrograma> getList() {
        String sql = "SELECT * FROM " + table;
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<NPrograma> programas = new ArrayList();
            while (rs.next()) {
                NPrograma p = new NPrograma();
                p.setId(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setCsTipo(rs.getString(3));
                programas.add(p);
            }
            return programas;
        } catch (SQLException | DatabaseException ex) {
            new Erro(ex.getMessage()).show();
        } 
        return new ArrayList();
    }
    
    
    
    public List<NPrograma> getList(int idPrograma, int idUsuario) {
        String sql = "SELECT * FROM " + table;
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<NPrograma> programas = new ArrayList();
            while (rs.next()) {
                NPrograma p = new NPrograma();
                p.setId(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setCsTipo(rs.getString(3));
                List<NOperacao> oprs = getOperacoes(idPrograma, idUsuario);
                p.setOperacoes(oprs);
                p.setIdUsuario(idUsuario);
                programas.add(p);
            }
            return programas;
        } catch (SQLException | DatabaseException ex) {
            new Erro(ex.getMessage()).show();
        } 
        return new ArrayList();
    }
    
    
    
    public NPrograma pesquisar(int idPrograma, int idUsuario) {
        String sql = "SELECT * FROM " + table + " WHERE ID = " + idPrograma;
        System.out.println("pesquisar: " + sql);
        
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                NPrograma p = new NPrograma();
                p.setId(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setCsTipo(rs.getString(3));
                List<NOperacao> oprs = getOperacoes(idPrograma, idUsuario);
                p.setOperacoes(oprs);
                p.setIdUsuario(idUsuario);
                return p;
            }
            return null;
        } catch (DatabaseException | SQLException ex) {
            new Erro(ex.getMessage()).show();
        }
        return null;
    }
    
    
    
    public List<NPrograma> pesquisar(String nome, int idUsuario) {
        List<NPrograma> programas = new ArrayList();
        String sql = "SELECT * FROM " + table + " WHERE NOME LIKE '%" + nome + "%' AND ID_USUARIO = " + idUsuario;
        System.out.println("pesquisar: " + sql);
        
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NPrograma p = new NPrograma();
                p.setId(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setCsTipo(rs.getString(3));
                List<NOperacao> oprs = getOperacoes(p.getId(), idUsuario);
                p.setOperacoes(oprs);
                p.setIdUsuario(idUsuario);
                programas.add(p);
            }
        } catch (DatabaseException | SQLException ex) {
            new Erro(ex.getMessage()).show();
        }
        return programas;
    }
        
    
    
    public NOperacao getOperacaoPorNome(String nomeOperacao) {
        String sql = "SELECT ID, CS_OPERACAO FROM OPERACAO WHERE CS_OPERACAO = '" + nomeOperacao + "'";
        System.out.println("getOperacaoPorNome: " + sql);
        try {
            NOperacao op = new NOperacao();
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                op.setId(rs.getInt(1));
                op.setDescricao(rs.getString(2));
            }
            return op;
        } catch (DatabaseException | SQLException ex) {
            new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass())).show();
        }
        return null;
    }
    
    
    
    public boolean save(NPrograma p) {
        System.out.println("\nsave: " + p.toString());
        try {
            String c = "SELECT COUNT(*) FROM PROGRAMA_OPERACAO WHERE ID_PROGRAMA = " + p.getId() + " AND ID_USUARIO = " + p.getIdUsuario();
            System.out.println("count: " + c);
            
            if (count(c) > 0) {
                // deletando as operacoes
                String deleteOperacoes = "DELETE FROM PROGRAMA_OPERACAO WHERE ID_PROGRAMA = " + p.getId() + " AND ID_USUARIO = " + p.getIdUsuario();
                System.out.println("deleteOperacoes: " + deleteOperacoes);
                delete(deleteOperacoes);
                commit();                
            }
        } catch (DatabaseException ex) {} catch (PersistenteException ex) {
            Logger.getLogger(PPrograma.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {    
            // inserindo as operacaoes
            for (NOperacao op : p.getOperacoes()) {
                String saveOperacoes = "INSERT INTO "
                                        + "PROGRAMA_OPERACAO (ID_PROGRAMA, ID_OPERACAO, ID_USUARIO) "
                                    + " VALUES (" 
                                        + p.getId() + ", " 
                                        + op.getId() + ", " 
                                        + p.getIdUsuario() + ") ";
                System.out.println("saveOperacoes: " + saveOperacoes);
                super.save(saveOperacoes);
                commit();
            }
        } catch (DatabaseException ex) {
            new Erro("Registro não salvo, favor verificar!" + StackDebug.getLineNumber(this.getClass())).show();
            return false;
        }
        return true;
    }
    
    
    public NPrograma pesquisarPrograma(String nome, int idUsuario) {
        
        String sql = "SELECT * FROM " + table + " WHERE NOME = '" + nome + "'";
        System.out.println("pesquisarPrograma: " + sql);
        
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                NPrograma p = new NPrograma();
                p.setId(rs.getInt(1));
                p.setNome(rs.getString(2));
                p.setCsTipo(rs.getString(3));
                List<NOperacao> oprs = getOperacoes(p.getId(), idUsuario);
                p.setOperacoes(oprs);
                p.setIdUsuario(idUsuario);
                return p;
            }
        } catch (DatabaseException | SQLException ex) {
            new Erro(ex.getMessage()).show();
        }
        return null;        
    }
    
    
    
    public boolean update(NPrograma p) {        
        return this.save(p);
    }
    
}
