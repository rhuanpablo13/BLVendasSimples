/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import sistemavendas.negocio.FormaPagamento;
import sistemavendas.negocio.NFormaPagamento;

/**
 *
 * @author rhuan
 */
public class PFormaPagamento extends PersistenteSimples<NFormaPagamento>{
    
    public PFormaPagamento(String table) {
        super(table);        
    }
    

    
    public void inserir(NFormaPagamento model, int idVenda) throws Erro {
        String sql = "INSERT INTO " + table +  " (DESCRICAO, ENTRADA, PARCELAS, ID_VENDA) VALUES (";
        sql += "'" + model.getFormaPagamento() +"',";
        sql += model.getEntrada()+",";
        sql += model.getParcelas()+",";
        sql += idVenda;
        sql += ")";
        System.out.println(sql);
        try {
            save(sql);
            commit();
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
    }
    
    
    public void alterar(NFormaPagamento model, int idVenda) throws Erro {
        String sql = "UPDATE " + table + " SET ";
        sql += "DESCRICAO = '" + model.getFormaPagamento()+"'"; 
        sql += ", ENTRADA = " + model.getEntrada();
        sql += ", PARCELAS = " + model.getParcelas();
        sql += ", ID_VENDA = " + idVenda;
        sql += " WHERE ID = " + model.getId();
        System.out.println(sql);
        try {
            update(sql);
            commit();
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
    }
    
    
    public void excluir(int id) throws Erro {
        String sql = "DELETE FROM " + table + " WHERE ID = " + id;
        try {
            delete(sql);
            commit();
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
    }
    
    
    public List<NFormaPagamento> getList() throws Erro {
        String sql = "SELECT * FROM " + table;
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql);
        } catch (SQLException ex) {
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {}
        
        try {        
            return banco2ListModel(NFormaPagamento.class, rs, table);
        } catch (Exception ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }        
    }
    
    
    
    public NFormaPagamento pesquisar(NFormaPagamento formaPagamento, Integer idVenda) throws Erro {
        String sql = "SELECT * FROM " + table + " WHERE ";
        
        if (formaPagamento.getFormaPagamento() == FormaPagamento.A_VISTA) {
            sql += " DESCRICAO = " + FormaPagamento.A_VISTA;
        } else {
            
            if (formaPagamento.getFormaPagamento() == FormaPagamento.CARTAO_CREDITO) {
                sql += " DESCRICAO = " + FormaPagamento.CARTAO_CREDITO + " AND ";
                sql += " ENTRADA = " + formaPagamento.getEntrada() + " AND ";
                sql += " PARCELAS = " + formaPagamento.getParcelas() + " AND ";
            } else {                
                sql += " DESCRICAO = " + FormaPagamento.CARTAO_DEBITO;
            }
        }
        if (idVenda != null) {
            sql += " AND ID_VENDA = " + idVenda;
        }
        
        
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql);
        } catch (SQLException | DatabaseException ex) {
            throw new Erro("pesquisar NFormaPagamento\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {}
        
        try {        
            return (NFormaPagamento) banco2Model(NFormaPagamento.class, rs, table);
        } catch (Exception ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }        
    }
    
    
    public NFormaPagamento pesquisar(Integer idVenda) throws Erro {
        
        String sql = "SELECT * FROM " + table + " WHERE ID_VENDA = " + idVenda;
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql);
        } catch (SQLException | DatabaseException ex) {
            throw new Erro("pesquisar NFormaPagamento\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException ex) {}
        
        try {
            NFormaPagamento p = new NFormaPagamento();
            if (rs.first()) {
                Object o = rs.getObject("DESCRICAO");
                Integer nrEntrada = rs.getInt("ENTRADA");
                Integer nrParcelas = rs.getInt("PARCELAS");
                Integer venda = rs.getInt("ID_VENDA");
                Integer id = rs.getInt("ID");
                FormaPagamento formaPag = FormaPagamento.getFormaPagamento((String) o);
                
                p.setId(id);
                p.setEntrada(nrEntrada);
                p.setIdVenda(venda);
                p.setParcelas(nrParcelas);
                p.setFormaPagamento(formaPag);
            }
            
            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }  
        
    }
    
    
    
    public NFormaPagamento pesquisar(NFormaPagamento formaPagamento) throws Erro {
        return pesquisar(formaPagamento, null);
    }    

    @Override
    protected Object convert(Field field, ResultSet rs, String column) throws Exception {
        
        Object o = null;
        if (field.getType().getSimpleName().equalsIgnoreCase(NFormaPagamento.class.getName())) {
            o = rs.getString(column);
        } else {
            o = super.convert(field, rs, column); //To change body of generated methods, choose Tools | Templates.
        }
        return o;
    }
    
    
    
    
    
}
