/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.controle.CCliente;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NVendas;

/**
 *
 * @author rhuan
 */
public class PVendas extends Persistente <NVendas> {
    
    
    public PVendas(String table) {
        super(table);
    }

    
    
    public void inserir(NVendas model) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        super.inserir(model, sql); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void alterar(NVendas model, int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleUpdate(table, getTableColumns());
        super.alterar(model, codigo, sql); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void excluir(int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleDelete(table);
        super.excluir(codigo, sql); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void excluir(NVendas venda) throws Erro {        
        String sql = "DELETE FROM " + table + " WHERE ID = ?";
        super.excluir(venda, sql); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List getLista() throws Erro {
        PersistenceServices ps = new PersistenceServices();
        return super.getLista(ps.createSimpleSelect(table)); //To change body of generated methods, choose Tools | Templates.
    }

    
    public NVendas buscarPorCodigo(int codigo) throws Erro {
        return super.buscarPorCodigo(codigo, "SELECT * FROM " + super.table + " WHERE CODIGO = ?;"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public NVendas buscarPorId(int id) throws Erro {
        return super.buscarPorId(id, "SELECT * FROM " + super.table + " WHERE ID = ?;"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public List<NVendas> pesquisarPorAtributos(NVendas model) throws Erro {
        Map<List<String>, Object> atributos = null;
        try {
            atributos = getAttributesAndValues(model, true);
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Atenção");
        }
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSelect(table, atributos);
        System.out.println(sql);
        return super.getLista(sql);
    }

    
    public List<NVendas> pesquisar(Integer nrVenda, Integer idCliente, Date dataVenda, Double valorTotalInicio, Double valorTotalFim, Double subTotalInicio, Double subTotalFim) throws Erro {
        
        String sql = "SELECT * FROM " + table;
        
        if (nrVenda != null) {
            sql += " WHERE CODIGO = ?";
            NVendas venda = super.buscarPorCodigo(nrVenda, sql);
            return Arrays.asList(venda);
        }        
        List<String> where = new ArrayList();        

        if (idCliente != null) {
            where.add(" ID_CLIENTE = " + idCliente);
        }
        
        if (dataVenda != null) {
            where.add(" DATA_VENDA = '" + dateJava2sqlDate(dataVenda) + "' ");
        }
        
        if (valorTotalInicio != null && valorTotalFim != null) {
            where.add(" (VALOR_TOTAL BETWEEN " + valorTotalInicio + " AND " + valorTotalFim + ")");
        }
        
        if (subTotalInicio != null && subTotalFim != null) {
            where.add(" (SUB_TOTAL BETWEEN " + subTotalInicio + " AND " + subTotalFim + ")");
        }
        
        if (!where.isEmpty()) {
            sql += " WHERE ";
            sql = sql + (String.join(" AND ", where));
        }
        
        PreparedStatement stmt;
        List<NVendas> lista = new ArrayList<>();
        try {            
            stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            lista = banco2ListModel(NVendas.class, rs, table);
            
        } catch (SQLException | IllegalArgumentException ex) {
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage(), "");
        } catch (Exception ex) {
            Logger.getLogger(PVendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    
    @Override
    public NVendas recuperarUltimoRegistro() throws PersistenteException {
        return (NVendas) super.recuperarUltimoRegistro(NVendas.class); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public NCliente getClienteCompraMaisFrequencia() {
        String sql = "SELECT \n" +
                    "	id_cliente, \n" +
                    "	COUNT(*)  count\n" +
                    "FROM \n" +
                    "	venda \n" +
                    "GROUP BY \n" +
                    "	id_cliente\n" +
                    "ORDER BY count desc LIMIT 1";
        System.out.println(sql);
        
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                int id = rs.getInt(1);
                CCliente c = new CCliente();
                return c.buscarPorId(id);
            }
        } catch (DatabaseException | SQLException | Erro ex) {
            Logger.getLogger(PVendas.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return new NCliente();
    }
    
    
    
    public Double getCompraDeMaiorValor() {
        String sql = "SELECT\n" +
                    "	SUM(VALOR_TOTAL) VALOR_TOTAL	\n" +
                    "FROM \n" +
                    "	VENDA\n" +
                    "GROUP BY \n" +
                    "	ID_CLIENTE\n" +
                    "ORDER BY VALOR_TOTAL DESC LIMIT 1";
        System.out.println(sql);
        
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                Double vl = rs.getDouble(1);
                return vl;
            }
        } catch (DatabaseException | SQLException ex) {
            Logger.getLogger(PVendas.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return 0.0;
    }
        
        
        
    public NCliente getClienteCompraMaiorValor() {
        String sql = "SELECT\n" +
                    "	ID_CLIENTE,\n" +
                    "	SUM(VALOR_TOTAL) VALOR_TOTAL	\n" +
                    "FROM \n" +
                    "	VENDA\n" +
                    "GROUP BY \n" +
                    "	ID_CLIENTE\n" +
                    "ORDER BY VALOR_TOTAL DESC LIMIT 1";
        System.out.println(sql);
        
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                int id = rs.getInt(1);
                CCliente c = new CCliente();
                return c.buscarPorId(id);
            }
        } catch (DatabaseException | SQLException | Erro ex) {
            Logger.getLogger(PVendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new NCliente();
    }
    
    
    public NCliente getClienteCompraMenosFrequencia() {
        String sql = "SELECT \n" +
                    "	id_cliente, \n" +
                    "	COUNT(*)  count\n" +
                    "FROM \n" +
                    "	venda \n" +
                    "GROUP BY \n" +
                    "	id_cliente\n" +
                    "ORDER BY count asc LIMIT 1";
        System.out.println(sql);
        
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                int id = rs.getInt(1);
                CCliente c = new CCliente();
                return c.buscarPorId(id);
            }
        } catch (DatabaseException | SQLException | Erro ex) {
            Logger.getLogger(PVendas.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return new NCliente();
    }
    
    
    public NVendas ultimaVendaRegistrada() {
        String sql = "SELECT id FROM venda ORDER BY ID DESC LIMIT 1";
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                int id = rs.getInt(1);
                return buscarPorId(id, "SELECT * FROM VENDA WHERE ID=" + id);
            }
        } catch (DatabaseException | SQLException | Erro ex) {
            Logger.getLogger(PVendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new NVendas();
    }
    
    
    public boolean deletarFormaPagamento(int idVenda) {
        String sql = "DELETE FROM FORMA_PAGAMENTO WHERE ID_VENDA = " + idVenda;
        try {
            delete(sql);
            commit();
        } catch (DatabaseException ex) {
            return false;
        }
        return true;
    }
            
    
    
    
}

