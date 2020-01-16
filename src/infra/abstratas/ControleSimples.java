/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.abstratas;

import java.util.List;

/**
 *
 * @author rhuan
 * @param <T>
 */
public class ControleSimples <T extends NegocioSimples> {
    
    
    
    public ControleSimples() {
    }
    
    /**
     * Método de inserção que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param model
     * @throws Exception 
     */
    public void inserir(T model) throws Exception {
        throw new UnsupportedOperationException("Método inserir precisa ser sobreescrito"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /**
     * Método de alteração que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param model
     * @throws Exception 
     */
    public void alterar (T model) throws Exception {
        throw new UnsupportedOperationException("Método alterar precisa ser sobreescrito"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /**
     * Método de exclusão que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param id
     * @throws Exception 
     */
    public void excluir (int id) throws Exception {
        throw new UnsupportedOperationException("Método excluir precisa ser sobreescrito"); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * Método de pesquisa por código que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param codigo
     * @return T
     * @throws Exception 
     */
    public T  buscarPorCodigo(int codigo) throws Exception {
        throw new UnsupportedOperationException("Método buscarPorCódigo precisa ser sobreescrito"); //To change body of generated methods, choose Tools | Templates.
    }
        
    
    
    /**
     * Método de pesquisa por id que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param id
     * @return T
     * @throws Exception 
     */
    public T  buscarPorId(int id) throws Exception {
        throw new UnsupportedOperationException("Método buscarPorId precisa ser sobreescrito"); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    /**
     * Método de pesquisar todos os registros, recuperando as informações 
     * da DAO (Persistence) repassar para a View
     * 
     * @return List
     * @throws Exception 
     */
    public List<T>  getLista() throws Exception {
        throw new UnsupportedOperationException("Método getLista precisa ser sobreescrito"); //To change body of generated methods, choose Tools | Templates.
    }
    
}
