/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.vendas;

import infra.abstratas.NegocioSimples;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sistemavendas.controle.CProduto;
import sistemavendas.negocio.NProduto;

/**
 * 
 * @author rhuan
 */
public class NCarrinhoCompras extends NegocioSimples {
    
    private String descricao;
    private HashMap<NProduto, Integer> produtos;



    
    public NCarrinhoCompras() {
        this.produtos = new HashMap();
        SimpleDateFormat formate = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat = formate.format(new Date());
        this.descricao = ("carrinho_" + dateFormat);
    }
    
    
    public NCarrinhoCompras(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.produtos = new HashMap();
    }

    
    public NCarrinhoCompras(HashMap<NProduto, Integer> produtos) {
        this.produtos = produtos;
    }
    
    
    
    /////////////// MÉTODOS PARA ADICIONAR PRODUTOS ////////////////////////
    
    /**
     * @param produto
     * @param quantidade
     * @return true para adicionado, false para não adicionado
     */
    public boolean adicionarProduto(NProduto produto, Integer quantidade) throws IllegalArgumentException {
        
        // Se o carrinho estiver vazio ou não tiver o produto passado por parametro
        if (produtos.isEmpty() || !produtos.containsKey(produto)) {
            //System.out.println(produto.toString());
            produtos.put(produto, quantidade);
            return true;
        }
        
        // Se o produto já existe no carrinho
        if (produtos.containsKey(produto)) {
            int quantidadeJaNoCarrinho = produtos.get(produto);
            boolean podeAdicionar = existeQuantidadeSuficienteEmEstoque(produto, quantidade, quantidadeJaNoCarrinho);
            if (podeAdicionar) {
                produtos.put(produto, (quantidade + quantidadeJaNoCarrinho));
            } else {
                throw new IllegalArgumentException("Não existe quantidade em estoque suficiente do produto: " + produto.getCodigo() + " - " + produto.getNome());
            }
        }
        return true;
    }
    
    
    
    /**
     * Verifica se a somatoria da quantidade do produto que já existe no carrinho, com
     * a quantidade que o usuário deseja adicionar, é menor que a quantidade em estoque.
     * @param produto
     * @param quantidade
     * @param quantidadeJaNoCarrinho
     * @return 
     */
    private boolean existeQuantidadeSuficienteEmEstoque(NProduto produto, Integer quantidade, Integer quantidadeJaNoCarrinho) {
        // se tentar adicionar mais produtos que disponível no estoque retorna false
        int soma = quantidade + quantidadeJaNoCarrinho;
        return (soma <= produto.getQtdEstoque());
    }
    
    
    
    /**
     * Verifica se a somatoria da quantidade do produto que já existe no carrinho, com
     * a quantidade que o usuário deseja adicionar, é menor que a quantidade em estoque.
     * @param produto
     * @param quantidade
     * @return 
     */
    public boolean existeQuantidadeSuficienteEmEstoque(NProduto produto, Integer quantidade) {
        // se tentar adicionar mais produtos que disponível no estoque retorna false
        if (produtos.containsKey(produto)) {
            int quantidadeJaNoCarrinho = produtos.get(produto);
            return existeQuantidadeSuficienteEmEstoque(produto, quantidade, quantidadeJaNoCarrinho);
        }
        return false;
    }
    
    
    
    /////////////// MÉTODOS PARA REMOVER PRODUTOS ////////////////////////
    public void removerTodos(NProduto produto) {
        if (produtos.containsKey(produto)) {
            produtos.remove(produto);
        }
    }
    
    public void removerQuantidadeProduto(NProduto produto, Integer quantidade) {
        if (produtos.containsKey(produto)) {
            Integer qtdJaNoCarrinho = produtos.get(produto);
            if (qtdJaNoCarrinho < quantidade) {
                produtos.remove(produto);
            } else {
                produtos.replace(produto, (qtdJaNoCarrinho - quantidade));
            }
        }
    }
    
        
    /////////////// MÉTODOS PARA RECUPERAR PRODUTOS ////////////////////////
    public List<NProduto> buscarProdutos(String nome) {
        List<NProduto> temp = new ArrayList();
        for (Map.Entry<NProduto, Integer> produto : produtos.entrySet()) {
            NProduto key = produto.getKey();
            if (key.getNome().equals(nome)) {
                temp.add(key);
            }
        }
        return temp;
    }
    
    
    public List<NProduto> buscarProduto(Integer codigo) {
        List<NProduto> temp = new ArrayList();
        for (Map.Entry<NProduto, Integer> produto : produtos.entrySet()) {
            NProduto key = produto.getKey();
            if (key.getCodigo().equals(codigo)) {
                temp.add(key);
            }
        }
        return temp;
    }
    
    
    public List<NProduto> buscarProdutoPorId(Integer id) {
        List<NProduto> temp = new ArrayList();
        for (Map.Entry<NProduto, Integer> produto : produtos.entrySet()) {
            NProduto key = produto.getKey();
            if (key.getId().equals(id)) {
                temp.add(key);
            }
        }
        return temp;
    }
    
    
    public List<NProduto> buscarProdutoPorGrupo(Integer idGrupo) {
        List<NProduto> temp = new ArrayList();
        for (Map.Entry<NProduto, Integer> produto : produtos.entrySet()) {
            NProduto key = produto.getKey();
            if (key.getIdGrupo().equals(idGrupo)) {
                temp.add(key);
            }
        }
        return temp;
    }
    

