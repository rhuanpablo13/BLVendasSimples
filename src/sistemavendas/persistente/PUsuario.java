package sistemavendas.persistente;

import infra.abstratas.Persistente;
import infra.comunicacao.DatabaseException;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import infra.comunicacao.StackDebug;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NUsuario;




public class PUsuario extends Persistente<NUsuario> {
    
    
    
    public PUsuario(String table) {
        super(table);
    }
    
    
    public void inserir(NUsuario model) throws Erro {
        //map.print();
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        super.inserir(model, sql);
    }



    public void alterar(NUsuario model, int id) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleUpdate(table, getTableColumns());
        super.alterar(model, id, sql);
    }


    
    public void excluir(int codigo) throws Erro {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleDelete(table);
        super.excluir(codigo, sql);
    }


    public List<NUsuario> getLista() throws Erro {
        PersistenceServices ps = new PersistenceServices();
        return super.getLista(ps.createSimpleSelect(table));
    }


    public NUsuario buscarPorCodigo(int codigo) throws Erro {
        return super.buscarPorCodigo(codigo, "SELECT * FROM " + super.table + " WHERE CODIGO = ?;");
    }

    
    public NUsuario buscarPorId(int id) throws Erro {        
        try {
            String sql = "SELECT ID, CODIGO, ATIVO, ADMINISTRADOR, CPF, USUARIO, SENHA, NOME, EMAIL FROM " + table + " WHERE ID = " + id;
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                NUsuario u = new NUsuario();
                u.setId(rs.getInt(1));
                u.setCodigo(rs.getInt(2));
                u.setAtivo(rs.getBoolean(3));
                u.setAdministrador(rs.getBoolean(4));
                u.setCpf(rs.getString(5));
                u.setUsuario(rs.getString(6));
                u.setSenha(rs.getString(7));
                u.setNome(rs.getString(8));
                u.setEmail(rs.getString(9));
                return u;
            }            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Erro("Usuário não encontrado!" + StackDebug.getLineNumber(this.getClass()), "");
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        return null;
    }
    
    
    
    
    public List<NUsuario> pesquisarPorAtributos(NUsuario usuario) throws Erro {
        
        Map<List<String>, Object> atributos = null;
        try {
            atributos = getAttributesAndValues(usuario, true);
        } catch (PersistenteException ex) {
            Logger.getLogger(PUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSelect(table, atributos);
        System.out.println(sql);
        return super.getLista(sql);
    }

    @Override
    public NUsuario recuperarUltimoRegistro() throws Exception {
        return super.recuperarUltimoRegistro(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public NUsuario recuperarUsuario(String usuario) throws Erro {
        try {
            String sql = "SELECT ID, CODIGO, ATIVO, ADMINISTRADOR, CPF, USUARIO, SENHA, NOME, EMAIL FROM " + table + " WHERE USUARIO = '" + usuario + "'";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                NUsuario u = new NUsuario();
                u.setId(rs.getInt(1));
                u.setCodigo(rs.getInt(2));
                u.setAtivo(rs.getBoolean(3));
                u.setAdministrador(rs.getBoolean(4));
                u.setCpf(rs.getString(5));
                u.setUsuario(rs.getString(6));
                u.setSenha(rs.getString(7));
                u.setNome(rs.getString(8));
                u.setEmail(rs.getString(9));
                return u;
            }            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Erro("Usuário não encontrado!" + StackDebug.getLineNumber(this.getClass()), "");
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        return null;
    }
    
    
    
    public NUsuario recuperaPorEmail(String email) throws Erro {
        try {
            String sql = "SELECT ID, CODIGO, ATIVO, ADMINISTRADOR, CPF, USUARIO, SENHA, NOME, EMAIL FROM " + table + " WHERE EMAIL = '" + email + "'";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                NUsuario u = new NUsuario();
                u.setId(rs.getInt(1));
                u.setCodigo(rs.getInt(2));
                u.setAtivo(rs.getBoolean(3));
                u.setAdministrador(rs.getBoolean(4));
                u.setCpf(rs.getString(5));
                u.setUsuario(rs.getString(6));
                u.setSenha(rs.getString(7));
                u.setNome(rs.getString(8));
                u.setEmail(rs.getString(9));
                return u;
            }            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Erro("Usuário não encontrado!" + StackDebug.getLineNumber(this.getClass()), "");
        } catch (DatabaseException ex) {
            throw new Erro(ex.getMessage() + StackDebug.getLineNumber(this.getClass()), "");
        }
        return null;
    }
    
    public boolean autorizado(NUsuario usuario) throws Erro {
        
        System.out.println(usuario.toString());
        NUsuario u = null;
        try {
            String sql = "SELECT USUARIO, SENHA FROM " + table + " WHERE USUARIO like '" + usuario.getUsuario() + "'";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            u = new NUsuario();
            u.setUsuario(rs.getString(1));
            u.setSenha(rs.getString(2));
        } catch (SQLException ex) {
            throw new Erro("Usuário não encontrado!" + StackDebug.getLineNumber(this.getClass()), "");
        } catch (DatabaseException ex) {
            Logger.getLogger(PUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (u != null) {
            try {
                String senhaCadastrada = u.desencriptarSenha();
                if (senhaCadastrada.equals(usuario.getSenha())) {
                    return true;
                }
            } catch (Erro e) {throw e;}           
        }
        return false;
    }
    
    
    public boolean primeiroAcesso() {
        
        try {
            String sql = "SELECT COUNT(*) FROM " + table;
            if (count(sql) == 0) { // não tem registro
                return true;
            }

            sql = "SELECT COUNT(*) FROM " + table  + " WHERE USUARIO = 'admin' AND SENHA = 'admin'";
        
            if (count(sql) > 0) { // tem registro
                return true;
            }            
        } catch (PersistenteException ex) {
            new Erro("Falha ao verificar primeiro acesso! Contate o suporte", "").show();
        }
        return false;
    }
    
}   