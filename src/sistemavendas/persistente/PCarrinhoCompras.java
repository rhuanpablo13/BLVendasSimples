/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.persistente;

import infra.comunicacao.DatabaseException;
import infra.comunicacao.PersistenteException;
import infra.comunicacao.StackDebug;
import infra.utilitarios.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NProduto;
import sistemavendas.negocio.NVendas;
import sistemavendas.vendas.NCarrinhoCompras;

/**
 *
 * @author rhuan
 */
public class PCarrinhoCompras extends PersistenteSimples<NCarrinhoCompras> {
    
    
    public PCarrinhoCompras(String table) {
        super(table);
    }
    
    
    public void inserir(int idVenda, NCarrinhoCompras carrinho) throws Exception {
        PersistenceServices ps = new PersistenceServices();
        String sql = ps.createSimpleInsert(table, getTableColumns());
        List<Object> values = model2Banco(carrinho, true);
        values.add(idVenda);
        super.inserir(values, sql);
    }
    
    
    
    public void atualizarProdutosDoCarrinho(NCarrinhoCompras carrinho, Map<NProduto, Integer> produtos) throws Exception, SQLException {
        
        List<String> idsProdutos = new ArrayList();
        if (!produtos.isEmpty()) {
            
            for (Map.Entry<NProduto, Integer> entry : produtos.entrySet()) {
                NProduto produto = entry.getKey();
                Integer quantidade = entry.getValue();
                idsProdutos.add(produto.getId().toString());
                
                String sqlCount = "SELECT COUNT(*) FROM CARRINHO_PRODUTO WHERE ID_PRODUTO = " + produto.getId() + " AND ID_CARRINHO = " + carrinho.getId();
                int count = count(sqlCount);
                if (count > 0) {
                    String sql = "UPDATE CARRINHO_PRODUTO SET QUANTIDADE = " + quantidade  + " WHERE ID_CARRINHO = " + carrinho.getId() + " AND ID_PRODUTO = " + produto.getId();
                    System.out.println("Atualizando a tabela carrinho_produto, " + sql);
                    try {
                        super.atualizarTabelaRelacionamento(sql);
                        commit();
                    } catch (DatabaseException e) {
                        try {
                            rollbackAndClose();
                        } catch (DatabaseException e2) {
                            throw e2;
                        }
                        throw e;
                    }
                    
                } else {
                    /* Se o produto não existe, insere */
                    String sql = "INSERT INTO CARRINHO_PRODUTO (ID_PRODUTO, QUANTIDADE, ID_CARRINHO) VALUES (" + produto.getId() +", "+ quantidade +", "+ carrinho.getId() + ")";
                    System.out.println("Produto " + produto.getNome() + " não existe na tabela carrinho_produto, " + sql);
                    save(sql);
                    commit();
                }
            }
            
            /* Se o usuário tiver retirado algum produto do carrinho */
            try {
                deletaProdutosNoCarrinho(idsProdutos, carrinho.getId());                
            } catch (PersistenteException e) {
                try {
                    rollbackAndClose();
                } catch (DatabaseException e2) {
                    throw e2;
                }
                throw e;
            }
        }
        System.out.println("");
    }
    
    
    private void deletaProdutosNoCarrinho(List<String> idsProdutos, int idCarrinho) throws PersistenteException {
        
        /* se na tabela CARRINHO_PRODUTO existe mais produtos do que o recebido por parametro, excluir os excedentes */
        String sqlCount = "SELECT COUNT(*) FROM CARRINHO_PRODUTO WHERE ID_PRODUTO NOT IN (" + String.join(", ", idsProdutos) + ") AND ID_CARRINHO = " + idCarrinho;
        System.out.println(sqlCount);
        int count = count(sqlCount);
        if (count > 0) {
            sqlCount = "SELECT ID_PRODUTO FROM CARRINHO_PRODUTO WHERE ID_PRODUTO NOT IN (" + String.join(", ", idsProdutos) + ") AND ID_CARRINHO = " + idCarrinho;
            List<Integer> ids = recuperaArrayId(sqlCount);
            for (Integer id : ids) {
                String sqlDelete = "DELETE FROM CARRINHO_PRODUTO WHERE ID_PRODUTO = " + id + " AND ID_CARRINHO = " + idCarrinho;
                System.out.println("ID_PRODUTO " + id + " será excluido da tabela carrinho_produto, " + sqlDelete);
                try {
                    update(sqlDelete);
                    commit();
                } catch(DatabaseException e) {
                    throw new PersistenteException(table, e);
                }
            }
        }
        System.out.println("");
    }
    
    

