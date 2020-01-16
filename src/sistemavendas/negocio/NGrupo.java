/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.negocio;

import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rhuan
 */
public class NGrupo extends Negocio {

    private String descricao;

    public NGrupo() {
    }

    public NGrupo(String descricao) {
        this.descricao = descricao;
    }

    
    
    
    
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public Integer getCodigo() {
        return super.getCodigo(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCodigo(Integer codigo) {
        super.setCodigo(codigo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    @Override
    public NGrupo getClone() {
        try {
            return (NGrupo) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NGrupo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }    
    
    @Override
    public void executarAntesInserir() throws Erro {
    }

    @Override
    public boolean executarDepoisInserir() {
        return true;
    }

    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
    }

    @Override
    public boolean executarDepoisAlterar() {
        return true;
    }

    @Override
    public String toString() {
        return "NGrupo{" + "código = " + codigo + ", descricao = " + descricao + '}';
    }
    
    
    
}
