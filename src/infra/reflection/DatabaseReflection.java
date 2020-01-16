/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.reflection;

import infra.conexao.ConnectionFactory;
import infra.conexao.Schema;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rhuan
 */
public class DatabaseReflection {

    
    /**
     * Recupera do banco de dados uma lista contendo os nomes das colunas de uma tabela
     * @param tableName
     * @return List
     */
    public List<String> getColumnsName(String tableName) {
        List<String> columnName = new ArrayList<>();
        DatabaseMetaData metadata;
        ResultSet result;
        try{
                Connection connection = ConnectionFactory.getConnection(Schema.MYSQL_SISVENDASSIMPLES);
                metadata = connection.getMetaData();
                result = metadata.getColumns(null, null, tableName, null);
                
                while(result.next()){
                    String column = result.getString("COLUMN_NAME");
                    //if (column.equalsIgnoreCase("ID") == false) {
                        columnName.add(column);
                    //}
                }
                result.close();
                connection.close();
        }catch(SQLException e){
            System.out.println("Erro ao recuperar os nomes da tabela: " + tableName);
        }
        return columnName;
    }
    
    
    public Map<String, Integer> getColumnsAndIndex(String tableName) {
        Map<String, Integer> hm = new HashMap();
        DatabaseMetaData metadata;
        ResultSet result;
        String columnName = "";
        try{
                Connection connection = ConnectionFactory.getConnection(Schema.MYSQL_SISVENDASSIMPLES);
                metadata = connection.getMetaData();
                metadata = connection.getMetaData();
                result = metadata.getColumns(null, null, tableName, null);
                
                while(result.next()){
                    Integer columnIndex = result.getInt("ORDINAL_POSITION");
                    columnName = result.getString("COLUMN_NAME");
//                    if (columnName.equalsIgnoreCase("ID") == false) {
                        hm.put(columnName, columnIndex);
//                    }
                }
                result.close();
                connection.close();
        }catch(SQLException e){
            System.out.println("Erro ao recuperar o index da coluna:  "+ columnName +" da tabela: " + tableName);
        }        
        return hm;
    }
    
    
    
    
}
