/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.mapeamento;

import infra.comunicacao.Warning;
import infra.reflection.ObjectRefflection;
import infra.utilitarios.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * Classe criada com o intuito de facilitar a comunicação entre a as camadas de 
 * visualização, negócio e persistencia (model, view, controller (persistencia)) 
 * 
 * OBS >> View -> Swing
 * 
 * Passando as classes de negócio, view e o nome da tabela a qual ela representa,
 * é possível montar um "mapa" onde pode-se acessar os métodos get e set de um atributo
 * pesquisando apenas pelo nome. 
 * 
 * Para usar recursos de Reflection, essas informações ficam bem mais fáceis
 * de serem acessadas.
 * 
 * 
 *  Estrutura básica da tabela:
 * 
 *  //          Banco           |          Classe Negocio        |           Classe View          |  
    //Campo Banco | index Banco | Field nome | Field | get | set | Field nome | Field | get | set | 
 *         0            1              2         3      4     5        6          7      8     9  
 *
 * OBS >> Para que tudo consiga ser mapeado corretamente, a classe view precisa ter os 
 * métodos get e set implementados com a mesma assinatura dos métodos da classe de negócio.
 * Ou seja, as regras de conversão (model -> view) ou (view -> model) serão implementadas
 * nestes métodos, quando necessário.
 * 
 * OBS >> Os campos da view são prefixados com a letra "f" fazendo referencia à Field
 * OBS >> Os campos de pesquisa da view são prefixados com a palavra "pesq"
 * 
 * 
 * @author rhuan
 */
public abstract class Mapeamento {
    
    protected Object [][] mapa;
    protected Class classeNegocio;
    protected ObjectRefflection objectReflection;

    protected int nrLinhas = 0;
    protected int nrColunas = 0;
    
    protected List<Method> getsFromNegocio;
    protected List<Method> setsFromNegocio;
    protected List<Field> fieldsFromNegocio;
    
    protected Map<Field, List<Method>> mapFieldsMethodsNegocio;
    
    protected int linha = 0;
    protected List<String> getsPatterns;
    protected List<String> setsPatterns;

    
    protected void init() {
        objectReflection = new ObjectRefflection();
        initArrays();
        initNegocio();
    }
    
    
    private void initArrays() {
        getsPatterns = Arrays.asList("get", "is");
        setsPatterns = Arrays.asList("set");
        mapFieldsMethodsNegocio = new LinkedHashMap();
    }

    
    protected void initNegocio() {
        getsFromNegocio = new ArrayList();
        setsFromNegocio = new ArrayList();
        fieldsFromNegocio = new ArrayList();
        mapFieldsMethodsNegocio = getMapModel();
    }
    
    public void printMapFieldsMethodsNegocio() {
        for (Map.Entry<Field, List<Method>> entry : mapFieldsMethodsNegocio.entrySet()) {
            Field key = entry.getKey();
            List<Method> value = entry.getValue();            
            System.out.println(key + " - " + value);
        }
    }
            

    /**
     * Método que deve ser sobreescrito para ordenar o mapeamento de classes
     */
    protected abstract void sort();
    
    
    /**
     * Método para iniciar as variáveis de posição do mapa
     */
    protected abstract void initVariables();
    
    
    
    /**
     * Método que deve ser sobreescrito para calcular o número de linhas da tabela
     * @return int
     * @throws Exception 
     */
    protected abstract int calculateNrLinhas() throws Exception;
    
    
    
    /**
     * Busca uma coluna da tabela pelo atributo da classe de negócio
     * @param field
     * @return String | null
     */
    protected String getColumnDatabaseByFieldNegocio(Field field) {        
        for (int i = 0; i < nrLinhas; i++) {
            if (mapa[i][INDEX_FIELD_NEGOCIO] != null) {
                if (mapa[i][INDEX_FIELD_NEGOCIO].equals(field)) {
                    return (String) mapa[i][INDEX_COLUMNS];
                }
            }
        }
        return null;
    }
        
    /**
     * Retorna uma lista com as colunas da tabela do banco de dados
     * @return List | empty
     */
    protected List<String> getColumnsDatabase() {
        List<String> list = new ArrayList();
        for (int i = 0; i < nrLinhas; i++) {
            list.add((String) mapa[i][INDEX_COLUMNS]);
        }
        return list;
    }
    
    /**
     * Retorna os indexes das colunas da tabela do banco de dados
     * @return List | empty
     */
    protected List<Integer> getIndexesDatabase() {
        List<Integer> list = new ArrayList();
        for (int i = 0; i < nrLinhas; i++) {
            list.add((Integer) mapa[i][INDEX_POSITION_COLUMNS]);
        }
        return list;    
    }
    
