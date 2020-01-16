/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.comunicacao;

import java.sql.SQLException;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author rhuan
 */
public class PersistenteException extends Message {
    
    protected SQLException sqlException;
    protected DatabaseException databaseException;
    
    
    public PersistenteException(String msg, String title) {
        super(msg, title);
    }

    public PersistenteException(String msg, SQLException sqlException) {
        this.sqlException = sqlException;
        msg += getMsg(sqlException);
        super.setMsg(msg);
        super.setTitle("Falha");
    }
    
    public PersistenteException(String msg, DatabaseException databaseException) {
        this.databaseException = databaseException;
        msg += getMsg(databaseException);
        super.setMsg(msg);
        super.setTitle("Falha");
    }
    
    private String getMsg(Exception ex) {
        String msgTemp = "\nMessage:" + ExceptionUtils.getMessage(ex) + "\nCause:" + ExceptionUtils.getRootCauseMessage(ex);
        return msgTemp;
    }
    
    
}
