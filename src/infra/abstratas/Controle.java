/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.abstratas;

import infra.comunicacao.Erro;
import java.util.List;

/**
 * Classe que representa uma controle, que vai fazer a comunicação entre
 * a view (negócio) e a persistencia
 * 
 * @author RHUAN
 * @param <T> Classe de Negócio 
 */
public abstract class Controle <T extends Negocio> {
    
    
    
    /**
     * Método de inserção que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param model
     * @return true if sucess
     * @throws infra.comunicacao.Erro
     */
    public abstract boolean inserir(T model) throws Erro;
    
    
    /**
     * Método de alteração que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param model
     * @return true if sucess
     */
    public abstract boolean alterar (T model) throws Erro;
    
    
    /**
     * Método de exclusão que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param id
     * @return true if sucess
     */
    public abstract boolean excluir (int id) throws Erro;

    
    /**
     * Método de pesquisa por código que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param codigo
     * @return T
     */
    public abstract T  buscarPorCodigo(int codigo) throws Erro;
        
    
    
    /**
     * Método de pesquisa por id que vai receber os dados da View 
     * e repassar para a DAO (Persistence)
     * 
     * @param id
     * @return T
     */
    public T  buscarPorId(int id) throws Erro {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    /**
     * Método de pesquisar todos os registros, recuperando as informações 
     * da DAO (Persistence) repassar para a View
     * 
     * @return List
     */
    public abstract List<T>  getLista() throws Erro;

}