    public List<NProduto> buscarProdutoPorFornecedor(Integer idFornecedor) {
        List<NProduto> temp = new ArrayList();
        for (Map.Entry<NProduto, Integer> produto : produtos.entrySet()) {
            NProduto key = produto.getKey();
            if (key.getIdFornecedor().equals(idFornecedor)) {
                temp.add(key);
            }
        }
        return temp;
    }
    
    
    public HashMap<NProduto, Integer> buscarTodosProdutos() {        
        return produtos;
    }

    
    public List<NProduto> buscarListaProdutos() {
        List<NProduto> ps = new ArrayList();
        for (Map.Entry<NProduto, Integer> entry : produtos.entrySet()) {
            ps.add(entry.getKey());
        }
        return ps;
    }

    
    public int count() {
        return produtos.size();
    }
    
    
    
    ////////// MÉTODOS PARA ALTERAÇÕES DE PRODUTOS NO CARRINHO /////////////////
    public void alteraQuantidade(NProduto produto, Integer novaQuantidade) throws IllegalArgumentException {
        if (produtos.containsKey(produto)) {
            if (produto.getQtdEstoque() >= novaQuantidade) {
                produtos.replace(produto, novaQuantidade);
            } else {
                throw new IllegalArgumentException("Não existe quantidade em estoque suficiente do produto: " + produto.getCodigo() + " - " + produto.getNome());
            }
        }
    }
    
    
    
    
    /////////////////////// MÉTODOS DE PROCESSAMENTO ///////////////////////////
    /**
     * Calcula o valor total do carrinho de compras
     * @return Double
     */
    public Double getTotal() {
        Double soma = (double) 0;
        for (Map.Entry<NProduto, Integer> entrySet : produtos.entrySet()) {
            NProduto produto   = entrySet.getKey();
            Integer quantidade = entrySet.getValue();
            soma += calculaTotalPorProduto(produto, quantidade);            
        }
        return soma;
    }
    
    private Double calculaTotalPorProduto(NProduto produto, Integer quantidade) {
        return produto.getValorVenda() * quantidade;
    }
    
    
    
    /**
     * Aplica desconto sobre o valor do total do carrinho de compras,
     * podendo aplicar um desconto em valor monetário, ou porcentagem
     * 
     * @param valorDesconto
     * @param tipo
     * @return Double | Valor total do carrinho subtraido do desconto aplicado
     * @throws Exception 
     */
    public Double aplicarDesconto(Double valorDesconto, TipoDesconto tipo) throws Exception {
        Double total = getTotal();
        switch (tipo) {
            case DINHEIRO:
                return total - valorDesconto;
            case PORCENTAGEM:
                return (total - (total * valorDesconto) / 100);
        }
        throw new Exception("Não foi possível aplicar desconto!");
    }

    
    /**
     * Aplica desconto sobre o valor do total do carrinho de compras,
     * podendo aplicar um desconto em valor monetário, ou porcentagem
     * 
     * @param valorTotal
     * @param valorDesconto
     * @param tipo
     * @return Double | Valor total do carrinho subtraido do desconto aplicado
     * @throws Exception 
     */
    public Double aplicarDesconto(Double valorTotal, Double valorDesconto, TipoDesconto tipo) throws Exception {
        switch (tipo) {
            case DINHEIRO:
                return valorTotal - valorDesconto;
            case PORCENTAGEM:
                return (valorTotal - (valorTotal * valorDesconto) / 100);
        }
        throw new Exception("Não foi possível aplicar desconto!");
    }

    public Double aplicarDesconto(Double valorTotal, Float valorDesconto, TipoDesconto tipo) throws Exception {
        switch (tipo) {
            case DINHEIRO:
                return valorTotal - valorDesconto;
            case PORCENTAGEM:
                return (valorTotal - (valorTotal * valorDesconto) / 100);
        }
        throw new Exception("Não foi possível aplicar desconto!");
    }
        
