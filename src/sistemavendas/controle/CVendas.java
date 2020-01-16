package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NVendas;
import sistemavendas.persistente.PVendas;




public class CVendas extends Controle <NVendas> {

    
    private PVendas vendasDao = new PVendas("VENDA");

    @Override
    public boolean inserir(NVendas model) throws Erro {
        try {
            vendasDao.inserir(model);
            return true;
        } catch (Exception e) {
            throw new Erro(e.getMessage(), "Erro");
        }        
    }

    

    @Override
    public boolean alterar(NVendas model) throws Erro {
        try {
            vendasDao.alterar(model, model.getId());
            return true;
        } catch (Exception e) {
            throw new Erro(e.getMessage(), "Erro");
        }
    }


    public boolean excluir(NVendas venda) throws Erro {
        try {
            vendasDao.excluir(venda);
        } catch (Exception e) {
            throw new Erro(e.getMessage(), "Erro");
        }
        return true;
    }
    

    @Override
    public boolean excluir(int id) throws Erro {
        try {
            vendasDao.excluir(id);            
        } catch (Exception e) {
            throw new Erro(e.getMessage(), "Erro");
        }
        return true;
    }
    
    

    @Override
    public List<NVendas> getLista() throws Erro {
        List<NVendas> lista = new ArrayList();
        try {
            lista = vendasDao.getLista();            
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Erro");
        }
        return lista;
    }

    
    
    @Override
    public NVendas buscarPorCodigo(int codigo) throws Erro {
        try {
            return vendasDao.buscarPorCodigo(codigo);            
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Erro");
        }
    }
    
    
    @Override
    public NVendas buscarPorId(int id) throws Erro {
        try {
            return vendasDao.buscarPorId(id);            
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Erro");
        }
    }    


    
    public List<NVendas> pesquisar(Integer nrVenda, Integer idCliente, Date dataVenda, Double valorTotalInicio, Double valorTotalFim, Double subTotalInicio, Double subTotalFim) throws Erro {
        List<NVendas> clientes = new ArrayList();
        try {
            clientes = vendasDao.pesquisar(nrVenda, idCliente, dataVenda, valorTotalInicio, valorTotalFim, subTotalInicio, subTotalFim);
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Erro");
        }        
        return clientes;
    }
    
    

    public NVendas recuperaUltimoRegistro () throws Erro {
        try {
            return vendasDao.recuperarUltimoRegistro();
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Erro");
        }
    }

    
    
    public NCliente getClienteCompraMaisFrequencia() {
        return vendasDao.getClienteCompraMaisFrequencia();
    }
    
    
    public NCliente getClienteCompraMaiorValor() {
        return vendasDao.getClienteCompraMaiorValor();
    }
    
    
    public NCliente getClienteCompraMenosFrequencia() {
        return vendasDao.getClienteCompraMenosFrequencia();
    }
    
    
    public Double getCompraDeMaiorValor() {
        return vendasDao.getCompraDeMaiorValor();
    }
    
    
    public boolean deletarFormaPagamento(int idVenda) {
        return vendasDao.deletarFormaPagamento(idVenda);
    }
    
    
    public boolean deletarFormaPagamento(NVendas venda) {
        return deletarFormaPagamento(venda.getId());
    }
    
    
    
    
}