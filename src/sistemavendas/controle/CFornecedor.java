package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import infra.comunicacao.Sucess;
import infra.comunicacao.Warning;
import java.util.ArrayList;
import java.util.List;
import sistemavendas.negocio.NFornecedor;
import sistemavendas.persistente.PFornecedor;


public class CFornecedor extends Controle <NFornecedor> {
    
    
    private PFornecedor fornecedorDao = new PFornecedor("FORNECEDOR");
    
    
    
    @Override
    public boolean inserir(NFornecedor model) throws Erro {
        fornecedorDao.inserir(model);
        return true;
    }

    
    
    @Override
    public boolean alterar(NFornecedor model) throws Erro {
        fornecedorDao.alterar(model, model.getId());
        return true;
    }
    
    

    @Override
    public boolean excluir(int id) throws Erro {
        fornecedorDao.excluir(id);
        return true;
    }

    
    
    @Override
    public NFornecedor buscarPorCodigo(int codigo) throws Erro {
        return fornecedorDao.buscarPorCodigo(codigo);
    }

    
    
    @Override
    public List<NFornecedor> getLista() throws Erro {
        return fornecedorDao.getLista();
    }
    
    
    public NFornecedor recuperarUltimoRegistro() throws Exception {
        return fornecedorDao.recuperarUltimoRegistro();
    }

    
    
    public List<NFornecedor> pesquisar(NFornecedor fornecedor) throws Exception {
        return fornecedorDao.pesquisarPorAtributos(fornecedor);
    }
    

}