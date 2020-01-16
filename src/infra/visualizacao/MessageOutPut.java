/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import infra.comunicacao.Erro;
import infra.comunicacao.Message;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author rhuan
 */
public class MessageOutPut {
    
    
    private boolean status;
    
    public void show(Message message) {
        message.show();
        this.status = message.isSucess();
        message.printStackTrace();
    }
    
    public void show(Exception message) {        
        if (message instanceof NullPointerException) {
            new Erro(ExceptionUtils.getStackTrace(message), "").show();
            return;
        }
        
        Message m = new Message();
        try {
            m = (Message) message;
            m.show();
        } catch (NullPointerException e) {
            String em = message.getMessage();
            if (em != null) {
                m = new Message(em, "Mensagem");
                m.show();
            } else {
                m = new Message(ExceptionUtils.getStackTrace(message), "Mensagem");
                m.show();
            }
        }
        this.status = m.isSucess();
        //message.printStackTrace();
    }
    
    
    public boolean sucess() {
        return status;
    }
    
    
//    public void show(NullPointerException ex) {
//        Message m = new Message(ExceptionUtils.getStackTrace(ex), "Erro");
//        m.show();
//    }
    
}
