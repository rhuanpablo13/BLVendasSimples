package sistemavendas.negocio;

import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.controle.CProduto;




public class NProduto extends Negocio {

    private Integer codigoBarras; // tamanho: 8, 12, 13, 14
    private Integer idFornecedor;
    private Integer idGrupo;
    private String nome;
    private Double valorCusto;
    private Double valorVenda;
    private Double valorLucro;
    private Double percentLucro;
    private Integer qtdEstoque;
    private Integer qtdEstoqueMin;
    private Integer qtdEstoqueMax;
    private String localizacao;
    private Boolean ativo;
    private String srcImg;
    private Boolean gerarCodigoBarras;
    
    
    // VALORES E TRIBUTOS
    private Integer idUnidadeMedidaComercial; // unidade medida comercial
    
    
    //ISSQN - IMPOSTO SOBRE SERVIÇOS DE QUALQUER NATUREZA
    
    
    /**
    * Construtor
    */
    public NProduto() {
    }

    
    public NProduto(String nome) {
        this.nome = nome;
        this.valorCusto = new Double(0);
        this.valorLucro = new Double(0);
        this.valorVenda = new Double(0);
        this.qtdEstoque = 0;
        this.qtdEstoqueMax = 0;
        this.qtdEstoqueMin = 0;
    }
    
    
    public NProduto(Integer codigo) {
        this.codigo = codigo;
        this.valorCusto = new Double(0);
        this.valorLucro = new Double(0);
        this.valorVenda = new Double(0);
        this.qtdEstoque = 0;
        this.qtdEstoqueMax = 0;
        this.qtdEstoqueMin = 0;
    }


