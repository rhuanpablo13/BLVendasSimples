/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.negocio;

import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import infra.md5.MD5;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rhuan
 */
public class NEmpresa extends Negocio {
    
    private String razaoSocial;
    private Boolean produtorRural;
    private String cnpj;
    private String cpf;
    private String nomeFantasia;
    private CRT crt;
    private Integer inscricaoEstadual;
    private Integer inscricaoMunicipal;
    private String cep;
    private String logradouro;
    private String bairro;
    private Integer numero;
    private String complemento;
    private String uf;
    private String ibge;
    private String municipio;
    private String cidade;
    private String certificado;
    private String senha;
    private String email;
    private String telefone;
    private String celular;
    private String logomarca;

    
    
    
    
    @Override
    public Negocio getClone() {
        try {
            return (NEmpresa) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NEmpresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    
    
    @Override
    public void executarAntesInserir() throws Erro {
        
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        if (isEmptyOrNull(razaoSocial)) {
            throw new Erro("Campo Razão Social é obrigatório");
        }
        
        if (produtorRural) {
            // Cpf
            this.cpf = Utils.removeCaracteresEspeciais(cpf);
            if (isEmptyOrNull(cpf) || isInvalid(cpf)) {
                throw new Erro("Campo CPF é obrigatório");
            }

            if (! Validador.isCpfValid(cpf) || cpf.isEmpty()) {
                throw new Erro("CPF inválido!");
            }
            
        } else {
            // cnpj
            this.cnpj = Utils.removeCaracteresEspeciais(cnpj);
            if (isEmptyOrNull(cnpj) || isInvalid(cnpj)) {
                throw new Erro("Campo CNPJ é obrigatório");
            }

            if (! Validador.isCnpjValid(cnpj) || cnpj.isEmpty()) {
                throw new Erro("CNPJ inválido!");
            }
        }
        
        if (isEmptyOrNull(nomeFantasia)) {
            throw new Erro("Campo Nome Fantasia é obrigatório");
        }
        
        if (crt == null) {
            throw new Erro("Campo CRT (Código de Regime Tributário da NF-e) é obrigatório");
        }
        
        cep = Utils.removeCaracteresEspeciais(cep);
        if (isEmptyOrNull(cep)) {
            throw new Erro("Campo CEP é obrigatório");
        }
        
        if (isEmptyOrNull(logradouro)) {
            throw new Erro("Campo Logradouro é obrigatório");
        }
        
        if (isEmptyOrNull(bairro)) {
            throw new Erro("Campo Bairro é obrigatório");
        }
        
        if (isEmptyOrNull(numero)) {
            throw new Erro("Campo Número é obrigatório");
        }
        
        if (isEmptyOrNull(uf)) {
            throw new Erro("Campo UF é obrigatório");
        }
        
        if (isEmptyOrNull(municipio)) {
            throw new Erro("Campo Municipio é obrigatório");
        }
        
        if (isEmptyOrNull(email)) {
            throw new Erro("Campo E-mail é obrigatório");
        }
        
        if (Validador.isEmailValid(email)) {
            throw new Erro("E-mail informado é inválido");
        }
        
                
                
        telefone = Utils.removeCaracteresEspeciais(telefone);
        celular = Utils.removeCaracteresEspeciais(celular);
        
        
        if (!isEmptyOrNull(certificado)) {
            if (isEmptyOrNull(senha)) {
                throw new Erro("O campo [Senha] do certificado digital precisa ser informado");                
            }
            
            // Encriptando as senhas
            String senhaEncriptada = encriptarSenha();
            if (senhaEncriptada == null) {
                throw new Erro("Falha ao encriptar a senha da Empresa: " + razaoSocial);
            }
            this.senha = senhaEncriptada;
        }
        
    }

    
    
    @Override
    public boolean executarDepoisInserir() {
        return true;
    }

    
    
    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
        
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        if (isEmptyOrNull(razaoSocial)) {
            throw new Erro("Campo Razão Social é obrigatório");
        }
        
        if (produtorRural) {
            // Cpf
            this.cpf = Utils.removeCaracteresEspeciais(cpf);
            if (isEmptyOrNull(cpf) || isInvalid(cpf)) {
                throw new Erro("Campo CPF é obrigatório");
            }

            if (! Validador.isCpfValid(cpf) || cpf.isEmpty()) {
                throw new Erro("CPF inválido!");
            }
            
        } else {
            // cnpj
            this.cnpj = Utils.removeCaracteresEspeciais(cnpj);
            if (isEmptyOrNull(cnpj) || isInvalid(cnpj)) {
                throw new Erro("Campo CNPJ é obrigatório");
            }

            if (! Validador.isCnpjValid(cnpj) || cnpj.isEmpty()) {
                throw new Erro("CNPJ inválido!");
            }
        }
        
        if (isEmptyOrNull(nomeFantasia)) {
            throw new Erro("Campo Nome Fantasia é obrigatório");
        }
        
        if (crt == null) {
            throw new Erro("Campo CRT (Código de Regime Tributário da NF-e) é obrigatório");
        }
        
        cep = Utils.removeCaracteresEspeciais(cep);
        if (isEmptyOrNull(cep)) {
            throw new Erro("Campo CEP é obrigatório");
        }
        
        if (isEmptyOrNull(logradouro)) {
            throw new Erro("Campo Logradouro é obrigatório");
        }
        
        if (isEmptyOrNull(bairro)) {
            throw new Erro("Campo Bairro é obrigatório");
        }
        
        if (isEmptyOrNull(numero)) {
            throw new Erro("Campo Número é obrigatório");
        }
        
        if (isEmptyOrNull(uf)) {
            throw new Erro("Campo UF é obrigatório");
        }
        
        if (isEmptyOrNull(municipio)) {
            throw new Erro("Campo Municipio é obrigatório");
        }
        
        if (isEmptyOrNull(email)) {
            throw new Erro("Campo E-mail é obrigatório");
        }
        
        if (Validador.isEmailValid(email)) {
            throw new Erro("E-mail informado é inválido");
        }
        
        
        telefone = Utils.removeCaracteresEspeciais(telefone);
        celular = Utils.removeCaracteresEspeciais(celular);
        
        
        if (!isEmptyOrNull(certificado)) {
            if (isEmptyOrNull(senha)) {
                throw new Erro("O campo [Senha] do certificado digital precisa ser informado");                
            }
            
            // Encriptando as senhas
            String senhaEncriptada = encriptarSenha();
            if (senhaEncriptada == null) {
                throw new Erro("Falha ao encriptar a senha da Empresa: " + razaoSocial);
            }
            this.senha = senhaEncriptada;
        }
    }

    
    
    @Override
    public boolean executarDepoisAlterar() {
        return true;
    }
    
    
    
    
    /**
     * Método responsável por encriptar a senha do usuário,
     * para a chave, vai ser utilizado o cpf do usuário.
     * 
     * @return String, null
     */
    private String encriptarSenha() throws Erro {        
        String senhaAux = this.senha;
        String chave = "";
        if (produtorRural) {
            chave = Utils.removeCaracteresEspeciais(cpf);
        } else {
            chave = Utils.removeCaracteresEspeciais(cnpj);
        }
        
        if (chave.isEmpty()) {
            throw new Erro("Erro ao encriptar senha, o campo cpf/cnpj precisa ser informado");
        }
        senhaAux = MD5.encrypt(senhaAux, chave);
        if (senhaAux != null) {
            return senhaAux;
        }
        return null;
    }
    
    
    
    /**
     * Método responsável por desencriptar a senha do usuário,
     * para a chave, vai ser utilizado o cpf do usuário.
     * @return
     * @throws Erro 
     */
    public String desencriptarSenha() throws Erro {
        String senhaAux = this.senha;
        String chave = "";
        if (produtorRural) {
            chave = Utils.removeCaracteresEspeciais(cpf);
        } else {
            chave = Utils.removeCaracteresEspeciais(cnpj);
        }
        if (chave.isEmpty()) {
            throw new Erro("Erro ao desencriptar senha, o campo cpf/cnpj precisa ser informado");
        }
        senhaAux = MD5.decrypt(senhaAux, chave);
        if (senhaAux != null) {
            return senhaAux;
        }
        return null;
    }

    
    
    
    @Override
    public String toString() {
        return "NEmpresa{" + "razaoSocial=" + razaoSocial + ", produtorRural=" + produtorRural + ", cnpj=" + cnpj + ", cpf=" + cpf + ", nomeFantasia=" + nomeFantasia + ", crt=" + crt + ", inscricaoEstadual=" + inscricaoEstadual + ", inscricaoMunicipal=" + inscricaoMunicipal + ", cep=" + cep + ", logradouro=" + logradouro + ", bairro=" + bairro + ", numero=" + numero + ", complemento=" + complemento + ", uf=" + uf + ", municipio=" + municipio + ", certificado=" + certificado + ", senha=" + senha + ", email=" + email + ", telefone=" + telefone + ", celular=" + celular + ", logomarca=" + logomarca + '}';
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Boolean isProdutorRural() {
        return produtorRural;
    }

    public void setProdutorRural(Boolean produtorRural) {
        this.produtorRural = produtorRural;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public CRT getCrt() {
        return crt;
    }

    public void setCrt(CRT crt) {
        this.crt = crt;
    }
                   
    public Integer getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(Integer inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public Integer getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(Integer inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getLogomarca() {
        return logomarca;
    }

    public void setLogomarca(String logomarca) {
        this.logomarca = logomarca;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getIbge() {
        return ibge;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }


    
    
    
}
