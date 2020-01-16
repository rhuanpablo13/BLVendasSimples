/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.validadores;

import infra.operacoes.Operacao;
import infra.webservices.WebServiceCep;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Rhuan
 */
public class ViewValidator {

    static final String MSG_OBRIGATORIO = "Campo obrigatório";
    static final Color ERRO_TXT = new Color(231, 76, 60); // vermelho
    static final Color COR_PADRAO_TXT = new Color(0,0,0);    
    private final String obrigatorio = "obrigatorio";
    
    private Operacao operacao;

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }
    
    
    public void removeValidation(final JTextField field) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            
            String val = !field.getText().isEmpty() && !field.getText().equalsIgnoreCase(MSG_OBRIGATORIO) ? field.getText() : "";
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {                
                field.setText(val);
                field.setForeground(COR_PADRAO_TXT);
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setText("");
                if (val.equalsIgnoreCase(MSG_OBRIGATORIO)) {
                    field.setText(val);
                }                
                field.setForeground(COR_PADRAO_TXT);
            }
        });
    }
    
    public void requiredSenha(final JPasswordField field) {
        required(field, MSG_OBRIGATORIO);
    }

    
    public void required(final JTextField field) {
        required(field, MSG_OBRIGATORIO);
    }

    public void required(final JPasswordField field, final String message) {
            
        field.addFocusListener(new java.awt.event.FocusAdapter() {

            Character echo = field.getEchoChar();
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {

                char[] pass = field.getPassword();
                String pass2String = String.copyValueOf(pass);
                
                if (operacao != Operacao.CADASTRO) {
                    if (! removeCaracteresEspeciais(field).isEmpty()) {
                        if (pass2String.equalsIgnoreCase(message)) {
                            field.setEchoChar(echo);
                            field.setForeground(COR_PADRAO_TXT);
                            field.setText("");
                        }
                    }
                } else {
                    field.setForeground(COR_PADRAO_TXT);
                    if (pass2String.equalsIgnoreCase(message)) {
                        field.setEchoChar(echo);
                        field.setText("");
                    }
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                char[] pass = field.getPassword();
                String pass2String = String.copyValueOf(pass);
                if (removeCaracteresEspeciais(pass2String).isEmpty()) {
                    field.setEchoChar((char)0);
                    field.setForeground(ERRO_TXT);
                    field.setText(message);
                }
            }
        });

    }

    
    public void required(final JTextField field, final String message) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setToolTipText("");
                if (operacao  != Operacao.CADASTRO) {
                    if (! removeCaracteresEspeciais(field).isEmpty()) {
                       if (field.getText().equalsIgnoreCase(message)) {
                            field.setForeground(COR_PADRAO_TXT);
                            field.setText("");
                       }
                    }
                } else {
                    field.setForeground(COR_PADRAO_TXT);
                    if (field.getText().equalsIgnoreCase(message)) {                        
                        field.setText("");
                    } else {
                        field.setText(field.getText());
                    }
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (removeCaracteresEspeciais(field).isEmpty()) {
                   field.setToolTipText(obrigatorio);
                   field.setForeground(ERRO_TXT);
                   field.setText(message);
                }                
            }
        });
    }


    public void requiredValue(final JFormattedTextField field, final boolean required, final int min, final int max) {
        requiredValue(field, "*********", required, MSG_OBRIGATORIO, min, max);
    }

    
    public void requiredStringLength(final JFormattedTextField field, final String mask, final int min, final int max) {
        requiredStringLength(field, mask, false, MSG_OBRIGATORIO, min, max);
    }
    
    
    public void requiredStringLength(final JFormattedTextField field, final String mask, final boolean required, final String invalidMessage, final int min, final int max) {

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String newField = removeCaracteresEspeciais(field);
                field.setToolTipText("");
                
                if (newField.equalsIgnoreCase(removeCaracteresEspeciais(invalidMessage)) || newField.isEmpty()) {
                    setMask(field, mask);
                    field.setForeground(COR_PADRAO_TXT);
                    field.setText("");
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String newField = removeCaracteresEspeciais(field);
//                if (newField.isEmpty()) {
//                    return;
//                }
                
                if (required) {
                    if (newField.length() < min || newField.length() > max) {
                       removeMask(field);
                       field.setForeground(ERRO_TXT);
                       field.setText(invalidMessage);
                    }
                } else if (!required && !newField.isEmpty()) {
                    if (newField.length() < min || newField.length() > max) {
                       removeMask(field);
                       field.setForeground(ERRO_TXT);
                       field.setText(invalidMessage);
                    }
                }
            }
        });
    }
    
    
    public void requiredValue(final JFormattedTextField field, final String mask, final boolean required, final String invalidMessage, final int min, final int max) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            
            String parseNumberError = "Número inválido";
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                String newField = removeCaracteresEspeciais(field);
                field.setToolTipText("");
                
                if (newField.equalsIgnoreCase(removeCaracteresEspeciais(invalidMessage)) || newField.isEmpty() || newField.equalsIgnoreCase(removeCaracteresEspeciais(parseNumberError))) {
                    setMask(field, mask);
                    field.setForeground(COR_PADRAO_TXT);
                    field.setText("");
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String newField = removeCaracteresEspeciais(field);
                if (newField.isEmpty()) {
                    return;
                }
                
                try {
                    Integer value = Integer.parseInt(newField);
                    if (required) {
                        if (value < min || value > max) {
                           field.setToolTipText(obrigatorio);
                           removeMask(field);
                           field.setForeground(ERRO_TXT);
                           field.setText(invalidMessage);
                        }
                    }
                } catch (NumberFormatException e) {
                    field.setToolTipText(obrigatorio);
                    removeMask(field);
                    field.setForeground(ERRO_TXT);
                    field.setText(parseNumberError);
                }
            }
        });
    }


    public void fieldPhone(final JFormattedTextField field, final boolean required) {
        requiredStringLength(field, "(##) ####-####", required, "Telefone inválido", 10, 10);
    }

    
    public void fieldCellPhone(final JFormattedTextField field, final boolean required) {
        requiredStringLength(field, "(##) #####-####", required, "Número inválido", 10, 11);
    }
    
    
    public void fieldCpf(final JFormattedTextField formatField, final String oldValue, final boolean removeEspecialCaracters, final String method, final Object validator, final Class... params) {
        fieldWithMask(formatField, "###.###.###-##", oldValue, removeEspecialCaracters, true, "CPF Inválido", method, validator, params);
    }
    
    //13.938.929/0001-08
    public void fieldCnpj(JFormattedTextField formatField, String oldValue, boolean removeEspecialCaracters, String method, Validador validator, Class<String> params) {
        fieldWithMask(formatField, "##.###.###/####-##", oldValue, removeEspecialCaracters, true, "CNPJ Inválido", method, validator, params);
    }
    
    
    public void fieldDate(final JFormattedTextField formatField, final String oldValue, final boolean removeEspecialCaracters, final boolean required, final String invalidMessage) {
        fieldWithMask(formatField, "##/##/####", oldValue, removeEspecialCaracters, required, invalidMessage);
    }
    
    
    public void fieldMonetaryValue(final JFormattedTextField formatField, final Double oldValue, final boolean removeEspecialCaracters, final boolean required, final String method, final Object validator, final boolean allArguments, Object... arguments) {
        formatField.setFocusLostBehavior(JFormattedTextField.COMMIT);
        DecimalFormat decimal = new DecimalFormat("#,###,###.00");
        //NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String oldVal = oldValue == null ? "" : Double.toString(oldValue);
        fieldWithMask(formatField, decimal, oldVal, removeEspecialCaracters, required, "Valor incorreto", method, validator, allArguments, arguments);
    }
    
    
    public void fieldCep(final JFormattedTextField formatField, final String oldValue, final JTextField endereco, final JTextField bairro, final JTextField cidade, final JTextField uf) {

        
        formatField.addFocusListener(new java.awt.event.FocusAdapter() {
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {                
                if (operacao  != Operacao.CADASTRO) {
                    String value = formatField.getText();
                    formatField.setForeground(COR_PADRAO_TXT);
                    setMask(formatField, "#####-###");
                    formatField.setText(value);
                    return;
                }
                String value = formatField.getText();
                System.out.println(value);
                setMask(formatField, "#####-###");
                formatField.setText(value);
                formatField.setForeground(COR_PADRAO_TXT);
            }
            
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                
                //formatField.setToolTipText(obrigatorio);
                String field = removeCaracteresEspeciais(formatField);
                
                boolean flag = false;
                if (field.isEmpty() || field.length() < 8) {
                    flag = true;
                }

                if (flag) {
                    removeMask(formatField);
                    formatField.setForeground(ERRO_TXT);
                    formatField.setText(MSG_OBRIGATORIO);
                    return;
                }
                
                WebServiceCep client = new WebServiceCep();
                JSONObject json = new JSONObject();
                
                try {
                    json = client.getWsCep(field, "json");
                    bairro.setText(json.getString("bairro"));
                    endereco.setText(json.getString("logradouro"));
                    uf.setText(json.getString("uf"));
                    cidade.setText(json.getString("localidade")); 
                    
                } catch(JSONException | IOException e) {
                    removeMask(formatField);
                    formatField.setForeground(ERRO_TXT);
                    formatField.setText("Cep incorreto");
                    bairro.setText("");
                    endereco.setText("");
                    uf.setText("");
                    cidade.setText(""); 
                    //new Message("Parece que houve uma falha em conectar à internet para validar o CEP\n", "Falha de conexão").show();
                }
                    
            }
            
        });

    }
    
    
    public void fieldWithMask(final JFormattedTextField formatField, final String mask, final String oldValue, final boolean removeEspecialCaracters, final boolean required, String invalidMessage, final String method, final Object validator, final Class... params) {
        
        formatField.addFocusListener(new java.awt.event.FocusAdapter() {
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (operacao  != Operacao.CADASTRO) {
                    String value = formatField.getText();
                    formatField.setForeground(COR_PADRAO_TXT);
                    setMask(formatField, mask);
                    formatField.setText(value);
                    return;
                }
                String value = formatField.getText();
                setMask(formatField, mask);
                formatField.setText(value);
                formatField.setForeground(COR_PADRAO_TXT);
            }
            
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                boolean flag = false;
                if (required) {
                    String field = formatField.getText();
                    if (removeEspecialCaracters) {
                        field = removeCaracteresEspeciais(formatField);
                    }

                    if (method != null) { // erro aqui campo de data do cliente
                        Method m = getMethod(validator.getClass(), method, params);
                        try {
                            flag = (boolean) m.invoke(validator, field);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Logger.getLogger(ViewValidator.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    } else {
                        if (! field.isEmpty()) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                        removeMask(formatField);
                        formatField.setForeground(ERRO_TXT);
                        formatField.setText(invalidMessage); 
                    }
                }
            }
        });
        
    }    

    
    
    public void fieldWithMask(final JFormattedTextField formatField, final DecimalFormat mask, final String oldValue, final boolean removeEspecialCaracters, final boolean required, String invalidMessage, final String method, final Object validator, final boolean allArguments, final Object... arguments) {
        
        formatField.addFocusListener(new java.awt.event.FocusAdapter() {
            
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                
                if (operacao  != Operacao.CADASTRO) {
                    String value = formatField.getText();
                    formatField.setForeground(COR_PADRAO_TXT);
                    setMask(formatField, mask);
                    formatField.setText(value);
                    return;
                }
                String value = formatField.getText();
                //String a = !formatField.getText().isEmpty() ? formatField.getText() : "";                
                setMask(formatField, mask);
                formatField.setForeground(COR_PADRAO_TXT);
                formatField.setText(value);
            }
            
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                
                boolean flag = false;
                if (required) {
                    String field = formatField.getText();
                    if (removeEspecialCaracters) {
                        field = removeCaracteresEspeciais(formatField);
                    }
                    
                    if (field.isEmpty()) {
                        flag = false;
                    }
                    
                    if (method != null && !field.isEmpty()) {
                        List params = new ArrayList();
                        if (allArguments) {
                            params.add(field);
                        }
                        params.addAll(Arrays.asList(arguments));
                        Method m = getMethod(validator.getClass(), method, toArrayClass(params));
                        try {
                            flag = (boolean) m.invoke(validator, toArrayObject(params));
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            Logger.getLogger(ViewValidator.class.getName()).log(Level.SEVERE, null, ex);
                        }                        
                    } else {
                        if (! field.isEmpty()) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                        removeMask(formatField);
                        formatField.setForeground(ERRO_TXT);
                        formatField.setText(invalidMessage); 
                    }
                }
            }
        });
        
    }    

    
    
    public void fieldWithMask(final JFormattedTextField formatField, final String mask, final String oldValue, final boolean removeEspecialCaracters, final boolean required, String invalidMessage) {
        fieldWithMask(formatField, mask, oldValue, removeEspecialCaracters, required, invalidMessage, null, null, null);
        //fieldWithMask(formatField, mask, oldValue, removeEspecialCaracters, required, invalidMessage, null);
    }

    
    public void fieldWithMask(final JFormattedTextField formatField, final String mask, final String oldValue, final boolean removeEspecialCaracters, final boolean required, final String method, final Object validator, final Class... params) {
        fieldWithMask(formatField, mask, oldValue, removeEspecialCaracters, required, MSG_OBRIGATORIO, method, validator, params);
    }
    
    
    private Method getMethod(Class classe, String methodName, Class... params) {
        try {
            Method method = classe.getMethod(methodName, params);
            return method;
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ViewValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    private void removeMask(JFormattedTextField field) {
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter("****************************************");
            formatter.setPlaceholderCharacter(' ');
            field.setFormatterFactory(new DefaultFormatterFactory(formatter));            
            field.selectAll();
        } catch (ParseException ex) {
            Logger.getLogger(ViewValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    private void setMask(JFormattedTextField field, String mask) {
        
        MaskFormatter formatter = null;
        try {
            field.setValue(null);            
            //set mask
            formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter(' ');
            
            field.setFormatterFactory(new DefaultFormatterFactory(formatter));            
            field.selectAll();
            
        } catch (ParseException ex) {
            ex.printStackTrace();
        }        
    }
    
    


    /**
     * Set DecimalFormat for fields type money values
     * @param field
     * @param mask 
     */
    private void setMask(JFormattedTextField field, DecimalFormat mask) {
        
        field.setValue(null);
        //set mask
        NumberFormatter numFormatter = new NumberFormatter(mask);
        numFormatter.setFormat(mask);
        numFormatter.setAllowsInvalid(false);
        DefaultFormatterFactory dfFactory = new DefaultFormatterFactory(numFormatter);
        field.setFormatterFactory(dfFactory);
        field.selectAll();
    }    
    
    
    
    private String removeCaracteresEspeciais(JTextField field) {
        return removeCaracteresEspeciais(field.getText());
    }

    
    private String removeCaracteresEspeciais(String str) {
        return str.replace(".", "").replace("-", "").replace("/", "").replace(" ", "")
        .replace("*", "").replace("*", "").replace(";", "").replace("?", "")
        .replace("#", "").replace("=", "").replace(">", "").replace("<", "")
        .replace("&", "").replace("!", "").replace("%", "").replace("@", "")
        .replace("^", "").replace("~", "").replace("(", "").replace(")", "");
    }


    public boolean canRemoveText(JFormattedTextField field) {
        if (field.getToolTipText().equalsIgnoreCase(obrigatorio)) {
            return true;
        }
        return false;
    }
    
    public boolean canRemoveText(JTextField field) {
        if (field.getToolTipText().equalsIgnoreCase(obrigatorio)) {
            return true;
        }
        return false;
    }

    
    private Class[] toArrayClass(List values) {
        Class[] c = new Class[values.size()];
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) != null) {
                c[i] = values.get(i).getClass();
            }            
        }
        return c;
    }
    
    
    private Object[] toArrayObject(List values) {
        Object[] c = new Object[values.size()];
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) != null) {
                c[i] = values.get(i);
            }
        }
        return c;
    }
    
}
