/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import java.util.List;
import sistemavendas.negocio.NGrupo;
import sistemavendas.persistente.PGrupo;

/**
 *
 * @author rhuan
 */
public class CGrupo extends Controle <NGrupo> {

    private PGrupo grupoDao = new PGrupo("GRUPO");

    
    @Override
    public boolean inserir(NGrupo model) throws Erro {
        grupoDao.inserir(model);
        return true;
    }

    @Override
    public boolean alterar(NGrupo model) throws Erro {
        throw new Erro("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean excluir(int id) throws Erro {
        throw new Erro("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NGrupo buscarPorCodigo(int codigo) throws Erro {
        return grupoDao.buscarPorCodigo(codigo);
    }

    @Override
    public NGrupo buscarPorId(int id) throws Erro {
        return grupoDao.buscarPorId(id);
    }

    
    @Override
    public List<NGrupo> getLista() throws Erro {
        return grupoDao.getLista();
    }
    
    public NGrupo recuperarUltimoRegistro() throws Exception {
        return grupoDao.recuperarUltimoRegistro();
    }
    
    public List<NGrupo> pesquisar(NGrupo grupo) throws Erro {
        return grupoDao.pesquisarPorAtributos(grupo);
    }
    
}
