/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.abstratas;

import infra.comunicacao.Erro;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author RHUAN
 */
public abstract class Negocio implements Comparable<Negocio>, Cloneable {
    
    protected Integer id;
    protected Integer codigo;
    protected boolean cadastroRapido = false;

    
    
    @Override
    public int compareTo(Negocio o) {
        if (getId() < o.getId()) return -1;
        if (getId() > o.getId()) return 1;
        return 0;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Negocio other = (Negocio) obj;
        return Objects.equals(this.id, other.id);
    }

    
    public boolean isEmpty() {
        if (this == null) return true;
        return (id == null || codigo == null);
    }
    
    
    public abstract Negocio getClone();
    
    
    protected boolean isInvalid(String str) {
        return (str.contains("obrigatório") || str.contains("inválid"));
    }
    
    
    public abstract void executarAntesInserir() throws Erro;
    public abstract boolean executarDepoisInserir();
    public abstract void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro;
    public abstract boolean executarDepoisAlterar();
    
    
    public boolean executarAntesExcluir() {
        return true;
    }
    
    
    public boolean executarDepoisExcluir() {
        return true;
    }
    
    
    public void setCadastroRapido(boolean cadastroRapido) {
        this.cadastroRapido = cadastroRapido;
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    
    public boolean isEmptyOrNull(String campo) {
        return (campo == null || campo.isEmpty() || isInvalid(campo));
    }
    
    public boolean isEmptyOrNull(Integer campo) {
        return (campo == null);
    }
    
    public boolean isEmptyOrNull(Double campo) {
        return (campo == null);
    }

    public boolean isEmptyOrNull(BigDecimal campo) {
        return (campo == null);
    }
        
    public boolean isEmptyOrNull(Float campo) {
        return (campo == null);
    }
    
    public boolean isEmptyOrNull(Date campo) {
        return (campo == null);
    } 
}
