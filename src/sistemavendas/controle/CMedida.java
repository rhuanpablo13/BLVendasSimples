/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import java.util.List;
import sistemavendas.negocio.NMedida;
import sistemavendas.persistente.PMedida;

/**
 *
 * @author rhuan
 */
public class CMedida extends Controle<NMedida> {

    
    private PMedida medidaDao = new PMedida("UNIDADE_MEDIDA_COMERCIAL");
    
    
    @Override
    public boolean inserir(NMedida model) throws Erro {
        medidaDao.inserir(model);
        return true;
    }

    @Override
    public boolean alterar(NMedida model) throws Erro {
        medidaDao.alterar(model);
        return true;
    }

    @Override
    public boolean excluir(int id) throws Erro {
        medidaDao.excluir(id);
        return true;
    }

    @Override
    public NMedida buscarPorCodigo(int id) throws Erro {
        return medidaDao.buscarPorCodigo(id);
    }

    @Override
    public List getLista() throws Erro {
        return medidaDao.getLista();
    }
    
    public List<NMedida> pesquisar(NMedida medida) throws Erro {
        return medidaDao.pesquisar(medida);
    }
    
    
    public NMedida recuperarUltimoRegistro() throws Exception {
        return medidaDao.recuperarUltimoRegistro();
    }
    
    @Override
    public NMedida buscarPorId(int id) throws Erro {
        return medidaDao.buscarPorId(id);
    }
}
