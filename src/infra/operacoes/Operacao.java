/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.operacoes;

/**
 *
 * @author rhuan
 */
public enum Operacao {
    
    CADASTRO("Cadastro"),
    PESQUISAR("Pesquisar"),
    ALTERAR("Alterar"),
    EXCLUIR("Excluir");
    
    private String operacao;
    
    private Operacao(String operacao) {
        this.operacao = operacao;
    }
    
    
    /**
     * Retorna o valor da constante
     * @return String
     */
    public String getOperacao() {
        return operacao;
    }
    
}
