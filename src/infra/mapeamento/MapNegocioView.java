/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.mapeamento;

import infra.abstratas.Negocio;
import infra.abstratas.View;
import infra.comunicacao.Warning;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * | Classe Negocio | Classe View | |Field nome | Field | get | set | Field nome
 * | Field | get | set | 0 1 2 3 4 5 6 7
 *
 * @author rhuan
 */
public class MapNegocioView extends Mapeamento {

    private Class classeView;
    private Map<Field, List<Method>> mapFieldsMethodsView;
    private final int NR_COLUNAS = 8;

    private List<Method> getsFromView;
    private List<Method> setsFromView;
    private List<Field> fieldsFromView;

    public MapNegocioView(Class<? extends Negocio> negocio, Class<? extends View> view) {
        initVariables();
        super.mapa = null;
        super.classeNegocio = negocio;
        this.classeView = view;
        super.nrColunas = NR_COLUNAS;
        this.init();
        System.out.println("Negocio View");
        //print();
    }

    @Override
    protected void initVariables() {
        INDEX_NM_FIELD_NEGOCIO = 0; //2 - nomeFieldNegocio
        INDEX_FIELD_NEGOCIO = 1; //3 - fieldNegocio
        INDEX_GET_NEGOCIO = 2; //4 - getNegocio
        INDEX_SET_NEGOCIO = 3; //5 - setNegocio
        INDEX_NM_FIELD_VIEW = 4; //6 - nomeFieldView
        INDEX_FIELD_VIEW = 5; //7 - fieldView
        INDEX_GET_VIEW = 6; //8 - getView
        INDEX_SET_VIEW = 7; //9 - setView        
    }

    @Override
    protected void init() {
        super.init();
        getsFromView = new ArrayList();
        setsFromView = new ArrayList();
        fieldsFromView = new ArrayList();
        mapFieldsMethodsView = getMapView();

        try {
            super.nrLinhas = calculateNrLinhas();
            if (nrLinhas == 0) {
                new Warning("Atenção, o mapeamento não pode ser feito, pois o número de linhas é igual a 0 ", "Atenção").show();
            }
        } catch (Exception ex) {
            new Warning("Falha ao calcular número de linhas. nrLinhas = " + nrLinhas, "Falha").show();
        }

        super.mapa = new Object[nrLinhas][nrColunas];
        sort();

    }

    @Override
    protected void sort() {

        int pos = 0;
        // popula o mapa referente ao negocio
        for (Map.Entry<Field, List<Method>> entry : mapFieldsMethodsNegocio.entrySet()) {
            Field key = entry.getKey();
            List<Method> value = entry.getValue();

            mapa[pos][INDEX_NM_FIELD_NEGOCIO] = key.getName();
            mapa[pos][INDEX_FIELD_NEGOCIO] = key;
            mapa[pos][INDEX_GET_NEGOCIO] = value.get(GET_INDEX);
            mapa[pos][INDEX_SET_NEGOCIO] = value.get(SET_INDEX);
            pos++;
        }

        // popula o mapa view
        for (Map.Entry<Field, List<Method>> entry : mapFieldsMethodsView.entrySet()) {
            Field key = entry.getKey();
            List<Method> value = entry.getValue();
            Integer linha = getIndexFromMapNegocio(key);
            if (linha != null) {
                mapa[linha][INDEX_NM_FIELD_VIEW] = key.getName();
                mapa[linha][INDEX_FIELD_VIEW] = key;
                mapa[linha][INDEX_GET_VIEW] = value.get(GET_INDEX);
                mapa[linha][INDEX_SET_VIEW] = value.get(SET_INDEX);
            }
        }
    }

    private Integer getIndexFromMapNegocio(Field field) {
        int pos = 0;
        for (Map.Entry<Field, List<Method>> entry : getMapModel().entrySet()) {
            Field key = entry.getKey();
            if (key.getName().equalsIgnoreCase(field.getName())) {
                return pos;
            } else if (field.getName().startsWith("f")) {
                String campo = field.getName().substring(1);
                if (campo.equalsIgnoreCase(key.getName())) {
                    return pos;
                }
            }
            pos++;
        }
        return null;
    }

    @Override
    public Object[][] getMapa() {
        return mapa; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNrLinhas() {
        return super.getNrLinhas(); //To change body of generated methods, choose Tools | Templates.
    }

    public int getNrColunas() {
        return nrColunas;
    }

    @Override
    protected int calculateNrLinhas() throws Exception {
        int nrLinhasNeg = objectReflection.getFields(classeNegocio, true).size();
        int nrLinhasView = objectReflection.getFieldsStartsWith(classeView, "f").size();
        return Math.max(nrLinhasNeg, nrLinhasView);
    }

    /**
     * Recupera os campos da tela de visualização que contenham os métodos get e
     * set
     *
     * @return Map<String, List<Method>>
     */
    protected Map<Field, List<Method>> getMapView() {
        return getFieldsAndMethods(allFieldsFromView(), allGetsFromView(), allSetsFromView());
    }

    public void p(List<Method> list, boolean flag) {
        for (Method string : list) {
            System.out.println(string.getName());
        }
        System.out.println("");
    }
    public void p(List<String> list) {
        for (String string : list) {
            System.out.println(string);
        }
        System.out.println("");
    }
    
    // -> gets
    private List<Method> allGetsFromView() {
        if (this.getsFromView.isEmpty()) {
            return objectReflection.getMethodsInitName(this.classeView, getsPatterns);
        }
        return getsFromView;
    }

    // -> sets
    private List<Method> allSetsFromView() {
        if (this.setsFromView.isEmpty()) {
            return objectReflection.getMethodsInitName(this.classeView, setsPatterns);
        }
        return setsFromView;
    }

    // -> fields
    private List<Field> allFieldsFromView() {
        if (this.fieldsFromView.isEmpty()) {
            return objectReflection.getFields(this.classeView);
        }
        return fieldsFromView;
    }

    @Override
    public Method findGetFromView(Field field) {
        return super.findGetFromView(field);
    }

    @Override
    public Method findGetFromView(String field) {
        return super.findGetFromView(field);
    }

    @Override
    public Method findSetFromView(Field field) {
        return super.findSetFromView(field);
    }

    @Override
    public Method findSetFromView(String field) {
        return super.findSetFromView(field);
    }

    @Override
    public List<Method> getsFromView() {
        return super.getsFromView();
    }

    @Override
    public List<Method> setsFromView() {
        return super.setsFromView();
    }

    @Override
    public Method findGetFromNegocio(Field field) {
        return super.findGetFromNegocio(field);
    }

    @Override
    public Method findGetFromNegocio(String field) {
        return super.findGetFromNegocio(field);
    }

    @Override
    public Method findSetFromNegocio(Field field) {
        return super.findSetFromNegocio(field);
    }

    @Override
    public Method findSetFromNegocio(String field) {
        return super.findSetFromNegocio(field);
    }

    @Override
    public List<Method> getsFromNegocio() {
        return super.getsFromNegocio();
    }

    @Override
    public List<Method> setsFromNegocio() {
        return super.setsFromNegocio();
    }

    @Override
    public List<Field> fieldsView() {
        return super.fieldsView();
    }

    @Override
    public List<Field> fieldsNegocio() {
        return super.fieldsNegocio();
    }

}
