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
public class NMedida extends Negocio {
    
    
    private String descricao;
    private String unidade;
    
    
    @Override
    public void executarAntesInserir() throws Erro {
        if (codigo == null) {
            throw new Erro("Campo código é obrigatório");
        }
        if (descricao.isEmpty() || isInvalid(descricao)) {
            throw new Erro("Campo descrição é obrigatório");
        }
    }

    @Override
    public boolean executarDepoisInserir() {
        return true;
    }

    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
        if (codigo == null) {
            throw new Erro("Campo código é obrigatório");
        }
        if (descricao.isEmpty() || isInvalid(descricao)) {
            throw new Erro("Campo descrição é obrigatório");
        }
    }

    @Override
    public boolean executarDepoisAlterar() {
        return true;
    }

    
    
    @Override
    public NMedida getClone() {
        try {
            return (NMedida) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NMedida.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }    
    
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getUnidade() {
        return unidade;
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
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    @Override
    public String toString() {
        return "id: " + id + " codigo: " + codigo + " descrição: " + descricao + "\n";
    }
        
}
