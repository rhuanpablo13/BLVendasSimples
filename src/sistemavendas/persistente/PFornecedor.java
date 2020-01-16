package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NFornecedor;



public class PFornecedor extends Persistente<NFornecedor> {

    
    public PFornecedor(String table) {
        super(table);
    }

    
    public void inserir(NFornecedor model) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        System.out.println(sql);
        super.inserir(model, sql); 
    }

    
    
    public void alterar(NFornecedor model, int id) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleUpdate(table, getTableColumns());        
        super.alterar(model, id, sql);
    }   
    
    
    public void excluir(int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleDelete(table);
        super.excluir(codigo, sql);
    }


    public List<NFornecedor> getLista() throws Erro {
        PersistenceServices ps = new PersistenceServices();
        System.out.println(ps.createSimpleSelect(table));
        return super.getLista(ps.createSimpleSelect(table));
    }


    public NFornecedor buscarPorCodigo(int codigo) throws Erro {
        return super.buscarPorCodigo(codigo, "SELECT * FROM FORNECEDOR WHERE CODIGO = ?;");
    }
  
    
    public List<NFornecedor> pesquisarPorAtributos(NFornecedor fornecedor) throws Erro {
        Map<List<String>, Object> atributos = null;
        try {
            atributos = getAttributesAndValues(fornecedor, true);
        } catch (PersistenteException ex) {
            Logger.getLogger(PFornecedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSelect(table, atributos);
        System.out.println(sql);
        return super.getLista(sql);
    }    

    
    
    @Override
    public NFornecedor recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}