package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import java.util.List;
import sistemavendas.email.Email;
import sistemavendas.negocio.NSistema;
import sistemavendas.negocio.NUsuario;
import sistemavendas.persistente.PUsuario;


public class CUsuario extends Controle <NUsuario> {

    
    private final PUsuario usuarioDao = new PUsuario("USUARIO");
    
    
    @Override
    public boolean inserir(NUsuario model) throws Erro {
        usuarioDao.inserir(model);
        return true;
    }


    @Override
    public boolean alterar(NUsuario model) throws Erro {
        usuarioDao.alterar(model, model.getId());
        return true;
    }


    @Override
    public boolean excluir(int id)  throws Erro {
        usuarioDao.excluir(id);
        return true;
    }
    

    @Override
    public List<NUsuario> getLista() throws Erro {
        return usuarioDao.getLista();
    }

        
    @Override
    public NUsuario buscarPorCodigo(int codigo) throws Erro {
        return usuarioDao.buscarPorCodigo(codigo);
    }

    
    @Override
    public NUsuario buscarPorId(int id) throws Erro {
        return usuarioDao.buscarPorId(id); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    public List<NUsuario> pesquisar(NUsuario usuario) throws Erro {
        return usuarioDao.pesquisarPorAtributos(usuario);
    }
    
    public NUsuario recuperaUltimoRegistro() throws Exception {
        return usuarioDao.recuperarUltimoRegistro();
    }
    
    
    public NUsuario recuperaPorEmail(String email) throws Erro {
        return usuarioDao.recuperaPorEmail(email);
    }
    
    
    public boolean autorizado(NUsuario usuario) throws Erro {
        NUsuario usuarioBanco = usuarioDao.recuperarUsuario(usuario.getUsuario());
        
        if (usuarioBanco == null) {
            throw new Erro("Usuário não cadastrado", "Erro");
        }
        String senhaBanco = usuarioBanco.desencriptarSenha();        
        if (senhaBanco == null) return false;        
        return senhaBanco.equals(usuario.getSenha());
    }
    
    
    public boolean recuperarSenha(NUsuario usuario) {
        String emailDestinatario = usuario.getEmail();
        try {
            String senha = usuario.desencriptarSenha();
            Email email = new Email();
            CSistema controllerSis = new CSistema();
            NSistema sistema = controllerSis.buscarConfiguracaoVigente();
            if (sistema == null || sistema.getEmail().isEmpty()) {
                new Erro("Nenhum e-mail foi registrado para este sistema. Acesse o menu [Configuração Sistema] e registre um e-mail válido" + StackDebug.getLineNumber(this.getClass()), "").show();
                return false;
            }
            String emailSistema = sistema.getEmail();
            String senhaSistema = sistema.desencriptarSenha();
            
            return email.enviarEmail(emailSistema, senhaSistema, emailDestinatario, "Recuperação de senha", "Sua senha é: " + senha);
        } catch (Erro ex) {
            ex.show();
        }
        return false;
    }
    
    
    public NUsuario recuperarPorUsuario(String usuario) {
        try {
            return usuarioDao.recuperarUsuario(usuario);
        } catch (Erro ex) {
            ex.show();            
        }
        return null;
    }
    
    
    public boolean primeiroAcesso() {
        return usuarioDao.primeiroAcesso();
    }
    
    
}