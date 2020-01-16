/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.reflection;

import infra.comunicacao.ReflectionException;
import infra.comunicacao.StackDebug;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 
 * @author rhuan
 */
public class ObjectRefflection {
    
    private static List<Field> fields = new ArrayList();

    /**
     * **************************************************************
     *  MÉTODOS PARA RECUPERAR VALORES (GET)
     * **************************************************************
     */

    
    
    /**
     * Retorna o nome de todos os métodos
     * @param classe
     * @return List
     */
    public List<String> getAllMethodsName(Class classe) {
        Method[] mts = classe.getDeclaredMethods();
        List<String> names = new ArrayList();
        for (Method mt : mts) {
            names.add(mt.getName());
        }
        return names;
    }

    
    
    /**
     * Retorna todas as assinaturas dos métodos
     * @param classe
     * @return List
     */
    public List<Method> getAllMethods(Class classe) {
        Method[] mts = classe.getDeclaredMethods();        
        return Arrays.asList(mts);
    }    
    
    
    

    /**
     * Retorna todas as assinaturas de métodos que contenham este prefixo
     * @param fields
     * @param classe
     * @param prefix
     * @return List
     */
    public List<Method> getMethodsForFields(List<Field> fields, Class classe, String prefix) {
        Method[] mts = classe.getDeclaredMethods();
        List<Method> m = new ArrayList();
        String cap = "";

        for (int i = 0; i < fields.size(); i++) {
            for (Method method : mts) {
                cap = (prefix) + fields.get(i).getName().substring(0, 1).toUpperCase() + fields.get(i).getName().substring(1);
                if (method.getName().equals(cap)) {
                   m.add(method);
                }
            }
        }
        return m;
    }
 

    
    /**
     * Retorna a assinatura de todos os atributos
     * @param classe
     * @return List
     */
    public List<Field> getFields(Class classe) {
        return Arrays.asList(classe.getDeclaredFields());
    }


    
    /**
     * Executa o método no object, e recupera o valor retornado pelo método.
     * @param object
     * @param method
     * @return Object
     * @throws java.lang.Exception
     */
    public Object getValueByMethod(Object object, Method method) throws Exception {
//        try {
//            Method m = object.getClass().getDeclaredMethod(method.getName());
//            m.setAccessible(true);
//            return m.invoke(object);
//        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
//            throw new Exception("Erro ao recuperar valor pelo método: "+ method.getName() +" do objeto: " + object.getClass().getCanonicalName() + "\n" + ExceptionUtils.getStackTrace(ex));
//        }
        
        try {
            Method m = object.getClass().getDeclaredMethod(method.getName());
            m.setAccessible(true);
            return m.invoke(object);
        } catch (SecurityException | IllegalAccessException e) {
            throw new Exception("Sem permissão no método: " + method.getName() + "(" + getTypeParametersFromMethod(method) + ")" + " da classe: " + object.getClass().getName());
        } catch (IllegalArgumentException e2) {
            throw new Exception("Falha ao invocar o método: " + method.getName() + "(" + getTypeParametersFromMethod(method) + ")" + " da classe: " + object.getClass().getName());
        } catch (NoSuchMethodException e4) {
            throw new Exception("O método: " + method.getName() + "(" + getTypeParametersFromMethod(method) + ")" + " não foi encontrado na classe: " + object.getClass().getName());
        } catch (InvocationTargetException e5) {}
        return null;
    }

    
    
    /**
     * Executa uma lista de métodos no object, e retorna um array contendo os valores
     * retornados por estes métodos.
     * @param object
     * @param methods
     * @return Object[]
     * @throws java.lang.Exception
     */
    public Object[] getValuesByMethods(Object object, List<Method> methods) throws Exception {
        List<Object> values = new ArrayList<>();
        for (Method method : methods) {
            values.add( getValueByMethod(object, method) );
        }
        return values.toArray();
    }    
    


    /**
     * Recebe uma lista com os nomes (String) dos métodos, executa-os no object, 
     * e retorna um array contendo os valores retornados por estes métodos
     * retornados por estes métodos.
     * @param object
     * @param methodsName
     * @return Object[]
     * @throws java.lang.Exception
     */
    public Object[] getValuesByMethodsNames(Object object, List<String> methodsName) throws Exception {
        List<Object> values = new ArrayList<>();
        for (String name : methodsName) {
            values.add(getValueByMethodName(object, name));
        }
        return values.toArray();
    } 


