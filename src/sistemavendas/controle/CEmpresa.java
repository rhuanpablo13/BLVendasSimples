/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import java.io.File;
import java.util.List;
import sistemavendas.arquivos.Imagem;
import sistemavendas.configuracoes.SystemConfig;
import sistemavendas.negocio.NEmpresa;
import sistemavendas.persistente.PEmpresa;

/**
 *
 * @author rhuan
 */
public class CEmpresa extends Controle <NEmpresa> {

    
    private final PEmpresa dao = new PEmpresa("EMPRESA");
    
    
    @Override
    public boolean inserir(NEmpresa model) throws Erro {
        System.out.println("logo ");
        System.out.println(model.getLogomarca());
               
        if (model.getLogomarca() != null && !model.getLogomarca().isEmpty()) {
            File destino = new File(SystemConfig.IMAGENS_CLIENTE);
            File origem = new File(model.getLogomarca());
            
            String retorno = Imagem.save(origem, destino);
            model.setLogomarca(retorno);
        }
        dao.salvar(model);
        return true;
    }
    
    
    @Override
    public boolean alterar(NEmpresa model) throws Erro {
        if (model.getLogomarca() != null && !model.getLogomarca().isEmpty()) {
            File destino = new File(SystemConfig.IMAGENS_CLIENTE);
            File origem = new File(model.getLogomarca());
            String retorno = Imagem.save(origem, destino);
            model.setLogomarca(retorno);
        }
        dao.alterar(model, model.getId());
        return true;
    }

    
    @Override
    public boolean excluir(int id) throws Erro {
        dao.excluir(id);
        return true;
    }

    
    @Override
    public NEmpresa buscarPorCodigo(int codigo) throws Erro {
        return dao.buscarPorCodigo(codigo);
    }

    
    @Override
    public List<NEmpresa> getLista() throws Erro {
        return dao.getLista();
    }
    
    
    @Override
    public NEmpresa buscarPorId(int id) throws Erro {
        return dao.buscarPorId(id);
    }
    
    
    public NEmpresa recuperarUltimoRegistro() throws Exception {
        return dao.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
