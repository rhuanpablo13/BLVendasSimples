package sistemavendas.persistente;




public class PProdutos  {

    
//    private Utils u = new Utils();
//    private Message message = new Message();
    
    
//    @Override
//    public boolean inserir(NProduto model) throws PException {
//        
//        String sql = "INSERT INTO PRODUTO ("
//                + montaColunasQuery()
//                + ") VALUES (" ;
//                sql += model2Banco(model);
//                sql += ");";
//                
//        System.out.println("Inserir: \n" + sql);        
//        
//        try {
//            this.getConnection();
//            this.insertSQL(
//                    sql
//            );
//        }catch(Exception e){
//            message.err("Verifique se a query está correta para inserir ", "Erro ao executar Query");
//            if (! this.rollback()) {
//                System.out.println("Falha ao executar rollback");
//                return false;
//            }
//            return false;
//        }finally{
//            this.closeConnection();
//        }
//        return true;
//    }
//
//    @Override
//    public NProduto buscarPorCodigo(int codigo) throws PException {
//        NProduto modelProdutos = null;
//        String sql =  "SELECT "
//                        + montaColunasQuery()
//                +  " FROM "
//                     + " produto"
//                 + " WHERE"
//                     + " codigo = '" + codigo + "'"
//                + ";";
//        
//        System.out.println("Buscar por codigo: \n" + sql);
//        try {
//            this.getConnection();
//            this.executarSQL(
//                sql
//            );
//
//            modelProdutos = new NProduto();
//            while(this.getResultSet().next()){
//                modelProdutos = banco2Model();
//            }
//        }catch(Exception e){
//            message.err("Verifique se a query está correta para buscarPorCodigo ", "Erro ao executar Query");
//            e.printStackTrace();
//        }finally{
//            this.closeConnection();
//        }
//        return modelProdutos;
//    }
//
//    
//    
//
//    public NProduto buscarPorNome(String pNome) throws PException{
//        NProduto modelProdutos = new NProduto();
//        String sql =  "SELECT " +
//                          montaColunasQuery()
//                    + " FROM"
//                         + " produto"
//                    + " WHERE"
//                         + " nome = '" + pNome + "'"
//                    + ";";
//        
//        System.out.println("Buscar por nome: \n" + sql);
//        try {
//            this.getConnection();
//            this.executarSQL(
//                sql
//            );
//
//            while(this.getResultSet().next()){
//                modelProdutos = banco2Model();
//            }
//        }catch(Exception e){
//            message.err("Verifique se a query está correta para buscar por nome ", "Erro ao executar Query");
//            e.printStackTrace();
//        }finally{
//            this.closeConnection();
//        }
//        return modelProdutos;
//    }
//
//    @Override
//    public List<NProduto> getLista() throws PException {
//        ArrayList<NProduto> listamodelProdutos = new ArrayList();
//        NProduto produto = new NProduto();
//        String sql = "SELECT " +
//                    montaColunasQuery()
//                 + " FROM"
//                 + "    produto"
//                + ";";
//        
//        System.out.println("getLista: \n" + sql);
//        try {
//            this.getConnection();
//            this.executarSQL(
//                 sql
//            );
//
//            while(this.getResultSet().next()){
//                produto = banco2Model();
//                listamodelProdutos.add(produto);
//            }
//        }catch(Exception e){
//            message.err("Verifique se a query está correta para getLista ", "Erro ao executar Query");
//            e.printStackTrace();
//        }finally{
//            this.closeConnection();
//        }
//        return listamodelProdutos;
//    }
//
//
//    
//    @Override
//    public boolean alterar(NProduto model) throws PException {
//        String sql = "UPDATE produto SET "
//                        + model2Banco(model)
//                    + " WHERE "
//                        + "id = '" + model.getCodigo() + "'"
//                    + ";";
//        
//        System.out.println("alterar: \n" + sql);            
//        try {
//            this.getConnection();
//            this.executarUpdateDeleteSQL(
//                sql
//            );
//        }catch(Exception e){
//            message.err("Verifique se a query está correta para alterar ", "Erro ao executar Query");
//            if (! this.rollback()) {
//                System.out.println("Falha ao executar rollback");
//                return false;
//            }
//            e.printStackTrace();
//        }finally{
//            this.closeConnection();
//        }
//        return true;
//    }
//
//    
//    
//    @Override
//    public boolean excluir(int id) throws PException {
//        String sql =   "DELETE FROM produto "
//                    + " WHERE "
//                        + "id = '" + id + "'"
//                    + ";";
//        try {
//            this.getConnection();
//            this.executarUpdateDeleteSQL(
//                sql
//            );
//            return true;
//        }catch(Exception e){
//            e.printStackTrace();
//            message.err("Verifique se a query está correta para excluir ", "Erro ao executar Query");
//            if (! this.rollback()) {
//                System.out.println("Falha ao executar rollback");
//                return false;
//            }
//            return false;
//        }finally{
//            this.closeConnection();
//        }
//    }
//
//    
//    public List<NProduto> pesquisa(NProduto produto) throws PException {
//        List<NProduto> listaModel = null;
//        NProduto nproduto = null;
//        String sql = "SELECT"
//                    +   montaColunasQuery()
//                    + "FROM"
//                            + "produto"
//                    + "WHERE "
//                + montaQueryPesquisa(produto);
//        
//        System.out.println("Pesquisar: \n" + sql);
//        
//        try {
//            this.getConnection();
//            this.executarSQL(
//                    sql
//            );
//            
//            listaModel = new ArrayList();
//            while(this.getResultSet().next()){
//                nproduto = banco2Model();
//                listaModel.add(nproduto);
//            }
//
//        }catch(Exception e) {
//            message.err("Verifique se a query está correta para pesquisar ", "Erro ao executar Query");
//            e.printStackTrace();
//        }finally{
//            this.closeConnection();
//        }
//        return listaModel;        
//    }
//
//    
//    private String montaQueryPesquisa(NProduto model) {
//        String sql = "";
//        
//        if (model.getId() != -1)                sql += " id = '" + model.getId() + "' AND ";
//        if (model.getCodigo() != -1)            sql += " codigo = '" + model.getCodigo() + "' AND ";
//        if (model.getCodigoBarras() != -1)      sql += " codigo_barras = '" + model.getCodigoBarras() + "' AND ";
//        if (model.getIdFornecedor()!= -1)       sql += " id_fornecedor = '" + model.getIdFornecedor() + "' AND ";
//        if (model.getIdGrupo() != -1)           sql += " id_grupo = '" + model.getIdGrupo() + "' AND ";
//        if (model.getLocalizacao()!= null)      sql += " localizacao like '%" + model.getLocalizacao() + "%' AND ";
//        if (model.getNome()!= null)             sql += " nome like '%" + model.getNome() + "%' AND ";
//        if (model.getPercentDescontoMax()!= -1) sql += " perc_desc_max = '" + model.getPercentDescontoMax() + "' AND ";
//        if (model.getPercentLucro()!= -1)       sql += " perc_lucro = '" + model.getPercentLucro() + "' AND ";
//        if (model.getQtdEstoque()!= -1)         sql += " qtd_estoque = '" + model.getQtdEstoque() + "' AND ";
//        if (model.getQtdEstoqueMax()!= -1)      sql += " qtd_estoque_maximo = '" + model.getQtdEstoqueMax() + "' AND ";
//        if (model.getQtdEstoqueMin()!= -1)      sql += " qtd_estoque_minimo = '" + model.getQtdEstoqueMin() + "' AND ";
//        if (model.getValorCusto()!= -1)         sql += " valor_custo = '" + model.getValorCusto() + "' AND ";
//        if (model.getValorLucro()!= -1)         sql += " valor_lucro = '" + model.getValorLucro() + "' AND ";
//        if (model.getValorVenda()!= -1)         sql += " valor_venda = '" + model.getValorVenda() + "' AND ";
//        sql += " ativo = '" + u.boolean2TinyInt(model.isAtivo()) + "' AND";
//        if (model.getSrcImg() != null)         sql += " src_img = '" + model.getValorVenda() + "' ";
//        return sql;
//    }
//    
//   
//    
//    @Override
//    public String model2Banco(NProduto model) {
//        String sql = "";
//        
//        sql += " codigo = '" + model.getCodigo() + "' , ";
//        sql += " codigo_barras = '" + model.getCodigoBarras() + "' , ";
//        sql += " id_fornecedor = '" + model.getIdFornecedor() + "' , ";
//        sql += " id_grupo = '" + model.getIdGrupo() + "' , ";
//        sql += " localizacao '" + model.getLocalizacao() + "' , ";
//        sql += " nome = '" + model.getNome() + "' , ";
//        sql += " perc_desc_max = '" + model.getPercentDescontoMax() + "' , ";
//        sql += " perc_lucro = '" + model.getPercentLucro() + "' , ";
//        sql += " qtd_estoque = '" + model.getQtdEstoque() + "' , ";
//        sql += " qtd_estoque_maximo = '" + model.getQtdEstoqueMax() + "' , ";
//        sql += " qtd_estoque_minimo = '" + model.getQtdEstoqueMin() + "' , ";
//        sql += " valor_custo = '" + model.getValorCusto() + "' , ";
//        sql += " valor_lucro = '" + model.getValorLucro() + "' , ";
//        sql += " valor_venda = '" + model.getValorVenda() + "' , ";
//        sql += " ativo = '" + u.boolean2TinyInt(model.isAtivo()) + "' ";
//        sql += " src_img = '" + model.getSrcImg() + "' ";
//        return sql;
//    }
//
//    @Override
//    public NProduto banco2Model() {
//        NProduto modelProdutos = new NProduto();
//        try {
//            modelProdutos.setId(this.getResultSet().getInt(1));
//            modelProdutos.setCodigo(this.getResultSet().getInt(2));
//            modelProdutos.setCodigoBarras(this.getResultSet().getInt(3));
//            modelProdutos.setIdFornecedor(this.getResultSet().getInt(4));
//            modelProdutos.setIdGrupo(this.getResultSet().getInt(5));
//            modelProdutos.setNome(this.getResultSet().getString(6));
//            modelProdutos.setValorCusto(this.getResultSet().getDouble(7));
//            modelProdutos.setValorVenda(this.getResultSet().getDouble(8));
//            modelProdutos.setValorLucro(this.getResultSet().getDouble(9));
//            modelProdutos.setPercentDescontoMax(this.getResultSet().getDouble(10));
//            modelProdutos.setPercentLucro(this.getResultSet().getDouble(11));
//            modelProdutos.setQtdEstoque(this.getResultSet().getDouble(12));
//            modelProdutos.setQtdEstoqueMin(this.getResultSet().getDouble(13));
//            modelProdutos.setQtdEstoqueMax(this.getResultSet().getDouble(14));
//            modelProdutos.setLocalizacao(this.getResultSet().getString(15));
//            modelProdutos.setAtivo(this.getResultSet().getBoolean(16));
//        } catch (SQLException ex) {
//            Logger.getLogger(PProdutos.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return modelProdutos;
//    }

    
    
    
    /**
    * atualiza Produtos
    * @param pModelProdutos
    * return boolean
    */
//    public boolean atualizarProdutosQuantidadeDAO(NProduto pModelProdutos){
//        try {
//            this.getConnection();
//            
//            int sizeLista = pModelProdutos.getLista().size();
//            for (int i = 0; i < sizeLista; i++) {
//                this.executarUpdateDeleteSQL(
//                "UPDATE produto SET "
//                    + "estoque = '" + pModelProdutos.getLista().get(i).getEstoque() + "'"
//                + " WHERE "
//                    + "id = '" + pModelProdutos.getListaModelProdutoses().get(i).getCodigo() + "'"
//                + ";"
//            );
//            }
//            return true;
//        }catch(Exception e){
//            e.printStackTrace();
//            return false;
//        }finally{
//            this.closeConnection();
//        }
//    }
//    
    
    private String montaColunasQuery() {
        String colunas = "codigo,"
                        + "codigo_barras,"
                        + "id_fornecedor,"
                        + "id_grupo,"
                        + "nome,"
                        + "valor_custo,"
                        + "valor_venda,"
                        + "valor_lucro,"
                        + "perc_desc_max,"
                        + "perc_lucro,"
                        + "qtd_estoque,"
                        + "qtd_estoque_minimo,"
                        + "qtd_estoque_maximo,"
                        + "localizacao,"
                        + "ativo,"
                        + "src_img";
        return colunas;
    }

}