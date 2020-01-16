/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.controle;

import infra.abstratas.Controle;
import infra.comunicacao.Erro;
import infra.comunicacao.PersistenteException;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.arquivos.Imagem;
import sistemavendas.configuracoes.SystemConfig;
import sistemavendas.negocio.NProduto;
import sistemavendas.persistente.PProduto;

/**
 *
 * @author rhuan
 */
public class CProduto extends Controle<NProduto>{

    
    private final PProduto produtoDao = new PProduto("PRODUTO");
    
    
    
    
    @Override
    public boolean inserir(NProduto model) throws Erro {
        
        if (model.getSrcImg() != null && !model.getSrcImg().isEmpty()) {
            File destino = new File(SystemConfig.IMAGENS_CLIENTE);
            File origem = new File(model.getSrcImg());
            String retorno = Imagem.save(origem, destino);
            model.setSrcImg(retorno);
        }
        produtoDao.inserir(model);
        return true;
    }


    @Override
    public boolean alterar(NProduto model) throws Erro {
        if (model.getSrcImg() != null && !model.getSrcImg().isEmpty()) {
            File destino = new File(SystemConfig.IMAGENS_CLIENTE);
            File origem = new File(model.getSrcImg());
            String retorno = Imagem.save(origem, destino);
            model.setSrcImg(retorno);
        }
        produtoDao.alterar(model, model.getId());
        return true;
    }


    @Override
    public boolean excluir(int id) throws Erro {
        produtoDao.excluir(id);
        return true;
    }
    
    
    
    public boolean excluir(NProduto model) throws Erro {
        produtoDao.excluir(model);
        return true;
    }
    
    

    @Override
    public List<NProduto> getLista() throws Erro {
       return produtoDao.getLista();
    }

    
    
    @Override
    public NProduto buscarPorCodigo(int codigo) throws Erro {
        return produtoDao.buscarPorCodigo(codigo);
    }
    
    
    @Override
    public NProduto buscarPorId(int id) throws Erro {
        return produtoDao.buscarPorId(id);
    }
    
    
    public List<NProduto> pesquisar(NProduto produto) throws Erro {
        return produtoDao.pesquisarPorAtributos(produto);
    }
    
    
    public List<NProduto> getLista(boolean emEstoque, Integer idGrupo) throws Erro {
        return produtoDao.getLista(idGrupo, emEstoque);
    }
    
    
    public NProduto recuperaUltimoRegistro() throws Exception {
        return produtoDao.recuperarUltimoRegistro();
    }

    
    public boolean inserirTabelaCodigoBarra(NProduto produto, String codigoBarras) throws Erro {
        return produtoDao.inserirTabelaCodigoBarra(produto, codigoBarras);
    }
    
    public boolean inserirTabelaCodigoBarra(NProduto produto, Integer codigoBarras) throws Erro {
        return produtoDao.inserirTabelaCodigoBarra(produto, codigoBarras);
    }

    public boolean removerCodigoBarraTabela(NProduto produto, Integer codigoBarras) throws Erro {
        return produtoDao.removerCodigoBarraTabela(produto, Integer.toString(codigoBarras));
    }

    public Integer recuperarUltimoCodigoBarras(NProduto produto) {
        return produtoDao.recuperarUltimoCodigoBarras(produto);
    }
    
    public Integer recuperarUltimoCodigoBarras() {
        return produtoDao.recuperarUltimoCodigoBarras();
    }
    
    public Integer countCodigoBarras(NProduto produto) {
        return produtoDao.countCodigoBarras(produto);
    }
    
    public boolean deletarCodigosBarras(Integer codigoBarras) throws Erro {
        return produtoDao.deletarCodigosBarras(codigoBarras);
    }
    
    public boolean deletarCodigosBarras(String codigoBarras) throws Erro {
        return produtoDao.deletarCodigosBarras(codigoBarras);
    }
    
    public List<NProduto> getLoteProduto(NProduto produto) throws Erro {
        return produtoDao.getLoteProduto(produto);
    }
    
    public boolean existe(Integer id) {
        try {
            if (produtoDao.count(id) > 0) {
                return true;
            }
        } catch (PersistenteException ex) {
            Logger.getLogger(CProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    
    public Integer count() {
        return produtoDao.count();
    }
    
    
    public Integer totalProdutosEmEstoque() {
        return produtoDao.totalProdutosEmEstoque();
    }


    public Double totalValorVendaEmEstoque() {
        return produtoDao.totalValorVendaEmEstoque();
    }
    
    
    public Integer totalProdutosCadastroRapido() {
        return produtoDao.totalProdutosCadastroRapido();
    }
    
    
    public NProduto maisVendido() {
        return produtoDao.maisVendido();
    }

    
    public NProduto menosVendido() {
        return produtoDao.menosVendido();
    }
    
}
