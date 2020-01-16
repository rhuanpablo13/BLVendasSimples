/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import infra.abstratas.Negocio;
import java.util.Arrays;
import java.util.List;
import sistemavendas.negocio.NCliente;

/**
 *
 * @author rhuan
 */
public class Map {

    
    private List arDados;
    
    
    public static void main(String[] args) {
        
        Map map = new Map();
        map.arDados = Arrays.asList("nome", new NCliente());
        
        
        
        
    }
    
}
