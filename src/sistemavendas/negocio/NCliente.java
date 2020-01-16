package sistemavendas.negocio;

import java.util.Date;
import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NCliente extends Negocio {

    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String telefone;
    private String celular;
    private Boolean ativo;
    private String sexo;
    private String email;
    private String cpf;
    private String rg;
    private Date dtNascimento;
    

    public NCliente(){}

    
    public NCliente(Integer codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }
    
    
    
    
    
    public NCliente(String nome){
        this.nome = nome;
    }
    
    public NCliente(Integer codigo){
        this.codigo = codigo;
    }
    
    
    @Override
    public NCliente getClone() {
        try {
            return (NCliente) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }    
    
    
    @Override
    public void executarAntesInserir() throws Erro {
    
        // Codigo
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        // Nome
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        // Cpf
        cpf = Utils.removeCaracteresEspeciais(cpf);
        if (isEmptyOrNull(cpf) || isInvalid(cpf)) {
            throw new Erro("Campo CPF é obrigatório");
        }
        
        if (! Validador.isCpfValid(cpf) || cpf.isEmpty()) {
            throw new Erro("CPF inválido!");
        }
        
        if (cadastroRapido) {
            return;
        }
        
        // Data de Nascimento
        if (isEmptyOrNull(dtNascimento)) {
            throw new Erro("Data de nascimento é obrigatório 2");
        }
        
        // Cep
        cep = Utils.removeCaracteresEspeciais(cep);
        if (cep.isEmpty() || isInvalid(cep)) {
            throw new Erro("O campo CEP é obrigatório");
        }
        
        // Endereço, cidade, bairro...
        if (! cep.isEmpty() && !isInvalid(cep)) {
            if (endereco.isEmpty() || cidade.isEmpty() || bairro.isEmpty() || uf.isEmpty()) {
                throw new Erro("Os campos de endereço precisam estar preenchidos");
            }
        }
        
        // Celular
        if (!celular.matches("[0-9]+")) {
            celular = Utils.removeCaracteresEspeciais(celular);
        }
        
        // Telefone
        if (!telefone.matches("[0-9]+")) {
            telefone = Utils.removeCaracteresEspeciais(telefone);
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
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        // Cpf
        cpf = Utils.removeCaracteresEspeciais(cpf);
        if (isEmptyOrNull(cpf) || isInvalid(cpf)) {
            throw new Erro("Campo CPF é obrigatório");
        }
        
        if (! Validador.isCpfValid(cpf) || cpf.isEmpty()) {
            throw new Erro("CPF inválido!");
        }
        
        // Data de Nascimento
        if (isEmptyOrNull(dtNascimento)) {
            throw new Erro("Data de nascimento é obrigatório");
        }
        
        // Cep
        cep = Utils.removeCaracteresEspeciais(cep);
        if (cep.isEmpty() || isInvalid(cep)) {
            throw new Erro("O campo CEP é obrigatório");
        }
        
        // Endereço, cidade, bairro...
        if (! cep.isEmpty() && !isInvalid(cep)) {
            if (endereco.isEmpty() || cidade.isEmpty() || bairro.isEmpty() || uf.isEmpty()) {
                throw new Erro("Os campos de endereço precisam estar preenchidos");
            }
        }
        
        // Celular
        if (!celular.matches("[0-9]+")) {
            celular = Utils.removeCaracteresEspeciais(celular);
        }
        
        // Telefone
        if (!telefone.matches("[0-9]+")) {
            telefone = Utils.removeCaracteresEspeciais(telefone);
        }
    }

    @Override
    public boolean executarDepoisAlterar() {
        return true;
    }

    
    @Override
    public String toString() {
        return "NCliente{" + "id=" + getId() + ", codigo=" + codigo + ", nome=" + nome + ", endereco=" + endereco + ", bairro=" + bairro + ", cidade=" + cidade + ", uf=" + uf + ", cep=" + cep + ", telefone=" + telefone + ", celular=" + celular + ", ativo=" + ativo + ", sexo=" + sexo + ", email=" + email + ", cpf=" + cpf + ", dtNascimento=" + dtNascimento + '}';
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
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }     

    public void setNome(String pNome){
        this.nome = pNome;
    }

    public void setEndereco(String pEndereco){
        this.endereco = pEndereco;
    } 
        
    public void setBairro(String pBairro){
        this.bairro = pBairro;
    }
        
    public void setCidade(String pCidade){
        this.cidade = pCidade;
    }

    public void setUf(String pUf){
        this.uf = pUf;
    }
    
    public void setCep(String pCep){
        this.cep = pCep;
    }
    
    public void setTelefone(String pTelefone){
        this.telefone = pTelefone;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }
        
    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }


    
    @Override
    public Integer getCodigo() {
        return codigo;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public String getEndereco(){
        return this.endereco;
    }

    public String getBairro(){
        return this.bairro;
    }
    
    public String getCidade(){
        return this.cidade;
    }
    
    public String getUf(){
        return this.uf;
    }
    
    public String getCep(){
        return this.cep;
    }
    
    public String getTelefone(){
        return this.telefone;
    }

    public String getCelular() {
        return celular;
    }

    public Boolean isAtivo() {
        return ativo;
    }
    
    public String getSexo() {
        return sexo;
    }
    
    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getRg() {
        return rg;
    }
    
    public Date getDtNascimento() {
        return dtNascimento;
    }

 
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NCliente other = (NCliente) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }

    



    


}