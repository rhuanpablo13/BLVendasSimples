/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.abstratas;

import infra.comunicacao.DatabaseException;
import infra.comunicacao.PersistenteException;
import infra.comunicacao.StackDebug;
import infra.mapeamento.MapNegocioBanco;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author rhuan
 */
public abstract class PersistenteGeneric extends GenericDao {
 
    protected String table;

    public PersistenteGeneric(String table) {
        this.table = table;
    }
    
    
    
    protected Integer recuperarCodigoUltimoRegistro(Class<? extends Negocio> negocioClass) throws PersistenteException {
        String sql = ("SELECT * FROM " + table + " ORDER BY ID DESC LIMIT 1;");
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Object obj = banco2Model(negocioClass, rs, table);
            if (obj instanceof Negocio) {
                return ((Negocio) obj).getCodigo();
            }
        } catch (SQLException ex) {
            throw new PersistenteException("Erro SQL ao buscar último registro da tabela: " + table + " \n" + sql + "\n" + StackDebug.getLineNumber(this.getClass()), "Falha");
        } catch (DatabaseException ex) {
            throw new PersistenteException(ex.getMessage(), "");
        } catch (PersistenteException ex) {
            throw ex;
        }
        return -1;
    }

    
    protected Object recuperarUltimoRegistro(Class negocioClass) throws PersistenteException {
        String sql = ("SELECT * FROM " + table + " ORDER BY ID DESC LIMIT 1;");
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            return banco2Model(negocioClass, rs, table);
        } catch (SQLException | IllegalArgumentException ex) {
            throw new PersistenteException("Erro SQL ao buscar último registro da tabela: " + table + " \n" + sql + "\n" + StackDebug.getLineNumber(this.getClass()), "Falha");
        } catch (DatabaseException ex) {
            throw new PersistenteException(ex.getMessage(), "Falha");
        }
    }
    
    
    protected int count(String sql) throws PersistenteException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count;
            }            
        } catch (SQLException | IllegalArgumentException ex) {
            throw new PersistenteException("Erro SQL ao contar os registro. Tabela[ " + table + " ]\n Sql[ " + sql + " ]\n" + StackDebug.getLineNumber(this.getClass()), "Falha");
        } catch (DatabaseException ex) {
            throw new PersistenteException(ex.getMessage(), "Falha");
        }
        return 0;
    }
    
    
    protected int recuperaId(String sql) throws PersistenteException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                rs.close();
                stmt.close();
                return id;
            }
        } catch (SQLException | IllegalArgumentException ex) {
            throw new PersistenteException("Não foi possível recuperar campo [ID].\nSql: " + sql + "\n" + StackDebug.getLineNumber(this.getClass()), "Falha");
        } catch (DatabaseException ex) {
            throw new PersistenteException(ex.getMessage(), "Falha");
        }
        return 0;
    }
    
    
    protected List<Integer> recuperaArrayId(String sql) throws PersistenteException {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Integer> ids = new ArrayList();
            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
            rs.close();
            stmt.close();
            return ids;
        } catch (SQLException | IllegalArgumentException ex) {
             throw new PersistenteException("Não foi possível recuperar campo [ID].\nSql: " + sql + "\n" + StackDebug.getLineNumber(this.getClass()), "Falha");
        } catch (DatabaseException ex) {
            throw new PersistenteException(ex.getMessage(), "Falha");
        }
    }
    
    
    protected Object banco2Model(Object object, ResultSet rs, String tableName) throws PersistenteException {
        
        Class classe = object.getClass();
        MapNegocioBanco map = new MapNegocioBanco(classe, tableName);

        List<Field> fieldsNegocio = map.fieldsNegocio();            
        for (Field field : fieldsNegocio) {
            if (field != null) {
                field.setAccessible(true);
                String column = map.getColumnDatabaseByFieldNegocio(field);

                if (column != null) {
                    Object value = null;
                    try {
                        value = convert(field, rs, column);
                    } catch (Exception ex) {
                        throw new PersistenteException("Não foi encontrado um mapeamento [ResultSet -> Field] para os campos\n"
                                    + "Field: " + field.getName() + " da classe: " + classe.getCanonicalName() + " e a Coluna: " + column + " "
                                    + StackDebug.getLineNumber(this.getClass()), "Falha");
                    }

                    try {
                        field.set(object, value);
                    } catch (IllegalArgumentException ex) {
                        throw new PersistenteException("O Field " + field.getName() + " não pode receber o valor " + value 
                                    + StackDebug.getLineNumber(this.getClass()), "Falha");
                    } catch (IllegalAccessException ex) { Logger.getLogger(PersistenteGeneric.class.getName()).log(Level.SEVERE, null, ex); }
                }
            }
        }            
        return object;
    }
    
    
    protected Object banco2Model(Class classe, ResultSet rs, String tableName) throws PersistenteException {
        
        Object obj;
        try {
            obj = getInstance(classe);
        } catch (Exception ex) {
            throw new PersistenteException("Impossível instanciar classe: " + classe.getCanonicalName() + StackDebug.getLineNumber(this.getClass()), "Falha");
        }
        
        MapNegocioBanco map = new MapNegocioBanco(classe, tableName);
            
        List<Field> fieldsNegocio = map.fieldsNegocio();            
        for (Field field : fieldsNegocio) {
            if (field != null) {
                field.setAccessible(true);
                try {
                    if (rs.first()) {
                        String column = map.getColumnDatabaseByFieldNegocio(field);
                        if (column != null) {
                            
                            Object value;                            
                            try {
                                value = convert(field, rs, column);
                            } catch (Exception ex) {
                                throw new PersistenteException("Não foi encontrado um mapeamento [ResultSet -> Field] para os campos\n"
                                        + "Field: " + field.getName() + " da classe: " + classe.getCanonicalName() + " e a Coluna: " + column + " "
                                        + StackDebug.getLineNumber(this.getClass()), "Falha");
                            }
                            
                            //System.out.println(obj +" - "+ value);
                            try {
                                field.set(obj, value);
                            } catch (IllegalArgumentException ex) {
                                throw new PersistenteException("O Field " + field.getName() + " não pode receber o valor " + value 
                                        + StackDebug.getLineNumber(this.getClass()), "Falha");
                                
                            } catch (IllegalAccessException | NullPointerException ex) {
                                Logger.getLogger(PersistenteGeneric.class.getName()).log(Level.SEVERE, null, ex); 
                            }
                        }
                    }
                } catch (SQLException ex) {
                    throw new PersistenteException(StackDebug.getLineNumber(this.getClass()), ex);
                }
            }
        }        
        return obj;        
    }
    
    
    
    protected Object banco2Model(ResultSet rs, String tableName, String columnName, String typeJava) throws Exception {
        try {
            return convert(typeJava, rs, columnName);
        } catch (SecurityException | SQLException | IllegalAccessException ex) {
            throw new Exception ("Erro ao recuperar dados da tabela: " + tableName + "\n" + ExceptionUtils.getStackTrace(ex));
        }
    }
    
    
    protected String getTable() {
        return table;
    }
    
    
    protected void rollbackAndClose() throws DatabaseException {
        try {
            super.rollback();        
            closeConnection();
        } catch (DatabaseException ex) {
            this.connection = null;
            throw ex;
        }
    }
    
    
    protected void commitData() throws DatabaseException {
        try {
            super.commit();
        } catch (DatabaseException ex) {
            super.connection = null;
            throw ex;
        }        
    }
    
    
    protected void commitAndClose() throws DatabaseException {
        super.commit();
        super.closeConnection();
    }

 
    
    protected Object convert(Field field, ResultSet rs, String column) throws Exception {
        
        try {
            if (field.getType().getTypeName().equals("java.util.Date")) {
                if (rs.getDate(column) == null) return null;
                return dateMysql2DateJava(rs.getDate(column));
            }

            if (field.getType().getTypeName().equals("java.lang.Boolean")) {
                return tinyInt2boolean(rs.getInt(column));
            }

            if (field.getType().getTypeName().equals("java.lang.String")) {            
                return rs.getObject(column);
            }

            if ( field.getType().getTypeName().equals("java.lang.Integer")) {
                return rs.getInt(column);
            }

            if ( field.getType().getTypeName().equals("java.lang.Float")) {
                return rs.getFloat(column);
            }
            
            if ( field.getType().getTypeName().equals("java.lang.Double")) {
                return rs.getDouble(column);
            }

            if ( field.getType().getTypeName().equals("java.lang.Character")) {
                return rs.getCharacterStream(column);
            }

            if ( field.getType().getTypeName().equals("java.math.BigDecimal")) {
                return rs.getBigDecimal(column);                
            }
             
        } catch (IllegalArgumentException | SQLException ex) {
            throw new Exception("Não foi possível encontrar um mapeamento de ResultSet do campo: " + field.getName() + "para a coluna: " + column + "\n" + ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }    
    
    
    protected PreparedStatement convert(Object value, PreparedStatement ps, int posicao) throws Exception {
        
        try {
            if (value == null) {
                return ps;
            }
            
            if ("java.util.Date".contains(value.getClass().getCanonicalName())) {                
                System.out.println("Date " + posicao);
                Date date = (Date) value;
                java.sql.Date ds = new java.sql.Date(date.getTime());
                ps.setDate(posicao, ds);
            }

            if ("java.lang.Boolean".contains(value.getClass().getCanonicalName())) {
                ps.setInt(posicao, (int) value);
            }

            if ("java.lang.String".contains(value.getClass().getCanonicalName())) {            
                ps.setString(posicao, (String) value);
            }

            if ( "java.lang.Integer".contains(value.getClass().getCanonicalName())) {
                System.out.println("Integer " + posicao);
                ps.setInt(posicao, (int) value);
            }

            if ( "java.lang.Float".contains(value.getClass().getCanonicalName())) {
                ps.setFloat(posicao, (float) value);
            }
            
            if ( "java.lang.Double".contains(value.getClass().getCanonicalName())) {
                System.out.println("Double " + posicao);
                ps.setDouble(posicao, (double) value);
            }

            if ( "java.lang.Character".contains(value.getClass().getCanonicalName())) {
                ps.setString(posicao, (String) value);
            }

            if ( "java.math.BigDecimal".contains(value.getClass().getCanonicalName())) {
                System.out.println("BigDecimal " + posicao);
                ps.setBigDecimal(posicao, (BigDecimal) value);
            }
             
        } catch (IllegalArgumentException | SQLException ex) {
            throw new Exception("Não foi possível encontrar um mapeamento de PreparedStament para o valor: " + value.getClass().getCanonicalName() + "\n" + ExceptionUtils.getStackTrace(ex));
        }
        return ps;
    }
    
    
    protected PreparedStatement convertPreparedStatement(PreparedStatement ps, String whereClause, List<Object> valores) {
        
        char[] arrayWhere = whereClause.toCharArray();
        List<String> pointsInterrogations = new ArrayList();
        
        for (int i = 0; i < arrayWhere.length; i++) {
            if (arrayWhere[i] == '?') {
                pointsInterrogations.add("?");
            }
        }
        System.out.println(whereClause);
        if (pointsInterrogations.size() == valores.size()) {            
            for (int i = 0; i < valores.size(); i++) {
                try {
                    ps = convert(valores.get(i), ps, i+1);
                } catch(Exception e) {}
            }
        }
        //System.exit(0);
        return ps;
    }
    
    
    protected Object convert(String field, ResultSet rs, String column) throws Exception {
        
        try {
            if ("java.util.Date".contains(field)) {
                if (rs.getDate(column) == null) return null;
                return dateMysql2DateJava(rs.getDate(column));
            }

            if ("java.lang.Boolean".contains(field)) {
                return tinyInt2boolean(rs.getInt(column));
            }

            if ("java.lang.String".contains(field)) {            
                return rs.getObject(column);
            }

            if ( "java.lang.Integer".contains(field)) {
                return rs.getInt(column);
            }

            if ( "java.lang.Float".contains(field)) {
                return rs.getFloat(column);
            }
            
            if ( "java.lang.Double".contains(field)) {
                return rs.getDouble(column);
            }

            if ( "java.lang.Character".contains(field)) {
                return rs.getCharacterStream(column);
            }

            if ( "java.math.BigDecimal".contains(field)) {
                return rs.getBigDecimal(column);                
            }
             
        } catch (IllegalArgumentException | SQLException ex) {
            throw new Exception("Não foi possível encontrar um mapeamento de ResultSet do campo: " + field + "para a coluna: " + column + "\n" + ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }
    
    
    protected Object convert(Object valor) {

        if (valor == null) {
            return (null);
        }

        String type = valor.getClass().getTypeName();
        if (type.contains("java.util.Date")) {
            return (dateJava2DateMysql((Date) valor));
        }

        if (type.contains("java.lang.Boolean")) {
            return (boolean2NegocioinyInt((boolean) valor));
        }
        
        if (type.contains("java.util.Date")) {            
            return dateMysql2DateJava((java.sql.Date) valor);
        }

        if (type.contains("java.lang.Boolean")) {
            return tinyInt2boolean((int) valor);
        }

        if (type.contains("java.lang.String")) {            
            return (String) valor;
        }

        if ( type.contains("java.lang.Integer")) {
            return (Integer) valor;
        }

        if ( type.contains("java.lang.Float")) {
            return (Float) valor;
        }

        if ( type.contains("java.lang.Double")) {
            return (Double) valor;
        }

        if ( type.contains("java.lang.Character")) {
            return (Character) valor;
        }

        if ( type.contains("java.math.BigDecimal")) {
            return (BigDecimal) valor;                
        }
        return null;
    }
    
    
    protected List<Object> convert(List<Object> valores) {
        List<Object> converted = new ArrayList();
        for (Object valor : valores) {
            Object value = convert(valor);
            converted.add(value);
        }
        return converted;
    }
    
    
    protected String dateJava2DateMysql(Date pData){    
        java.text.SimpleDateFormat sdf = 
             new java.text.SimpleDateFormat("yyyy-MM-dd");
        String currentNegocioime = sdf.format(pData);
        return currentNegocioime;
    }

    
    protected java.sql.Date dateJava2sqlDate(java.util.Date pData){
        return new java.sql.Date(pData.getTime());
    }
    
    
    protected Date dateMysql2DateJava(java.sql.Date dateMysql) {
         String d = new SimpleDateFormat("dd/MM/yyyy").format(dateMysql);
         try {
             return new SimpleDateFormat("dd/MM/yyyy").parse(d);
         } catch (ParseException ex) {
             Logger.getLogger(Persistente.class.getName()).log(Level.SEVERE, null, ex);
         }
         return null;
    }
    
    
    protected String boolean2NegocioinyInt(boolean bo) {
        if (bo) return "1";
        return "0";
    }
    
    
    protected boolean tinyInt2boolean(int t) {
        return t == 1;
    }
    
    
    protected Object getInstance(Class classe) throws Exception {
        try {
            return classe.newInstance();           
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new Exception("(Persistente) Impossível instanciar a classe: " + classe.getName() + "\n" + ExceptionUtils.getStackTrace(ex));
        }
    }

    
    
    
}
