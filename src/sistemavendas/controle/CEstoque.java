/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.ControleSimples;
import infra.comunicacao.Erro;
import java.util.HashMap;
import java.util.Map;
import sistemavendas.negocio.NEstoque;
import sistemavendas.negocio.NProduto;
import sistemavendas.persistente.PEstoque;

/**
 *
 * @author rhuan
 */
public class CEstoque extends ControleSimples<NEstoque> {
    
    
    public boolean atualizarEstoque(Map<NProduto, Integer> produtosVendidos) throws Erro {
        PEstoque persistente = new PEstoque();
        return persistente.atualizarEstoque(produtosVendidos);
    }

    public boolean atualizarEstoque(NProduto p, Integer q) throws Erro {
        HashMap<NProduto, Integer> produtosVendidos = new HashMap();
        produtosVendidos.put(p, q);
        return atualizarEstoque(produtosVendidos);
    }
    
    public boolean devolver(NProduto produto, int quantidade) throws Erro {
        PEstoque persistente = new PEstoque();
        return persistente.devolver(produto, quantidade);
    }
    
    
    public boolean retirar(NProduto produto, int quantidade) throws Erro {
        PEstoque persistente = new PEstoque();
        return persistente.retirar(produto, quantidade);
    }
}
