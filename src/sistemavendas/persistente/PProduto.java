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
import infra.comunicacao.StackDebug;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class PProduto extends Persistente<NProduto>{
    
    
    public PProduto(String table) {
        super(table);
    }
    

        
    public void inserir(NProduto model) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        super.inserir(model, sql);
    }
    
    public void alterar(NProduto model, int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleUpdate(table, getTableColumns());
        super.alterar(model, codigo, sql);
    }
    
    public void excluir(int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleDelete(table);
        super.excluir(codigo, sql);
    }
    
    
    
    public void excluir(NProduto produto) throws Erro {
        String sql = "DELETE FROM PRODUTO_CODIGO_BARRAS WHERE ";
        sql += " ID_PRODUTO = " + produto.getId() + " AND CODIGO_BARRAS = " + produto.getCodigoBarras();
        
        System.out.println(sql);
        try {
            delete(sql);
            commit();
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage(), "Falha ao excluir produtos");
        }
    }

    
    
    public List<NProduto> getLista() throws Erro {
        PersistenceServices ps = new PersistenceServices();
        return super.getLista(ps.createSimpleSelect(table));
    }


    public NProduto buscarPorCodigo(int codigo) throws Erro {
        NProduto p = super.buscarPorCodigo(codigo, "SELECT * FROM " + super.table + " WHERE CODIGO = ?");
        Integer codigoBarras = recuperarUltimoCodigoBarras(p);
        if (codigoBarras != null) {
            p.setCodigoBarras(codigoBarras);
        }
        return p;
    }    
    
    
    public NProduto buscarPorId(int id) throws Erro {
        NProduto p = super.buscarPorId(id, "SELECT * FROM " + super.table + " WHERE ID = ?");
//        Integer codigoBarras = recuperarUltimoCodigoBarras(p);
//        if (codigoBarras != null) {
//            p.setCodigoBarras(codigoBarras);
//        }
        return p;
    }
    
    

    
    
    
    public List<NProduto> pesquisarPorAtributos (NProduto produto) throws Erro {
        Map<List<String>, Object> atributos = null;
        try {
            atributos = getAttributesAndValues(produto, true);
        } catch (PersistenteException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSelect(table, atributos);
        System.out.println(sql);
        return super.getLista(sql);
    }
    
    
    public List<NProduto> getLista(Integer idGrupo) throws Erro {
        try {
            String sql = "SELECT * FROM PRODUTO WHERE ID_GRUPO = " + idGrupo;
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return banco2ListModel(NProduto.class, rs, table);
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage(), "");
        } catch (SQLException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public List<NProduto> getLista(Integer idGrupo, boolean emEstoque) throws Erro {
        try {
            if (!emEstoque) {
                return getLista(idGrupo);
            }
            String sql = "SELECT * FROM PRODUTO WHERE QTD_ESTOQUE > 0 AND ID_GRUPO = " + idGrupo;
            System.out.println(sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return banco2ListModel(NProduto.class, rs, table);
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage(), "");
        } catch (SQLException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    @Override
    public NProduto recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    public boolean inserirTabelaCodigoBarra(NProduto produto, Integer codigoBarras) throws Erro {
        return inserirTabelaCodigoBarra(produto, Integer.toString(codigoBarras));
    }
        
        
    public boolean inserirTabelaCodigoBarra(NProduto produto, String codigoBarras) throws Erro {
        String sql = "INSERT INTO PRODUTO_CODIGO_BARRAS (ID_PRODUTO, CODIGO_BARRAS) VALUES ("+produto.getId()+","+codigoBarras+")";
        try {
            save(sql);
            commit();
        } catch (DatabaseException ex) {
            try {
                rollback();
            } catch(DatabaseException ex1) {}
            throw new Erro("Não foi possível salvar código de barras informado! " + StackDebug.getLineNumber(this.getClass()), "Falha ao salvar código de barras");
        }
        return true;
    }
    
    
    public boolean removerCodigoBarraTabela(NProduto produto, String codigoBarras) throws Erro {
        String sql = "DELETE FROM PRODUTO_CODIGO_BARRAS WHERE ID_PRODUTO = " + produto.getId() + " AND CODIGO_BARRAS = '" + codigoBarras + "'";
        System.out.println("removerCodigoBarraTabela -> " + sql);
        try {
            delete(sql);
            commit();
        } catch (DatabaseException ex) {
            try {
                rollback();
            } catch(DatabaseException ex1) {}
            throw new Erro("Não foi possível excluir código de barras informado! [" + codigoBarras + "]" + StackDebug.getLineNumber(this.getClass()), "Falha ao excluir código de barras");
        }
        return true;
    }
    
    
    public Integer recuperarUltimoCodigoBarras(NProduto produto) {
        String sql = ("SELECT MAX(CODIGO_BARRAS) FROM PRODUTO_CODIGO_BARRAS WHERE ID_PRODUTO = " + produto.getId());
        System.out.println("recuperarUltimoCodigoBarras -> " + sql);        
        
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                return rs.getInt(1);
            }
        } catch (SQLException | IllegalArgumentException | DatabaseException ex) {
            return null;
        }
        return null;
    }
    
    
    
    public Integer countCodigoBarras(NProduto produto) {
        String sql = ("SELECT COUNT(CODIGO_BARRAS) FROM PRODUTO_CODIGO_BARRAS WHERE ID_PRODUTO = " +produto.getId());
        System.out.println("countCodigoBarras -> " + sql);

        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                return rs.getInt(1);
            }
        } catch (SQLException | IllegalArgumentException | DatabaseException ex) {
            return null;
        }
        return null;
    }
    
    
    
    public Integer recuperarUltimoCodigoBarras() {
        String sql = ("SELECT MAX(CODIGO_BARRAS) FROM PRODUTO_CODIGO_BARRAS");
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                return rs.getInt(1);
            }
        } catch (SQLException | IllegalArgumentException | DatabaseException ex) {
            return null;
        }
        return null;
    }    
    
    
    public boolean deletarCodigosBarras(String codigoBarras) throws Erro {
        String sql = "DELETE FROM PRODUTO_CODIGO_BARRAS WHERE CODIGO_BARRAS = " + codigoBarras;
        try {
            delete(sql);
            commit();
        } catch (DatabaseException ex) {
            throw new Erro("Falha ao excluir código de barras: " + codigoBarras, "");
        }
        return false;
    }
    
    
    public boolean deletarCodigosBarras(Integer codigoBarras) throws Erro {
        return deletarCodigosBarras(Integer.toString(codigoBarras));
    }
    
    
    public List<NProduto> getLoteProduto(NProduto produto) throws Erro {
        
        List<NProduto> pds = new ArrayList();
        try {
            String sql = "SELECT PCB.CODIGO_BARRAS, P.ID, P.CODIGO, P.NOME FROM PRODUTO_CODIGO_BARRAS PCB, PRODUTO P ";
            sql += "WHERE PCB.ID_PRODUTO = " + produto.getId() + " AND P.ID = " + produto.getId();
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NProduto p = new NProduto();                
                p.setCodigoBarras(rs.getInt(1));
                p.setId(rs.getInt(2));
                p.setCodigo(rs.getInt(3));
                p.setNome(rs.getString(4));
                pds.add(p);
            }
            return pds;
        } catch (DatabaseException | SQLException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pds;
    }
    
    
    
    public int count(Integer id) throws PersistenteException {
        return super.count("SELECT COUNT(*) FROM " + table + " WHERE ID = " + id);
    }
    
 
    
        public Integer count() {
        try {
            return super.count("SELECT COUNT(*) FROM " + table);
        } catch (PersistenteException ex) {
            Logger.getLogger(PCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    public Integer count(boolean ativos) {
        try {
            if (!ativos) return count();
            return super.count("SELECT COUNT(*) FROM " + table + " WHERE ATIVO = TRUE");
        } catch (PersistenteException ex) {
            Logger.getLogger(PCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    public Integer totalProdutosEmEstoque() {
        try {
            String sql = "SELECT SUM(QTD_ESTOQUE) FROM " + table;
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return rs.getInt(1);
            }
        } catch (DatabaseException | SQLException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    
    public Double totalValorVendaEmEstoque() {
        try {
            String sql = "SELECT SUM(TOTAL) FROM (\n" +
                        "	SELECT SUM(VALOR_VENDA) * QTD_ESTOQUE AS TOTAL FROM " + table + " WHERE ATIVO = TRUE  GROUP BY id\n" +
                        ") P";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return rs.getDouble(1);
            }
        } catch (DatabaseException | SQLException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0.0;
    }
    
    
    public Integer totalProdutosCadastroRapido() {
        try {
            String sql = "SELECT COUNT(*) FROM " + table + " WHERE VALOR_CUSTO IS NULL";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return rs.getInt(1);
            }
        } catch (DatabaseException | SQLException ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    
    public NProduto maisVendido() {
        NProduto p = new NProduto();
        try {
            String sql = "SELECT ID_PRODUTO FROM (\n" +
                        "	SELECT \n" +
                        "		SUM(QUANTIDADE) QUANTIDADE, \n" +
                        "		ID_PRODUTO\n" +
                        "	FROM (\n" +
                        "		SELECT\n" +
                        "	 		SUM(QUANTIDADE) QUANTIDADE,\n" +
                        "	 		ID_PRODUTO\n" +
                        "	 	FROM \n" +
                        "	 		CARRINHO_PRODUTO\n" +
                        "	 	GROUP BY \n" +
                        "	 		ID_PRODUTO, QUANTIDADE\n" +
                        "	) AS A\n" +
                        "	GROUP BY A.ID_PRODUTO\n" +
                        ") B \n" +
                        "ORDER BY ID_PRODUTO DESC\n" +
                        "LIMIT 1;";
            
            System.out.println(sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                int id = rs.getInt(1);
                p = buscarPorId(id);
            }
        } catch (DatabaseException | SQLException | Erro ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    
    
    public NProduto menosVendido() {
        NProduto p = new NProduto();
        try {
            String sql = "SELECT ID_PRODUTO, MIN(QUANTIDADE) FROM (\n" +
                        "	SELECT \n" +
                        "		SUM(QUANTIDADE) QUANTIDADE, \n" +
                        "		ID_PRODUTO \n" +
                        "	FROM CARRINHO_PRODUTO \n" +
                        "	GROUP BY ID_PRODUTO\n" +
                        ") VENDAS";
            
            System.out.println(sql);
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                int id = rs.getInt(1);
                p = buscarPorId(id);
            }
        } catch (DatabaseException | SQLException | Erro ex) {
            Logger.getLogger(PProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
}
