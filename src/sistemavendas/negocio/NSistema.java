/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.negocio;

import infra.abstratas.Negocio;
import infra.abstratas.NegocioSimples;
import infra.comunicacao.Erro;
import infra.md5.MD5;
import infra.utilitarios.Utils;

/**
 *
 * @author rhuan
 */
public class NSistema extends NegocioSimples {

    
    private String email;
    private String senha;

    
    

    
    
    @Override
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "NSistema{" + "email=" + email + ", senha=" + senha + '}';
    }
    
    
    
    
    
    /**
     * Método responsável por encriptar a senha do usuário,
     * para a chave, vai ser utilizado o cpf do usuário.
     */
    public void encriptarSenha() throws Erro {
        
        String senhaAux = this.senha;
        String cpfAux = Utils.removeCaracteresEspeciais(email);
        if (cpfAux.isEmpty()) {
            throw new Erro("Erro ao encriptar senha, o campo email precisa ser informado");
        }        
        senhaAux = MD5.encrypt(senhaAux, cpfAux);
        if (senhaAux != null) {
            this.senha = senhaAux;
        }
    }
    
    
    
    /**
     * Método responsável por desencriptar a senha do usuário,
     * para a chave, vai ser utilizado o cpf do usuário.
     * @return
     * @throws Erro 
     */
    public String desencriptarSenha() throws Erro {
        String senhaAux = this.senha;
        String cpfAux = Utils.removeCaracteresEspeciais(email);
        if (cpfAux.isEmpty()) {
            throw new Erro("Erro ao desencriptar senha, o campo email precisa ser informado");
        }
        senhaAux = MD5.decrypt(senhaAux, cpfAux);
        if (senhaAux != null) {
            return senhaAux;
        }
        return null;
    }
    
    
    
    
}
