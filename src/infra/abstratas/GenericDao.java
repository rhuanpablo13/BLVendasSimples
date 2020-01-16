/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.abstratas;

/**
 *
 * @author rhuan
 */
import infra.comunicacao.DatabaseException;
import infra.comunicacao.StackDebug;
import infra.conexao.ConnectionFactory;
import infra.conexao.Schema;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao {
    
    protected Connection connection = null;
    
    
    /**
     * Executa a inserção no banco de dados.
     * @param insertSql
     * @param parametros      
     * @throws infra.comunicacao.DatabaseException 
     */
    protected void save(String insertSql, List<Object> parametros) throws DatabaseException {
        System.out.println(parametros);
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertSql)) {
            for (int i = 0; i < parametros.size(); i++) {
                pstmt.setObject((i+1), parametros.get(i));
            }
            pstmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException("Falha ao executar método QUERY [" + insertSql + "] no método [save]" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }

    
    /**
     * Executa a inserção no banco de dados.
     * @param insertSql     
     * @throws infra.comunicacao.DatabaseException     
     */
    protected void save(String insertSql) throws DatabaseException {
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertSql)) {
            pstmt.execute();
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao executar método QUERY [" + insertSql + "] no método [save]" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }    
    
    
    /**
     * Executa o update no banco de dados.
     * @param updateSql
     * @param id
     * @param parametros
     * @throws infra.comunicacao.DatabaseException
     */
    protected void update(String updateSql, Object id, List<Object> parametros) throws DatabaseException {
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateSql)) {
            for (int i = 1; i < parametros.size(); i++) {
                pstmt.setObject(i, parametros.get(i));
            }
            pstmt.setObject(parametros.size(), id);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao executar método QUERY [" + updateSql + "] no método [update]" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }


    /**
     * Executa o update no banco de dados.
     * @param updateSql
     * @throws infra.comunicacao.DatabaseException     
     */
    protected void update(String updateSql) throws DatabaseException {
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateSql)) {
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException();
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao executar método QUERY [" + updateSql + "] no método [update]" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }

    

    
    /**
     * Executa o delete no banco de dados
     * @param deleteSql
     * @param parametros     
     * @throws infra.comunicacao.DatabaseException     
     */
    protected void delete(String deleteSql, Object... parametros) throws DatabaseException {
        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteSql)) {            
            for (int i = 0; i < parametros.length; i++) {
                pstmt.setObject(i+1, parametros[i]);
            }            
            pstmt.execute();
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao executar método QUERY [" + deleteSql + "] no método [delete]" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }


    
/**
     * Executa o delete no banco de dados
     * @param deleteSql       
     * @throws infra.comunicacao.DatabaseException       
     */
    protected void delete(String deleteSql) throws DatabaseException {
        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteSql)) {
            pstmt.execute();
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao executar método delete QUERY [" + deleteSql + "] no método [delete]" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }
    
    
    
    
    /**
     * Recupera uma conexão com o banco de dados, caso já exista, retorna uma nova.
     * Se não, retorna a mesma
     * @return Connection
     */
    protected Connection getConnection() throws DatabaseException {
        try {

            if (this.connection == null || this.connection.isClosed()) {
                this.connection = ConnectionFactory.getConnection(Schema.MYSQL_SISVENDASSIMPLES);
            }
            
        } catch (SQLException | NullPointerException ex) {            
            throw new DatabaseException("Falha ao recuperar nova conexão com o banco de dados!" + StackDebug.getLineNumber(this.getClass()) );
        }
        return this.connection;
    }
    
    
    /**
     * Faz o rollback da operação no banco. 
     * @throws infra.comunicacao.DatabaseException
     */
    protected void rollback() throws DatabaseException {
        if (this.connection == null) {
            throw new DatabaseException("Conexão fechada!" + StackDebug.getLineNumber(this.getClass()));
        }
        try {
            this.connection.rollback();
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao executar rollback" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }
    
    
    /**
     * Fecha a conexão com o banco.
     * @throws infra.comunicacao.DatabaseException
     */
    protected void closeConnection() throws DatabaseException {
        try {
            if (this.connection != null || !this.connection.isClosed()) {
                this.connection.close();            
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Falha ao fechar conexão" + StackDebug.getLineNumber(this.getClass()), ex);
        }
    }
    
    
    /**
     * Commita as operações no banco de dados.
     * @throws infra.comunicacao.DatabaseException
     */
    protected void commit() throws DatabaseException {
        try {
            this.connection.commit();
        } catch (SQLException ex) {            
            throw new DatabaseException("Falha ao commitar conexão" + StackDebug.getLineNumber(this.getClass()), ex);
        }        
    }    
    
    
    protected String decodeSqlException (DatabaseException ex) {
        int code = ex.getErrorCode();
        switch (code) {
            case 1062:
                return "Código já cadastrado";
        }
        return null;
    }
     
    protected String decodeSqlException (SQLException ex) {
        int code = ex.getErrorCode();
        switch (code) {
            case 1062:
                return "Código já cadastrado";
        }
        return null;
    }
    
}
