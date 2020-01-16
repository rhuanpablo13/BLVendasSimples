package sistemavendas.controle;

import infra.abstratas.Controle;
import sistemavendas.persistente.PProdutos;


public class CProdutos  {

    
    private final PProdutos daoProdutos;


    
    
    public CProdutos (PProdutos persistence) {
        this.daoProdutos = persistence;

    }

    
    
    
    
    
    
    
//    
//    
//    @Override
//    public boolean inserir(NProduto produto){
//        try {
//            return this.daoProdutos.inserir(produto);
//        } catch (PException ex) {
//            System.out.println("Erro ao inserir produto: " + produto.toString());            
//        }
//        return false;
//    }

//    @Override
//    public boolean alterar(NProduto produto){
//        try {
//            return this.daoProdutos.alterar(produto);
//        } catch (PException ex) {
//            System.out.println("Erro ao alterar produto: " + produto.toString());
//        }
//        return false;
//    }

//    @Override
//    public NProduto buscarPorId(int codigo){
//        try {
//            return this.daoProdutos.buscarPorCodigo(codigo);
//        } catch (PException ex) {
//            System.out.println("Erro ao buscar por codigo: " + codigo);
//        }
//        return null;
//    }

//    public NProduto buscarPorNome(String nome){
//        try {
//            return this.daoProdutos.buscarPorNome(nome);
//        } catch (PException ex) {
//            System.out.println("Erro ao buscar por nome: " + nome);
//        }
//        return null;
//    }

//    @Override
//    public List<NProduto> getLista(){
//        try {
//            return this.daoProdutos.getLista();
//        } catch (PException ex) {
//            System.out.println("Erro ao buscar lista de produtos");
//        }
//        return null;
//    }
    
//    @Override
//    public boolean excluir(int id){
//        try {
//            return this.daoProdutos.excluir(id);
//        } catch (PException ex) {
//            System.out.println("Erro ao excluir produto de id: " + id);
//        }
//        return false;
//    }


//    public boolean atualizarProdutosQuantidadeController(NProduto pModelProdutos){
//        return this.daoProdutos.atualizarProdutosQuantidadeDAO(pModelProdutos);
//    }



    
    
    

    
    
    
}