    /**
     * Retorna um mapa com as colunas sendo a chave, e os indexes os valores
     * @return Map | empty
     */
    protected Map<String, Integer> getColumnsAndIndexTable() {
        Map<String, Integer> list = new LinkedHashMap();
        for (int i = 0; i < nrLinhas; i++) {
            list.put((String) mapa[i][INDEX_COLUMNS], (Integer) mapa[i][INDEX_POSITION_COLUMNS]);
        }
        return list;
    }
    
    /**
     * Retorna um mapa com os indexes sendo a chave e as colunas os valores
     * @return Map | empty
     */
    protected Map<Integer, String> getIndexsAndColumnsTable() {
        Map<Integer, String> list = new LinkedHashMap();
        for (int i = 0; i < nrLinhas; i++) {
            list.put((Integer) mapa[i][INDEX_POSITION_COLUMNS], (String) mapa[i][INDEX_COLUMNS]);
        }
        return list;
    }

    /**
     * Busca uma coluna da tabela pelo seu index
     * @param index
     * @return String| null
     */
    protected String getTableColumnByIndex(int index) {
        if (index < 1 || index > nrLinhas) {
            new Warning("Index: " + index + " não existente.", "Erro").show();
            return null;
        }
        return (String) mapa[index-1][INDEX_COLUMNS];
    }

    /**
     * Busca um index pelo nome da coluna da tabela
     * @param column
     * @return Integer
     */
    protected Integer getIndexByTableColumn(String column) {
        for (int i = 0; i < nrLinhas; i++) {
            if (mapa[i][INDEX_COLUMNS].toString().equalsIgnoreCase(column)) {
                return (Integer) mapa[i][INDEX_POSITION_COLUMNS];
            }
        }
        new Warning("Coluna: " + column + " não encontrada.", "Erro").show();
        return null;
    }
    
    /**
     * Busca um método GET pela assinatura do seu atributo correspondente
     * @param field
     * @return Method
     */
    protected Method findGetFromView(Field field) {
        return find(field, INDEX_FIELD_VIEW, INDEX_GET_VIEW);
    }
    
    /**
     * Busca um método GET pelo nome do seu atributo correspondente
     * @param field
     * @return Method
     */
    protected Method findGetFromView(String field) {
        return find(field, INDEX_NM_FIELD_VIEW, INDEX_FIELD_VIEW, INDEX_GET_VIEW);
    }
    
    /**
     * Busca um método SET pela assinatura do seu atributo correspondente
     * @param field
     * @return Method
     */
    protected Method findSetFromView(Field field) {
        return find(field, INDEX_FIELD_VIEW, INDEX_SET_VIEW);
    }

    /**
     * Busca um método SET pelo nome do seu atributo correspondente
     * @param field
     * @return Method
     */
    protected Method findSetFromView(String field) {
        return find(field, INDEX_NM_FIELD_VIEW, INDEX_FIELD_VIEW, INDEX_SET_VIEW);
    }    
    
    /**
     * Retorna uma lista com todos os métodos GET da classe View
     * @return List
     */
    protected List<Method> getsFromView() {        
        return extractColumnMethod(INDEX_GET_VIEW);
    }
    
    /**
     * Retorna uma lista com todos os métodos SET da classe View
     * @return List
     */    
    protected List<Method> setsFromView() {
        return extractColumnMethod(INDEX_SET_VIEW);
    }
    
    /**
     * Busca um método GET pela assinatura do seu atributo correspondente
     * @param field
     * @return Method
     */
    protected Method findGetFromNegocio(Field field) {
        return find(field, INDEX_FIELD_NEGOCIO, INDEX_GET_NEGOCIO);
    }

    /**
     * Busca um método GET pelo nome do seu atributo correspondente
     * @param field
     * @return Method
     */
    protected Method findGetFromNegocio(String field) {
        return find(field, INDEX_NM_FIELD_NEGOCIO, INDEX_FIELD_NEGOCIO, INDEX_GET_NEGOCIO);    
    }
    
    /**
     * Busca um método SET pela assinatura do seu atributo correspondente
     * @param field
     * @return Method
     */    
    protected Method findSetFromNegocio(Field field) {
        return find(field, INDEX_FIELD_NEGOCIO, INDEX_SET_NEGOCIO);
    }

    /**
     * Busca um método SET pelo nome do seu atributo correspondente
     * @param field
     * @return Method
     */    
    protected Method findSetFromNegocio(String field) {
        return find(field, INDEX_NM_FIELD_NEGOCIO, INDEX_FIELD_NEGOCIO, INDEX_SET_NEGOCIO);      
    }

