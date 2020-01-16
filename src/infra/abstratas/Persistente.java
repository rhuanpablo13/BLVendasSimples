
/*
 * Negocioo change this license header, choose License Headers in Project Properties.
 * Negocioo change this template file, choose Negocioools | Negocioemplates
 * and open the template in the editor.
 */
package infra.abstratas;

import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import infra.comunicacao.StackDebug;
import infra.mapeamento.MapNegocioBanco;
import infra.reflection.ObjectRefflection;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;




/**
 * Classe abstrata para implementação dos métodos de persistencia
 * @author RHUAN
 * @param <T>
 */
public abstract class Persistente <T extends Negocio> extends PersistenteGeneric {
    
    protected Class<T> negocioClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];;
    protected MapNegocioBanco map;


    public Persistente(String table) {
        super(table);
        map = new MapNegocioBanco(negocioClass, table);
//        map.print();
    }
    
    
    
    
    protected void inserir(T model, String sql) throws Erro {

        try {
            model.executarAntesInserir();
        } catch (Erro ex) {
            throw ex;
        }
        
        
        List<Object> values;
        try {
            values = model2Banco(model, true);
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Falha na inserção Model/Banco" + StackDebug.getLineNumber(this.getClass()));
        }
        
        try {
            save(sql, values);
            commitData();
        } catch (DatabaseException ex) {
            String msg = decodeSqlException(ex);
            if (msg == null) {
                msg = ex.getMessage();
            }
            throw new Erro(msg, "Erro de inserção");
        }
       
        
        if (! model.executarDepoisInserir()) {
            try {
                rollback();
                System.out.println("rollback");
            } catch (DatabaseException ex) {
                throw new Erro(ex.getMessage(), "Erro de inserção");
            }
            throw new Erro("Falha ao executar método [executarDepoisInserir] do objeto [" + model.getClass().getCanonicalName()+"], rollback efetuado!");
        }
        
        
        try {
            /* Fechando conexão */
            closeConnection();
        } catch (DatabaseException ex) {
            Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    
    protected void alterar(T model, int id, String sql) throws Erro {
        
        try {
            model.executarAntesAlterar(model);
        } catch (Erro ex) {
            throw ex;
        }

        
        List<Object> values;
        try {
            values = model2Banco(model, false);
        } catch (Exception ex) {
            throw new Erro(ex.getMessage(), "Falha na alteração Model/Banco" + StackDebug.getLineNumber(this.getClass()));
        }


        try {
            update(sql, id, values);
            commit();
        } catch (DatabaseException ex) {
            Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        if (!model.executarDepoisAlterar()) {
            try {
                rollbackAndClose();
            } catch (DatabaseException ex) {
                Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new Erro("Falha ao executar método [executarDepoisAlterar] do objeto [" + model.getClass().getCanonicalName()+"], rollback efetuado!");
        }
        
        
        try {
            /* Fechando conexão */
            closeConnection();
        } catch (DatabaseException ex) {
            Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    protected void excluir(T model, String sql) throws Erro {
        
        if (!model.executarAntesExcluir()) {
            try {
                rollback();
                System.out.println("rollback exclusão");
            } catch (DatabaseException ex) {
                throw new Erro(ex.getMessage(), "Erro de exclusão");
            }
            throw new Erro("Falha ao executar método [executarDepoisExcluir] do objeto [" + model.getClass().getCanonicalName()+"], rollback efetuado!");
        }
        
        try {
            System.out.println(sql);
            System.out.println(model.getId());
            delete(sql, model.getId());
            commit();
        } catch (DatabaseException ex) {
            throw new Erro("Falha ao executar método [excluir]\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()));
        }
        
        if (!model.executarDepoisExcluir()) {
            try {
                rollback();
                System.out.println("rollback exclusão");
            } catch (DatabaseException ex) {
                throw new Erro(ex.getMessage(), "Erro de exclusão");
            }
            throw new Erro("Falha ao executar método [executarDepoisExcluir] do objeto [" + model.getClass().getCanonicalName()+"], rollback efetuado!");
        }
    }
    
    
    protected void excluir(int codigo, String sql) throws Erro {
        try {
            delete(sql, codigo);
            commit();            
        } catch (DatabaseException ex) {
            throw new Erro("Falha ao executar método [excluir]\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()));
        }
    }
    
    
    
    /**
     * Retorna uma lista com todos os registros do banco, pode retornar uma lista
     * vazia também.
     * @param sql
     * @return
     * @throws infra.comunicacao.Erro
     */
    protected List getLista(String sql) throws Erro {
        List lista = new ArrayList();
        ResultSet rs = null;
        
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
        } catch (SQLException | DatabaseException ex) {
            throw new Erro("Falha ao executar método [getLista]\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()));
        }
        
        try {
            lista = banco2ListModel(negocioClass, rs, table);
        } catch (Exception ex) {
            throw new Erro("Falha ao executar método [getLista]\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()));
        }
        return lista;
    }
    
    
    protected T buscarPorCodigo(int codigo, String sql) throws Erro {
        try {
            PreparedStatement stmt;
            stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return banco2Model(negocioClass, rs, table);
        } catch (DatabaseException | SQLException | PersistenteException ex) {
            throw new Erro("Falha ao executar método [buscarPorCodigo]\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()));
        }
    }
    


    protected T buscarPorId(int id, String sql) throws Erro {
        try {
            PreparedStatement stmt;
            stmt = getConnection().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return banco2Model(negocioClass, rs, table);
        } catch (DatabaseException | SQLException | PersistenteException ex) {
            throw new Erro("Falha ao executar método [buscarPorId]\n" + ex.getMessage() + StackDebug.getLineNumber(this.getClass()));
        }
    }


    protected T recuperarUltimoRegistro() throws Exception {
        return (T) recuperarUltimoRegistro(negocioClass);
    }
    
    
    
    protected T recuperarMaiorRegistroPorAtributo(String nomeColuna) throws PersistenteException {
        String sql = ("SELECT MAX(" + nomeColuna + ") FROM " + table);
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            return banco2Model(negocioClass, rs, table);
        } catch (SQLException | IllegalArgumentException ex) {
            
            throw new PersistenteException("Erro SQL ao buscar maior registro da tabela: " + table + " \n" + sql + "\n" + StackDebug.getLineNumber(this.getClass()), "Falha");
        } catch (DatabaseException ex) {
            throw new PersistenteException(ex.getMessage(), "Falha");
        }
    }
    
    
    /**
     * Método que recebe um objeto de negócio preenchido, e retorna um Map
     * com os valores e as colunas da tabela, seguida do tipo do atributo.
     * A flag isSearch diz se o tipo de operação que vai ser realizada é de
     * pesquisa ou não.
     * 
     * Ex: [[ID, INT], 001]
     *     [[NOME, STRING], JOANA DA SILVA]
     * 
     * @param isSearch
     * @param model
     * @return 
     * @throws infra.comunicacao.PersistenteException 
     */
    protected Map<List<String>, Object> getAttributesAndValues(Negocio model, boolean isSearch) throws PersistenteException {
        Map<List<String>, Object> localMap = new LinkedHashMap<>();
        ObjectRefflection or = new ObjectRefflection();
        String column = "";
        Object value = null;
            
        for (Field field : map.fieldsNegocio()) {

            if (field != null) {
                try {
                    if (isSearch) {
                        value = or.getValueByFieldForSeach(model, field);
                    } else {
                        value = or.getValueByField(model, field);
                    }
                } catch (Exception ex) {
                    throw new PersistenteException (
                        "Erro ao recuperar valor da coluna: " + 
                        column + " da classe de negócio: " + model.getClass().getSimpleName() + 
                        " \nPesquisa: " + isSearch + "\n" + StackDebug.getLineNumber(this.getClass()), ""
                    );
                }
                if (value != null) {
                    column = map.getColumnDatabaseByFieldNegocio(field);
                    String type = or.getFieldType(field);
                    localMap.put(Arrays.asList(column, type), value);
                }
            }
        }
        return localMap;
    }
    
    
    
    /**
     * 
     * @param model
     * @param removeId
     * @return Lista de valores do objeto de negócio
     * @throws Exception 
     */
    protected List<Object> model2Banco(Negocio model, boolean removeId) throws Exception {
        List<Object> newValues;
        try {
            List<Field> fieldsNegocio = map.fieldsNegocio();
            fieldsNegocio = map.removeFieldByName(fieldsNegocio, "cadastroRapido");
            fieldsNegocio = map.removeFieldByName(fieldsNegocio, "null");
            if (removeId) {
                fieldsNegocio = map.removeFieldByName(fieldsNegocio, "id");
            }
            ObjectRefflection objRefflection = new ObjectRefflection();
            newValues = objRefflection.getValuesByFields(model, fieldsNegocio);            
            return convert(newValues);
        } catch (Exception ex) {
            throw new Exception ("Erro ao converter dados do objeto para banco: \n" + ExceptionUtils.getStackTrace(ex));
        }
    }    


    @Override
    protected T banco2Model(Class classe, ResultSet rs, String tableName) throws PersistenteException {
        T obj;
        try {
            obj = (T) getInstance(classe);
        } catch (Exception ex) {
            throw new PersistenteException(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        return (T) super.banco2Model(obj.getClass(), rs, tableName);
    }
    

    /**
     * Retorna uma lista de objetos com todos os registros do banco de dados
     * @param classe
     * @param rs
     * @param tableName
     * @return
     * @throws Exception 
     */
    protected List banco2ListModel(Class classe, ResultSet rs, String tableName) throws Exception {
        
        List l = new ArrayList<>();
        
        try {
            
            List<Field> fieldsNegocio = map.fieldsNegocio();
            while (rs.next()) {
                
                Negocio obj = (Negocio) getInstance(classe);
                if (obj != null) {
                    for (Field field : fieldsNegocio) {
                        
                        if (field != null) {
                            field.setAccessible(true);
                            Object value = convert(field, rs, map.getColumnDatabaseByFieldNegocio(field));
                            field.set(obj, value);                            
                        } 
                    }
                    l.add(obj);
                }
            }    
            rs.close();
            
        } catch (SecurityException | SQLException | IllegalAccessException ex) {
            throw new Exception ("Erro ao recuperar lista de dados do banco!\n" + ExceptionUtils.getStackTrace(ex));
        }
        return l;
    }

    protected List<String> getTableColumns() {
        return map.getColumnsDatabase();
    }

    
    
}
