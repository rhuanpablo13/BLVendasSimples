/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.validadores;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;



/**
 *
 * @author RHUAN
 */
public class Validador {
   
    
    
    
    public static boolean isMoneyValid(JFormattedTextField money, Double min, Double max) {
        String value = money.getText();
        return isMoneyValid(value, min, max);
    }
    
    public static boolean isMoneyValid(JTextField money, Double min, Double max) {
        String value = money.getText();
        return isMoneyValid(value, min, max);
    }
    
    public static boolean isMoneyValid(Double money, Double min, Double max) {
        return (money > min && money < max);
    }
    
    public static boolean isMoneyValid(String money, Double min, Double max) {
        if (money.contains(".") || money.contains(",")) {
            money = money.replace(".", "");
            money = money.replace(",", ".");
        }
        Double value = Double.parseDouble(money);
        return isMoneyValid(value, min, max);
    }
    
    public static boolean isEmailValid(JFormattedTextField email) {
        email.setFormatterFactory(null);
        return emailPattern(email.getText());
    }
    
    public static boolean isEmailValid(JTextField email) {        
        return emailPattern(email.getText());
    }    

    public static boolean isEmailValid(String email) {        
        return emailPattern(email);
    }
    
    private static boolean emailPattern(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    
    
    public static boolean isCpfValid(JFormattedTextField cpf) {
        cpf.setFormatterFactory(null);
        return cpfPattern(cpf.getText());
    }
    
    public static boolean isCpfValid(JTextField cpf) {
        return cpfPattern(cpf.getText());
    }
    
    public static boolean isCpfValid(String cpf) {
        return cpfPattern(cpf);
    }
    
    
    private static boolean cpfPattern(String CPF) {
        
        CPF = removeCaracterEspecialCpf(CPF);
        
        if (CPF.isEmpty()) return false;
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            CPF.length() != 11)   return(false);        
            
                         
        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {              
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0         
                // (48 eh a posicao de '0' na tabela ASCII)         
                num = (int)(CPF.charAt(i) - 48); 
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
                } catch (InputMismatchException erro) {
                return(false);
            }
    }    
    
    
    
    
    public static boolean isCnpjValid(JFormattedTextField cnpj) {
        cnpj.setFormatterFactory(null);
        return cnpjPattern(cnpj.getText());
    }
    
    public static boolean isCnpjValid(JTextField cnpj) {
        return cnpjPattern(cnpj.getText());
    }
    
    public static boolean isCnpjValid(String cnpj) {
        return cnpjPattern(cnpj);
    }
    
    private static boolean cnpjPattern(String CNPJ) {
        
        CNPJ = removeCaracterEspecialCpf(CNPJ);
        
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
            CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
            CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
            CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
            CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
            (CNPJ.length() != 14)) {
            return(false);
        }

        char dig13, dig14;
        int sm, i, r, num, peso;

        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=11; i>=0; i--) {
                num = (int)(CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
               dig13 = '0';
            else dig13 = (char)((11-r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i=12; i>=0; i--) {
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                   peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
               dig14 = '0';
            else dig14 = (char)((11-r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
               return(true);
            else return(false);
            
        } catch (InputMismatchException erro) {
            return(false);
        }
    }
 
    
    private static String removeCaracterEspecialCpf(String cpf) {
        return cpf.replace(".", "").replace("-", "").replace(" ", "");        
    }
        
    
}
