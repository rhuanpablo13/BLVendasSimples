/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import java.util.List;
import java.util.Map;
import sistemavendas.negocio.NGrupo;

/**
 *
 * @author rhuan
 */
public class PGrupo extends Persistente <NGrupo>{
    
    public PGrupo(String table) {
        super(table);
    }

    public List<NGrupo> getLista() throws Erro {        
        return super.getLista("SELECT * FROM " + table); 
    }
 
    public void inserir(NGrupo grupo) throws Erro { 
        String sql = "INSERT INTO GRUPO (CODIGO, DESCRICAO) VALUES (?,?)";
        super.inserir(grupo, sql);
    }

    
    @Override
    public NGrupo recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }


    public NGrupo buscarPorId(int id) throws Erro {
        return super.buscarPorId(id, "SELECT * FROM " + super.table + " WHERE ID = ?");
    }


    public NGrupo buscarPorCodigo(int codigo) throws Erro {
        return super.buscarPorCodigo(codigo, "SELECT * FROM " + super.table + " WHERE CODIGO = ?");
    }
    
    
    public List<NGrupo> pesquisarPorAtributos(NGrupo grupo) throws Erro {
        Map<List<String>, Object> atributos = null;
        try {
            atributos = getAttributesAndValues(grupo, true);
        } catch (PersistenteException ex) {
            throw new Erro(ex.getMessage(), "");
        }
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSelect(table, atributos);
        System.out.println(sql);
        return super.getLista(sql);
    }
    
}
