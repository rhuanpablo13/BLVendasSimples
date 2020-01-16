/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.negocio;

import infra.abstratas.NegocioSimples;
import java.util.List;

/**
 *
 * @author rhuan
 */
public class NPrograma extends NegocioSimples {
    
    private int idUsuario;
    private String nome;
    private String csTipo;
    private List<NOperacao> operacoes;
    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCsTipo() {
        return csTipo;
    }

    public void setCsTipo(String csTipo) {
        this.csTipo = csTipo;
    }

    public List<NOperacao> getOperacoes() {
        return operacoes;
    }

    public void setOperacoes(List<NOperacao> operacoes) {
        this.operacoes = operacoes;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    
    
    
    @Override
    public String toString() {
        String ops = "";
        if (operacoes != null) {
            ops = operacoes.toString();
        }
        return "NPrograma{" + "idUsuario=" + idUsuario + ", nome=" + nome + ", csTipo=" + csTipo + ", operacoes=" + ops + '}';
    }

    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
