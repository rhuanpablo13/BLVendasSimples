/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author rhuan
 */
public class WebServiceCep {
 
    
    
    public JSONObject getWsCep(String cep, String tipo) throws JSONException, IOException {

        HttpURLConnection connection = null;
        JSONObject json = null;
        String retorno = "";
        try {            
            URL url = new URL("http://viacep.com.br/ws/" + cep + "/" + tipo);
            connection = (HttpURLConnection)url.openConnection();
            
            if (connection.getResponseCode() != 200) {
                throw new JSONException("Código: " + connection.getResponseCode() + "\nMensagem:" + connection.getResponseMessage());
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            while (br.ready()) {
                retorno += (br.readLine());
            }
            json = new JSONObject(retorno);
                  
        } catch (IOException ex) {
            throw new IOException("Não foi possível conectar em: " + "http://viacep.com.br/ws/" + cep + "/" + tipo);
        } finally {
            connection.disconnect();
        }        
        return json;
    }
    
}
