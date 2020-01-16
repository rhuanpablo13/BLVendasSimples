package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import java.util.List;
import sistemavendas.negocio.NCliente;
import sistemavendas.persistente.PCliente;


public class CCliente extends Controle <NCliente> {
    
    
    private final PCliente clienteDao = new PCliente("CLIENTE");
    
    
    @Override
    public boolean inserir(NCliente model) throws Erro {
        clienteDao.inserir(model);
        return true;
    }


    @Override
    public boolean alterar(NCliente model) throws Erro {
        clienteDao.alterar(model, model.getId());
        return true;
    }



    @Override
    public boolean excluir(int id) throws Erro {
        clienteDao.excluir(id);
        return true;
    }
    
    

    @Override
    public List<NCliente> getLista() throws Erro {
        return clienteDao.getLista();
    }

    
    
    @Override
    public NCliente buscarPorCodigo(int codigo) throws Erro {
        return clienteDao.buscarPorCodigo(codigo);
    }
    
    
    @Override
    public NCliente buscarPorId(int id) throws Erro {
        return clienteDao.buscarPorId(id);
    }
    
    
    public List<NCliente> pesquisar(NCliente cliente) throws Erro {
        return clienteDao.pesquisarPorAtributos(cliente);
    }
    
    
    public NCliente recuperaUltimoRegistro() throws Exception {
        return clienteDao.recuperarUltimoRegistro();
    }
    
    
    public Integer count() {
        return clienteDao.count();
    }
}