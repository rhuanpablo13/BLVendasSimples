package sistemavendas.negocio;

import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.controle.CCarrinhoCompras;
import sistemavendas.controle.CEstoque;
import sistemavendas.controle.CFormaPagamento;
import sistemavendas.controle.CVendas;
import sistemavendas.persistente.PCarrinhoCompras;
import sistemavendas.vendas.NCarrinhoCompras;



public class NVendas extends Negocio {

    private Integer idCliente;
    private Date dataVenda;    
    private Double valorTotal;
    private Double subTotal;  //valor sem aplicar o descontoPercent valorTotal
    private Double totalPago;
    private Float descontoPercent;
    private Float descontoValor;
    //private ArrayList<NProduto> listaProdutos;
    private NCarrinhoCompras carrinhoCompras;
    private NCarrinhoCompras carrinhoComprasAntesAlterar;
    private NFormaPagamento formaPagamento;
    

    /**
    * Construtor
    */
    public NVendas(){}

    
    
    
    
    
    @Override
    public String toString() {
        String stCarrinho = "";
        if (carrinhoCompras != null) {
            stCarrinho = carrinhoCompras.toString();
        }
        
        String stFormaPag = "";
        if (formaPagamento != null) {
            stFormaPag = formaPagamento.toString();
        }
        
        return "NVendas{" + "idCliente=" + idCliente + ", dataVenda=" + dataVenda + ", valorTotal=" + valorTotal + ", subTotal=" + subTotal + ", totalPago=" + totalPago + ", descontoPercent=" + descontoPercent + ", descontoValor=" + descontoValor + ", pagamento=" + stFormaPag + "}\n"
        + "Carrinho: " + stCarrinho;
    }

    
    @Override
    public NVendas getClone() {
        try {
            return (NVendas) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NVendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    
    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
        
        if (isEmptyOrNull(dataVenda)) {
            throw new Erro("Por favor, informe a data de realização da venda!");
        }
        
        if (isEmptyOrNull(idCliente)) {
            throw new Erro("Por favor, selecione um cliente para efetuar a venda!");
        }
        
        if (isEmptyOrNull(totalPago)) {
            throw new Erro("Por favor, preencha o campo [Total Pago] com um valor válido!");
        }
        
        
        /* Validar campos monetários negativos */
        if (totalPago < 0) {
            throw new Erro("O campo [Total Pago] não pode ser negativo");
        }
        
        if (descontoPercent < 0) {
            throw new Erro("O campo [Desconto (%)] não pode ser negativo");
        }
       
        if (descontoValor < 0) {
            throw new Erro("O campo [Desconto (R$)] não pode ser negativo");
        }
        /* Fim Validar campos monetários negativos */
        
        
        if (carrinhoCompras.isEmpty()) {
            throw new Erro("Para realizar uma venda, é necessário selecionar algum produto para venda!");
        }
        
        CCarrinhoCompras controllerCarrinho = new CCarrinhoCompras();        
        try {
            this.carrinhoComprasAntesAlterar = controllerCarrinho.getCarrinhoCompras(this);
        } catch (Exception ex) {
            throw new Erro("Não foi possível recuperar o carrinho de compras da venda: " + codigo);
        }
    }

    
    @Override
    public void executarAntesInserir() throws Erro {
        if (isEmptyOrNull(dataVenda)) {
            throw new Erro("Por favor, informe a data de realização da venda!");
        }
        
        if (isEmptyOrNull(idCliente)) {
            throw new Erro("Por favor, selecione um cliente para efetuar a venda!");
        }
        
        if (isEmptyOrNull(totalPago)) {
            throw new Erro("Por favor, preencha o campo [Total Pago] com um valor válido!");
        }
        
        
        /* Validar campos monetários negativos */
        if (totalPago < 0) {
            throw new Erro("O campo [Total Pago] não pode ser negativo");
        }
        
        if (descontoPercent < 0) {
            throw new Erro("O campo [Desconto (%)] não pode ser negativo");
        }
       
        if (descontoValor < 0) {
            throw new Erro("O campo [Desconto (R$)] não pode ser negativo");
        }
        /* Fim Validar campos monetários negativos */
        
        
        if (carrinhoCompras.isEmpty()) {
            throw new Erro("Para realizar uma venda, é necessário selecionar algum produto para venda!");
        }
        
    }

    
    @Override
    public boolean executarDepoisAlterar() {
        
        try {
            CFormaPagamento pagamento = new CFormaPagamento();
            pagamento.alterar(formaPagamento, id);            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        
        boolean iguais = carrinhoComprasAntesAlterar.equals(carrinhoCompras);
        if (!iguais) {
                
            /* Devolver os produtos para o estoque */
            HashMap<NProduto, Integer> produtosCarrinhoAntigo = carrinhoComprasAntesAlterar.buscarTodosProdutos();
            HashMap<NProduto, Integer> produtosCarrinhoAtual = carrinhoCompras.buscarTodosProdutos();
            HashMap<NProduto, Integer> novosProdutos = new HashMap();
            CEstoque controllerEstoque = new CEstoque();

            /* Percorre o produtosCarrinhoAtual */
            for (Map.Entry<NProduto, Integer> entry : produtosCarrinhoAtual.entrySet()) {
                NProduto produtoAtual = entry.getKey();
                Integer quantidadeAtual = entry.getValue();

                /* Verifica se o carrinho antigo, continha o produto corrente, se sim, verifica
                se a quantidade de antes era maior que a do produto corrente, se sim, devolve pro estoque a diferença */
                if (produtosCarrinhoAntigo.containsKey(produtoAtual)) {
                    Integer quantidadeAntigo = produtosCarrinhoAntigo.get(produtoAtual);
                    if (quantidadeAntigo > quantidadeAtual) {
                        try {
                            controllerEstoque.devolver(produtoAtual, (quantidadeAntigo - quantidadeAtual));
                            novosProdutos.put(produtoAtual, (quantidadeAntigo - quantidadeAtual));
                        } catch (Erro ex) {
                            return false;
                        }
                        
                    } else if (quantidadeAntigo < quantidadeAtual) {
                        /* Se o produto corrente é em maior quantidade, retira a diferença dos produtos
                        antiigos com o corrente */
                        if (quantidadeAntigo < quantidadeAtual) {
                            try {
                                controllerEstoque.retirar(produtoAtual, (quantidadeAtual - quantidadeAntigo));
                                novosProdutos.put(produtoAtual, quantidadeAtual);
                            } catch (Erro ex) {
                                return false;
                            }
                        }                        
                    } else {
                        novosProdutos.put(produtoAtual, quantidadeAtual);
                    }
                    
                } else {
                    try {
                        controllerEstoque.retirar(produtoAtual, quantidadeAtual);
                        novosProdutos.put(produtoAtual, quantidadeAtual);
                    } catch (Erro ex) {
                        return false;
                    }
                }
            }
            
            
            /* Se o usuário removeu produtos do carrinho, devolve */
            if (carrinhoComprasAntesAlterar.temMaisProdutosQue(carrinhoCompras)) {
                NCarrinhoCompras carrinhoTemp = carrinhoComprasAntesAlterar.menos(carrinhoCompras);
                for (Map.Entry<NProduto, Integer> entry : carrinhoTemp.buscarTodosProdutos().entrySet()) {
                    NProduto key = entry.getKey();
                    Integer value = entry.getValue();
                    
                    try {
                        controllerEstoque.devolver(key, value);
                    } catch (Erro ex) {
                        return false;
                    }
                }
            }
            
            
            CCarrinhoCompras controllerCarrinho = new CCarrinhoCompras();
            try {
                controllerCarrinho.atualizarProdutosDoCarrinho(carrinhoCompras, novosProdutos);
            } catch (Erro ex) {
                return false;
            }
        }
        return true;
    }
    
  
    
    @Override
    public boolean executarDepoisInserir() {
        
        CVendas controller = new CVendas();
        NVendas ultimaVenda;
        try {
            ultimaVenda = controller.recuperaUltimoRegistro();
        } catch (Exception ex) {
            return false;
        }
        
        
        try {
            CFormaPagamento pagamento = new CFormaPagamento();
            pagamento.inserir(formaPagamento, ultimaVenda.getId());            
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        
        
        PCarrinhoCompras persistente = new PCarrinhoCompras("CARRINHO");     
        try {
            /* inserir um carrinho de vendas */
            persistente.inserir(ultimaVenda.getId(), carrinhoCompras);
            
            /* inserir os produtos no carrinho */
            persistente.inserirProdutosNoCarrinho(carrinhoCompras.buscarTodosProdutos());
            
        } catch (Exception ex) {
            return false;
        }
        
        
        CEstoque controllerEstoque = new CEstoque();
        try {
            return controllerEstoque.atualizarEstoque(carrinhoCompras.buscarTodosProdutos());
        } catch (Erro ex) {
            return false;
        }
    }

    
    @Override
    public boolean executarDepoisExcluir() {        
        return true;
    }
    
    
    @Override
    public boolean executarAntesExcluir() {
        System.out.println("executarAntesExcluir -> deletando forma pagamento");
        CVendas controller = new CVendas();
        if (!controller.deletarFormaPagamento(this)) {
            new Erro("Não foi possivel excluir a forma de pagamento desta venda!", "").show();
            return false;
        }
        
        // Excluir carrinho de compras
        CCarrinhoCompras carrinhoController = new CCarrinhoCompras();
        try {
            NCarrinhoCompras carrinho = carrinhoController.getCarrinhoCompras(this);
            carrinhoController.excluir(carrinho.getId());
        } catch (Erro ex) {
            ex.show();
            return false;
        } catch (Exception ex2) {
            new Erro("Carrinho de compras não encontrado!" + StackDebug.getLineNumber(this.getClass())).show();
            return false;
        }
        return true;
    }

    
    
    @Override
    public void setCodigo(Integer codigo) {
        super.setCodigo(codigo); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getCodigo() {
        return super.getCodigo(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }
    
        
    
    public void setCarrinhoCompras(NCarrinhoCompras carrinhoCompras) {
        this.carrinhoCompras = carrinhoCompras;
    }

    public NCarrinhoCompras getCarrinhoCompras() throws Erro {
        CCarrinhoCompras controller = new CCarrinhoCompras();
        try {
            return controller.getCarrinhoCompras(this);
        } catch (Exception ex) { }
        return null;
    }

    
    
    
    /**
    * seta o subTotal de idCliente
    * @param clienteCodigo
    */
    public void setIdCliente(Integer clienteCodigo){
        this.idCliente = clienteCodigo;
    }
    /**
    * return idCliente
    */
    public Integer getIdCliente(){
        return this.idCliente;
    }

    /**
    * seta o subTotal de dataVenda
    * @param dataVenda
    */
    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }
    
    /**
    * return dataVenda
    */
    public Date getDataVenda(){
        return this.dataVenda;
    }

    /**
     * @return the valorTotal
     */
    public Double getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the descontoPercent
     */
    public Float getDescontoPercent() {
        return descontoPercent;
    }

    /**
     * @param descontoPercent the descontoPercent to set
     */
    public void setDescontoPercent(Float descontoPercent) {
        this.descontoPercent = descontoPercent;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Float getDescontoValor() {
        return descontoValor;
    }

    public void setDescontoValor(Float descontoValor) {
        this.descontoValor = descontoValor;
    }

    public Double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(Double totalPago) {
        this.totalPago = totalPago;
    }

    public NFormaPagamento getFormaPagamento() throws Erro {
        CFormaPagamento controllerPagamento = new CFormaPagamento();
        try {
            NFormaPagamento pagamento = controllerPagamento.pesquisar(id);
            return pagamento;
        } catch (Erro ex) {
            throw new Erro(ex.getMessage(), "Falha ao buscar parametro Forma Pagamento");
        }
    }

    public void setFormaPagamento(NFormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    
    
}