    /**
     * Executa um método pelo seu nome (String) no object, e retorna o valore retornado
     * pelo método.
     * @param object
     * @param methodName
     * @return Object[]
     * @throws java.lang.Exception
     */
    public Object getValueByMethodName(Object object, String methodName) throws Exception {
        Object value = new Object();
        try {
            Method method = object.getClass().getDeclaredMethod(methodName);
            value = getValueByMethod(object, method);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new Exception("Método: " + methodName + " não encontrado no objeto: " + object.getClass().getCanonicalName() + "\n" + ExceptionUtils.getStackTrace(ex));
        }        
        return value;
    }
    
    

    /**
     * Retorna um array de valores correspondentes ao array de campos passados como parametro
     * @param object
     * @param field
     * @return Object
     * @throws java.lang.Exception
     */
    public Object getValueByField(Object object, Field field) throws Exception {
        Object value = new Object();
        try {
            field.setAccessible(true);
            value = field.get(object);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new ReflectionException(
                    "Não foi possível recuperar o valor do atributo: " + field.getName() + 
                    ", do objeto: " + object.getClass().getCanonicalName() + 
                    StackDebug.getLineNumber(this.getClass()), "Falha"
            );
        } catch (NullPointerException ex) {
            throw new ReflectionException("O Filed: " + field.getName() + " está nulo!" + StackDebug.getLineNumber(this.getClass()), "Campo nulo");
        }
        return value;
    }

    
    /**
     * Retorna um array de valores correspondentes ao array de campos passados como parametro
     * @param object
     * @param field
     * @return Object
     * @throws java.lang.Exception
     */
    public Object getValueByFieldForSeach(Object object, Field field) throws Exception {
        Object value = new Object();
        try {
            if (field != null) {
                field.setAccessible(true);
                value = field.get(object);
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception("Não foi possível recuperar o valor do atributo: "+ field.getName() +", do objeto: " + object.getClass().getCanonicalName());
        } 
        return value;
    }
    
    
    

    
    /**
     * Retorna um array de valores correspondentes ao array de campos passados como parametro
     * @param object
     * @param fieldsName
     * @return Object[]
     * @throws java.lang.Exception
     */
    public List<Object> getValuesByFields(Object object, List<Field> fieldsName) throws Exception {
        List<Object> values = new ArrayList<>();
        for (Field field : fieldsName) {
            if (field != null) {
                values.add( getValueByField(object, field) );                
            }
        }
        return values;
    }    
    


    /**
     * Retorna um Map de valores correspondentes ao array de campos passados com oparametro
     * @param object
     * @param fields
     * @return Map<Field, Object>
     * @throws java.lang.Exception
     */
    public Map<Field, Object> getMapValuesByFields(Object object, List<Field> fields) throws Exception {
        Map<Field, Object> values = new HashMap<>();
        for (Field field : fields) {
            Object value = getValueByField(object, field);
            values.put(field, value);
        }
        return values;
    }     
    

    
    /**
     * Retorna uma lista com os nomes de todos os atributos de uma classe, 
     * podendo incluir também suas superclasses.
     * @param classe
     * @param includeSuperclass
     * @return List String
     * @throws java.lang.Exception
     */
    public List<String> getFieldsName(Class classe, boolean includeSuperclass) throws Exception {
        List<String> fieldsStr = new ArrayList();
        List<Field> fieldsLocal;
        if (includeSuperclass) {
            fieldsLocal = getFieldsIncludeSuperClasses(classe);
        } else {
            fieldsLocal = getFields(classe);
        }
        
        for (Field field : fieldsLocal) {
            field.setAccessible(true);
            fieldsStr.add(field.getName());
        }
        return fieldsStr;
    }


    
    /**
     * Retorna uma lista com os nomes de todos os atributos de uma classe, 
     * podendo incluir também suas superclasses.
     * @param classe
     * @param includeSuperclass
     * @return List String
     * @throws java.lang.Exception
     */
    public List<Field> getFields(Class classe, boolean includeSuperclass) throws Exception {
        if (includeSuperclass) {
            return getFieldsIncludeSuperClasses(classe);
        } 
        return getFields(classe);
    }
    
    
    /**
     * Retorna o tipo do atributo, em formato de Canonical Name.
     * @param field
     * @return String
     */
    public String getFieldType(Field field) {
        return field.getType().getCanonicalName();
    }
    
    
    /**
     * Retorna o tipo do atributo, em formato de Canonical Name.
     * @param classe
     * @param name
     * @return String
     * @throws java.lang.Exception
     */
    public String getFieldTypeName(Class classe, String name) throws Exception {
        try {
            Field field = classe.getDeclaredField(name);
            return field.getType().getCanonicalName();
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new Exception("Campo: " + name + " não encontrado, na classe: " + classe.getCanonicalName());
        }
    }   
    
    
    
    /**
     * Retorna os tipos de todos os atributos de uma classe
     * @param classe
     * @return List
     */
    public List<String> getFieldsTypeName(Class classe) {
        List<String> fieldsStr = new ArrayList();
        Field[] fieldsLocal = classe.getDeclaredFields();
        for (Field field : fieldsLocal) {
            field.setAccessible(true);            
            fieldsStr.add(field.getType().getCanonicalName());
        }
        return fieldsStr;
    }    
    
    
    
    /**
     * Retorna todos os métodos que iniciem com algum dos prefixos
     * @param classe
     * @param prefixes
     * @return List
     */
    public List<Method> getMethodsInitName(Class classe, List<String> prefixes) {
        Method[] mts = classe.getDeclaredMethods();
        List<Method> m = new ArrayList();
        for (Method mt : mts) {
            for (String prefix : prefixes) {
                if (mt.getName().startsWith(prefix)) {
                    m.add(mt);
                }
            }                        
        }        
        return m;
    }
    
    
    /**
     * Retorna todos os métodos que contenha uma substring
     * @param classe
     * @param substr
     * @return List
     */
    public List<Method> getMethodsLikeName(Class classe, String substr) {
        Method[] mts = classe.getDeclaredMethods();
        List<Method> m = new ArrayList();
        for (Method mt : mts) {
            if (mt.getName().contains(substr)) {
                m.add(mt);
            }
        }        
        return m;
    }
    
    
    /**
     * Retorna todos os métodos que contenha uma substring
     * @param classe
     * @param substr
     * @return List
     */
    public List<Field> getFieldsLikeName(Class classe, String substr) {
        Field[] mts = classe.getDeclaredFields();
        List<Field> m = new ArrayList();
        for (Field mt : mts) {
            if (mt.getName().contains(substr)) {
                m.add(mt);
            }
        }        
        return m;
    }

    /**
     * Retorna todos os métodos que terminem com um sufixo
     * @param classe
     * @param substr
     * @return List
     */
    public List<Field> getFieldsEndsWith(Class classe, String end) {
        Field[] mts = classe.getDeclaredFields();
        List<Field> m = new ArrayList();
        for (Field mt : mts) {
            if (mt.getName().endsWith(end)) {
                m.add(mt);
            }
        }        
        return m;
    }



    /**
     * Retorna todos os métodos que comecem com um prefixo
     * @param classe
     * @param substr
     * @return List
     */
    public List<Field> getFieldsStartsWith(Class classe, String start) {
        Field[] mts = classe.getDeclaredFields();
        List<Field> m = new ArrayList();
        for (Field mt : mts) {
            if (mt.getName().startsWith(start)) {
                m.add(mt);
            }
        }
        return m;
    }    
    
    







    /**
     * **************************************************************
     *  MÉTODOS PARA SETAR VALORES (SET)
     * **************************************************************
     */

    
    /**
     * Seta valores em um objeto através de um método
     * @param targetObject
     * @param invokeMethod
     * @param values
     * @throws java.lang.Exception
     */
    public void setValuesByMethod(Object targetObject, Method invokeMethod, List values) throws Exception {
        
        try {
            Class  classDefinition = Class.forName(targetObject.getClass().getCanonicalName());
            Method method = classDefinition.getDeclaredMethod(invokeMethod.getName(), toArrayClass(values));
            method.setAccessible(true);
            method.invoke(targetObject, toArrayObject(values));
            
            //System.out.println("setValuesByMethod > Class: " + classDefinition.getName() + " método: " + method.getName());
            
        } catch (SecurityException | ClassNotFoundException | IllegalAccessException | InvocationTargetException ex) {
            throw new Exception("Falha ao setar valores: " + values + " no método: " + invokeMethod + "\n" + ExceptionUtils.getStackTrace(ex));
        
        } catch (NoSuchMethodException ex2) {
            throw new Exception("O método: " + invokeMethod.getName() + " (" + getTypeArraysClass(toArrayClass(values)) + ") " + " não foi encontrado na classe: " + targetObject.getClass().getName());
        
        } catch (IllegalArgumentException ex3) {
            throw new Exception("O método: " + invokeMethod.getName() + " da classe: " + targetObject.getClass().getName() + " espera receber [" + getTypeParametersFromMethod(invokeMethod) + "], porém recebeu [" + getTypeArraysClass(toArrayClass(values)) + "]\n");
        } catch (NullPointerException ex4) {
            
        }
    }
    
    
    private String getTypeParametersFromMethod(Method invokeMethod) {
        List<String> params = new ArrayList();
        for (TypeVariable value : invokeMethod.getTypeParameters()) {
            params.add(value.getName());
        }
        return String.join(", ", params);
    }

    private String getTypeArraysClass(Class... invokeMethod) {
        List<String> params = new ArrayList();
        for (Class value : invokeMethod) {
            params.add(value.getName());
        }
        return String.join(", ", params);
    }

    
    /**
     * Seta valores em uma classe através de um método
     * @param classe
     * @param invokeMethod
     * @param values
     * @throws java.lang.Exception
     */
    public void setValuesByMethod(Class classe, String invokeMethod, List values) throws Exception {
        try {
            Class classDefinition = Class.forName(classe.getCanonicalName());
            Method method = classDefinition.getMethod(invokeMethod);
            method.setAccessible(true);
            method.invoke(classDefinition, values);
        } catch (IllegalArgumentException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | InvocationTargetException ex) {
            throw new Exception("Falha ao setar valores: " + values + " no método: " + invokeMethod + "\n" + ExceptionUtils.getStackTrace(ex));
        } 
    }


    
    public void setValuesByFields(Object obj, List<Field> fields, List values) throws Exception {
        
        Field field = null;
        try {
            Class classDefinition = Class.forName(obj.getClass().getCanonicalName());
            for (int j = 0; j < fields.size(); j++) {
                field = classDefinition.getDeclaredField(fields.get(j).getName());
                field.setAccessible(true);
                if (values == null) {
                    field.set(obj, null);
                } else {
                    field.set(obj, values.get(j));
                }
            }            
        } catch (IllegalArgumentException | ClassNotFoundException | SecurityException | IllegalAccessException | NoSuchFieldException ex) {
            throw new Exception("Falha ao setar os valores: " + values + " no atributo: " + field + "\n" + ExceptionUtils.getStackTrace(ex));
        } 
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



    /**
     * Retorna a instancia de uma classe
     * @return Object
     */
    private Object getInstance(Class classe) throws Exception {
        try {
            return classe.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new Exception("Falha ao instanciar: " + classe.getCanonicalName() + "\n" + ExceptionUtils.getStackTrace(ex));
        }
    } 
  

    private static boolean containId(Field[] fs) {
        for (Field f : fs) {
            if (f.getName().equals("id")) {
                return true;
            }
        }
        return false;
    }


    private static void getFieldsRecursive(Class classe) throws NoSuchFieldException, Exception {
        
        Field[] fs = classe.getDeclaredFields();
        if (containId(fs)) {
            fields.addAll(Arrays.asList(fs));
            return;
        } else {
            if (classe.equals(Object.class)) {
                throw new Exception("A classe de negócio precisa possuir um atributo id");
            }
        }
        getFieldsRecursive(classe.getSuperclass());
        fields.addAll(Arrays.asList(fs));
    }

    
    public List<Field> getFieldsIncludeSuperClasses(Class classe) throws Exception {
        fields = new ArrayList();
        try {
            getFieldsRecursive(classe);
        } catch (NoSuchFieldException ex) {
            throw new Exception("Houve uma falha ao recuperar os campos da classe: " + classe.getName() + " via recursividade.");
        } catch (Exception ex) {
            throw ex;
        }
        return fields;
    }
}
