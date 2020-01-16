package sistemavendas.negocio;


import infra.abstratas.Negocio;
import infra.comunicacao.Erro;
import infra.md5.MD5;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.controle.CPrograma;

/**
*
* @author Rhuan
*/
public class NUsuario extends Negocio {

    private Boolean ativo;
    private Boolean administrador;
    private String nome;
    private String usuario;
    private String senha;
    private String confirmaSenha;
    private String email;
    private String cpf;
    
    
    /**
    * Construtor
    */
    public NUsuario(){}

    
    
    @Override
    public void executarAntesInserir() throws Erro {
        
        
        // Validar primeiro acesso, usuário não pode cadastrar 'admin' como usuario/senha
        if (usuario.equalsIgnoreCase("admin") && senha.equalsIgnoreCase("admin")) {
            throw new Erro("Usuário/Senha não pode ser cadastrado como 'admin'.\nEscolha outro Usuário/Senha");
        }
        
        // Codigo
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        // Nome
        if (isEmptyOrNull(nome)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        // Cpf
        this.cpf = Utils.removeCaracteresEspeciais(cpf);
        if (isEmptyOrNull(cpf) || isInvalid(cpf)) {
            throw new Erro("Campo CPF é obrigatório");
        }
        
        if (! Validador.isCpfValid(cpf) || cpf.isEmpty()) {
            throw new Erro("CPF inválido!");
        }
        
        // Usuário
        if (isEmptyOrNull(usuario)) {
            throw new Erro("Campo Usuário é obrigatório");
        }
        
        // E-mail
        if (isEmptyOrNull(email)) {
            throw new Erro("Campo E-mail é obrigatório");
        }
        
        // Senhas
        if (isEmptyOrNull(senha) || isEmptyOrNull(confirmaSenha)) {
            throw new Erro("Campo Senha é obrigatório");
        }
        
        if (! senha.equalsIgnoreCase(confirmaSenha)) {
            throw new Erro("As senhas são diferentes!");
        }
        
        // Encriptando as senhas
        String senhaEncriptada = encriptarSenha();
        if (senhaEncriptada == null) {
            throw new Erro("Falha ao encriptar a senha do usuário: " + usuario);
        }
        this.senha = senhaEncriptada;        
    }

    @Override
    public boolean executarDepoisInserir() {
        return true;
    }
    
    @Override
    public void executarAntesAlterar(Negocio negocioAntesAlterar) throws Erro {
        
        // Validar primeiro acesso, usuário não pode cadastrar 'admin' como usuario/senha
        if (usuario.equalsIgnoreCase("admin") && senha.equalsIgnoreCase("admin")) {
            throw new Erro("Usuário/Senha não pode ser cadastrado como 'admin'.\nEscolha outro Usuário/Senha");
        }
        
        
        // Codigo
        if (isEmptyOrNull(codigo)) {
            throw new Erro("Campo Código é obrigatório");
        }
        
        // Nome
        if (isEmptyOrNull(nome)) {
            throw new Erro("Campo Nome é obrigatório");
        }
        
        // Cpf
        this.cpf = Utils.removeCaracteresEspeciais(cpf);
        if (isEmptyOrNull(cpf) || isInvalid(cpf)) {
            throw new Erro("Campo CPF é obrigatório");
        }
        
        if (! Validador.isCpfValid(cpf) || cpf.isEmpty()) {
            throw new Erro("CPF inválido!");
        }
        
        // Usuário
        if (isEmptyOrNull(usuario)) {
            throw new Erro("Campo Usuário é obrigatório");
        }
        
        // E-mail
        if (isEmptyOrNull(email)) {
            throw new Erro("Campo E-mail é obrigatório");
        }
        
        // Senhas
        if (isEmptyOrNull(senha) || isEmptyOrNull(confirmaSenha)) {
            throw new Erro("Campo Senha é obrigatório");
        }
        
        if (! senha.equalsIgnoreCase(confirmaSenha)) {
            throw new Erro("As senhas são diferentes!");
        }
        
        // Encriptando as senhas
        String senhaEncriptada = encriptarSenha();
        if (senhaEncriptada == null) {
            throw new Erro("Falha ao encriptar a senha do usuário: " + usuario);
        }
        this.senha = senhaEncriptada;
    }

    @Override
    public boolean executarDepoisAlterar() {
        return true;
    }


    @Override
    public NUsuario getClone() {
        try {
            return (NUsuario) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(NUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }
    
    
    /**
     * Método responsável por encriptar a senha do usuário,
     * para a chave, vai ser utilizado o cpf do usuário.
     * 
     * @return String, null
     */
    private String encriptarSenha() throws Erro {
        
        String senhaAux = this.senha;
        String cpfAux = Utils.removeCaracteresEspeciais(cpf);
        if (cpfAux.isEmpty()) {
            throw new Erro("Erro ao encriptar senha, o campo cpf precisa ser informado");
        }        
        senhaAux = MD5.encrypt(senhaAux, cpfAux);
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
        String cpfAux = Utils.removeCaracteresEspeciais(cpf);
        if (cpfAux.isEmpty()) {
            throw new Erro("Erro ao desencriptar senha, o campo cpf precisa ser informado");
        }
        senhaAux = MD5.decrypt(senhaAux, cpfAux);
        if (senhaAux != null) {
            return senhaAux;
        }
        return null;
    }
    

    public List<NOperacao> operacoesAutorizadas(String nomePrograma) {
        CPrograma controllerPrograma = new CPrograma();
        NPrograma programa = controllerPrograma.pesquisarPrograma(nomePrograma, id);
        if (programa != null) {
            return programa.getOperacoes();
        }
        return null;
    }

    @Override
    public String toString() {
        return "NUsuario{" + "ativo=" + ativo + ", administrador=" + administrador + ", nome=" + nome + ", usuario=" + usuario + ", senha=" + senha + ", confirmaSenha=" + confirmaSenha + ", email=" + email + ", cpf=" + cpf + '}';
    }

    
    
    @Override
    public Integer getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(Integer id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    /**
    * seta o valor de codigo
    * @param pCodigo
    */
    @Override
    public void setCodigo(Integer pCodigo){
        this.codigo = pCodigo;
    }
    /**
    * return pk_codigo
     * @return 
    */
    @Override
    public Integer getCodigo(){
        return this.codigo;
    }

    /**
    * seta o valor de nome
    * @param nome
    */
    public void setNome(String nome){
        this.nome = nome;
    }
    /**
    * return nome
    */
    public String getNome(){
        return this.nome;
    }

    /**
    * seta o valor de usuario
    * @param pLogin
    */
    public void setUsuario(String pLogin){
        this.usuario = pLogin;
    }
    /**
    * return usuario
     * @return 
    */
    public String getUsuario(){
        return this.usuario;
    }

    /**
    * seta o valor de senha
    * @param pSenha
    */
    public void setSenha(String pSenha){
        this.senha = pSenha;
    }
    /**
    * return senha
     * @return 
    */
    public String getSenha(){
        return this.senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public Boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    
    
    
}