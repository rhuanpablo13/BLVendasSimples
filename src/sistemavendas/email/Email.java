/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.email;

/**
 *
 * @author rhuan
 */
public class Email {

    
    public boolean enviarEmail(String remetente, String senha, String destinatario, String subtitulo, String mensagem) {
        if (remetente.toLowerCase().contains("@hotmail")) {
            EnviaHotmail hotmail = new EnviaHotmail(remetente, senha);
            return hotmail.enviarEmail(destinatario, subtitulo, mensagem);
            
        }
        
        if (remetente.toLowerCase().contains("@gmail")) {
            EnviaGmail gmail = new EnviaGmail(remetente, senha);
            return gmail.enviarEmail(destinatario, subtitulo, mensagem);
        }
        
        return false;
    }

}
