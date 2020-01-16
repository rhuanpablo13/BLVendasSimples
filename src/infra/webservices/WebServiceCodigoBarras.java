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
 * Exemples :
 * 
 * http://barcodes4.me/barcode/[type]/[value].[imagetype]
 * http://www.barcodes4.me/barcode/c39/AnyValueYouWish.png
 * http://www.barcodes4.me/barcode/c128b/AnyValueYouWish.gif
 * http://www.barcodes4.me/barcode/i2of5/1234567.jpg
 * 
 * Doc: http://barcodes4.me/apidocumentation
 * @author rhuan
 */


enum TipoCodigoBarras {    
    C39("c39"), C128A("c128a"), C128B("c128b"), C128C("c128c"), I2OF5("i2of5");
    
    private String tipo;

    TipoCodigoBarras(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }    
}

public class WebServiceCodigoBarras {
    
    
    public JSONObject getWsCodigoBarras(String codigoBarras, TipoCodigoBarras tipo) throws JSONException, IOException {

        HttpURLConnection connection = null;
        JSONObject json = null;
        String retorno = "";
        try {
            URL url = new URL("http://www.barcodes4.me/barcode/" + tipo.getTipo() + "/" + codigoBarras + ".png");
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
            throw new IOException("Não foi possível acessar o endereço [" +"http://www.barcodes4.me/barcode/" + tipo.getTipo() + "/" + codigoBarras + ".png"+ "]");
        } finally {
            connection.disconnect();
        }
        return json;
    }
        
        
}
