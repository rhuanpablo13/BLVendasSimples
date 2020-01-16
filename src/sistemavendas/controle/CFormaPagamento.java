/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.ControleSimples;
import infra.comunicacao.Erro;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sistemavendas.negocio.NFormaPagamento;
import sistemavendas.negocio.NVendas;
import sistemavendas.persistente.PFormaPagamento;

/**
 *
 * @author rhuan
 */
public class CFormaPagamento extends ControleSimples<NFormaPagamento> {

    
    private PFormaPagamento persistente = new PFormaPagamento("FORMA_PAGAMENTO");
    
    
    public void inserir(NFormaPagamento model, int idVenda) throws Erro {
        persistente.inserir(model, idVenda);
    }

    
    public void alterar(NFormaPagamento model, int idVenda) throws Erro {
        persistente.alterar(model, idVenda);
    }

    
    public void excluir(int id) throws Erro {
        persistente.excluir(id);
    }


    public List<NFormaPagamento> getLista() throws Erro {
        return persistente.getList();
    }
    
    
    public NFormaPagamento pesquisar(NFormaPagamento model, int idVenda) throws Erro {
        return persistente.pesquisar(model, idVenda);
    }
    
    
    public NFormaPagamento pesquisar(NFormaPagamento model) throws Erro {
        return persistente.pesquisar(model);
    }
    
    
    
    public NFormaPagamento pesquisar(Integer nrVenda) throws Erro {
        return persistente.pesquisar(nrVenda);
    }
    
}
