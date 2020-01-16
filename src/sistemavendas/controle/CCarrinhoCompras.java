/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.ControleSimples;
import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NProduto;
import sistemavendas.negocio.NVendas;
import sistemavendas.persistente.PCarrinhoCompras;
import sistemavendas.vendas.NCarrinhoCompras;

/**
 *
 * @author rhuan
 */
public class CCarrinhoCompras extends ControleSimples {

    PCarrinhoCompras persistente = new PCarrinhoCompras("CARRINHO");
    
    
    @Override
    public List getLista() throws Exception {
        return persistente.getList();
    }

    
    public NCarrinhoCompras getCarrinhoCompras(NVendas venda) throws SQLException, Exception {
        return persistente.getCarrinhoCompras(venda);
    }
    
  
    public void atualizarProdutosDoCarrinho(NCarrinhoCompras carrinho, Map<NProduto, Integer> produtos) throws Erro {
        try {
            persistente.atualizarProdutosDoCarrinho(carrinho, produtos);
        } catch (Exception ex) {
            throw new Erro("Não foi possível atualizar o carrinho de compras: " + carrinho.getDescricao(), "");
        }
    }

    
    @Override
    public void excluir(int id) throws Erro {
        try {
            persistente.excluir(id);
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            throw new Erro(ex.getMessage(), "");
        }
    }
}