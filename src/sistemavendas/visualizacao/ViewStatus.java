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
public enum ViewStatus {
    
    INSERIR(1), ALTERAR(2), EXCLUIR(3), PESQUISAR(4);

    private final int status;
    private ViewStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    

    
}
