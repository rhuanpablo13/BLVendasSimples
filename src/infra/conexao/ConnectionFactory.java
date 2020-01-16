package infra.conexao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import infra.comunicacao.Erro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Br
 */
public class ConnectionFactory {
    
    
    public static java.sql.Connection getConnection(Schema schema) {  

        Connection connection = null;  
    	
    	try {  
            /* Obtém o driver de conexão com o banco de dados */  
            Class.forName("com.mysql.jdbc.Driver");   

            /* Configura os parâmetros da conexão */  
            String url = schema.getUrl();  
            String username = schema.getUsuario();   
            String password = schema.getSenha();  

            /* Tenta se conectar */  
            connection = DriverManager.getConnection(url, username, password);  

            /* Desabilita o auto commit */
            connection.setAutoCommit(false);
            
            /* Caso a conexão ocorra com sucesso, a conexão é retornada */  
            return connection;  
    		
    	} catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Erro("O driver do banco de dados não foi encontrado.", "Erro").show();
    		
    	} catch (SQLException e) { 
            e.printStackTrace();
            new Erro("Nao foi possivel conectar ao banco de dados.", "Erro").show();
    	}
        return null;        
    }   



}
