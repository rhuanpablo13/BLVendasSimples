/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.vendas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sistemavendas.negocio.NProduto;

/**
 *
 * Sacola de compras é uma coleção de produtos de apenas um único tipo.
 * Para isso, sempre que se insere um novo produto, verifica-se o código
 * do mesmo. Caso seja um código de grupo distinto dos outros itens da coleção, 
 * é retornado false e o item não é adicionado à coleção.
 * 
 * @author rhuan 
 */


public class Sacola extends ArrayList <NProduto> {
    
    private final Integer id;
    private String descricao;
    private Integer idGrupo;
    private NProduto lastProdutoAdd;
    
    
    public Sacola(int id) {
        this.id = id;
        this.descricao = "";
        lastProdutoAdd = new NProduto();
    }
    
    public Sacola(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        lastProdutoAdd = new NProduto();
    }
    
    
    
    
    /////////////// MÉTODOS PARA ADICIONAR PRODUTOS ////////////////////////
    
    @Override
    public boolean add(NProduto e) {
        if (lastProdutoAdd == null || lastProdutoAdd.getCodigo() == null || lastProdutoAdd.getIdGrupo() == null) {
            lastProdutoAdd = e;
            idGrupo = e.getIdGrupo();
        } else {
            if (!Objects.equals(this.lastProdutoAdd.getIdGrupo(), e.getIdGrupo()) || !Objects.equals(this.lastProdutoAdd.getCodigo(), e.getCodigo())) {
                return false;
            }
        }
        return super.add(e);
    }
    
    
    public boolean add(NProduto e, int quantidade) {
        if (lastProdutoAdd == null || lastProdutoAdd.getCodigo() == null || lastProdutoAdd.getIdGrupo() == null) {
            lastProdutoAdd = e;
            idGrupo = e.getIdGrupo();
        } else {
            if (!Objects.equals(this.lastProdutoAdd.getIdGrupo(), e.getIdGrupo())) {
                return false;
            }
        }
        return super.add(e);
    }    
    
    /////////////// MÉTODO PARA REMOVER PRODUTOS ////////////////////////
    
    
    public boolean removerProdutoPorCodigo(int codigoProduto) {
        return this.removeIf((p -> (p.getCodigo() == codigoProduto)));
    }
    
    public boolean removerProdutoPorId(int idProduto) {        
        for (NProduto p : this) {
            if (p.getId() == idProduto) {
                return this.remove(p);
            }
        }
        return false;
    }
    
    
    
    public NProduto getProduto(Integer idProduto) {
        if (this.isEmpty()) return null;
        for (NProduto p : this) {
            if (Objects.equals(p.getId(), idProduto)) {
                return p;
            }
        }
        return null;
    }
    
    
    
    
    
    
    public List<NProduto> getAll() {
        return super.subList(0, size());
    }
    
    
    public Double somarValorLucro() {
        return size() * lastProdutoAdd.getValorLucro();
    }
    
    public Double somarValorCusto() {
        return size() * lastProdutoAdd.getValorCusto();
    }
    
    public Double somarValorVenda() {
        return size() * lastProdutoAdd.getValorVenda();
    }
    
    public Double somarProdutos() {
        return size() * lastProdutoAdd.getValorVenda();
    }

    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Integer getId() {
        return id;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    
    
    @Override
    public String toString() {
        return "Sacola = " + "id: " + id + ", descricao: " + descricao + " {\n" + toStringProdutos() + "\n}";
    }
    
    private String toStringProdutos() {
        String dados = "";
        for (NProduto nProduto : this) {
            dados +=
            "\tid: "      + nProduto.getId()         + "\n" +
            "\tidGrupo: " + nProduto.getIdGrupo()    + "\n" +
            "\tCódigo Produto: "  + nProduto.getCodigo()     + "\n" +
            "\tNome: "    + nProduto.getNome()       + "\n" +
            "\tVlr.Custo: " + nProduto.getValorCusto() + "\n" +
            "\tVlr.Venda: " + nProduto.getValorVenda() + "\n" +
            "\tQtd.Estoque: " + nProduto.getQtdEstoque() + "\n\n";
        }
        return dados;        
    }
    
}
