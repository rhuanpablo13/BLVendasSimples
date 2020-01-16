/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.negocio;

import infra.abstratas.NegocioSimples;

/**
 *
 * @author rhuan
 */
public class NOperacao extends NegocioSimples {
    
    public final static String INCLUIR = "INCLUIR";
    public final static String ALTERAR = "ALTERAR";
    public final static String EXCLUIR = "EXCLUIR";
    public final static String PESQUISAR = "PESQUISAR";
    public final static String GERAR_RELATORIO = "GERAR_RELATORIO";
    public final static String VISUALIZACAO = "VISUALIZACAO";
    
    private String descricao;

    
    
    
    public NOperacao(String descricao) {
        this.descricao = descricao;
    }

    public NOperacao() {
    }

    
    
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "NOperacao{" + "id=" + id + ", descricao=" + descricao + '}';
    }
    
    
    
}