    public NProduto(Integer codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.valorCusto = new Double(0);
        this.valorLucro = new Double(0);
        this.valorVenda = new Double(0);
        this.qtdEstoque = 0;
        this.qtdEstoqueMax = 0;
        this.qtdEstoqueMin = 0;
    }
    
    
    public NProduto(Integer codigo, String nome, Integer idGrupo) {
        this.codigo = codigo;
        this.nome = nome;
        this.idGrupo = idGrupo;
        this.valorCusto = new Double(0);
        this.valorLucro = new Double(0);
        this.valorVenda = new Double(0);
        this.qtdEstoque = 0;
        this.qtdEstoqueMax = 0;
        this.qtdEstoqueMin = 0;
    }
    
    
    public NProduto(Integer id, Integer codigo, String nome, Integer idGrupo) {
        this.codigo = codigo;
        this.nome = nome;
        this.idGrupo = idGrupo;
        this.id = id;
        this.valorCusto = new Double(0);
        this.valorLucro = new Double(0);
        this.valorVenda = new Double(0);
        this.qtdEstoque = 0;
        this.qtdEstoqueMax = 0;
        this.qtdEstoqueMin = 0;
    }    
    
    
    @Override
    public void executarAntesInserir() throws Erro {
        
        
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        if (isEmptyOrNull(codigoBarras)) {
            throw new Erro("Campo Código de Barras é obrigatório");
        }
        
        if (isEmptyOrNull(nome)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        if (isEmptyOrNull(valorVenda) || valorVenda < 0) {
            throw new Erro("Campo Preço de Venda é obrigatório");
        }
        
        if (cadastroRapido) {
            return;
        }
        
        if (isEmptyOrNull(idFornecedor)) {
            throw new Erro("Campo Fornecedor é obrigatório");
        }
        
        if (isEmptyOrNull(idGrupo)) {
            throw new Erro("Campo Grupo é obrigatório");
        }
        
        if (isEmptyOrNull(valorCusto) || valorCusto < 0) {
            throw new Erro("Campo Valor de Custo é obrigatório");
        }
        
        if (isEmptyOrNull(valorLucro) || valorLucro < 0) {
            throw new Erro("Campo Valor de Lucro é obrigatório");
        }
        
        if (isEmptyOrNull(qtdEstoque) || qtdEstoque < 0) {
            throw new Erro("Campo Qtde. em Estoque é obrigatório");
        }
        
        if (qtdEstoque == 0) {
            ativo = false;
        }
        
        if (gerarCodigoBarras) {
            this.codigoBarras = gerarCodigoBarras();
        }
    }

    @Override
    public boolean executarDepoisInserir() {
        
        // registrar os códigos de barra dos produtos
        try {
            CProduto controller = new CProduto();
            NProduto produto = controller.recuperaUltimoRegistro();
            if (produto != null) {
                Integer codigoBarrasTemp = produto.getCodigoBarras();
                for (int i = 0; i < produto.getQtdEstoque(); i++) {
                    boolean atualizou = controller.inserirTabelaCodigoBarra(produto, Integer.toString(codigoBarrasTemp++));
                    if (!atualizou) {
                        return false;
                    }
                }
            }
        } catch (Erro ex) {
            ex.show();
            return false;
        } catch (Exception ex) {            
            return false;
        }
        return true;
    }

    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        if (isEmptyOrNull(nome)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        if (isEmptyOrNull(valorVenda) || valorVenda < 0) {
            throw new Erro("Campo Preço de Venda é obrigatório");
        }
        
        if (isEmptyOrNull(idFornecedor)) {
            throw new Erro("Campo Fornecedor é obrigatório");
        }
        
        if (isEmptyOrNull(idGrupo)) {
            throw new Erro("Campo Grupo é obrigatório");
        }
        
        if (isEmptyOrNull(valorCusto) || valorCusto < 0) {
            throw new Erro("Campo Valor de Custo é obrigatório");
        }
        
        if (isEmptyOrNull(valorLucro) || valorLucro < 0) {
            throw new Erro("Campo Valor de Lucro é obrigatório");
        }
        
        if (isEmptyOrNull(qtdEstoque) || qtdEstoque < 0) {
            throw new Erro("Campo Qtde. em Estoque é obrigatório");
        }
        
        if (qtdEstoque == 0) {
            ativo = false;
        }        
        
    }

    @Override
    public boolean executarDepoisAlterar() {
        try {
            // atualizar a tabela de PRODUTO_CODIGO_BARRAS
            CProduto controller = new CProduto();
            
            Integer count = controller.countCodigoBarras(this);
            if (count != null) {
                
                if (qtdEstoque > count) {
                    Integer cb = gerarCodigoBarras();                
                    for (int i = 0; i < (qtdEstoque-count); i++) {
                        controller.inserirTabelaCodigoBarra(this, cb + i);
                    }
                } else if (qtdEstoque < count) {
                    for (int i = 0; i < (count - qtdEstoque); i++) {
                        Integer ultimoCB = controller.recuperarUltimoCodigoBarras(this);
                        controller.removerCodigoBarraTabela(this, ultimoCB);
                    }
                }
                
            } else {
                Integer cb = gerarCodigoBarras();
                for (int i = 0; i < (qtdEstoque-count); i++) {
                    controller.inserirTabelaCodigoBarra(this, cb + i);
                }
            }
            return true;
        } catch (Erro ex) {
            Logger.getLogger(NProduto.class.getName()).log(Level.SEVERE, null, ex);
            ex.show();
        }
        return false;
    }
    
    
    
    
    public Integer gerarCodigoBarras() {
        CProduto controller = new CProduto();
        Integer codigoBarra = -1;
        try {
            if (this != null && !this.isEmpty()) {
                codigoBarra = controller.recuperarUltimoCodigoBarras(this);
            } else {
                codigoBarra = controller.recuperarUltimoCodigoBarras();
            }
        } catch (Exception ex) {
            Logger.getLogger(NProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (codigoBarra != null) {
            codigoBarra++;
            return codigoBarra;
        }
        return 0;
    } 

    @Override
    public boolean isEmpty() {
        return super.isEmpty(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    @Override
    public NProduto getClone() {
        try {
            return (NProduto) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.codigo);
        hash = 47 * hash + Objects.hashCode(this.idGrupo);
        hash = 47 * hash + Objects.hashCode(this.nome);
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
        final NProduto other = (NProduto) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.idGrupo, other.idGrupo)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }



    
    
    @Override
    public int compareTo(Negocio o) {
        if (getCodigo() < o.getCodigo()) return -1;
        if (getCodigo() > o.getCodigo()) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "NProduto{" + "id= " + id + ", codigo= " + codigo + ", codigoBarras= " + codigoBarras + ", idFornecedor= " + idFornecedor + ", idGrupo= " + idGrupo + ", nome= " + nome + ", valorCusto= " + valorCusto + ", valorVenda= " + valorVenda + ", valorLucro= " + valorLucro + ", percentLucro= " + percentLucro + ", qtdEstoque= " + qtdEstoque + ", qtdEstoqueMin= " + qtdEstoqueMin + ", qtdEstoqueMax= " + qtdEstoqueMax + ", localizacao= " + localizacao + ", ativo= " + ativo + ", srcImg= " + srcImg + "}\n";
    }

    public String toStringReduzido() {
        return "NProduto{" + "id= " + id + ", codigo= " + codigo + ", codigoBarras= " + codigoBarras +", nome= " + nome + ", qtdEstoque= " + qtdEstoque + "}\n";
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
    public void setCodigo(Integer pCodigo){
        this.codigo = pCodigo;
    }
    
    @Override
    public Integer getCodigo(){
        return this.codigo;
    }

    
    /**
    * seta o valorCusto de idFornecedor
     * @param idFornecedor
    */
    public void setIdFornecedor(Integer idFornecedor){
        this.idFornecedor = idFornecedor;
    }
    /**
    * @return idFornecedor
    */
    public Integer getIdFornecedor(){
        return this.idFornecedor;
    }

    /**
    * seta o valorCusto de nome
    * @param pNome
    */
    public void setNome(String pNome){
        this.nome = pNome;
    }
    /**
    * @return nome
    */
    public String getNome(){
        return this.nome;
    }

    /**
    * seta o valorCusto de valorCusto
    * @param pValor
    */
    public void setValorCusto(Double pValor){
        this.valorCusto = pValor;
    }
    /**
    * @return valorCusto
    */
    public Double getValorCusto(){
        return this.valorCusto;
    }

    public Integer getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(Integer codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Double getValorLucro() {
        return valorLucro;
    }

    public void setValorLucro(Double valorLucro) {
        this.valorLucro = valorLucro;
    }

    public Double getPercentLucro() {
        return percentLucro;
    }

    public void setPercentLucro(Double percentLucro) {
        this.percentLucro = percentLucro;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public Integer getQtdEstoqueMin() {
        return qtdEstoqueMin;
    }

    public void setQtdEstoqueMin(Integer qtdEstoqueMin) {
        this.qtdEstoqueMin = qtdEstoqueMin;
    }

    public Integer getQtdEstoqueMax() {
        return qtdEstoqueMax;
    }

    public void setQtdEstoqueMax(Integer qtdEstoqueMax) {
        this.qtdEstoqueMax = qtdEstoqueMax;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public Integer getIdUnidadeMedidaComercial() {
        return idUnidadeMedidaComercial;
    }

    public void setIdUnidadeMedidaComercial(Integer idUnidadeMedidaComercial) {
        this.idUnidadeMedidaComercial = idUnidadeMedidaComercial;
    }

    public Boolean getGerarCodigoBarras() {
        return gerarCodigoBarras;
    }

    public void setGerarCodigoBarras(Boolean gerarCodigoBarras) {
        this.gerarCodigoBarras = gerarCodigoBarras;
    }

    
}