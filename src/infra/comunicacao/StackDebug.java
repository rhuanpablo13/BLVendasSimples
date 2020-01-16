/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.comunicacao;

/**
 *
 * @author rhuan
 */
public class StackDebug {
    
    
    public static String getLineNumber(Class point) {
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        return "\n[" + lineNumber + " : " + point.getCanonicalName() + "]\n";
    }
        
}
