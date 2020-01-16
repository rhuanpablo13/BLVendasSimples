package sistemavendas.negocio;

import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NFornecedor extends Negocio {

    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String telefone;
    private String celular;
    private String cnpj;
    private Boolean ativo;
    private String email;
    
    //private Boolean contribuinteICMS
    //private String ncm // nomeclatura comum mercosul
    //private String cest // codigo especificador da substituição tributaria
    //private String cBenef // codigo beneficio fiscal
    //private String inscricaoEstadual; // pode ser isento

    public NFornecedor(){}

    

    
    @Override
    public NFornecedor getClone() {
        try {
            return (NFornecedor) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NFornecedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    
    @Override
    public void executarAntesInserir() throws Erro {
        
        System.out.println("executarAntesInserir");
        System.out.println( toString() );
        
        // Codigo
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        // Nome
        if (isEmptyOrNull(nome)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        // Cnpj
        if (isEmptyOrNull(cnpj)) {
            throw new Erro("Campo CNPJ é obrigatório");
        }
            
        cnpj = Utils.removeCaracteresEspeciais(cnpj);
        if (cnpj.isEmpty() || isInvalid(cnpj)) {
            throw new Erro("Campo CNPJ é obrigatório");
        }

        if (! Validador.isCnpjValid(cnpj) || cnpj.isEmpty()) {
            throw new Erro("CNPJ inválido!");
        }        
        
        if (cadastroRapido) {
            return;
        }
        
        // Cep
        cep = Utils.removeCaracteresEspeciais(cep);
        if (isEmptyOrNull(cep) || isInvalid(cep)) {
            throw new Erro("O campo CEP é obrigatório");
        }
        
        
        // Endereço, cidade, bairro...
        if (!isEmptyOrNull(cep) && !isInvalid(cep)) {
            
            if (endereco.isEmpty() || cidade.isEmpty() || bairro.isEmpty() || uf.isEmpty()) {
                throw new Erro("Os campos de endereço precisam estar preenchidos");
            }
        }

        // Telefone
        telefone = Utils.removeCaracteresEspeciais(this.telefone);
        if (isInvalid(telefone)) {
            throw new Erro("O campo Telefone está incorreto");
        }
        
        // Celular
        celular = Utils.removeCaracteresEspeciais(this.celular);
        if (isInvalid(celular)) {            
            throw new Erro("O campo Celular está incorreto");
        }
        
    }
    
    @Override
    public boolean executarDepoisInserir() {
        return true;
    }

    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
        // Codigo
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        // Nome
        if (isEmptyOrNull(nome)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        // Cnpj
        if (isEmptyOrNull(cnpj)) {
            throw new Erro("Campo CNPJ é obrigatório");
        }
            
        cnpj = Utils.removeCaracteresEspeciais(cnpj);
        if (cnpj.isEmpty() || isInvalid(cnpj)) {
            throw new Erro("Campo CNPJ é obrigatório");
        }

        if (! Validador.isCnpjValid(cnpj) || cnpj.isEmpty()) {
            throw new Erro("CNPJ inválido!");
        }        
        
        // Cep
        cep = Utils.removeCaracteresEspeciais(cep);
        if (isEmptyOrNull(cep) || isInvalid(cep)) {
            throw new Erro("O campo CEP é obrigatório");
        } 
        
        
        // Endereço, cidade, bairro...
        if (!isEmptyOrNull(cep) && !isInvalid(cep)) {
            
            if (endereco.isEmpty() || cidade.isEmpty() || bairro.isEmpty() || uf.isEmpty()) {
                throw new Erro("Os campos de endereço precisam estar preenchidos");
            }
        }
                
        // Telefone
        telefone = Utils.removeCaracteresEspeciais(this.telefone);
        if (isInvalid(telefone)) {
            throw new Erro("O campo Telefone está incorreto");
        }
        
        // Celular
        celular = Utils.removeCaracteresEspeciais(this.celular);
        if (isInvalid(celular)) {            
            throw new Erro("O campo Celular está incorreto");
        }
    }

    @Override
    public boolean executarDepoisAlterar() {
        return true;
    }

    
    @Override
    public Integer getId() {
        return super.id; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(Integer id) {
        super.id = id; //To change body of generated methods, choose Tools | Templates.
    }    
    
    @Override
    public void setCodigo(Integer codigo){
        this.codigo = codigo;
    }

    @Override
    public Integer getCodigo(){
        return this.codigo;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    
    public String getNome(){
        return this.nome;
    }

    public void setEndereco(String endereco){
        this.endereco = endereco;
    }

    public String getEndereco(){
        return this.endereco;
    }

    public void setBairro(String bairro){
        this.bairro = bairro;
    }

    public String getBairro(){
        return this.bairro;
    }

    public void setCidade(String cidade){
        this.cidade = cidade;
    }

    public String getCidade(){
        return this.cidade;
    }

    public void setUf(String uf){
        this.uf = uf;
    }

    public String getUf(){
        return this.uf;
    }

    public void setCep(String cep){
        this.cep = cep;
    }

    public String getCep(){
        return this.cep;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public String getTelefone(){
        return this.telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NFornecedor{" + "id=" + id + ", codigo=" + codigo + ", nome=" + nome + ", endereco=" + endereco + ", bairro=" + bairro + ", cidade=" + cidade + ", uf=" + uf + ", cep=" + cep + ", telefone=" + telefone + ", celular=" + celular + ", cnpj=" + cnpj + ", ativo=" + ativo + ", email=" + email + '}';
    }

    
}