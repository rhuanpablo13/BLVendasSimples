/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.Erro;
import java.util.List;
import sistemavendas.negocio.CRT;
import sistemavendas.negocio.NEmpresa;

/**
 *
 * @author rhuan
 */
public class PEmpresa extends Persistente <NEmpresa> {
    
    public PEmpresa(String table) {
        super(table);
    }

    
    @Override
    protected Object convert(Object valor) {
        
        if (valor == null) {
            return (null);
        }
        
        String type = valor.getClass().getTypeName();
        if (type.contains("sistemavendas.negocio.CRT")) {
            CRT crt = (CRT) valor;
            if (crt == CRT.REGIME_NORMAL) {
                return ("REGIME_NORMAL");
            }
            if (crt == CRT.SIMPLES_NACIONAL) {
                return ("SIMPLES_NACIONAL");
            }
            if (crt == CRT.SIMPLES_NACIONAL_EXCESSO_RECEITA_BRUTA) {
                return ("SIMPLES_NACIONAL_EXCESSO_RECEITA_BRUTA");
            }
        }
        return super.convert(valor);
    }

    
    public void salvar(NEmpresa empresa) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        super.inserir(empresa, sql);
    }
    
    
    public void alterar(NEmpresa model, int id) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleUpdate(table, getTableColumns());        
        super.alterar(model, id, sql);
    }


    public void excluir(int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleDelete(table);
        super.excluir(codigo, sql);
    }
    
    public NEmpresa buscarPorCodigo(int codigo) throws Erro {
        return super.buscarPorCodigo(codigo, "SELECT * FROM " + super.table + " WHERE CODIGO = ?;");
    }

    
    public NEmpresa buscarPorId(int id) throws Erro {
        return super.buscarPorId(id, "SELECT * FROM " + super.table + " WHERE ID = ?;");
    }
    
    
    public List<NEmpresa> getLista() throws Erro {
        PersistenceServices ps = new PersistenceServices();
        return super.getLista(ps.createSimpleSelect(table));
    }

    @Override
    public NEmpresa recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
