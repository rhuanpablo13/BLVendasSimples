/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

/**
 *
 * @author rhuan
 */
public class Item {
    
    private Integer id;
    private String descricao;

    public Item() {
    }
    
    public Item(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


}
