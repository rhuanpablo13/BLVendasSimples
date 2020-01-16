package infra.abstratas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import infra.comunicacao.Warning;
import infra.mapeamento.MapNegocioView;
import infra.mapeamento.Mapeamento;
import infra.operacoes.Operacao;
import infra.reflection.ObjectRefflection;
import infra.utilitarios.Utils;
import infra.validadores.ViewValidator;
import infra.visualizacao.MessageOutPut;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author rhuan
 * @param <T>
 */
public abstract class View <T extends Negocio> extends JInternalFrame {
    
    private MapNegocioView map;
    protected Class model;
    protected Operacao operacao;
    protected ViewValidator validator;
    protected MessageOutPut out;
    
    
    public View() {
        this.model = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.operacao = Operacao.CADASTRO;
        this.validator = new ViewValidator();
        this.validator.setOperacao(operacao);
        this.out = new MessageOutPut();
        map = new MapNegocioView(model, this.getClass());
        removeTopBar();
        //map.print();
    }

    private void removeTopBar() {
        //remove a barra superior da window
        Utils.removeBarTopWindow(this);
        ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    }
    
    
    protected void initializeView() {
        initActions();
    }


    protected abstract void initActions();
    
    
    // View > Model
    public T setValuesView2Model() {
        
        System.out.println(operacao);
        try {
            ObjectRefflection or = new ObjectRefflection();
            Object instanceModel = getNewInstance(model);
            int nrLinhas = map.getNrLinhas();
            
            if (instanceModel != null) {
                
                List<Method> getView = map.getsFromView();
                List<Method> setModel = map.setsFromNegocio();
                
                // removendo os itens nulos
                getView.removeIf((get) -> get == null);
                setModel.removeIf((set) -> set == null);
                
                // Tratamento para pesquisar
                if (operacao == Operacao.PESQUISAR) {
                    List<Method> getViewPesquisa = or.getMethodsLikeName(this.getClass(), "getPesq");
                    for (Method getViewPesq : getViewPesquisa) { //getPesqNome
                        for (Method setFromModel : setModel) {   //setNome
                            
                            if (setFromModel != null) {
                                String campo = setFromModel.getName().substring(3, setFromModel.getName().length());
                                if (getViewPesq.getName().endsWith(campo)) {                                
                                    try {
                                        Object valueViewPesq = or.getValueByMethod(this, getViewPesq);
                                        if (valueViewPesq != null) {
                                            or.setValuesByMethod(instanceModel, setFromModel, Arrays.asList(valueViewPesq));
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                        }
                    }
                    return (T) instanceModel;
                }
                
                List<Method> getViewTemp = new ArrayList();
                List<Method> setModelTemp = new ArrayList();

                /* Percorrer o array de gets da view e sets da model e "casar" os métodos correspondentes */
                for (Method set : setModel) {
                    String setTemp = set.getName().substring(3, set.getName().length());
                    for (Method get : getView) {
                        String getTemp = "";
                        if (get.getName().startsWith("is")) {
                            getTemp = get.getName().substring(2, get.getName().length());
                        } else {
                            getTemp = get.getName().substring(3, get.getName().length());
                        }
                        if (getTemp.equalsIgnoreCase(setTemp)) {
                            getViewTemp.add(get);
                            setModelTemp.add(set);
                        }
                    }
                }
                
                /* Remove os itens antigos e adiciona os novos */
                getView.clear();
                setModel.clear();
                getView.addAll(getViewTemp);
                setModel.addAll(setModelTemp);
                
                
                // Tratamento para inserção
                if (operacao == Operacao.CADASTRO) {
                    getView = removeMethod(getView, "getId");
                    setModel = removeMethod(setModel, "setId");                    
                    nrLinhas = Math.max(getView.size(), setModel.size());
                }
                
                
                
                for (int i = 0; i < nrLinhas; i++) {
                    try {
                        
                        if (getView.get(i) != null && setModel.get(i) != null) {
                            Object value = or.getValueByMethod(this, getView.get(i));
                            //System.out.println("getView > " + getView.get(i).getName() + " - setModel > " + setModel.get(i).getName() + " = " + value);
                            if (value != null) {
                                or.setValuesByMethod(instanceModel, setModel.get(i), Arrays.asList(value));
                            }
                        }
                    } catch (Exception ex) {
                        //new Warning(ex.getMessage(), "Falha").show();
                    }
                }
                return (T) instanceModel;
            }
            
        } catch(Exception ex) {
            new Warning("Falha em setValuesView2Model", "Erro").show();
            ex.printStackTrace();
        }
        return null;
    }
    
    
    
    
    /**
     * Envia os valores do parametro model, para a tela de visualização atual,
     * ou seja, seta os valores da model na tela view do usuário.
     * @param model 
     */
    public void setValuesModel2View(T model) {
        
        if (model != null) {
            
            List<Method> setView = map.setsFromView();
            List<Method> getModel = map.getsFromNegocio();
            
            ObjectRefflection or = new ObjectRefflection();
            int nrLinhas = map.getNrLinhas();
            for (int i = 0; i < nrLinhas; i++) {
                try {
                    if (setView.get(i) != null && getModel.get(i) != null) {
                        Object value = or.getValueByMethod(model, getModel.get(i));                        
                        ///System.out.println(getModel.get(i).getName() + " - " + setView.get(i).getName() + " - " + value);
                        if (value != null) {
                            or.setValuesByMethod(this, setView.get(i), Arrays.asList(value));                            
                        }
                    }
                } catch (Exception ex) {ex.printStackTrace();}
            }
        } else {
            new Warning("Objeto: " + model.getClass().getCanonicalName() + " nulo!", "Falha").show();
        }
    }


    protected void cleanViewFields() {
        this.clean(map.fieldsView());
    }
    
    
    protected void clean(List<Field> fields) {
        
        for (Field f : fields) {
            
            if (f == null) continue;
            try {
                Field field = this.getClass().getDeclaredField(f.getName());                
                field.setAccessible(true);
                Object o = field.get(this);
                
                if (o instanceof JTextField) {
                    JTextField tf = (JTextField) o;
                    tf.setText(null);
                }
                if (o instanceof JFormattedTextField) {
                    JFormattedTextField tf = (JFormattedTextField) o;
                    tf.setText(null);
                }
                if (o instanceof JCheckBox) {
                    JCheckBox tf = (JCheckBox) o;
                    tf.setSelected(false);
                }
                                
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | NullPointerException ex) {
                new Warning("Falha ao limpar o campo: "+ f.getName() +" na view", "Falha").show();
            }
        }
    }
    

    protected void setMask(JFormattedTextField field, String mask, boolean decimalFormat) {
        
        if (decimalFormat) {
            field.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat(mask))));
            return;
        }
        
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

    public Mapeamento getMap() {
        return this.map;
    }
   
    
    /**
     * Retorna a instancia de uma classe
     * @return Object
     */
    private Object getNewInstance(Class<?> classe) throws Exception {
        try {
            return classe.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new Exception("Falha ao instanciar classe: " + classe.getCanonicalName() + "\n" + ex.getMessage());
        }
    }    
    
    
 
    private List<Method> removeMethod(List<Method> methods, String remove) {
        List<Method> aux = new ArrayList(methods);
        for (Method method : methods) {
            if (method != null && method.getName().equalsIgnoreCase(remove)) {
                aux.remove(method);
            }                
        }
        return aux;
    }



    /***************************************************************************/
    
    protected String parseUtf8(String str) {
        try {
            if (str == null || str.isEmpty()) return "";
            byte array[] = str.getBytes("UTF-8");
            return Arrays.toString(array);
        } catch (UnsupportedEncodingException ex) {
        }
        return str;
    }
    
    
    
    
    /* Double (View to Model) */
    private Double doubleToModel(String doubleValue) {
        if (doubleValue.isEmpty()) return null;
        String removed = doubleValue.replace(",", ".").replace("%", "").replace("R$", "").replace("$", "").replace("R", "");
        String[] vls = removed.split("\\.");
        if (vls.length > 1) {
            doubleValue = "";
            for (int i = 0; i < vls.length-1; i++) {
                doubleValue += vls[i];
            }
            doubleValue += ("." + vls[vls.length-1]);
        }
        return Double.parseDouble(doubleValue);
    }
    
    
    protected Double toDouble(JFormattedTextField doubleValue) {
        return doubleToModel(doubleValue);
    }
    
    protected Double doubleToModel(JFormattedTextField doubleValue) {
        if (doubleValue == null || doubleValue.getText().isEmpty()) return null;
        return doubleToModel(doubleValue.getText()); 
    }

    protected Double doubleToModel(JTextField doubleValue) {
        if (doubleValue == null || doubleValue.getText().isEmpty()) return null;
        return doubleToModel(doubleValue.getText()); 
    }    
    
    
    /* Double (Model to View) */
    protected String doubleToView(Double doubleValue) {
        if (doubleValue == null) return null;
        return converterDoubleString(doubleValue);
    }
    
    
    protected String doubleToView(JTextField doubleValue) {
        if (doubleValue == null || doubleValue.getText().isEmpty()) return null;
        return converterDoubleString(Double.parseDouble(doubleValue.getText()));
    }
        
    
    protected static String converterDoubleString(double precoDouble) {
        /*Transformando um double em 2 casas decimais*/
        DecimalFormat fmt = new DecimalFormat("0.00");   //limita o número de casas decimais    
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String preco = part[0]+","+part[1];
        return preco;
    }

    protected static double converterDoubleDoisDecimais(double precoDouble) {
        DecimalFormat fmt = new DecimalFormat("0.00");      
        String string = fmt.format(precoDouble);
        String[] part = string.split("[,]");
        String string2 = part[0]+"."+part[1];
        double preco = Double.parseDouble(string2);
        return preco;
    }      
    
    
 
    
    /* Integer (View to Model) */
    protected Integer integerToModel(String integerValue) {
        if (integerValue.isEmpty()) return null;
        return Integer.parseInt(integerValue);
    }
    
    protected Integer integerToModel(JFormattedTextField integerValue) {
        if (integerValue.getText().isEmpty()) return null;
        return integerToModel(integerValue.getText()); 
    }

    protected Integer integerToModel(JTextField integerValue) {
        if (integerValue.getText().isEmpty()) return null;
        return integerToModel(integerValue.getText()); 
    }    
    
    
    /* Integer (Model to View) */
    protected String toView(Integer integerValue) {
        return Integer.toString(integerValue);
    }
    
    
    
    
    
    /* Float (View to Model) */
    private Float floatToModel(String floatValue) {
        if (floatValue.isEmpty()) return null;
        return Float.parseFloat(
            floatValue.replace(",", ".").replace("%", "").replace("R$", "").replace("$", "").replace("R", "")
        );
    }
    
    protected Float floatToModel(JFormattedTextField floatValue) {
        if (floatValue.getText().isEmpty()) return null;
        return floatToModel(floatValue.getText()); 
    }

    protected Float floatToModel(JTextField floatValue) {
        if (floatValue.getText().isEmpty()) return null;
        return floatToModel(floatValue.getText()); 
    }    
    
    
    /* Float (Model to View) */
    protected String floatToModel(Float floatValue) {
        return converterDoubleString(floatValue);
    }
    
    
    /* Float (Model to View) */
    protected String toView(Float integerValue) {
        return Float.toString(integerValue);
    }
    
    /***************************************************************************/
    
    
    
    
    
    protected void setOperacao(Operacao operacao) {
        this.operacao = operacao;
        this.validator.setOperacao(operacao);
    }
    
    
    public void print(Map<String, List<Method>> map) {
        for (Map.Entry<String, List<Method>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Method> value = entry.getValue();
            System.out.println(key + ": \t" + value.get(0) + " \t" + value.get(1));
        }
    }

    
    
    
    
    
    protected String toReal_revisar(Double dValue) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(dValue));
        int intValue = bigDecimal.intValue();
        System.out.println("\nDouble Number: " + bigDecimal.toPlainString());
        System.out.println("Integer Part: " + intValue);
        System.out.println("Decimal Part: " + bigDecimal.subtract(
        new BigDecimal(intValue)).toPlainString());
        return bigDecimal.subtract(new BigDecimal(intValue)).toPlainString();
    }
    
}
