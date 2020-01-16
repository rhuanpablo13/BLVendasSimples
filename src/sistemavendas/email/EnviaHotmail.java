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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 *
 * @author RHUAN
 */
public class EnviaHotmail {

    private final String emailRemetente;
    private final String senha;

    public EnviaHotmail(String emailRemetente, String senha) {
        this.emailRemetente = emailRemetente;
        this.senha = senha;
    }

    
    
    
    
    public boolean enviarEmail(String destinatario, String subtitulo, String mensagem) {
        Properties props = new Properties();

        //Parâmetros de conexão com servidor Hotmail         
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailRemetente, senha);
            }
            
        });

        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailRemetente)); //Remetente

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); //Destinatário(s)
            message.setSubject(subtitulo);//Assunto
            message.setText(mensagem);
            //Método para enviar a mensagem criada
            Transport.send(message);
            System.out.println("Enviou mensagem Hotmail");

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Acessa o e-mail passado por parametro e lista todos os emails. Mostrando
     * somente as informaçõe de remetente e assunto
     *
     * @param host
     * @param storeType
     * @param user
     * @param password
     */
    public static void check(String host, String storeType, String user, String password) {

        try {

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void javaMailSearchInbox(String host, String user, String password) {

        try {
            // mail server info
            //        String host = "Outlook.office365.com";
            //        String user = "rhuanpablo13@hotmail.com";
            //        String password = "Princes@123";

            // connect to my pop3 inbox in read-only mode
            Properties properties = System.getProperties();
            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("pop3");
            store.connect(host, user, password);
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);

            // search for all "unseen" messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message messages[] = inbox.search(unseenFlagTerm);

            if (messages.length == 0) {
                System.out.println("No messages found.");
            }

            for (int i = 0; i < messages.length; i++) {
                // stop after listing ten messages
                if (i > 10) {
                    System.exit(0);
                    inbox.close(true);
                    store.close();
                }

                System.out.println("Message " + (i + 1));
                System.out.println("From : " + messages[i].getFrom()[0]);
                System.out.println("Subject : " + messages[i].getSubject());
                System.out.println("Sent Date : " + messages[i].getSentDate());
                System.out.println();
            }

            inbox.close(true);
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EnviaHotmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EnviaHotmail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void testeEmailOpened(String host, String user, String password) {
        
        Properties properties = System.getProperties();
        properties.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(properties, null);
            //create session instance
            Store store = session.getStore("imaps");//create store instance  
            store.connect(host, user, password);
            //set your user_name and password 
            System.out.println(store);
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            // search for all "unseen" messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message messages[] = inbox.search(unseenFlagTerm);


            if (messages.length == 0) System.out.println("No messages found.");

            for (Message message : messages) {
                System.out.println(message.getContent().toString());
            }


            store.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
