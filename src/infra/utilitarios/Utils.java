/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package infra.utilitarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;


public class Utils {





    public static String addBarras(String pString){
        String dataRetorno = new String();
        //substitui o '-' por '\'
        if(pString != null){
            dataRetorno += pString.charAt(8);
            dataRetorno += pString.charAt(9);
            dataRetorno += '/';
            dataRetorno += pString.charAt(5);
            dataRetorno += pString.charAt(6);
            dataRetorno += '/';
            dataRetorno += pString.charAt(0);
            dataRetorno += pString.charAt(1);
            dataRetorno += pString.charAt(2);
            dataRetorno += pString.charAt(3);
        }
        return dataRetorno;
    }

    public static String trocarTracos(String pString){
        String retorno = new String();
        if(pString != null){
            for(int i = 0; i < pString.length(); i++){
                if(pString.charAt(i) == '-'){
                    retorno += '/';
                } else {
                    retorno += pString.charAt(i);
                }
            }
        }
        return retorno;
    }
    

    public static Date addDias(int pQteDias, Date pDate){
        Calendar c = Calendar.getInstance();

        c.setTime(pDate);
        c.add(Calendar.DATE, pQteDias);

        return c.getTime();
    }
    

    public static Date addMes(Date dataAtual, int quantidadeMes){
        Calendar c = Calendar.getInstance();
        c.setTime(dataAtual);
        c.add(Calendar.MONTH, quantidadeMes);
        return c.getTime();
    }
    
    public static int diasEntreDatas(Date pDataInicio, Date pDataFim){
        GregorianCalendar ini = new GregorianCalendar();  
        GregorianCalendar fim = new GregorianCalendar();  
        ini.setTime(pDataInicio);  
        fim.setTime(pDataFim);  
        long dt1 = ini.getTimeInMillis();  
        long dt2 = fim.getTimeInMillis();  
        return (int) (((dt2 - dt1) / 86400000)+1); 
    }


