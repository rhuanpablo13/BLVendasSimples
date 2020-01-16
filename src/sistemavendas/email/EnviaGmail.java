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
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author RHUAN
 */
public class EnviaGmail {
    
    private final String emailRemetente;
    private final String senha;

    public EnviaGmail(String emailRemetente, String senha) {
        this.emailRemetente = emailRemetente;
        this.senha = senha;
    }


    public boolean enviarEmail(String destinatario, String subtitulo, String mensagem) {
        Properties props = new Properties();
        //Parâmetros de conexão com servidor gmail          
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", "587");
        
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", 
        "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailRemetente, senha);
            }
        });   
        session.setDebug (true);
        
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailRemetente)); //Remetente

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); //Destinatário(s)
            message.setSubject(subtitulo);//Assunto
            message.setText(mensagem);
            //Método para enviar a mensagem criada
            Transport.send(message);
            System.out.println("Enviou mensagem Gmail");

        }catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    
    
}
