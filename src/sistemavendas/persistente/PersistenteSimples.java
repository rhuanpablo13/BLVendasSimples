/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.abstratas.Negocio;
import infra.abstratas.NegocioSimples;
import infra.abstratas.PersistenteGeneric;
import infra.comunicacao.DatabaseException;
import infra.mapeamento.MapNegocioBancoSimples;
import infra.reflection.ObjectRefflection;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author rhuan
 * @param <T>
 */
public class PersistenteSimples <T extends NegocioSimples> extends PersistenteGeneric {

    protected MapNegocioBancoSimples map;
    protected Class<T> negocioSimplesClass;
    
    
    
    
    public PersistenteSimples(String table) {
        super(table);
        
        try {
            negocioSimplesClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.map = new MapNegocioBancoSimples(negocioSimplesClass, table);
    }
    
    
    
    
    protected void inserir(T model, String sql) throws Exception {
        try {            
            List<Object> values = model2Banco(model);
            inserir(values, sql);
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    
    protected void inserir(List<Object> values, String sql) throws DatabaseException {
        try {            
            save(sql, values);
            commit();
        } catch (DatabaseException ex) {
            try {
                rollback();
            } catch (DatabaseException ex2) {
                throw ex2;
            }
            throw ex;
        }
    }

    
    protected void alterar(T model, int id, String sql) throws Exception {
        try {            
            List<Object> values = model2Banco(model, false);
            update(sql, id, values);
            commit();
        } catch (SQLException ex) {
            rollback();            
            String msg = decodeSqlException(ex);
            if (msg == null) {
                //msg = "Erro SQL ao inserir dados em: " + table + "\n" + ExceptionUtils.getStackTrace(ex);
                msg = ex.getMessage();
            }
            throw new Exception(msg);
        }
    }
    
    
    protected void atualizarTabelaRelacionamento(String sql, int id, Object... parametros) throws DatabaseException {
        atualizarTabelaRelacionamento(sql, id, Arrays.asList(parametros));        
    }
    
    
    protected void atualizarTabelaRelacionamento(String sql, int id, List<Object> parametros) throws DatabaseException {
        update(sql, id, parametros);
    }
    
    
    protected void atualizarTabelaRelacionamento(String sql) throws DatabaseException {
        update(sql);
    }
    
    
    protected void inserirTabelaRelacionamento(String sql, Object... parametros) throws DatabaseException {
        inserirTabelaRelacionamento(sql, Arrays.asList(parametros));        
    }

    
    protected void inserirTabelaRelacionamento(String sql, List<Object> parametros) throws DatabaseException {
        save(sql, parametros);
    }
    
    
    
    /**
     * 
     * @param model
     * @return Lista de valores do objeto de negócio
     * @throws Exception 
     */
    protected List<Object> model2Banco(T model) throws Exception {
        return model2Banco(model, false);
    }

    
    protected List<Object> model2Banco(T model, boolean removerId) throws Exception {
        
        List<Field> fieldsNegocio = map.fieldsNegocio();
        fieldsNegocio = map.removeFieldByName(fieldsNegocio, "cadastroRapido");
        fieldsNegocio = map.removeFieldByName(fieldsNegocio, "null");
        if (removerId) {
            fieldsNegocio = map.removeFieldByName(fieldsNegocio, "id");
        }            
        return model2Banco(model, fieldsNegocio);
    }
    
    
    protected List<Object> model2Banco(T model, List<Field> fieldsNegocio) throws Exception {
        List<Object> newValues;
        try {
            ObjectRefflection objRefflection = new ObjectRefflection();
            newValues = objRefflection.getValuesByFields(model, fieldsNegocio);
            return convert(newValues);
        } catch (Exception ex) {
            throw new Exception ("Erro ao converter dados do objeto para banco: \n" + ExceptionUtils.getStackTrace(ex));
        }
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