    public Double aplicarDesconto(BigDecimal valorTotal, Double valorDesconto, TipoDesconto tipo) throws Exception {
        switch (tipo) {
            case DINHEIRO:
                return valorTotal.doubleValue() - valorDesconto;
            case PORCENTAGEM:
                return (valorTotal.doubleValue() - (valorTotal.doubleValue() * valorDesconto) / 100);
        }
        throw new Exception("Não foi possível aplicar desconto!");
    }    
    
    
    @Override
    public String toString() {
        return "CarrinhoCompras = " + "id: " + id + ", descricao: " + descricao + ", produtos: \n" + produtos + "\n";
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
        
        final NCarrinhoCompras other = (NCarrinhoCompras) obj;
        if (!Objects.equals(other.getId(), this.id)) return false;
        if (!Objects.equals(other.getTotal(), this.getTotal())) return false;
        if (!Objects.equals(other.count(), this.count())) return false;
        
        for (Map.Entry<NProduto, Integer> entry : other.produtos.entrySet()) {
            NProduto produtoother = entry.getKey();
            Integer quantidadeother = entry.getValue();
            
            /* Verifica se neste carrinho contem o produto do outro carrinho*/
            if (this.produtos.containsKey(produtoother)) {
                Integer quantidadeLocal = this.produtos.get(produtoother);
                
                /* Verifica se houve alteração na quantidade de produtos deste carrinho */
                if (!Objects.equals(quantidadeLocal, quantidadeother)) return false;
            } else {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Subtrai este carrinho pelo passado por parametro
     * @param carrinho
     * @return 
     */
    public NCarrinhoCompras menos(NCarrinhoCompras carrinho) {
        HashMap<NProduto, Integer> produtosRetorno = new HashMap();
        for (Map.Entry<NProduto, Integer> entry : this.produtos.entrySet()) {
            NProduto produto = entry.getKey();
            Integer quantidade = entry.getValue();
            
            if (!carrinho.produtos.containsKey(produto)) {
                Integer quantidadeAntes = 
                produtosRetorno.put(produto, (quantidade));
            }
        }
        return new NCarrinhoCompras(produtosRetorno);
    }
    
    
    
    public boolean temMaisProdutosQue(NCarrinhoCompras carrinho) {
        if (this.count() > carrinho.count()) return true;
        if (this.count() < carrinho.count()) return false;
        
        /* Compra a quantidade */
        return this.quatidadeItens() > carrinho.quatidadeItens();
    }
    
    
    public NCarrinhoCompras recuperarDiferencaEntreCarrinhos(NCarrinhoCompras carrinhoAntigo) {
        
        boolean include = false; // incluir em [produtosRetorno] os produtos excedentes do carrinho
        HashMap<NProduto, Integer> produtosRetorno = new HashMap();
        
        HashMap<NProduto, Integer> produtosA;
        HashMap<NProduto, Integer> produtosB;
        
        if (this.count() > carrinhoAntigo.count()) {
            produtosA = this.buscarTodosProdutos();
            produtosB = carrinhoAntigo.buscarTodosProdutos();
            include = true;
            
        } else if (this.count() < carrinhoAntigo.count()){
            produtosA = carrinhoAntigo.buscarTodosProdutos();
            produtosB = this.buscarTodosProdutos();
        } else {
            produtosA = this.buscarTodosProdutos();
            produtosB = carrinhoAntigo.buscarTodosProdutos();
        }
        
        
        for (Map.Entry<NProduto, Integer> entry : produtosA.entrySet()) {
            NProduto produtoA = entry.getKey();
            Integer quantidadeA = entry.getValue();
            
            /* Verifica se neste carrinho produtosLocal contem o produto do outro carrinho */
            if (produtosB.containsKey(produtoA)) {
                Integer quantidadeB = produtosB.get(produtoA);
                
                /* Verifica se houve alteração na quantidade de produtos deste carrinho */
                if (!Objects.equals(quantidadeB, quantidadeA)) {
                    
                    /* Pega a diferença entre a quantidade, para atualizar o estoque corretamente*/
                    if (quantidadeB > quantidadeA) {
                        produtosRetorno.put(produtoA, (quantidadeB - quantidadeA));
                    } else {                        
                        produtosRetorno.put(produtoA, quantidadeA);
                        CProduto controllerProduto = new CProduto();
                        
                    }
                }
                
            } else {
                if (include) {
                    produtosRetorno.put(produtoA, quantidadeA);
                }
            }
        }
        return new NCarrinhoCompras(produtosRetorno);
    }
    
    
    
    
    
    
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    
    public boolean isEmpty() {
        return produtos.isEmpty();
    }
    
    public int quatidadeItens() {
        int soma = 0;
        for (int qtd : produtos.values()) {
            soma += qtd;
        }
        return soma;
    }
    
    public int quatidadeItens(NProduto produto) {
        return produtos.containsKey(produto) ? produtos.get(produto) : -1;
    }
    
}