    /**
     * Retorna uma lista com todos os métodos GET da classe Negocio
     * @return List
     */    
    public List<Method> getsFromNegocio() {
        return extractColumnMethod(INDEX_GET_NEGOCIO);
    }

    /**
     * Retorna uma lista com todos os métodos SET da classe Negocio
     * @return List
     */     
    public List<Method> setsFromNegocio() {
        return extractColumnMethod(INDEX_SET_NEGOCIO);
    }

    /**
     * Retorna uma lista com todos os atributos da classe View
     * @return List
     */
    protected List<Field> fieldsView() {
        return extractColumnField(INDEX_FIELD_VIEW);
    }

    /**
     * Retorna uma lista com todos os atributos da classe Negocio
     * @return 
     */
    public List<Field> fieldsNegocio() {
        return extractColumnField(INDEX_FIELD_NEGOCIO);
    }    

    public List<String> fieldsNameNegocio() {
        return extractColumn(INDEX_NM_FIELD_NEGOCIO);
    }
    
    public List<Field> removeFieldByName(List<Field> list, String name) {
        List<Field> newFields = new ArrayList(list);
        for (Field field : list) {
            if (field != null && field.getName().equalsIgnoreCase(name)) {
                newFields.remove(field);
            }
        }
        return newFields;
    }
    
    private List<Field> extractColumnField(int index) {
        List<Field> list = new ArrayList();
        for (int i = 0; i < nrLinhas; i++) {
            list.add((Field) mapa[i][index]);
        }
        return list;
    }
    
    public List extractColumn(int index) {
        List list = new ArrayList();
        for (int i = 0; i < nrLinhas; i++) {
            list.add(mapa[i][index]);
        }
        return list;
    }
    
    private Method find (String field, int strFieldIndex, int fieldIndex, int methodIndex) {
        for (int i = 0; i < nrLinhas; i++) {
            if (mapa[i][strFieldIndex] != null) {
                Field aux = (Field) mapa[i][fieldIndex];
                if (aux.getName().equalsIgnoreCase(field)) {
                    return (Method) mapa[i][methodIndex];
                }
            }
        }
        new Warning(field + " não possui um método GET/SET implementado.", "Erro").show();
        return null;
    }
    
    private Method find (Field field, int fieldIndex, int methodIndex) {
        for (int i = 0; i < nrLinhas; i++) {
            if (mapa[i][fieldIndex] != null) {
                Field aux = (Field) mapa[i][fieldIndex];
                if (aux.equals(field)) {
                    return (Method) mapa[i][methodIndex];
                }
            }
        }
        new Warning(field + " não possui um método GET/SET implementado.", "Erro").show();
        return null;
    }    

    private List<Method> extractColumnMethod(int index) {
        List<Method> cols = new ArrayList();
        for (int i = 0; i < nrLinhas; i++) {            
            cols.add((Method) mapa[i][index]);
        }
        return cols;
    }



    

    /**
     * Recupera os campos da classe de negócio que contenham os métodos get e set
     * @return Map<String, List<Method>> 
     */
    protected Map<Field, List<Method>> getMapModel() {
        return getFieldsAndMethods(allFieldsFromModel(), allGetsFromModel(), allSetsFromModel());
    }
    
    
    protected Map<Field, List<Method>> getFieldsAndMethods(List<Field> fields, List<Method> gets, List<Method> sets) {
        Map<Field, List<Method>> map = new LinkedHashMap();
        List<Method> temp = new ArrayList(); // array auxiliar para debugar
        
        
        // Percorre todos os campos (fields)
        for (Field field : fields) {
            String f = field.getName().toLowerCase();
            if (f.startsWith("f")) {                
                f = f.substring(1, f.length());
            }
            
            Method get = null;
            Method set = null;
            
            // gets
            for (Method method : gets) {
                String m = method.getName().toLowerCase();
                if ((m.endsWith(f) && !m.contains("pesq")) && !f.contains("pesq")) {
                    get = method;
                    break;
                }
            }
            
            // sets
            for (Method method : sets) {
                String m = method.getName().toLowerCase();
                if ((m.endsWith(f) && !m.contains("pesq")) && !f.contains("pesq")) {
                    set = method;
                    break;
                }
            }
            
            if (set != null || get != null) {
                temp.add(get);
                temp.add(set);
                map.put(field, temp);
            } else {
                // não há gets e sets correspondentes na view
                if (field.getDeclaringClass().equals(classeNegocio)) {
                    temp = new ArrayList();
                    temp.add(null);
                    temp.add(null);
                    map.put(field, temp);
                }
            }
            temp = new ArrayList();
        }
        return map;
    }

