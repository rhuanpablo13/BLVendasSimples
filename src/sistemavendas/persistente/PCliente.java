package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NCliente;




public class PCliente extends Persistente<NCliente> {
    
    
    
    public PCliente(String table) {
        super(table);
    }
    
    
    public void inserir(NCliente model) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        super.inserir(model, sql);
    }


    public void alterar(NCliente model, int id) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleUpdate(table, getTableColumns());        
        super.alterar(model, id, sql);
    }


    public void excluir(int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleDelete(table);
        super.excluir(codigo, sql);
    }


    public List<NCliente> getLista() throws Erro {
        PersistenceServices ps = new PersistenceServices();
        return super.getLista(ps.createSimpleSelect(table));
    }
    
    
    public NCliente buscarPorCodigo(int codigo) throws Erro {
        return super.buscarPorCodigo(codigo, "SELECT * FROM " + super.table + " WHERE CODIGO = ?;");
    }

    
    public NCliente buscarPorId(int id) throws Erro {
        return super.buscarPorId(id, "SELECT * FROM " + super.table + " WHERE ID = ?;");
    }

    
    public List<NCliente> pesquisarPorAtributos(NCliente cliente) throws Erro {
        Map<List<String>, Object> atributos = null;
        try {
            atributos = getAttributesAndValues(cliente, true);
        } catch (PersistenteException ex) {
            throw new Erro(ex.getMessage(), "");
        }
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSelect(table, atributos);
        System.out.println(sql);
        return super.getLista(sql);
    }

    public NCliente recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public Integer count() {
        try {
            return super.count("SELECT COUNT(*) FROM CLIENTE");
        } catch (PersistenteException ex) {
            Logger.getLogger(PCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}   