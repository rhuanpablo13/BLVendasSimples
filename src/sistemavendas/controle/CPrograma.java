/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.ControleSimples;
import java.util.List;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NPrograma;
import sistemavendas.persistente.PPrograma;

/**
 *
 * @author rhuan
 */
public class CPrograma extends ControleSimples<NPrograma>{
    
    private final PPrograma dao = new PPrograma("PROGRAMA");
    
    
    
    public boolean salvar(NPrograma p) {
        if (pesquisar(p.getId(), p.getIdUsuario()) != null) {
            return atualizar(p);
        }
        return dao.save(p);
    }

    
    public boolean atualizar(NPrograma p) {        
        return dao.update(p);
    }
    
    
    public List<NPrograma> getList() {
        return dao.getList();
    }
    
    
    public NPrograma pesquisar(int idPrograma, int idUsuario) {
        return dao.pesquisar(idPrograma, idUsuario);
    }
    
    
    public List<NPrograma> pesquisar(String nome, int idUsuario) {
        return dao.pesquisar(nome, idUsuario);
    }


    public NPrograma pesquisarPrograma(String nome, int idUsuario) {
        return dao.pesquisarPrograma(nome, idUsuario);
    }

    
    public NOperacao getOperacaoPorNome(String nomeOperacao) {
        return dao.getOperacaoPorNome(nomeOperacao);
    }
    
}
