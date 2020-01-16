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
public class NFormaPagamento extends NegocioSimples {
    
    private FormaPagamento formaPagamento;
    private Integer entrada;
    private Integer parcelas;
    private Integer idVenda;

    
    
    
    
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Integer getEntrada() {
        return entrada;
    }

    public void setEntrada(Integer entrada) {
        this.entrada = entrada;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public String getDescricao() {
        return formaPagamento.getFormaPagamento();
    }


    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        this.idVenda = idVenda;
    }

    
    @Override
    public String toString() {
        return "NFormaPagamento{" + "id=" + id + ", descricao=" + formaPagamento + ", entrada=" + entrada + ", parcelas=" + parcelas + ", idVenda=" + idVenda + '}';
    }


    @Override
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
