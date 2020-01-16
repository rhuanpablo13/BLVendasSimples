/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.utilitarios.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rhuan
 */
public class PersistenceServices {



    public String createSimpleInsert(String table, List<String> columns, List<String> additionalColumns) {
        columns.addAll(additionalColumns);
        return createSimpleInsert(table, columns);
    }
    
    
    public String createSimpleInsert(String table, List<String> columns) {
        
        String sql = "INSERT INTO " + table + "(";
        String values = "(";
        columns.removeIf((a) -> a == null);
        int size = columns.size();        
        
        for (int i = 0; i < size; i++) {
            if (columns.get(i).equalsIgnoreCase("id") || columns.get(i).equalsIgnoreCase("null")) continue;
            
            if (i < size-1) {
                sql += columns.get(i) + ", ";
                values += "?, ";
            } else {
                sql += columns.get(i);
                values += "?)";
            }
        }
        sql += ") VALUES " + values;
        System.out.println("inserir: " + sql);
        return sql;
    }

    public String createSimpleUpdate(String table, List<String> columns) {
        
        String sql = "UPDATE " + table + " SET ";
        columns.removeIf((a) -> a == null || a.equalsIgnoreCase("id"));
        int size = columns.size();
        
        for (int i = 0; i < size; i++) {
            if (i < size-1) {
                sql += columns.get(i) + " = ?, ";
            } else {
                sql += columns.get(i) + " = ? ";
            }
        }
        sql += " WHERE ID = ?";
        System.out.println("alterar: " + sql);
        return sql;
    }

    public String createSimpleSelect(String table) {
        return "SELECT * FROM " + table;
    }
    
    public String createSelect(String table, List<String> columns, List<String> values) {
        String sql = "SELECT * FROM " + table + " WHERE ";
        int size = values.size();
        
        if (size <= 0) return createSimpleSelect(table);
        
        for (int i = 0; i < size-1; i++) {
            sql += columns.get(i);
            sql += " = " + values.get(i) + " AND ";
        }
        sql += columns.get(size-1);
        sql += " = " + values.get(size-1) + ";";
        
        return sql;
    }
    
    private String createUnitClause (String column, String type, Object value) {
        
        
        switch (type) {
            
            case "java.lang.Boolean":
                if ((Boolean) value == null) return null; 
                if ((Boolean) value) {
                    return column + " IS TRUE ";
                }
                return column + " IS FALSE ";
                
                
            case "java.lang.Integer":
                if ((Integer) value == null) return null;
                return column + " = " + (Integer) value;

                
            case "java.lang.Float":
                if ((Float) value == null) return null;
                return column + " = " + (Float) value;

                
            case "java.lang.Double":
                if ((Double) value == null) return null;
                return column + " = " + (Double) value;
                
                
            case "java.lang.String":
                String v = (String) value;
                if (v == null || v.isEmpty()) return null;
                return column + " LIKE '%" + (String) value + "%'";
                
                
            case "java.util.Date":
                if ((java.util.Date) value == null) return null;
                return column + " = TO_DATE( '" + Utils.formatDate((java.util.Date) value, "dd/MM/yyyy") + "' )";
            
            
            case "java.lang.Character":
                if ((Character) value == null) return null;
                return column + " = '" + (Character) value + "'";
                
            default:
                return null;
        }
    }
    
    public String createSelect(String table, Map<List<String>, Object> attributes) {

        List<String> wheres = new ArrayList();
        if (attributes.isEmpty()) return "SELECT * FROM " + table;
        String sql = "SELECT * FROM " + table;
        String clausules = "";
        int last = attributes.size();

        for (Map.Entry<List<String>, Object> entry : attributes.entrySet()) {
            List<String> key = entry.getKey();
            String column    = key.get(0);
            String type      = key.get(1);
            Object value     = entry.getValue();
            
            String clause = createUnitClause(column, type, value);
            if (clause != null) {
                wheres.add(clause);
            }
        }
        clausules = String.join(" AND ", wheres);
        if (clausules.isEmpty()) {
            return sql;
        }        
        return sql + " WHERE " + clausules;
    }
    
    public String createSimpleDelete(String table) {
        return "DELETE FROM " + table + " WHERE CODIGO = ?";
    }
 
}
