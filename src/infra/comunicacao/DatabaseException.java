/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.comunicacao;

import java.sql.SQLException;
import javax.swing.ImageIcon;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author rhuan
 */
public class DatabaseException extends Message {
    
    protected SQLException sqlException;
    protected int errorCode = -1;
    
    public DatabaseException(String msg) {
        super(msg, "Falha de Banco de Dados");
        icon = new ImageIcon("src/imagens/35x35/warning.png");
    }
    
    
    public DatabaseException(String msg, String title) {
        super(msg, title);
        icon = new ImageIcon("src/imagens/35x35/warning.png");
    }
    
    
    public DatabaseException(String msg, SQLException sqlException) {
        //super(msg, "Falha de Banco de Dados");
        msg += "\nMessage:" + ExceptionUtils.getMessage(sqlException) + "Cause:" + ExceptionUtils.getRootCauseMessage(sqlException);
        super.msg = this.msg;
        super.title = "Falha de Banco de Dados";
        icon = new ImageIcon("src/imagens/35x35/warning.png");
        this.errorCode = sqlException.getErrorCode();
    }

    public int getErrorCode() {
        return errorCode;
    }
    
    
}
