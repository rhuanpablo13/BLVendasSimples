/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.mapeamento;

import infra.abstratas.Negocio;
import infra.comunicacao.Warning;
import infra.reflection.DatabaseReflection;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *  //          Banco           |          Classe Negocio        |
    //Campo Banco | index Banco | Field nome | Field | get | set |
 *         0            1              2         3      4     5  
 * 
 * @author rhuan
 */
public class MapNegocioBanco extends Mapeamento {

    private String nmTable;
    private DatabaseReflection databaseReflection;
    private Map<String, Integer> mapCamposTabela;
    private final int NR_COLUNAS = 6;
    
    
    public MapNegocioBanco(Class<? extends Negocio> negocio, String table) {
        initVariables();
        super.mapa = null;
        super.classeNegocio = negocio;
        this.nmTable = table;
        super.nrColunas = NR_COLUNAS;
        this.init();
        //print();
    }

    
    
    @Override
    protected void initVariables() {
        INDEX_COLUMNS          = 0; //0 - nomes das colunas da tabela
        INDEX_POSITION_COLUMNS = 1; //1 - index das colunas
        INDEX_NM_FIELD_NEGOCIO = 2; //2 - nomeFieldNegocio
        INDEX_FIELD_NEGOCIO    = 3; //3 - fieldNegocio
        INDEX_GET_NEGOCIO      = 4; //4 - getNegocio
        INDEX_SET_NEGOCIO      = 5;
    }
    
    
    
    
    @Override
    protected void init() {
        super.init();
        databaseReflection = new DatabaseReflection();
        mapCamposTabela = databaseReflection.getColumnsAndIndex(nmTable);
        if (mapCamposTabela.isEmpty()) {
            new Warning("Tabela: " + nmTable + " não foi encontrada no banco de dados", "Atenção").show();
            return;
        }
        
        // O número de linhas da tabela é calculado, pegando o maior valor entre
        // o número de campos da tabela e da classe de negócio. Sendo assim,
        // se um for maior que o outro, a tabela pode conter linhas com valores null
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
    
    public void printAux(Map<String, Integer> aux) {
        for (Entry<String, Integer> entry : aux.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + " - " + value);
        }
    }
    
    @Override
    protected void sort() {
        
        // Aqui a tabela mapa está montada, porém, vazia com valores nulos
        
        // Ordena os campos da tabela pela ordem no banco de dados
        Map<String, Integer> camposTabelaOrdenados = sortByValue(mapCamposTabela);

        //Campo Banco | index Banco
        for (Map.Entry<String, Integer> entry : camposTabelaOrdenados.entrySet()) {
            String campo = entry.getKey(); // campo da tabela do banco de dados
            Integer index = entry.getValue(); // posição do campo na tabela do banco
            
            mapa[linha][INDEX_COLUMNS] = campo;
            mapa[linha][INDEX_POSITION_COLUMNS] = index;
            linha++;
        }        
        
        // zera o  contador de linhas
        linha = 0;
        
        // popula o mapa referente ao negocio
        for (Map.Entry<Field, List<Method>> entry : mapFieldsMethodsNegocio.entrySet()) {
            Field campoNegocio = entry.getKey();
            Integer posicaoCampoNaTabela = findIntegerValue(camposTabelaOrdenados, campoNegocio.getName());
            
            // Ignora os campos de negócio que não estejam na tabela do banco
            if (posicaoCampoNaTabela != null) {
                List<Method> metodosGetSet = entry.getValue(); // métodos get e set dos campos de negócio
                //System.out.println(campoNegocio + " - " + posicaoCampoNaTabela);
                mapa[posicaoCampoNaTabela-1][INDEX_NM_FIELD_NEGOCIO] = campoNegocio.getName();
                mapa[posicaoCampoNaTabela-1][INDEX_FIELD_NEGOCIO]    = campoNegocio;
                mapa[posicaoCampoNaTabela-1][INDEX_GET_NEGOCIO]      = metodosGetSet.get(GET_INDEX); // recupera o método get
                mapa[posicaoCampoNaTabela-1][INDEX_SET_NEGOCIO]      = metodosGetSet.get(SET_INDEX); // recupera o método set
            } 
//            else {
//                System.out.println("veio null " + linha + " campo " + campoNegocio.getName());
//                mapa[linha][INDEX_NM_FIELD_NEGOCIO] = null;
//                mapa[linha][INDEX_FIELD_NEGOCIO]    = null;
//                mapa[linha][INDEX_GET_NEGOCIO]      = null;
//                mapa[linha][INDEX_SET_NEGOCIO]      = null;
//            }
        }
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
        int nrLinhasBanco = databaseReflection.getColumnsName(nmTable).size();
        return Math.max(nrLinhasNeg, nrLinhasBanco);
    }
    

    @Override
    public String getColumnDatabaseByFieldNegocio(Field field) {
        return super.getColumnDatabaseByFieldNegocio(field);
    }
        

    @Override
    public List<String> getColumnsDatabase() {
        return super.getColumnsDatabase();
    }
    

    @Override
    public List<Integer> getIndexesDatabase() {
        return super.getIndexesDatabase();
    }
    

    @Override
    public Map<String, Integer> getColumnsAndIndexTable() {
        return super.getColumnsAndIndexTable();
    }
    

    @Override
    public Map<Integer, String> getIndexsAndColumnsTable() {
        return super.getIndexsAndColumnsTable();
    }


    @Override
    public String getTableColumnByIndex(int index) {
        return super.getTableColumnByIndex(index);
    }


    @Override
    public Integer getIndexByTableColumn(String column) {
        return super.getIndexByTableColumn(column);
    }
    
    
    /**
     * Ordena um Map pelos valores.
     * @param <K>
     * @param <V>
     * @param map
     * @return Map
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    

    
}