    public void inserirProdutosNoCarrinho(Map<NProduto, Integer> produtos) throws DatabaseException {

        String sql = "INSERT INTO CARRINHO_PRODUTO (ID_PRODUTO, ID_CARRINHO, QUANTIDADE) VALUES (?,?,?)";
        NCarrinhoCompras carrinho = null;
        try {
            carrinho = (NCarrinhoCompras) super.recuperarUltimoRegistro(NCarrinhoCompras.class);
        } catch (Exception ex) {
            Logger.getLogger(PCarrinhoCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!produtos.isEmpty()) {
            for (Map.Entry<NProduto, Integer> entry : produtos.entrySet()) {
                NProduto produto = entry.getKey();
                Integer quantidade = entry.getValue();
                
                System.out.println("inserindo: " + produto.toStringReduzido() + " - carrinho: " + carrinho.getId() + " - quantidade: " + quantidade);
                
                try {
                    super.inserirTabelaRelacionamento(sql, produto.getId(), carrinho.getId(), quantidade);
                } catch (DatabaseException ex) {
                    try {
                        rollbackAndClose();
                    } catch (Exception e) {
                        throw e;
                    }
                    throw ex;
                }
            }
        }
        commit();
        System.out.println("");
    }
    
    
    public List<NCarrinhoCompras> getList(String sql) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<NCarrinhoCompras> carrinhos = banco2ListModel(NCarrinhoCompras.class, rs, table);
            return carrinhos;
        } catch (DatabaseException ex) {
            Logger.getLogger(PCarrinhoCompras.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PCarrinhoCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } 
    
    
    public List<NCarrinhoCompras> getList() {
        String sql = new PersistenceServices().createSimpleSelect(table);        
        return getList(sql);
    }
    
    
    public List<NCarrinhoCompras> getList(NCliente cliente) {
        String sql = "SELECT * FROM " + table + "C, VENDA V"
                + "WHERE V.ID = C.ID_VENDA "
                + "AND V.ID_CLIENTE = " + cliente.getId();

        return getList(sql);
    }
    
    
    public List<NCarrinhoCompras> getList(NCliente cliente, Date dataCompra) {
        String sql = "SELECT * FROM " + table + " C, VENDA V"
                + " WHERE V.ID = C.ID_VENDA "
                + " AND V.ID_CLIENTE = " + cliente.getId()
                + " AND V.DATA_VENDA = '" + dateJava2DateMysql(dataCompra) + "'";

        return getList(sql);
    }
    
    
    public NCarrinhoCompras getCarrinhoCompras(NVendas venda) {
        try {
            String sql = "SELECT * FROM CARRINHO C " +
            "INNER JOIN VENDA V ON C.ID_VENDA = V.ID " +
            "AND V.ID = " + venda.getId();
            System.out.println(sql);
            
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            NCarrinhoCompras carrinho = (NCarrinhoCompras) banco2Model(NCarrinhoCompras.class, rs, table);
            if (carrinho == null) {
                throw new Exception(
                        "Nenhum carrinho de compras cadastrado para a venda: " + 
                        venda.getCodigo() + " para a Data de Compra: " + Utils.date2View(venda.getDataVenda())
                );
            }
            Map<NProduto, Integer> produtos = carregarListaProdutos(carrinho);
            carrinho = adicionarProdutoAoCarrinho(produtos, carrinho);
            return carrinho;
        } catch (Exception ex) {
            Logger.getLogger(PCarrinhoCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    private NCarrinhoCompras adicionarProdutoAoCarrinho(Map<NProduto, Integer> produtos, NCarrinhoCompras carrinho) {
        for (Map.Entry<NProduto, Integer> entry : produtos.entrySet()) {
            NProduto produto = entry.getKey();
            Integer quantidade = entry.getValue();
            carrinho.adicionarProduto(produto, quantidade);            
        }
        return carrinho;
    }
    
    
    private Map<NProduto, Integer> carregarListaProdutos(NCarrinhoCompras carrinho) {
        Map<NProduto, Integer> produtos = new HashMap();
        String sql = "SELECT * FROM PRODUTO P "
                + "INNER JOIN CARRINHO_PRODUTO CP "
                + "ON CP.ID_PRODUTO = P.ID "
                + "WHERE CP.ID_CARRINHO = " + carrinho.getId();
        System.out.println(sql);
        
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NProduto produto = new NProduto();
                produto = (NProduto) banco2Model(produto, rs, "PRODUTO");
                Integer quantidade = rs.getInt("QUANTIDADE");
                produtos.put(produto, quantidade);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PCarrinhoCompras.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PCarrinhoCompras.class.getName()).log(Level.SEVERE, null, ex);
        }
        return produtos;
    }
            
    
    
    public void excluir(int id) throws DatabaseException {
        try {
            System.out.println("DELETE FROM CARRINHO_PRODUTO WHERE ID_CARRINHO = " + id);
            delete("DELETE FROM CARRINHO_PRODUTO WHERE ID_CARRINHO = " + id);
            commit();
        } catch (DatabaseException e1) {
            throw new DatabaseException("Falha ao excluir registros da tabela PRODUTO_CARRINHO" + StackDebug.getLineNumber(this.getClass()), "");
        }
        
        try {
            System.out.println("DELETE FROM CARRINHO WHERE ID = " + id);
            delete("DELETE FROM CARRINHO WHERE ID = " + id);
            commit();
        } catch (DatabaseException e1) {
            throw new DatabaseException("Falha ao excluir carrinho de compras" + StackDebug.getLineNumber(this.getClass()), "");
        }
    }
    
    
    
    @Override
    public void closeConnection() throws DatabaseException {
        super.closeConnection();
    }
}
