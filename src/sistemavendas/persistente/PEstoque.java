/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.abstratas.GenericDao;
import infra.comunicacao.Erro;
import infra.comunicacao.Message;
import java.util.Map;
import sistemavendas.controle.CProduto;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class PEstoque extends GenericDao {
    
    
    public boolean atualizarEstoque(Map<NProduto, Integer> produtosVendidos) throws Erro {

        boolean rollback = false;
        CProduto controllerProduto = new CProduto();

        for (Map.Entry<NProduto, Integer> entry : produtosVendidos.entrySet()) {
            NProduto produto = entry.getKey();
            Integer quantidade = entry.getValue();
            
            try {
                produto.setQtdEstoque( (produto.getQtdEstoque() - quantidade) );
                controllerProduto.alterar(produto);
            } catch (Message ex) {
                if (!ex.isSucess()) {
                    rollback = true; break;                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                rollback = true; break;
            }
        }
        if (rollback) {
            try {
                rollback();
            } catch(Exception e) {e.printStackTrace();}            
        }
        return true;
    }
    
    
    
    public boolean devolver(NProduto produto, int quantidade) throws Erro {

        boolean rollback = false;
        CProduto controllerProduto = new CProduto();
        try {
            produto.setQtdEstoque( (produto.getQtdEstoque() + quantidade) );
            controllerProduto.alterar(produto);
        } catch (Message ex) {
            if (!ex.isSucess()) {
                rollback = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            rollback = true;
        }
        if (rollback) {
            try {
                rollback();
            } catch(Exception e) {e.printStackTrace();}            
        }
        return true;
    }
    
    
    
    public boolean retirar(NProduto produto, int quantidade) throws Erro {

        boolean rollback = false;
        CProduto controllerProduto = new CProduto();
        try {
            produto.setQtdEstoque( (produto.getQtdEstoque() - quantidade) );
            controllerProduto.alterar(produto);
        } catch (Message ex) {
            if (!ex.isSucess()) {
                rollback = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            rollback = true;
        }
        if (rollback) {
            try {
                rollback();
            } catch(Exception e) {e.printStackTrace();}            
        }
        return true;
    }    
    
}
