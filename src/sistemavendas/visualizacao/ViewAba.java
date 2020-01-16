/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

/**
 *
 * @author rhuan
 */
public enum ViewAba {
    
    CADASTRAR(0, "Cadastrar"),
    ALTERAR(0, "Alterar"),
    PESQUISAR(1, "Pesquisar");
    
    private final String title;
    private final int index;
    
    private ViewAba(int index, String title) {
        this.title = title; 
        this.index = index;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getIndex() {
        return index;
    }
    
}