    public static String getDataHora(){
        Date date = new Date(); 
        SimpleDateFormat teste = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return teste.format(date);
    }	


    
    
    
    /**
     * Retorna um objeto utils.Date usando uma mascara, a partir 
     * de uma data em formato string;
     * String > Date
     * 
     * @param data
     * @param pattern
     * @return Date, null
     */
    public static Date dataString2Date(String data, String pattern) {   
        if (data == null || data.equals(""))
            return null;
        
        data = data.replace(" ", "");
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        
        try {
            return formatter.parse(data);
        } catch (ParseException e) { }
        return null;
    }  
  
   
    /**
     * Retorna um objeto utils.Date em formato string (dd/MM/yyyy)
     * Date > Tela (String)
     * 
     * @param pData
     * @return String, null
     */
    public static String date2View(Date pData) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(pData);
    }
    

    /**
     * Converte uma data de formato string(dd/MM/yyyy) para utils.Date
     * Tela (String) > Date
     * @param sData
     * @return Date, null
     */
    public static Date view2Date(String sData) {        
        return dataString2Date(sData, "dd/MM/yyyy");
    }    

    
    /**
     * Converte um utils.Date para uma mascara passada como parametro,
     * e retorna uma data em formato string;
     * Date > Pattern (String)
     * 
     * @param pData
     * @param pattern
     * @return String, null
     */
    public static String formatDate(Date pData, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(pData);
    }    
    
    
    /**
     * Se bo for verdadeiro, retorna "1", senão "0".
     * @param bo
     * @return String
     */
    public static String boolean2Int(boolean bo) {
        if (bo) return "1";
        return "0";
    }
    
    
    /**
     * Se t for igual a 1, retorna true, senão false
     * @param t
     * @return 
     */
    public static boolean int2Boolean(int t) {
        return t == 1;
    }
    
    

    /**
     * Aplica uma máscara de cpf em uma string
     * @param value
     * @return String, null
     */
    private static String maskCpf(String value) {
        if (value == null || value.isEmpty()) return null;
        
        // removendo espaços em branco
        value = value.replace(" ", "");
        
        String cpfWithoutMask = "###########";
        String cpfWithMask = "###.###.###-##";
        
        // Verificando se a string já está no formato de cpf
        if (value.length() == cpfWithMask.length()) {
            String[] arrayCpf = value.split("\\.");            
            if (arrayCpf.length == 3 && arrayCpf[2].contains("-")) {
                return value;
            }
            return null;
        }
        
        if (value.length() == cpfWithoutMask.length()) {
            return(value.substring(0, 3) + "." +
                   value.substring(3, 6) + "." +
                   value.substring(6, 9) + "-" +
                   value.substring(9, 11));
        }
        return null;
    }
   
    
    /**
     * Aplica uma máscara de cnpj em uma string
     * @param value
     * @return 
     */
    private static String maskCnpj(String value) {
        if (value == null || value.isEmpty()) return null;
        
        // removendo espaços em branco
        value = value.replace(" ", "");
        
        String cnpjWithoutMask = "##############";
        String cnpjWithMask = "##.###.###/####-##";
        
        // Verificando se a string já está no formato de cnpj
        if (value.length() == cnpjWithMask.length()) {
            String[] arrayA = value.split("\\.");
            String[] arrayB = arrayA[2].split("/");
            
            if (arrayA.length == 3 && arrayB.length == 2 &&  arrayB[2].contains("-")) {
                return value;
            }
            return null;
        }
        
        if (value.length() == cnpjWithoutMask.length()) {
            return(value.substring(0, 2) + "." +
                   value.substring(2, 5) + "." +
                   value.substring(5, 8) + "/" +
                   value.substring(8, 12) + "-" +
                   value.substring(12, 14));
        }
        return null;
    }

    
    
    /**
     * Aplica uma máscara de cep em uma string
     * @param value
     * @return String, null
     */
    private static String maskCep(String value) {
        if (value == null || value.isEmpty()) return null;
        
        // removendo espaços em branco
        value = value.replace(" ", "");
        
        String cepWithoutMask = "########";
        String cepWithMask = "#####-###";
        
        // Verificando se a string já está no formato de cnpj
        if (value.length() == cepWithMask.length()) {
            String[] arrayA = value.split("-");
            if (arrayA.length == 2) {
                return value;
            }
            return null;
        }
        
        if (value.length() == cepWithoutMask.length()) {
            return(value.substring(0, 5) + "-" +
                   value.substring(5, 8));
        }
        return null;
    }
    
    
    
    /**
     * Aplica uma máscara de data em uma string
     * @param value
     * @param full
     * @return 
     */
    private static String maskData(String value, boolean full) {
        if (value == null || value.isEmpty()) return null;
        
        // removendo espaços em branco
        value = value.replace(" ", "");
        
        String dateWithoutMask = "########";
        String dateWithMask = "##/##/####";
        
        if (full) {
            // Verificando se a string já está no formato de data
            if (value.length() == dateWithMask.length()) {
                String[] arrayA = value.split("/");
                if (arrayA.length == 3) {
                    return value;
                }
                return null;
            }

            if (value.length() == dateWithoutMask.length()) {
                return(value.substring(0, 2) + "/" +
                       value.substring(2, 4) + "/" +
                       value.substring(4, 8));
            }            
        } else {
            
            // Verificando se a string já está no formato de data
            if (value.length() == dateWithMask.length() -2) {
                String[] arrayA = value.split("/");
                if (arrayA.length == 3) {
                    return value;
                }
                return null;
            }

            if (value.length() == dateWithoutMask.length()) {
                return(value.substring(0, 2) + "/" +
                       value.substring(2, 4) + "/" +
                       value.substring(4, 6));
            }
        }
        return null;
    }
    
    
    
    /**
     * Aplica uma máscara de telefone em uma string
     * @param value
     * @return 
     */
    private static String maskTelefone(String value) {
        if (value == null || value.isEmpty()) return null;
        
        // removendo espaços em branco
        value = value.replace(" ", "");
        
        String telWithoutMask = "##########";
        String telWithMask = "(##)####-####";
        
        // Verificando se a string já está no formato de cnpj
        if (value.length() == telWithMask.length()) {
            String[] arrayA = value.split("-");
            if (arrayA.length == 2 && arrayA[0].contains("(") && arrayA[0].contains(")")) {
                return value;
            }
            return null;
        }
        
        if (value.length() == telWithoutMask.length()) {
            return("(" + value.substring(0, 2) + ") " +
                         value.substring(2, 6) + "-" +
                         value.substring(6, 10));
        }
        return null;
    }
    
    
    /**
     * Aplica uma máscara de telefone em uma string
     * @param value
     * @return 
     */
    private static String maskCelular(String value) {
        if (value == null || value.isEmpty()) return null;
        
        // removendo espaços em branco
        value = value.replace(" ", "");
        
        String telWithoutMask = "###########";
        String telWithMask = "(##)#####-####";
        
        // Verificando se a string já está no formato de cnpj
        if (value.length() == telWithMask.length()) {
            String[] arrayA = value.split("-");
            if (arrayA.length == 2 && arrayA[0].contains("(") && arrayA[0].contains(")")) {
                return value;
            }
            return null;
        }
        
        if (value.length() == telWithoutMask.length()) {
            return("(" +  value.substring(0, 2) + ") " +
                          value.substring(2, 7) + "-" +
                          value.substring(7, 11));
        }
        return null;
    }   
    
    /**
     * Aplica uma máscara em uma string de acordo com o tipo passado
     * como parametro.
     * 
     * @param value
     * @param type
     * @return String, null
     */
    public static String setMask(String value, Mask type) {
    
        switch (type) {
            case CPF:
                return maskCpf(value);
            case CNPJ:
                return maskCnpj(value);
            case CEP:
                return maskCep(value);
            case DATA_FULL:
                return maskData(value, true);
            case DATA_MIN:
                return maskData(value, false);
            case TELEFONE:
                return maskTelefone(value);
            case CELULAR:
                return maskCelular(value);
        }
        return null;
    }


    
    
    
    public static void setSizeStage(int w, int h, JInternalFrame internalFrame) {
        if (w > 1080) {
            internalFrame.setSize(1080, h);
        } else {
            internalFrame.setSize(w, h);
        }
    }
    
    public static void removeBarTopWindow(JInternalFrame internalFrame) {
        BasicInternalFrameUI bi = (BasicInternalFrameUI)internalFrame.getUI();
        bi.setNorthPane(null);
    }

    
    public static String removeCaracteresEspeciais(String text) {
        if (text.isEmpty()) return text;
        return text.replace(".", "").replace("-", "").replace("/", "").replace(" ", "")
                .replace("*", "").replace("*", "").replace(";", "").replace("?", "")
                .replace("#", "").replace("=", "").replace(">", "").replace("<", "")
                .replace("&", "").replace("!", "").replace("%", "").replace("@", "")
                .replace("^", "").replace("~", "").replace("(", "").replace(")", "")
                .replace("_", "");
    }
    
   
    public static void removePlaceHolder(JTextField text, String MSG_VAZIO) {
        if(text.getText().trim().equals(MSG_VAZIO)){
            text.setText("");
        }
    }
    
    public static void removePlaceHolder(JFormattedTextField text, String MSG_VAZIO) {
        if(text.getText().trim().equals(MSG_VAZIO)){
            text.setText("");
        }
    }
    
    public static int pedeConfirmacao(String msg, JInternalFrame frame) {
        return JOptionPane.showConfirmDialog(frame, msg, "Atenção", JOptionPane.YES_NO_OPTION);
    }    
}