    protected Integer findIntegerValue(Map<String, Integer> campos, String key) {
        for (Map.Entry<String, Integer> entry : campos.entrySet()) {
            String aux = Utils.removeCaracteresEspeciais(entry.getKey());            
            if (key.equalsIgnoreCase(aux)) {
                return entry.getValue();
            }
        }
        return null;
    }

    protected String findStringKey(Map<String, Integer> campos, String key) {
        for (Map.Entry<String, Integer> entry : campos.entrySet()) {
            String aux = Utils.removeCaracteresEspeciais(entry.getKey());
            if (key.equalsIgnoreCase(aux)) {
                return entry.getKey();
            }
        }
        return null;
    }
    

    protected Field findInView(Map<Field, List<Method>> mapFieldsMethodsView, String key) {
        
        Set<Field> keys = mapFieldsMethodsView.keySet();
        for (Field field : keys) {
            String f = field.getName().toLowerCase();
            if (f.equalsIgnoreCase("id") && key.equalsIgnoreCase("id")) {
                return field;
            }
            if (f.startsWith("f")) {
                f = f.substring(1, f.length());
                if (f.equalsIgnoreCase(key)) {
                    return field;
                }
            } else {
                if (f.equalsIgnoreCase(key)) {
                    return field;
                }   
            }
        }
        return null;
    }

    

    
    
    /**
     * Recupera todos os métodos do tipo GET de uma classe que seja Negocio
     * @return List
     */
    private List<Method> allGetsFromModel () {
        if (this.getsFromNegocio.isEmpty()) {
            return objectReflection.getMethodsInitName(classeNegocio, getsPatterns);
        }
        return getsFromNegocio;
    }

    
    /**
     * Recupera todos os métodos do tipo SET de uma classe que seja Negocio
     * @return List
     */
    private List<Method> allSetsFromModel () {
        if (this.setsFromNegocio.isEmpty()) {
            return objectReflection.getMethodsInitName(classeNegocio, setsPatterns);
        }
        return setsFromNegocio;
    }
    
    
    /**
     * Recupera todos os campos de uma classe que seja Negocio
     * @return List
     */
    private List<Field> allFieldsFromModel () {
        if (this.fieldsFromNegocio.isEmpty()) {
            try {
                return objectReflection.getFields(classeNegocio, true);
            } catch (Exception ex) {
                new Warning(ex.getMessage(), "Falha").show();
            }
        }
        return fieldsFromNegocio;
    }
    
    
    public Object[][] getMapa() {
        return mapa;
    }

    
    public int getNrLinhas() {
        try {
            return calculateNrLinhas();
        } catch (Exception ex) {
            new Warning(ex.getMessage(), "Falha").show();
        }
        return 0;
    }
    
    
    public void print() {
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[0].length; j++) {
                System.out.print(mapa[i][j] + "\t\t\t|\t\t\t");
            }
            System.out.println("");
        }
    }
    
    
    protected static final int GET_INDEX = 0;
    protected static final int SET_INDEX = 1;
//    protected static int INDEX_COLUMNS          = 0; //0 - nomes das colunas da tabela
//    protected static int INDEX_POSITION_COLUMNS = 1; //1 - index das colunas
//    protected static int INDEX_NM_FIELD_NEGOCIO = 2; //2 - nomeFieldNegocio
//    protected static int INDEX_FIELD_NEGOCIO    = 3; //3 - fieldNegocio
//    protected static int INDEX_GET_NEGOCIO      = 4; //4 - getNegocio
//    protected static int INDEX_SET_NEGOCIO      = 5; //5 - setNegocio
//    protected static int INDEX_NM_FIELD_VIEW    = 6; //6 - nomeFieldView
//    protected static int INDEX_FIELD_VIEW       = 7; //7 - fieldView
//    protected static int INDEX_GET_VIEW         = 8; //8 - getView
//    protected static int INDEX_SET_VIEW         = 9; //9 - setView


    protected int INDEX_COLUMNS          = 0; //0 - nomes das colunas da tabela
    protected int INDEX_POSITION_COLUMNS = 0; //1 - index das colunas
    protected int INDEX_NM_FIELD_NEGOCIO = 0; //2 - nomeFieldNegocio
    protected int INDEX_FIELD_NEGOCIO    = 0; //3 - fieldNegocio
    protected int INDEX_GET_NEGOCIO      = 0; //4 - getNegocio
    protected int INDEX_SET_NEGOCIO      = 0; //5 - setNegocio
    protected int INDEX_NM_FIELD_VIEW    = 0; //6 - nomeFieldView
    protected int INDEX_FIELD_VIEW       = 0; //7 - fieldView
    protected int INDEX_GET_VIEW         = 0; //8 - getView
    protected int INDEX_SET_VIEW         = 0; //9 - setView
    
}
