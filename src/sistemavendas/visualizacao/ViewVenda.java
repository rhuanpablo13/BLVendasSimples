/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.abstratas.View;
import infra.comunicacao.Erro;
import java.util.List;
import infra.comunicacao.Message;
import infra.comunicacao.Sucess;
import infra.comunicacao.Warning;
import infra.listagem.DefaultTableModelProdutosVenda;
import infra.listagem.DefaultTableModelVendas;
import infra.listagem.TableListener; 
import infra.operacoes.Operacao;
import infra.utilitarios.Utils;
import infra.visualizacao.NClienteCellRenderer;
import infra.visualizacao.NClienteComboBox;
import infra.visualizacao.NGrupoCellRenderer;
import infra.visualizacao.NGrupoComboBox;
import infra.visualizacao.NProdutoCellRenderer;
import infra.visualizacao.NProdutoComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import sistemavendas.arquivos.Imagem;
import sistemavendas.cadastrosrapidos.CRCliente;
import sistemavendas.cadastrosrapidos.CRProduto;
import sistemavendas.controle.CCliente;
import sistemavendas.controle.CFormaPagamento;
import sistemavendas.controle.CGrupo;
import sistemavendas.controle.CProduto;
import sistemavendas.controle.CVendas;
import sistemavendas.negocio.FormaPagamento;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NFormaPagamento;
import sistemavendas.negocio.NGrupo;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NProduto;
import sistemavendas.negocio.NUsuario;
import sistemavendas.negocio.NVendas;
import sistemavendas.vendas.NCarrinhoCompras;
import sistemavendas.vendas.TipoDesconto;


public final class ViewVenda extends View<NVendas> {

    private static final long serialVersionUID = 1L;
    private CVendas vendaController;
    private NVendas vendasOld;
    private List<NProduto> produtos;
    
    
    
    
    public ViewVenda() {
        initComponents();
        initViewVendas();
        initializeView();
        initMasksValidators();
        configuraListagem();
        configuraListagemProdutosVenda();        
    }


    private void configuraListagemProdutosVenda() {
        DefaultTableModelProdutosVenda modelo = new DefaultTableModelProdutosVenda(jTableListagemProdutosVenda);
        modelo.addColumn("#");
        modelo.addColumn("Código");
        modelo.addColumn("Descrição");
        modelo.addColumn("Valor (R$)");
        modelo.addColumn("Quantidade");
        jTableListagemProdutosVenda.setModel(modelo);
        
        // id
        jTableListagemProdutosVenda.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTableListagemProdutosVenda.getColumnModel().getColumn(0).setMaxWidth(60);
        
        // codigo
        jTableListagemProdutosVenda.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableListagemProdutosVenda.getColumnModel().getColumn(1).setMaxWidth(60);

        // Valor
        jTableListagemProdutosVenda.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTableListagemProdutosVenda.getColumnModel().getColumn(3).setMaxWidth(80);

        // Quantidade
        jTableListagemProdutosVenda.getColumnModel().getColumn(4).setPreferredWidth(70);
        jTableListagemProdutosVenda.getColumnModel().getColumn(4).setMaxWidth(70);
        
    }
    
    
    // https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#editrender
    private void configuraListagem() {
    
        DefaultTableModelVendas modelo = new DefaultTableModelVendas();
        modelo.addColumn("#");
        modelo.addColumn("Nr. Venda");
        modelo.addColumn("Data");
        modelo.addColumn("Cliente");
        modelo.addColumn("Sub Total (R$)");
        modelo.addColumn("Desconto (R$)");
        modelo.addColumn("Desconto (%)");
        modelo.addColumn("Total (R$)");
        modelo.addColumn("Total Pago (R$)");
        modelo.addColumn("Forma Pagamento");
        
        jTableListagem.setModel(modelo);
        
        // id
        jTableListagem.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTableListagem.getColumnModel().getColumn(0).setMaxWidth(40);
        
        // codigo
        jTableListagem.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableListagem.getColumnModel().getColumn(1).setMaxWidth(60);

        // Data
        jTableListagem.getColumnModel().getColumn(2).setPreferredWidth(20); 
        
        // nome
        jTableListagem.getColumnModel().getColumn(3).setPreferredWidth(150);

        // subtotal R$
        jTableListagem.getColumnModel().getColumn(4).setPreferredWidth(35);
        
        // Desconto R$
        jTableListagem.getColumnModel().getColumn(5).setPreferredWidth(35);
        
        // Desconto %
        jTableListagem.getColumnModel().getColumn(6).setPreferredWidth(35);
        
        // Total R$
        jTableListagem.getColumnModel().getColumn(7).setPreferredWidth(30);
        
        // Total Pago R$
        jTableListagem.getColumnModel().getColumn(8).setPreferredWidth(40);
        
        // Forma Pagamento R$
        jTableListagem.getColumnModel().getColumn(9).setPreferredWidth(50);
        
    }
    
    
    
    private void initViewVendas() {
        vendaController = new CVendas();
        vendasOld = new NVendas();
        produtos = new ArrayList();        
        this.id.setVisible(false);
        this.fIdCliente.setVisible(false);
        this.fIdGrupo.setVisible(false);
        this.fIdProduto.setVisible(false);
        this.setName("view_venda");
        this.fCodigo.setText("");
        this.fValorTotal.setText("");
        this.fSubTotal.setText("0,00");
        this.fDescontoValor.setText("0,00");
        this.fDescontoPercent.setText("0%");
        this.fDataVenda.setText(Utils.date2View(new Date()));
        this.fIdFomaPagamento.setVisible(false);
        
        
        this.btSalvar.setEnabled(false);
        this.btEditar.setEnabled(false);
        this.btExcluir.setEnabled(false);
        this.btPesquisa.setEnabled(false);
        
        
        jTabbedPaneCadastroVenda.setTitleAt(0, "Cadastrar");
        jTabbedPaneCadastroVenda.setTitleAt(1, "Pesquisar");
        jLabelTituloCadastro.setText("Realizar Venda");
        
        carregaComboBoxClientes();
        carregaComboBoxGrupo();
        carregaComboBoxClientesPesquisa();
        carregarCodigoVenda();
        
        NUsuario usuarioLogado = ViewPrincipal.getUsuarioLogado();
        configurarOperacoesAutorizadas(usuarioLogado.operacoesAutorizadas(jLabelTituloCadastro.getText()));
    }
    
    
    private void configurarOperacoesAutorizadas(List<NOperacao> operacoes) {        
        for (NOperacao opr : operacoes) {
            if (opr.getId() == 1) { // incluir
                btSalvar.setEnabled(true);
            }
            if (opr.getId() == 2) { // alterar
                btEditar.setEnabled(true);
            }
            if (opr.getId() == 3) { // excluir
                btExcluir.setEnabled(true);
            }
            if (opr.getId() == 4) { // pesquisar
                btPesquisa.setEnabled(true);
            }
        }
    }
    
    
    
    private void carregarCodigoVenda() {
        CVendas controller = new CVendas();
        try {
            NVendas venda = controller.recuperaUltimoRegistro();
            this.fCodigo.setText(Integer.toString(venda.getCodigo()+1));
        } catch (Exception ex) {
            this.fCodigo.setText("1");
        }
    }
    
    
    

    public void model2View(NVendas model) {
        setValuesModel2View(model);
        try {
            NCarrinhoCompras carrinho = model.getCarrinhoCompras();
            setCarrinhoCompras(carrinho);
            model.setCarrinhoCompras(carrinho);
            
            NFormaPagamento pagamento = model.getFormaPagamento();
            setFormaPagamento(pagamento);
            model.setFormaPagamento(pagamento);
            
        } catch (Exception ex) { ex.printStackTrace(); }
        
        vendasOld = model;
    }
    
    
    public NVendas view2Model() {
        NVendas venda = setValuesView2Model();
        
        if (operacao != Operacao.PESQUISAR) {
            NCliente cliente = selectedCliente();
            venda.setIdCliente(cliente.getId());
            venda.setCarrinhoCompras(getCarrinhoCompras());
            
            NFormaPagamento pagamento = getFormaPagamento();
            venda.setFormaPagamento(pagamento);
            
        }
        
        if (operacao == Operacao.PESQUISAR) {
            NCliente cliente = selectedClientePesquisa();
            if (!cliente.getCodigo().equals(0)) {
                venda.setIdCliente(cliente.getId());
            }
        }
        return venda;
    }
   

    
    
    
    public void carregaComboBoxClientes(String nome) {
        
        try {            
            CCliente controller = new CCliente();
            try {
                NClienteComboBox cbmodel = new NClienteComboBox();
                List<NCliente> items = new ArrayList();
                if (!nome.isEmpty()) {
                    items = controller.pesquisar(new NCliente(nome));
                    cbmodel.addListCliente(items);
                } else {
                    items = controller.getLista();
                    cbmodel.addListCliente(items);
                }
                
                if (items.size() == 1) {
                    NCliente c = (NCliente) cbmodel.getSelectedItem();
                    this.fCodigoCliente.setText(Integer.toString(c.getCodigo()));
                    this.fNomeCliente.setText(c.getNome());  
                } else {
                    fCodigoCliente.setText("");
                    jComboBoxCliente.setModel(cbmodel);
                    jComboBoxCliente.setRenderer(new NClienteCellRenderer());
                }
                
            } catch (NullPointerException e) {
                new Warning("Informe o nome do cliente", "Atenção").show();
            }
            
        } catch(Exception e) {e.printStackTrace();}
        
    }
    
    public void carregaComboBoxClientesESetar(NCliente cliente) {
        try {
            CCliente controller = new CCliente();
            List<NCliente> items = controller.getLista();
            NClienteComboBox cbmodel = new NClienteComboBox(items);
            
            cbmodel.setSelectedItem(cliente);
            this.fCodigoCliente.setText(cliente.getCodigo().toString());
            this.fNomeCliente.setText(cliente.getNome());
            
            jComboBoxCliente.setModel(cbmodel);
            jComboBoxCliente.setRenderer(new NClienteCellRenderer());
            
        } catch(Exception e) {e.printStackTrace();}
    }
    
    public void carregaComboBoxClientes() {
        try {
            CCliente controller = new CCliente();
            List<NCliente> items = controller.getLista();
            NClienteComboBox cbmodel = new NClienteComboBox(items);
            
            if (! fCodigoCliente.getText().isEmpty()) {
                try {
                    int codigo = Integer.parseInt(fCodigoCliente.getText());
                    cbmodel.updateForCodigo(codigo);
                    NCliente c = (NCliente) cbmodel.getSelectedItem();
                    this.fNomeCliente.setText(c.getNome());
                    
                } catch (NumberFormatException | NullPointerException e) {
                    this.fCodigoCliente.setText("");
                    this.fNomeCliente.setText("");
                    new Warning("Informe apenas o código do cliente", "Atenção").show();
                }
            } else {
                cbmodel.setSelectedLastElement();
            }
            jComboBoxCliente.setModel(cbmodel);
            jComboBoxCliente.setRenderer(new NClienteCellRenderer());
            
        } catch(Exception e) {e.printStackTrace();}
    }

    
    public void carregaComboBoxProduto(Integer codigoGrupo) {
        CProduto controller = new CProduto();
        CGrupo controllerGrupo = new CGrupo();
        List<NProduto> items = new ArrayList();
        NProduto produto = new NProduto();
        NGrupo grupo = null;
        try {
            grupo = controllerGrupo.buscarPorCodigo(codigoGrupo);
            produto.setAtivo(true);
            produto.setIdGrupo(grupo.getId());
        } catch (Erro ex) {}
               
        
        try {
            items = controller.pesquisar(produto);
        } catch (Erro ex) { ex.show(); }
        
        NProdutoComboBox cbmodel;
        if (jComboBoxProduto.getModel() instanceof NProdutoComboBox) {
            cbmodel = (NProdutoComboBox) jComboBoxProduto.getModel();
            if (cbmodel.getSize() > 0) {
                cbmodel.removeAllElements();                
            }
            fCodigoProduto.setText("");
            fDescricaoProduto.setText("");
        } else {
            cbmodel = new NProdutoComboBox();            
        }
        
        if (items.size() == 1) {
            NProduto c = items.get(0);
            this.fCodigoProduto.setText(Integer.toString(c.getCodigo()));
            this.fDescricaoProduto.setText(c.getNome());
            setImagemProduto(c.getSrcImg());
            
        } else if (items.size() > 1) {
            cbmodel.addListProduto(items);
        } else if (items.isEmpty()){
            cbmodel = new NProdutoComboBox();
        }
        jComboBoxProduto.setModel(cbmodel);
        jComboBoxProduto.setRenderer(new NProdutoCellRenderer());
        
    }
    
    public void carregaComboBoxProduto(String nome) {
        try {
            CProduto controller = new CProduto();
            NProdutoComboBox cbmodel = new NProdutoComboBox();
            List<NProduto> items = new ArrayList();
            NGrupo grupo = selectedGrupo();
            
            try {
                
                if (!nome.isEmpty()) {
                    NProduto produto = new NProduto();
                    produto.setNome(nome);
                    produto.setIdGrupo(grupo.getId());
                    items = controller.pesquisar(produto);
                    cbmodel.addListProduto(items);
                } else {
                    items = controller.getLista(true, grupo.getId());
                    cbmodel.addListProduto(items);
                }
                
                if (items.size() == 1) {
                    NProduto c = (NProduto) cbmodel.getSelectedItem();
                    this.fCodigoProduto.setText(Integer.toString(c.getCodigo()));
                    this.fDescricaoProduto.setText(c.getNome());
                } else {
                    fCodigoProduto.setText("");
                    jComboBoxProduto.setModel(cbmodel);
                    jComboBoxProduto.setRenderer(new NProdutoCellRenderer());
                }
            } catch (NumberFormatException e) {
                new Warning("Informe apenas o código do produto", "Atenção").show();
            }
        } catch(Exception e) {e.printStackTrace();}
    }

    public void carregaComboBoxProdutoESetar(NProduto produto) {
        try {
            CProduto controller = new CProduto();
            NGrupo grupo = selectedGrupo();
            List<NProduto> items = controller.getLista(true, grupo.getId());
            NProdutoComboBox cbmodel = new NProdutoComboBox(items);
            
            cbmodel.setSelectedItem(produto);
            fCodigoProduto.setText(produto.getCodigo().toString());
            fDescricaoProduto.setText(produto.getNome());
            setImagemProduto(produto.getSrcImg());
            
            jComboBoxProduto.setModel(cbmodel);
            jComboBoxProduto.setRenderer(new NProdutoCellRenderer());
            
        } catch(Exception e) {e.printStackTrace();}
    }
    
    public void carregaComboBoxProduto() {
        try {            
             
            CProduto controller = new CProduto();
            NGrupo grupo = selectedGrupo();
            List<NProduto> items;
            if (grupo != null) {
                items = controller.getLista(true, grupo.getId());
            } else {
                items = controller.getLista();
            }
            NProdutoComboBox cbmodel = new NProdutoComboBox(items);

            if (! fCodigoProduto.getText().isEmpty()) {
                try {
                    int codigo = Integer.parseInt(fCodigoProduto.getText());
                    cbmodel.updateForCodigo(codigo);
                    NProduto p = (NProduto) cbmodel.getSelectedItem();                    
                    setImagemProduto(p.getSrcImg());
                    //fDescricaoProduto.setText(p.getNome());
                    //fCodigoProduto.setText(Integer.toString(p.getCodigo()));
                } catch (NumberFormatException e) {
                    new Warning("Informe apenas o código do produto", "Atenção").show();
                }
            } else {
                cbmodel.setSelectedLastElement();
            }

            jComboBoxProduto.setModel(cbmodel);
            jComboBoxProduto.setRenderer(new NProdutoCellRenderer());
            
        } catch(Exception e) {e.printStackTrace();}
    }
    
    
    public void setImagemProduto(String img) {        
        if (img != null && !img.isEmpty()) {  
            System.out.println("add");
            File origem = new File(img);
            jLabelImagem.setText("");
            jLabelImagem.setIcon(new ImageIcon(Imagem.loadImage(origem, 125, 125)));
            jPanelImagem.add(jLabelImagem);
            jPanelImagem.repaint();
        } else {
            removeImagem();
        }
    }
    
    
    public void removeImagem() {
        jLabelImagem.setIcon(null);
        jLabelImagem.setText("Sem imagem");
        //jPanelImagem.add(jLabelImagem);
        jPanelImagem.repaint();
    }
    
    public void carregaComboBoxGrupo(String nome) {
        
        try {            
            CGrupo controller = new CGrupo();
            try {
                NGrupoComboBox cbmodel = new NGrupoComboBox();
                List<NGrupo> items = new ArrayList();
                if (!nome.isEmpty()) {
                    items = controller.pesquisar(new NGrupo(nome));
                    cbmodel.addListGrupo(items);
                } else {
                    items = controller.getLista();
                    cbmodel.addListGrupo(items);
                }
                
                if (items.size() == 1) {
                    NGrupo c = (NGrupo) cbmodel.getSelectedItem();
                    this.fCodigoGrupo.setText(Integer.toString(c.getCodigo()));
                    this.fNomeGrupo.setText(c.getDescricao());  
                } else {
                    fCodigoGrupo.setText("");
                    jComboBoxGrupo.setModel(cbmodel);
                    jComboBoxGrupo.setRenderer(new NGrupoCellRenderer());
                }
                
                removeImagem();
                        
            } catch (NullPointerException e) {
                new Warning("Informe o nome do grupo", "Atenção").show();
            }
            
        } catch(Exception e) {e.printStackTrace();}
        
    }

    public void carregaComboBoxGrupoESetar(NGrupo grupo) {
        NGrupoComboBox cbmodel = new NGrupoComboBox();
        List<NGrupo> items = new ArrayList();
        cbmodel.addListGrupo(items);
        cbmodel.setSelectedItem(grupo);
        this.fCodigoGrupo.setText(Integer.toString(grupo.getCodigo()));
        this.fNomeGrupo.setText(grupo.getDescricao());  
        removeImagem();
    }
    
    public void carregaComboBoxGrupo() {
        try {
            CGrupo controller = new CGrupo();
            List<NGrupo> items = controller.getLista();
            NGrupoComboBox cbmodel = new NGrupoComboBox(items);
            
            if (! fCodigoGrupo.getText().isEmpty()) {
                try {                    
                    int codigo = Integer.parseInt(fCodigoGrupo.getText());
                    cbmodel.updateForCodigo(codigo);
                    NGrupo p = (NGrupo) cbmodel.getSelectedItem();
                    fNomeGrupo.setText(p.getDescricao());
                    //fCodigoProduto.setText(Integer.toString(p.getCodigo()));
                } catch (NumberFormatException e) {
                    new Warning("Informe apenas o código do grupo", "Atenção").show();
                }
            } else {
                cbmodel.setSelectedLastElement();
            }
            
            jComboBoxGrupo.setModel(cbmodel);
            jComboBoxGrupo.setRenderer(new NGrupoCellRenderer());
            removeImagem();
            
        } catch(Exception e) {e.printStackTrace();}
    }
    
    

    public void carregaComboBoxClientesPesquisa(String nome) {
        
        try {            
            CCliente controller = new CCliente();
            try {
                NClienteComboBox cbmodel = new NClienteComboBox();
                List<NCliente> items = new ArrayList();
                if (!nome.isEmpty()) {
                    items = controller.pesquisar(new NCliente(nome));
                    cbmodel.addListCliente(items);
                } else {
                    items = controller.getLista();
                    cbmodel.addListCliente(items);
                }
                
                if (items.size() == 1) {
                    NCliente c = (NCliente) cbmodel.getSelectedItem();
                    this.pesqCodigoCliente.setText(Integer.toString(c.getCodigo()));
                    this.pesqNomeCliente.setText(c.getNome());  
                } else {
                    pesqCodigoCliente.setText("");
                    pesqComboBoxCliente.setModel(cbmodel);
                    pesqComboBoxCliente.setRenderer(new NClienteCellRenderer());
                }
                
            } catch (NullPointerException e) {
                new Warning("Informe o nome do cliente", "Atenção").show();
            }
            
        } catch(Exception e) {e.printStackTrace();}
        
    }
    
    public void carregaComboBoxClientesPesquisa() {
        try {
            CCliente controller = new CCliente();            
            List<NCliente> items = new ArrayList();
            items.add(new NCliente(0, "Selecione"));
            items.addAll(controller.getLista());
            NClienteComboBox cbmodel = new NClienteComboBox(items);
            
            if (! pesqCodigoCliente.getText().isEmpty()) {
                try {
                    int codigo = Integer.parseInt(pesqCodigoCliente.getText());
                    cbmodel.updateForCodigo(codigo);
                    NCliente c = (NCliente) cbmodel.getSelectedItem();
                    this.pesqNomeCliente.setText(c.getNome());
                    
                } catch (NumberFormatException | NullPointerException e) {
                    this.pesqCodigoCliente.setText("");
                    this.pesqNomeCliente.setText("");
                    new Warning("Informe apenas o código do cliente", "Atenção").show();
                }
            } else {
                cbmodel.setSelectedLastElement();
            }
            pesqComboBoxCliente.setModel(cbmodel);
            pesqComboBoxCliente.setRenderer(new NClienteCellRenderer());
            
            
        } catch(Exception e) {e.printStackTrace();}
    }

   
    

    private NGrupo selectedGrupo() {
        NGrupoComboBox modelSelected = (NGrupoComboBox) jComboBoxGrupo.getModel();
        return (NGrupo) modelSelected.getSelectedItem();
    }
    
    
    private NCliente selectedCliente() {
        NClienteComboBox modelSelected = (NClienteComboBox) jComboBoxCliente.getModel();
        return (NCliente) modelSelected.getSelectedItem();
    }
    
    private NCliente selectedClientePesquisa() {        
        NClienteComboBox modelSelected = (NClienteComboBox) pesqComboBoxCliente.getModel();        
        return (NCliente) modelSelected.getSelectedItem();
    }
    
    
    private NProduto selectedProduto() {        
        NProdutoComboBox modelSelected = (NProdutoComboBox) jComboBoxProduto.getModel();
        return (NProduto) modelSelected.getSelectedItem();
    }
    
    private NProduto selectedProduto(boolean getClone) {
        if (jComboBoxProduto.getItemCount() > 0) {
            if (!getClone) return selectedProduto();        
            NProdutoComboBox modelSelected = (NProdutoComboBox) jComboBoxProduto.getModel();
            NProduto produto = (NProduto) modelSelected.getSelectedItem();
            return produto.getClone();
        }
        return null;
    }

 
    
    
    
    private void limparCampos() {
        //cleanViewFields();
        //this.fCodigo.setText("");
        
        if (operacao.equals(Operacao.CADASTRO)) {
            this.fValorTotal.setText("0,00");
            this.fDescontoPercent.setText("0,00");
            this.fDescontoValor.setText("0,00");
            this.fSubTotal.setText("0,00");
            this.fTotalPago.setText("0,00");
        } else {
            this.fIdCliente.setText("");
            this.fCodigoCliente.setText("");
            this.fNomeCliente.setText("");
            this.fIdGrupo.setText("");
            this.fCodigoGrupo.setText("");
            this.fNomeGrupo.setText("");
            this.fIdProduto.setText("");
            this.fCodigoProduto.setText("");
            this.fDescricaoProduto.setText("");
            this.fDataVenda.setText("");
            this.fValorTotal.setText("0,00");
            this.fDescontoPercent.setText("0,00");
            this.fDescontoValor.setText("0,00");
            this.fSubTotal.setText("0,00");
            this.fTotalPago.setText("0,00");            
        }        
        clearTableListProdutos();
    }


    private NVendas recebeDadosPesquisar() {
        limparCampos();
        return view2Model();
    }


    
    private NVendas getVendaSelecionada() throws Message {
        int row = jTableListagem.getSelectedRow();
        if (row > -1) {
            //pega o codigo do cliente na linha selecionada
            int id = (Integer) jTableListagem.getValueAt(row, 0);
            try {
                vendaController = new CVendas();
                return vendaController.buscarPorId(id);
            } catch (Exception e) {
                throw new Message("Nenhuma venda encontrada", "Atenção");
            }
        } else {
            throw new Message("Selecione uma venda para editar!", "Atenção");
        }
    }
    

    private int getProdutoSelecionadoLinha() {
        return jTableListagemProdutosVenda.getSelectedRow();        
    }
    
    
    private NProduto getProdutoSelecionado() throws Exception {
        int index = getProdutoSelecionadoLinha();
        if (index > -1) {
            int idLocal = (int) jTableListagemProdutosVenda.getValueAt(index, 0);
            CProduto controller = new CProduto();
            try {
                return controller.buscarPorId(idLocal);
            } catch (Exception ex) {
                throw new Warning("Nenhuma produto encontrado", "Atenção");
            }
        }
        throw new Warning("Selecione um produto da lista", "Atenção");
    }


    private NProduto getProdutoIn(int row) {
        
        int idLocal = (int) jTableListagemProdutosVenda.getValueAt(row, 0);
        CProduto controller = new CProduto();
        try {
            return controller.buscarPorId(idLocal);
        } catch (Exception ex) {System.out.println(ex.getMessage());}
        return null;
    }
    
    
    private int getQuantidadeProdutoSelecionado() {
        try {            
            NProduto p = getProdutoSelecionado();
            if (p != null) {
                DefaultTableModelProdutosVenda localModel = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
                return localModel.getValorColunaQuantidade(p);
            }
        } catch(Exception e) {}
        return -1;
    }
    
    
    private void atualizaTotais() {
        // Atualiza os campos de total e subtotal
        Double subTotal = getCarrinhoCompras().getTotal();
        setSubTotal(subTotal);
        aplicarDesconto();
    }
    
    private boolean isCadastro() {
        String titleLocal = jTabbedPaneCadastroVenda.getTitleAt(0);
        return titleLocal.equalsIgnoreCase("Cadastrar");
    }    
    
    
    private void mudarAba(int index, String title) {
        jTabbedPaneCadastroVenda.setSelectedIndex(index);
        jTabbedPaneCadastroVenda.setTitleAt(index, title);
    }   
    
    private void mudarAba(int index, String title, boolean carregarNovoCodigoVenda) {
        jTabbedPaneCadastroVenda.setSelectedIndex(index);
        jTabbedPaneCadastroVenda.setTitleAt(index, title);
        if (carregarNovoCodigoVenda && isCadastro()) {
            carregarCodigoVenda();
        }
    }
    

    private DefaultTableModelProdutosVenda getTableCarrinho() {
        return (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
    }
    
    @Override
    protected void initActions() {
        
        btSalvar.addActionListener((ActionEvent e) -> {
            onClickSalvar();
        });

        btExcluir.addActionListener((ActionEvent e) -> {
            onClickExcluir();
        });
        
        btCancelar.addActionListener((ActionEvent e) -> {
            onClickCancelar();
        });
        
        btEditar.addActionListener((ActionEvent e) -> {
            onClickEditar();
        });
        
        btPesquisa.addActionListener((ActionEvent e) -> {
            onClickPesquisar();
        });

        btAdicionarProduto.addActionListener((ActionEvent e) -> {
            onClickAdicionarProduto();
        });
        
        
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                TableListener tcl = (TableListener) e.getSource(); 
                Integer quantidadeNaoAlterada = (Integer.parseInt((String) tcl.getOldValue()));
                Integer quantidade = (Integer.parseInt((String) tcl.getNewValue()));
                NProduto produto = getProdutoIn(tcl.getRow());
                onClickAlteraQuantidade(produto, quantidadeNaoAlterada, quantidade);
                
            }
        };
        TableListener tcl = new TableListener(jTableListagemProdutosVenda, action);
        
        
        btRemoverProduto.addActionListener((ActionEvent e) -> {
            onClickRemoverProduto();            
        });
        
        
        jComboBoxGrupo.addActionListener((ActionEvent e) -> {
            NGrupo grupoSelecionado = selectedGrupo();
            if (grupoSelecionado != null) {                
                carregaComboBoxProduto(grupoSelecionado.getCodigo());
            }
            //carregaComboBoxProduto();
        });
        
        
        
        /**
         * ComboBox de cliente
         */        
        fCodigoCliente.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doRunnable = () -> {
                    carregaComboBoxClientes();
                };
                SwingUtilities.invokeLater(doRunnable);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doRunnable = () -> {
                    carregaComboBoxClientes();
                };
                SwingUtilities.invokeLater(doRunnable);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {}
        });
        
        jComboBoxCliente.addActionListener((ActionEvent e) -> {
            NCliente c = selectedCliente();
            fCodigoCliente.setText(Integer.toString(c.getCodigo()));
            fNomeCliente.setText(c.getNome());
            fIdCliente.setText(c.getId().toString());
        });
        
        btPesquisaRapidaCliente.addActionListener((ActionEvent e) -> {
            carregaComboBoxClientes(fNomeCliente.getText());
        });
        
        
        
        
        /**
         * ComboBox de produto
         */
        fCodigoProduto.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doRunnable = () -> {
                    carregaComboBoxProduto();
                };
                SwingUtilities.invokeLater(doRunnable);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doRunnable = () -> {
                    carregaComboBoxProduto();
                };
                SwingUtilities.invokeLater(doRunnable);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {}
        });
        
        jComboBoxProduto.addActionListener((ActionEvent e) -> {
            NProduto p = selectedProduto();
            fCodigoProduto.setText(Integer.toString(p.getCodigo()));
            fDescricaoProduto.setText(p.getNome());
            fQtdEstoque.setText(p.getQtdEstoque().toString());
            fIdProduto.setText(p.getId().toString());
        });
        
        btPesquisaRapidaProduto.addActionListener((ActionEvent e) -> {
            carregaComboBoxProduto(fDescricaoProduto.getText());
        });
        
        
        
        
        
        /**
         * Cadastro rápido de clientes
         */
        btCrNovoCliente.addActionListener((ActionEvent e) ->{
            CRCliente crCliente = new CRCliente();
            crCliente.init();
            crCliente.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    carregaComboBoxClientes();
                }
                
            });            
        });
        
        
        /**
         * Cadastro rápido de produto
         */
        btCrNovoProduto.addActionListener((ActionEvent e) -> {
            CRProduto crProduto = new CRProduto();
            crProduto.init();
            crProduto.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    carregaComboBoxProduto();
                }
            });
        });
        
        
        fQuantidade.getDocument().addDocumentListener(new DocumentListener() {
            @Override            
            public void changedUpdate(DocumentEvent e) {
                if (!fQuantidade.getText().isEmpty()) {
                    Runnable doRunnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Integer valor = Integer.parseInt(fQuantidade.getText());
                                if (valor < 1) {
                                    fQuantidade.setText("1");
                                }
                            } catch (NumberFormatException e) {
                                fQuantidade.setText("1");
                            }
                        }
                    };
                    SwingUtilities.invokeLater(doRunnable);
                }
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!fQuantidade.getText().isEmpty()) {
                    Runnable doRunnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Integer valor = Integer.parseInt(fQuantidade.getText());
                                if (valor < 1) {
                                    fQuantidade.setText("1");
                                }
                            } catch (NumberFormatException e) {
                                fQuantidade.setText("1");
                            }
                        }
                    };
                    SwingUtilities.invokeLater(doRunnable);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }
        });
        
        
        
        /**
         * ComboBox de grupo
         */        
        fCodigoGrupo.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doRunnable = () -> {
                    carregaComboBoxGrupo();
                };
                SwingUtilities.invokeLater(doRunnable);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doRunnable = () -> {
                    carregaComboBoxGrupo();
                };
                SwingUtilities.invokeLater(doRunnable);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {}
        });
        
        jComboBoxGrupo.addActionListener((ActionEvent e) -> {
            NGrupo c = selectedGrupo();
            fCodigoGrupo.setText(Integer.toString(c.getCodigo()));
            fNomeGrupo.setText(c.getDescricao());
            fIdGrupo.setText(c.getId().toString());
        });
        
        btPesquisaRapidaGrupo.addActionListener((ActionEvent e) -> {
            carregaComboBoxGrupo(fNomeGrupo.getText());
        });

        
        
        jComboBoxFormaPagamento.addActionListener((ActionEvent e) -> {
            
            JComboBox source = (JComboBox) e.getSource();
            String value = (String) source.getSelectedItem();
            
            if (value.equalsIgnoreCase("À Vista") || value.equalsIgnoreCase("Cartão Débito")) {
                this.fNrEntrada.setEditable(false);
                this.fNrParcelas.setEditable(false);
                this.fNrEntrada.setText("");
                this.fNrParcelas.setText("");
            } else {
                this.fNrEntrada.setEditable(true);
                this.fNrParcelas.setEditable(true);
            }
        });

        
        /**
         * Campos de desconto
         */
        
        // Desconto em dinheiro
        Action actionDescontoValor = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                aplicarDesconto();
            }
        };
        fDescontoValor.addActionListener(actionDescontoValor);
        
        
        
        // Desconto em porcentagem
        Action actionDescontoPercent = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                aplicarDesconto();
            }
        };
        fDescontoPercent.addActionListener(actionDescontoPercent);
    
        
        
        
    }
    
    
    private void aplicarDesconto() {
        double subTotal = 0;
        try {
            /* Se um dos dois descontos estão com algum desconto já aplicado.*/
            /* então aplica desconto nos dois */
            subTotal = getSubTotal();
            Float dp = (float) 0;
            Float dv = (float) 0;

            
            /* Desconto PORCENTAGEM */
            try {
                dp = getDescontoPercent();
            } catch (java.lang.NumberFormatException e) {}
            
            Double novoSubTotalTemp = getTableCarrinho().getCarrinho().aplicarDesconto(subTotal, dp, TipoDesconto.PORCENTAGEM);
            
            /* Desconto DINHEIRO */
            try {
                dv = getDescontoValor();
            } catch (java.lang.NumberFormatException e) {}
            
            subTotal = getTableCarrinho().getCarrinho().aplicarDesconto(novoSubTotalTemp, dv, TipoDesconto.DINHEIRO);
            setValorTotal(subTotal);
            
        } catch (Exception ex) {
            out.show(ex);
        }
    }
    
    
    
    
    /**
     * Ação para quando alterar a quantidade direto no registro da tabela
     * @param produto
     * @param quantidadeNaoAlterada
     * @param quantidadeAlterada
     */
    
    public void onClickAlteraQuantidade(NProduto produto, Integer quantidadeNaoAlterada, Integer quantidadeAlterada) {
        if (quantidadeNaoAlterada != null && quantidadeAlterada != null && produto != null) {
            if (!Objects.equals(quantidadeAlterada, quantidadeNaoAlterada)) {
                
                DefaultTableModelProdutosVenda modelLocal = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();                
                try {
                    modelLocal.alteraQuantidadeProduto(produto, quantidadeAlterada);
                } catch (IllegalArgumentException e2) {
                    new Warning(e2.getMessage(), "Atenção").show();
                    modelLocal.alteraQuantidadeProduto(produto, 1);
                }
                atualizaTotais();
            }
        }
    }
    
    
    
    
    private void clearTableList() {
        DefaultTableModel modelo = (DefaultTableModel) jTableListagem.getModel();
        modelo.setNumRows(0);
    }
    
    private void clearTableListProdutos() {
        DefaultTableModelProdutosVenda modelo = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
        modelo.limparCarrinho();
        modelo.setRowCount(0);
    }
    
    protected void refreshTableList(List<NVendas> dataList) {
        try {
            clearTableList();
            
            if (dataList.isEmpty()) {
                return;
            }
            
            DefaultTableModelVendas modelo = (DefaultTableModelVendas) jTableListagem.getModel();
            modelo.setNumRows(0);
            CCliente clienteController = new CCliente();
            CFormaPagamento pagamentoController = new CFormaPagamento();
            
            for (NVendas nVenda : dataList) {
                
                String subTotal = "R$ " + doubleToView(nVenda.getSubTotal());
                String descP = doubleToView(nVenda.getDescontoPercent().doubleValue()) + " %";
                String descV = "R$ " + doubleToView(nVenda.getDescontoValor().doubleValue());
                String total = "R$ " + doubleToView(nVenda.getValorTotal());
                String totalPago = "R$ " + doubleToView(nVenda.getTotalPago());
                
                try {
                    NCliente cliente = clienteController.buscarPorId(nVenda.getIdCliente());
                    NFormaPagamento pagamento = pagamentoController.pesquisar(nVenda.getId());
                    
                    String formaPag = pagamento.getFormaPagamento().name();
                    if (formaPag.equalsIgnoreCase("CARTAO_CREDITO")) {
                        formaPag = "Crédito: " + pagamento.getEntrada() + "+" + pagamento.getParcelas();                        
                    } else if (formaPag.equalsIgnoreCase("A_VISTA")){
                        formaPag = "À Vista";
                    } else {
                        formaPag = "Débito";
                    }
                    
                    
                    modelo.addRow(new Object[] {
                        nVenda.getId(),
                        nVenda.getCodigo(),
                        Utils.date2View(nVenda.getDataVenda()),
                        cliente.getNome(),
                        subTotal,
                        descV,
                        descP,
                        total,
                        totalPago,
                        formaPag
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //out.show(ex);
                }
            }
            
            
        } catch (Exception e) {
            out.show(e);
        }
    }
    

    ////////////////////////////////////////////////////////////////////////////
    private void onClickSalvar() {
        
        String msg = "";
        if (isCadastro()) {
            setOperacao(Operacao.CADASTRO);
            msg = "Confirma o cadastro ?";
        } else {
            setOperacao(Operacao.ALTERAR);
            msg = "Confirma a alteração ?";
        }
        
        
        int i = Utils.pedeConfirmacao(msg, this);

        if (i == 0) {
            NVendas c = view2Model();
            if (operacao == Operacao.CADASTRO) {
                try {
                    vendaController.inserir(c);                    
                    new Sucess("Registro salvo com sucesso!", "Sucesso").show();
                } catch (Erro ex) {
                   out.show(ex);
                }
            } else {
                try {
                    vendaController.alterar(c);                    
                    new Sucess("Registro alterado com sucesso!", "Sucesso").show();
                } catch (Erro ex) {
                    out.show(ex);
                }
            }
            mudarAba(0, "Cadastrar", true);
            clearTableListProdutos();   
        }
    }
    
    private void onClickCancelar() {
        limparCampos();
        clearTableListProdutos();
        mudarAba(0, "Cadastrar", true);
        ViewPrincipal.showDashBoard();
    }
    
    private void onClickEditar() {
        try {
            NVendas venda = getVendaSelecionada();            
            model2View(venda);
            mudarAba(0, "Alterar");
            setOperacao(Operacao.ALTERAR);
        } catch (Exception ex) {
            out.show(ex);
        }
    }
    
    private void onClickExcluir() {
        List<NVendas> list = new ArrayList();
        
        try {
            NVendas c = getVendaSelecionada();
            if (c != null) {
                int i = Utils.pedeConfirmacao("Deseja excluir o registro: " + c.getCodigo(), this);
                if (i==0) {
                    vendaController = new CVendas();
                    vendaController.excluir(c);
                    new Sucess("Registro excluído com sucesso!", "Sucesso").show();
                }
                list = vendaController.getLista();
            } else {
                new Warning("Você deve selecionar um registro!", "Falha").show();
            }
        } catch (Exception ex) {
            out.show(ex);
        }
        refreshTableList(list);
    }    

    private void onClickPesquisar() {
        setOperacao(Operacao.PESQUISAR);
        Integer nrVenda = getPesqCodigo();
        NCliente cliente = selectedClientePesquisa();
        Integer idCliente = cliente.getId();
        Date dataVenda = getPesqDataVenda();
        Double valorTotalInicio = getPesqTotalInicio();
        Double valorTotalFim = getPesqTotalFim();
        Double subTotalInicio = getPesqSubTotalInicio();
        Double subTotalFim = getPesqSubTotalFim();        
        
        List<NVendas> lista = new ArrayList();
        try {
            clearTableList();
            vendaController = new CVendas();
            lista = vendaController.pesquisar(nrVenda, idCliente, dataVenda, valorTotalInicio, valorTotalFim, subTotalInicio, subTotalFim);
            refreshTableList(lista);
        } catch (Exception ex) {
            out.show(ex);
        }
        
        if (lista == null || lista.isEmpty()) {
            new Message("Nenhuma venda encontrada", "Sem Registros").show();
        }
    }
    
    
    private void onClickAdicionarProduto() {
        NProduto produto = selectedProduto(true);
        if (produto != null) {
            DefaultTableModelProdutosVenda table = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
            int quantidade = Integer.parseInt(fQuantidade.getText());
            try {            
                table.adicionarProduto(produto, quantidade);
                atualizaTotais();
            } catch (Exception e) {
                e.printStackTrace();
                new Warning(e.getMessage(), "Atenção").show();
            }
        }
    }
    
    
    private void onClickRemoverProduto() {        
        try {
            NProduto produto = getProdutoSelecionado();            
            DefaultTableModelProdutosVenda table = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
            table.removeProduto(produto);
            atualizaTotais();
            
        } catch (Exception e) {
            out.show(e);
        }
    }
    
    
    
    ////////////////////////////////////////////////////////////////////////////

    private void initMasksValidators() {
        // validator.required(fCodigo);
        // validator.required(fNome);
        // validator.required(fEmail);
        // validator.required(fUsuario);
        // validator.requiredSenha(fSenha);
        // validator.requiredSenha(fConfirmaSenha);
        validator.fieldDate(fDataVenda, "", true, true, "Data inválida");
    } 

    

    
    
    public NCarrinhoCompras getCarrinhoCompras() {
        DefaultTableModelProdutosVenda table = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
        return table.getCarrinho();
    }
    
    
    public void setCarrinhoCompras(NCarrinhoCompras carrinho) {
        DefaultTableModelProdutosVenda table = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
        table.setCarrinho(carrinho);
        table.refresh();
    }
    
    
    
     /**
     * **************************************************************
     * MÉTODOS QUE VÃO RECEBER OS DADOS DA MODEL PARA MOSTRAR NA TELA
     * **************************************************************
     */
    public void setIdCliente(Integer idCliente) {        
        CCliente controller = new CCliente();
        try {
            NCliente cliente = controller.buscarPorId(idCliente);
            carregaComboBoxClientesESetar(cliente);
            this.fIdCliente.setText(Integer.toString(idCliente));
        } catch (Exception e) {}
    }

    
    public void setIdProduto(Integer idProduto) {
        CProduto controller = new CProduto();
        try {
            NProduto produto = controller.buscarPorId(idProduto);
            carregaComboBoxProdutoESetar(produto);
            this.fIdProduto.setText(Integer.toString(idProduto));
        } catch (Exception e) {}
    }

    
    public void setIdGrupo(Integer idGrupo) {
        CGrupo controller = new CGrupo();
        try {
            NGrupo grupo = controller.buscarPorId(idGrupo);
            carregaComboBoxGrupoESetar(grupo);
        } catch (Exception e) {}
        this.fIdGrupo.setText(Integer.toString(idGrupo));
    }
    
    
    public void setDescontoValor(Float fDescontoValor) {
        this.fDescontoValor.setText(toView(fDescontoValor));
    }

    public void setDescontoPercent(Float fDescontoPercent) {
        this.fDescontoPercent.setText(toView(fDescontoPercent));
    }

    public void setDescricaoProduto(String fDescricaoProduto) {
        this.fDescricaoProduto.setText(fDescricaoProduto);
    }

    public void setNomeCliente(String fNomeCliente) {
        this.fNomeCliente.setText(fNomeCliente);
    }

    public void setQuantidade(Integer fQuantidade) {
        this.fQuantidade.setText(Integer.toString(fQuantidade));
    }

    
    public void setTotalPago(Double fTotalPago) {
        this.fTotalPago.setText(doubleToView(fTotalPago));
    }

    public void setSubTotal(Double fSubTotal) {
        this.fSubTotal.setText(doubleToView(fSubTotal));
    }

    public void setValorTotal(Double fValorTotal) {
        this.fValorTotal.setText(doubleToView(fValorTotal));
    }    
    
    
    public void setId(Integer id) {
        this.id.setText(Integer.toString(id));
    }

    public void setCodigo(Integer codigo) {
        this.fCodigo.setText(Integer.toString(codigo));
    }    

    public void setDataVenda(Date dataVenda) {
        this.fDataVenda.setText(Utils.date2View(dataVenda));
    }
    
    public void setFormaPagamento(NFormaPagamento pagamento) {
        if (pagamento.getFormaPagamento() == FormaPagamento.A_VISTA || pagamento.getFormaPagamento() == FormaPagamento.CARTAO_DEBITO) {
            this.fNrEntrada.setText("");
            this.fNrEntrada.setEditable(false);
            this.fNrParcelas.setText("");
            this.fNrParcelas.setEditable(false);
        } else {
            this.fNrEntrada.setText(pagamento.getEntrada().toString());
            this.fNrParcelas.setText(pagamento.getParcelas().toString());
        }
        
        switch (pagamento.getFormaPagamento()) {
            case A_VISTA:
                this.jComboBoxFormaPagamento.setSelectedItem("À Vista");
                break;
            case CARTAO_CREDITO:
                this.jComboBoxFormaPagamento.setSelectedItem("Cartão Crédito");
                break;
            case CARTAO_DEBITO:
                this.jComboBoxFormaPagamento.setSelectedItem("Cartão Débito");
                break;
        }
        setIdFormaPagamento(pagamento.getId());
    }    
    
    
    public Integer getIdFormaPagamento() {
        return integerToModel(fIdFomaPagamento);
    }
    public void setIdFormaPagamento(Integer idFormaPagamento) {
        this.fIdFomaPagamento.setText(Integer.toString(idFormaPagamento));
    }
    
    public NFormaPagamento getFormaPagamento() {
        NFormaPagamento pagamento = new NFormaPagamento();
        String formaPag = (String) this.jComboBoxFormaPagamento.getSelectedItem();
        System.out.println("forma pagamento é: " + formaPag);
        
        
        pagamento.setId(getIdFormaPagamento());
        
        if (formaPag.equalsIgnoreCase("À Vista")) {
            pagamento.setFormaPagamento(FormaPagamento.A_VISTA);
            pagamento.setEntrada(0);
            pagamento.setParcelas(0);
        } else if (formaPag.equalsIgnoreCase("Cartão Crédito")) {
            pagamento.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
            pagamento.setEntrada(integerToModel(fNrEntrada));
            pagamento.setParcelas(integerToModel(fNrParcelas));
        } else {
            pagamento.setFormaPagamento(FormaPagamento.CARTAO_DEBITO);
            pagamento.setEntrada(0);
            pagamento.setParcelas(0);
        }
        return pagamento;
    }
    
    
    /**
     * ***********************************************************
     *  MÉTODOS QUE VÃO PEGAR OS DADOS DA TELA PARA SETAR NA MODEL
     * ***********************************************************
     */    

    public NCarrinhoCompras getCarrinho() {
        DefaultTableModelProdutosVenda table = (DefaultTableModelProdutosVenda) jTableListagemProdutosVenda.getModel();
        return table.getCarrinho();
    }
    
    public Date getDataVenda() {
        return Utils.view2Date(this.fDataVenda.getText());
    }
    
    public Integer getCodigo() {
        return Integer.parseInt(fCodigo.getText());
    }
    
    public Integer getIdCliente() {
        return Integer.parseInt(fCodigoCliente.getText());
    }

    public Float getDescontoPercent() {
        return floatToModel(fDescontoPercent);
    }

    public Float getDescontoValor() {
        return floatToModel(fDescontoValor);
    }

    public String getDescricaoProduto() {
        return fDescricaoProduto.getText();
    }

    public String getNomeCliente() {
        return fNomeCliente.getText();
    }

    public Integer getQuantidade() {
        return Integer.parseInt(fQuantidade.getText());
    }

    public Double getSubTotal() {
        return doubleToModel(fSubTotal);
    }

    public Double getTotalPago() {
        return doubleToModel(fTotalPago);
    }

    public Double getValorTotal() {
        return doubleToModel(fValorTotal);
    }

    public Integer getId() {
        return Integer.parseInt(id.getText());
    }


    /**
     * *******************************
     *  MÉTODOS USADOS PARA A PESQUISA
     * *******************************
     */
    public void setPesqCodigo(Integer pesqCodigo) {
        this.pesqCodigo.setText(Integer.toString(pesqCodigo));
    }

    public Integer getPesqCodigo() {
        if (pesqCodigo.getText().isEmpty()) return null;
        return Integer.parseInt(pesqCodigo.getText());
    }
    
    
    /* SubTotal */
    public void setPesqSubTotalInicio(Double pesqSubTotalInicio) {
        doubleToView(pesqSubTotalInicio);
    }

    public Double getPesqSubTotalInicio() {
        return doubleToModel(pesqSubTotalInicio);
    }
    
    public void setPesqSubTotalFim(Double pesqSubTotalFim) {
        doubleToView(pesqSubTotalFim);
    }

    public Double getPesqSubTotalFim() {
        return doubleToModel(pesqSubTotalFim);
    }
    
    
    
    /* Total */
    public void setPesqTotalInicio(Double pesqTotalInicio) {
        doubleToView(pesqTotalInicio);
    }

    public Double getPesqTotalInicio() {
        return doubleToModel(pesqTotalInicio);
    }

    public void setPesqTotalFim(Double pesqTotalFim) {
        doubleToView(pesqTotalFim);
    }

    public Double getPesqTotalFim() {
        return doubleToModel(pesqTotalFim);
    }
    
    
    
    /* Data de Venda */
    public Date getPesqDataVenda() {
        return Utils.view2Date(this.pesqDataVenda.getText());
    }
    
    public void setPesqDataVenda(Date dataVenda) {
        this.pesqDataVenda.setText(Utils.date2View(dataVenda));
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPaneCadastroVenda = new javax.swing.JTabbedPane();
        jPanelCadastro = new javax.swing.JPanel();
        jPanelCadastroProduto = new javax.swing.JPanel();
        jLabelTituloCadastro = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        label7 = new javax.swing.JLabel();
        fCodigo = new javax.swing.JLabel();
        fDataVenda = new javax.swing.JFormattedTextField();
        label12 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableListagemProdutosVenda = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        labelCifrao = new javax.swing.JLabel();
        fValorTotal = new javax.swing.JFormattedTextField();
        fDescontoPercent = new JFormattedTextField(NumberFormat.getNumberInstance());
        fDescontoValor = new JFormattedTextField(NumberFormat.getNumberInstance());
        fTotalPago = new JFormattedTextField(NumberFormat.getNumberInstance());
        fSubTotal = new JFormattedTextField(NumberFormat.getNumberInstance());
        jPanel4 = new javax.swing.JPanel();
        jComboBoxFormaPagamento = new javax.swing.JComboBox<String>();
        fNrEntrada = new javax.swing.JTextField();
        fNrParcelas = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fIdFomaPagamento = new javax.swing.JTextField();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        fCodigoCliente = new javax.swing.JTextField();
        fNomeCliente = new javax.swing.JTextField();
        jComboBoxCliente = new javax.swing.JComboBox();
        btCrNovoCliente = new javax.swing.JButton();
        label2 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        fIdCliente = new javax.swing.JTextField();
        btPesquisaRapidaCliente = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        fCodigoProduto = new javax.swing.JTextField();
        fDescricaoProduto = new javax.swing.JTextField();
        jComboBoxProduto = new javax.swing.JComboBox();
        btCrNovoProduto = new javax.swing.JButton();
        label5 = new javax.swing.JLabel();
        label6 = new javax.swing.JLabel();
        label8 = new javax.swing.JLabel();
        fQuantidade = new javax.swing.JTextField();
        btAdicionarProduto = new javax.swing.JButton();
        jPanelImagem = new javax.swing.JPanel();
        jLabelImagem = new javax.swing.JLabel();
        btPesquisaRapidaProduto = new javax.swing.JButton();
        btRemoverProduto = new javax.swing.JButton();
        label9 = new javax.swing.JLabel();
        fQtdEstoque = new javax.swing.JLabel();
        fIdProduto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        fCodigoGrupo = new javax.swing.JTextField();
        fNomeGrupo = new javax.swing.JTextField();
        jComboBoxGrupo = new javax.swing.JComboBox();
        btCrNovoGrupo = new javax.swing.JButton();
        label4 = new javax.swing.JLabel();
        label11 = new javax.swing.JLabel();
        fIdGrupo = new javax.swing.JTextField();
        btPesquisaRapidaGrupo = new javax.swing.JButton();
        jPanelPesquisa = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        pesqCodigo = new javax.swing.JTextField();
        label13 = new javax.swing.JLabel();
        btPesquisa = new javax.swing.JButton();
        label16 = new javax.swing.JLabel();
        jPanelListagemVendas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListagem = new javax.swing.JTable();
        pesqSubTotalInicio = new javax.swing.JFormattedTextField();
        label14 = new javax.swing.JLabel();
        pesqSubTotalFim = new javax.swing.JFormattedTextField();
        label15 = new javax.swing.JLabel();
        label17 = new javax.swing.JLabel();
        pesqTotalInicio = new javax.swing.JFormattedTextField();
        label18 = new javax.swing.JLabel();
        pesqTotalFim = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        pesqCodigoCliente = new javax.swing.JTextField();
        pesqNomeCliente = new javax.swing.JTextField();
        pesqComboBoxCliente = new javax.swing.JComboBox();
        label21 = new javax.swing.JLabel();
        label22 = new javax.swing.JLabel();
        pesqBtPesquisaRapidaCliente = new javax.swing.JButton();
        pesqDataVenda = new javax.swing.JFormattedTextField();
        label23 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(245, 246, 250));
        setBorder(null);
        setTitle("Produtos");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(920, 1000));
        setNormalBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(920, 1000));

        jTabbedPaneCadastroVenda.setBackground(new java.awt.Color(245, 246, 250));
        jTabbedPaneCadastroVenda.setMinimumSize(new java.awt.Dimension(920, 820));
        jTabbedPaneCadastroVenda.setPreferredSize(new java.awt.Dimension(920, 820));

        jPanelCadastro.setBackground(new java.awt.Color(245, 246, 250));
        jPanelCadastro.setMinimumSize(new java.awt.Dimension(920, 1000));
        jPanelCadastro.setPreferredSize(new java.awt.Dimension(920, 1000));
        jPanelCadastro.setLayout(new java.awt.GridBagLayout());

        jPanelCadastroProduto.setBackground(new java.awt.Color(245, 246, 250));
        jPanelCadastroProduto.setForeground(new java.awt.Color(245, 246, 250));
        jPanelCadastroProduto.setMinimumSize(new java.awt.Dimension(900, 900));
        jPanelCadastroProduto.setPreferredSize(new java.awt.Dimension(900, 900));

        jLabelTituloCadastro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro.setText("Realizar Venda");
        jLabelTituloCadastro.setAlignmentY(0.0F);

        jPanel5.setBackground(new java.awt.Color(245, 246, 250));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel5.setPreferredSize(new java.awt.Dimension(150, 150));

        label7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label7.setForeground(new java.awt.Color(153, 153, 153));
        label7.setText("Nr. Venda");

        fCodigo.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        fCodigo.setForeground(new java.awt.Color(153, 153, 153));
        fCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fCodigo.setText("150");

        fDataVenda.setBackground(new java.awt.Color(245, 246, 250));
        fDataVenda.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fDataVenda.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM))));
        fDataVenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fDataVenda.setText("01/01/2019");
        fDataVenda.setAlignmentX(0.0F);
        fDataVenda.setAlignmentY(0.0F);
        fDataVenda.setMaximumSize(new java.awt.Dimension(111, 20));
        fDataVenda.setMinimumSize(new java.awt.Dimension(100, 20));
        fDataVenda.setPreferredSize(new java.awt.Dimension(100, 20));

        label12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label12.setForeground(new java.awt.Color(153, 153, 153));
        label12.setText("Data da Venda");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(label7))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(fDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(label12))))
                        .addGap(0, 16, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(label7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(label12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        id.setEditable(false);
        id.setFocusable(false);

        jTableListagemProdutosVenda.setAutoCreateRowSorter(true);
        jTableListagemProdutosVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableListagemProdutosVenda.setGridColor(new java.awt.Color(102, 102, 102));
        jTableListagemProdutosVenda.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTableListagemProdutosVenda);

        jPanel8.setBackground(new java.awt.Color(2, 177, 219));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        jPanel8.setPreferredSize(new java.awt.Dimension(150, 150));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(70, 70));

        labelCifrao.setFont(new java.awt.Font("Dialog", 1, 22)); // NOI18N
        labelCifrao.setForeground(new java.awt.Color(51, 51, 51));
        labelCifrao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCifrao.setText("TOTAL (R$)");
        labelCifrao.setAlignmentY(0.0F);
        labelCifrao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelCifrao.setMaximumSize(new java.awt.Dimension(0, 0));
        labelCifrao.setMinimumSize(new java.awt.Dimension(0, 0));
        labelCifrao.setPreferredSize(new java.awt.Dimension(40, 50));

        fValorTotal.setEditable(false);
        fValorTotal.setBackground(new java.awt.Color(245, 246, 250));
        fValorTotal.setBorder(null);
        fValorTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        fValorTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fValorTotal.setText("0,00");
        fValorTotal.setFont(new java.awt.Font("Dialog", 0, 30)); // NOI18N
        fValorTotal.setSelectedTextColor(new java.awt.Color(0, 204, 0));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(fValorTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(labelCifrao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(labelCifrao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        fDescontoPercent.setBackground(new java.awt.Color(245, 246, 250));
        fDescontoPercent.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Desconto (%)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        fDescontoPercent.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        fDescontoPercent.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fDescontoPercent.setText("0,00");
        fDescontoPercent.setToolTipText("");
        fDescontoPercent.setAlignmentX(0.0F);
        fDescontoPercent.setAlignmentY(0.0F);
        fDescontoPercent.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        fDescontoPercent.setMinimumSize(new java.awt.Dimension(0, 0));
        fDescontoPercent.setPreferredSize(new java.awt.Dimension(90, 50));

        fDescontoValor.setBackground(new java.awt.Color(245, 246, 250));
        fDescontoValor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Desconto (R$)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        fDescontoValor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        fDescontoValor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fDescontoValor.setText("0,00");
        fDescontoValor.setAlignmentX(0.0F);
        fDescontoValor.setAlignmentY(0.0F);
        fDescontoValor.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        fDescontoValor.setPreferredSize(new java.awt.Dimension(130, 50));

        fTotalPago.setBackground(new java.awt.Color(245, 246, 250));
        fTotalPago.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Pago (R$)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        fTotalPago.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        fTotalPago.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fTotalPago.setText("0,00");
        fTotalPago.setAlignmentX(0.0F);
        fTotalPago.setAlignmentY(0.0F);
        fTotalPago.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        fTotalPago.setPreferredSize(new java.awt.Dimension(130, 50));

        fSubTotal.setEditable(false);
        fSubTotal.setBackground(new java.awt.Color(245, 246, 250));
        fSubTotal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sub Total (R$)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        fSubTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        fSubTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fSubTotal.setToolTipText("");
        fSubTotal.setAlignmentX(0.0F);
        fSubTotal.setAlignmentY(0.0F);
        fSubTotal.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        fSubTotal.setPreferredSize(new java.awt.Dimension(130, 50));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Forma de Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N

        jComboBoxFormaPagamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "À Vista", "Cartão Débito", "Cartão Crédito" }));

        fNrEntrada.setEditable(false);
        fNrEntrada.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        fNrParcelas.setEditable(false);
        fNrParcelas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel2.setText("Entrada");

        jLabel3.setText("Parcelas");

        fIdFomaPagamento.setEditable(false);
        fIdFomaPagamento.setFocusable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fIdFomaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fNrEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fNrParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxFormaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fNrEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fNrParcelas)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(fIdFomaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(fSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(fDescontoValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(fDescontoPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(fTotalPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fDescontoValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fDescontoPercent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fTotalPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar Venda");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));

        jPanel1.setBackground(new java.awt.Color(245, 246, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Cliente ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);

        fCodigoCliente.setBackground(new java.awt.Color(245, 246, 250));
        fCodigoCliente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigoCliente.setPreferredSize(new java.awt.Dimension(100, 20));

        fNomeCliente.setBackground(new java.awt.Color(245, 246, 250));
        fNomeCliente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNomeCliente.setPreferredSize(new java.awt.Dimension(100, 20));

        btCrNovoCliente.setBackground(new java.awt.Color(245, 246, 250));
        btCrNovoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Add.png"))); // NOI18N
        btCrNovoCliente.setToolTipText("Novo Fornecedor");
        btCrNovoCliente.setMinimumSize(new java.awt.Dimension(25, 22));
        btCrNovoCliente.setPreferredSize(new java.awt.Dimension(25, 22));

        label2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label2.setForeground(new java.awt.Color(153, 153, 153));
        label2.setText("Código");

        label3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label3.setForeground(new java.awt.Color(153, 153, 153));
        label3.setText("Nome");

        fIdCliente.setEditable(false);
        fIdCliente.setFocusable(false);

        btPesquisaRapidaCliente.setBackground(new java.awt.Color(245, 246, 250));
        btPesquisaRapidaCliente.setForeground(new java.awt.Color(245, 246, 250));
        btPesquisaRapidaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Find.png"))); // NOI18N
        btPesquisaRapidaCliente.setToolTipText("Pesquisa Cliente");
        btPesquisaRapidaCliente.setMinimumSize(new java.awt.Dimension(25, 22));
        btPesquisaRapidaCliente.setPreferredSize(new java.awt.Dimension(25, 22));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(fCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(fNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btPesquisaRapidaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCrNovoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label2)
                            .addComponent(label3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btCrNovoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btPesquisaRapidaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(fIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(245, 246, 250));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Produto ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N

        fCodigoProduto.setBackground(new java.awt.Color(245, 246, 250));
        fCodigoProduto.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigoProduto.setPreferredSize(new java.awt.Dimension(100, 20));

        fDescricaoProduto.setBackground(new java.awt.Color(245, 246, 250));
        fDescricaoProduto.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fDescricaoProduto.setPreferredSize(new java.awt.Dimension(100, 20));

        btCrNovoProduto.setBackground(new java.awt.Color(245, 246, 250));
        btCrNovoProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Add.png"))); // NOI18N
        btCrNovoProduto.setToolTipText("Novo Fornecedor");
        btCrNovoProduto.setMinimumSize(new java.awt.Dimension(25, 22));
        btCrNovoProduto.setPreferredSize(new java.awt.Dimension(25, 22));

        label5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label5.setForeground(new java.awt.Color(153, 153, 153));
        label5.setText("Código");

        label6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label6.setForeground(new java.awt.Color(153, 153, 153));
        label6.setText("Descrição");

        label8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label8.setForeground(new java.awt.Color(153, 153, 153));
        label8.setText("Quantidade");

        fQuantidade.setBackground(new java.awt.Color(245, 246, 250));
        fQuantidade.setText("1");
        fQuantidade.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fQuantidade.setPreferredSize(new java.awt.Dimension(100, 20));

        btAdicionarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Create.png"))); // NOI18N
        btAdicionarProduto.setText("Adicionar");
        btAdicionarProduto.setToolTipText("Adicionar Produto");
        btAdicionarProduto.setAlignmentY(0.0F);
        btAdicionarProduto.setMaximumSize(new java.awt.Dimension(80, 39));
        btAdicionarProduto.setMinimumSize(new java.awt.Dimension(80, 39));
        btAdicionarProduto.setOpaque(false);
        btAdicionarProduto.setPreferredSize(new java.awt.Dimension(80, 39));

        jPanelImagem.setMaximumSize(new java.awt.Dimension(125, 125));
        jPanelImagem.setMinimumSize(new java.awt.Dimension(125, 125));
        jPanelImagem.setPreferredSize(new java.awt.Dimension(125, 125));

        javax.swing.GroupLayout jPanelImagemLayout = new javax.swing.GroupLayout(jPanelImagem);
        jPanelImagem.setLayout(jPanelImagemLayout);
        jPanelImagemLayout.setHorizontalGroup(
            jPanelImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelImagem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
        );
        jPanelImagemLayout.setVerticalGroup(
            jPanelImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabelImagem.getAccessibleContext().setAccessibleName("");

        btPesquisaRapidaProduto.setBackground(new java.awt.Color(245, 246, 250));
        btPesquisaRapidaProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Find.png"))); // NOI18N
        btPesquisaRapidaProduto.setToolTipText("Pesquisa Produto");
        btPesquisaRapidaProduto.setMinimumSize(new java.awt.Dimension(25, 22));
        btPesquisaRapidaProduto.setPreferredSize(new java.awt.Dimension(25, 22));

        btRemoverProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Erase.png"))); // NOI18N
        btRemoverProduto.setText("Remover");
        btRemoverProduto.setToolTipText("Adicionar Produto");
        btRemoverProduto.setAlignmentY(0.0F);
        btRemoverProduto.setMaximumSize(new java.awt.Dimension(80, 39));
        btRemoverProduto.setMinimumSize(new java.awt.Dimension(80, 39));
        btRemoverProduto.setOpaque(false);
        btRemoverProduto.setPreferredSize(new java.awt.Dimension(80, 39));

        label9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label9.setForeground(new java.awt.Color(153, 153, 153));
        label9.setText("Em Estoque: ");

        fQtdEstoque.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        fQtdEstoque.setForeground(new java.awt.Color(0, 204, 51));
        fQtdEstoque.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        fIdProduto.setEditable(false);
        fIdProduto.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(fDescricaoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btPesquisaRapidaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(btAdicionarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btRemoverProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jComboBoxProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCrNovoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fIdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fQtdEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label9))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelImagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label5)
                    .addComponent(label6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btCrNovoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fDescricaoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btPesquisaRapidaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fIdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(label8)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btAdicionarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btRemoverProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(label9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fQtdEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(245, 246, 250));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Grupo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setAlignmentY(0.0F);

        fCodigoGrupo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigoGrupo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigoGrupo.setPreferredSize(new java.awt.Dimension(100, 20));

        fNomeGrupo.setBackground(new java.awt.Color(245, 246, 250));
        fNomeGrupo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNomeGrupo.setPreferredSize(new java.awt.Dimension(100, 20));

        btCrNovoGrupo.setBackground(new java.awt.Color(245, 246, 250));
        btCrNovoGrupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Add.png"))); // NOI18N
        btCrNovoGrupo.setToolTipText("Novo Fornecedor");
        btCrNovoGrupo.setMinimumSize(new java.awt.Dimension(25, 22));
        btCrNovoGrupo.setPreferredSize(new java.awt.Dimension(25, 22));

        label4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label4.setForeground(new java.awt.Color(153, 153, 153));
        label4.setText("Código");

        label11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label11.setForeground(new java.awt.Color(153, 153, 153));
        label11.setText("Descrição");

        fIdGrupo.setEditable(false);
        fIdGrupo.setFocusable(false);

        btPesquisaRapidaGrupo.setBackground(new java.awt.Color(245, 246, 250));
        btPesquisaRapidaGrupo.setForeground(new java.awt.Color(245, 246, 250));
        btPesquisaRapidaGrupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Find.png"))); // NOI18N
        btPesquisaRapidaGrupo.setToolTipText("Pesquisa Cliente");
        btPesquisaRapidaGrupo.setMinimumSize(new java.awt.Dimension(25, 22));
        btPesquisaRapidaGrupo.setPreferredSize(new java.awt.Dimension(25, 22));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fCodigoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(fNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btPesquisaRapidaGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jComboBoxGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCrNovoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fIdGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label4)
                            .addComponent(label11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btCrNovoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fCodigoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btPesquisaRapidaGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(fIdGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelCadastroProdutoLayout = new javax.swing.GroupLayout(jPanelCadastroProduto);
        jPanelCadastroProduto.setLayout(jPanelCadastroProdutoLayout);
        jPanelCadastroProdutoLayout.setHorizontalGroup(
            jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(336, 336, 336)
                .addComponent(jLabelTituloCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroProdutoLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanelCadastroProdutoLayout.setVerticalGroup(
            jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTituloCadastro))
                .addGap(18, 18, 18)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -15;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 15, 14, 18);
        jPanelCadastro.add(jPanelCadastroProduto, gridBagConstraints);

        jTabbedPaneCadastroVenda.addTab("Cadastro", jPanelCadastro);

        jPanelPesquisa.setBackground(new java.awt.Color(245, 246, 250));
        jPanelPesquisa.setMinimumSize(new java.awt.Dimension(920, 720));
        jPanelPesquisa.setPreferredSize(new java.awt.Dimension(920, 750));
        jPanelPesquisa.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBackground(new java.awt.Color(245, 246, 250));
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel7.setMinimumSize(new java.awt.Dimension(750, 650));
        jPanel7.setPreferredSize(new java.awt.Dimension(750, 750));

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");

        pesqCodigo.setBackground(new java.awt.Color(245, 246, 250));
        pesqCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqCodigo.setPreferredSize(new java.awt.Dimension(100, 20));

        label13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label13.setForeground(new java.awt.Color(153, 153, 153));
        label13.setText("Nr. Venda");

        btPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisa.setToolTipText("Pesquisar");

        label16.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        label16.setForeground(new java.awt.Color(51, 51, 51));
        label16.setText("Pesquisa de Vendas");
        label16.setAlignmentY(0.0F);

        jPanelListagemVendas.setBackground(new java.awt.Color(255, 102, 153));
        jPanelListagemVendas.setForeground(new java.awt.Color(255, 153, 153));
        jPanelListagemVendas.setPreferredSize(new java.awt.Dimension(864, 350));

        jTableListagem.setAutoCreateRowSorter(true);
        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableListagem);

        javax.swing.GroupLayout jPanelListagemVendasLayout = new javax.swing.GroupLayout(jPanelListagemVendas);
        jPanelListagemVendas.setLayout(jPanelListagemVendasLayout);
        jPanelListagemVendasLayout.setHorizontalGroup(
            jPanelListagemVendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanelListagemVendasLayout.setVerticalGroup(
            jPanelListagemVendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pesqSubTotalInicio.setBackground(new java.awt.Color(245, 246, 250));
        pesqSubTotalInicio.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqSubTotalInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pesqSubTotalInicio.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);

        label14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label14.setForeground(new java.awt.Color(153, 153, 153));
        label14.setText("Sub Total");

        pesqSubTotalFim.setBackground(new java.awt.Color(245, 246, 250));
        pesqSubTotalFim.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqSubTotalFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pesqSubTotalFim.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);

        label15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label15.setForeground(new java.awt.Color(153, 153, 153));
        label15.setText("a");

        label17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label17.setForeground(new java.awt.Color(153, 153, 153));
        label17.setText("Total");

        pesqTotalInicio.setBackground(new java.awt.Color(245, 246, 250));
        pesqTotalInicio.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqTotalInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pesqTotalInicio.setFocusCycleRoot(true);
        pesqTotalInicio.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);

        label18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label18.setForeground(new java.awt.Color(153, 153, 153));
        label18.setText("a");

        pesqTotalFim.setBackground(new java.awt.Color(245, 246, 250));
        pesqTotalFim.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqTotalFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pesqTotalFim.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);

        jPanel6.setBackground(new java.awt.Color(245, 246, 250));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, " Cliente ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanel6.setAlignmentX(0.0F);
        jPanel6.setAlignmentY(0.0F);

        pesqCodigoCliente.setBackground(new java.awt.Color(245, 246, 250));
        pesqCodigoCliente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqCodigoCliente.setPreferredSize(new java.awt.Dimension(100, 20));

        pesqNomeCliente.setBackground(new java.awt.Color(245, 246, 250));
        pesqNomeCliente.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqNomeCliente.setPreferredSize(new java.awt.Dimension(100, 20));

        label21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label21.setForeground(new java.awt.Color(153, 153, 153));
        label21.setText("Código");

        label22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label22.setForeground(new java.awt.Color(153, 153, 153));
        label22.setText("Nome");

        pesqBtPesquisaRapidaCliente.setBackground(new java.awt.Color(245, 246, 250));
        pesqBtPesquisaRapidaCliente.setForeground(new java.awt.Color(245, 246, 250));
        pesqBtPesquisaRapidaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Find.png"))); // NOI18N
        pesqBtPesquisaRapidaCliente.setToolTipText("Pesquisa Cliente");
        pesqBtPesquisaRapidaCliente.setMinimumSize(new java.awt.Dimension(25, 22));
        pesqBtPesquisaRapidaCliente.setPreferredSize(new java.awt.Dimension(25, 22));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(pesqCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(pesqNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pesqBtPesquisaRapidaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(pesqComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label21)
                    .addComponent(label22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pesqCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pesqNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pesqComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pesqBtPesquisaRapidaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pesqDataVenda.setBackground(new java.awt.Color(245, 246, 250));
        pesqDataVenda.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            pesqDataVenda.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        pesqDataVenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pesqDataVenda.setAlignmentX(0.0F);
        pesqDataVenda.setAlignmentY(0.0F);
        pesqDataVenda.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        pesqDataVenda.setMaximumSize(new java.awt.Dimension(111, 20));
        pesqDataVenda.setMinimumSize(new java.awt.Dimension(100, 20));
        pesqDataVenda.setPreferredSize(new java.awt.Dimension(100, 20));

        label23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label23.setForeground(new java.awt.Color(153, 153, 153));
        label23.setText("Data da Venda");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 200, Short.MAX_VALUE))))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(357, 357, 357)
                                .addComponent(label16))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                        .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(label13)
                                        .addGap(93, 93, 93)))
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pesqSubTotalInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pesqSubTotalFim, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pesqTotalInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pesqTotalFim, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label23)
                                    .addComponent(pesqDataVenda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanelListagemVendas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(label16)
                .addGap(40, 40, 40)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label13)
                            .addComponent(label14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqSubTotalInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label15)
                            .addComponent(pesqSubTotalFim, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label17)
                            .addComponent(label23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pesqTotalInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label18)
                            .addComponent(pesqTotalFim, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(17, 17, 17)
                .addComponent(btPesquisa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelListagemVendas, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btExcluir)
                    .addComponent(btEditar))
                .addGap(149, 149, 149))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.ipady = 90;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanelPesquisa.add(jPanel7, gridBagConstraints);

        jTabbedPaneCadastroVenda.addTab("Pesquisa", jPanelPesquisa);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroVenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroVenda, javax.swing.GroupLayout.DEFAULT_SIZE, 971, Short.MAX_VALUE)
        );

        jTabbedPaneCadastroVenda.getAccessibleContext().setAccessibleName("Cadastro");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdicionarProduto;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btCrNovoCliente;
    private javax.swing.JButton btCrNovoGrupo;
    private javax.swing.JButton btCrNovoProduto;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btPesquisa;
    private javax.swing.JButton btPesquisaRapidaCliente;
    private javax.swing.JButton btPesquisaRapidaGrupo;
    private javax.swing.JButton btPesquisaRapidaProduto;
    private javax.swing.JButton btRemoverProduto;
    private javax.swing.JButton btSalvar;
    private javax.swing.JLabel fCodigo;
    private javax.swing.JTextField fCodigoCliente;
    private javax.swing.JTextField fCodigoGrupo;
    private javax.swing.JTextField fCodigoProduto;
    private javax.swing.JFormattedTextField fDataVenda;
    private javax.swing.JFormattedTextField fDescontoPercent;
    private javax.swing.JFormattedTextField fDescontoValor;
    private javax.swing.JTextField fDescricaoProduto;
    private javax.swing.JTextField fIdCliente;
    private javax.swing.JTextField fIdFomaPagamento;
    private javax.swing.JTextField fIdGrupo;
    private javax.swing.JTextField fIdProduto;
    private javax.swing.JTextField fNomeCliente;
    private javax.swing.JTextField fNomeGrupo;
    private javax.swing.JTextField fNrEntrada;
    private javax.swing.JTextField fNrParcelas;
    private javax.swing.JLabel fQtdEstoque;
    private javax.swing.JTextField fQuantidade;
    private javax.swing.JFormattedTextField fSubTotal;
    private javax.swing.JFormattedTextField fTotalPago;
    private javax.swing.JFormattedTextField fValorTotal;
    private javax.swing.JTextField id;
    private javax.swing.JComboBox jComboBoxCliente;
    private javax.swing.JComboBox<String> jComboBoxFormaPagamento;
    private javax.swing.JComboBox jComboBoxGrupo;
    private javax.swing.JComboBox jComboBoxProduto;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelImagem;
    private javax.swing.JLabel jLabelTituloCadastro;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelCadastro;
    private javax.swing.JPanel jPanelCadastroProduto;
    private javax.swing.JPanel jPanelImagem;
    private static javax.swing.JPanel jPanelListagemVendas;
    private javax.swing.JPanel jPanelPesquisa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPaneCadastroVenda;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JTable jTableListagemProdutosVenda;
    private javax.swing.JLabel label11;
    private javax.swing.JLabel label12;
    private javax.swing.JLabel label13;
    private javax.swing.JLabel label14;
    private javax.swing.JLabel label15;
    private javax.swing.JLabel label16;
    private javax.swing.JLabel label17;
    private javax.swing.JLabel label18;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label21;
    private javax.swing.JLabel label22;
    private javax.swing.JLabel label23;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel label5;
    private javax.swing.JLabel label6;
    private javax.swing.JLabel label7;
    private javax.swing.JLabel label8;
    private javax.swing.JLabel label9;
    private javax.swing.JLabel labelCifrao;
    private javax.swing.JButton pesqBtPesquisaRapidaCliente;
    private javax.swing.JTextField pesqCodigo;
    private javax.swing.JTextField pesqCodigoCliente;
    private javax.swing.JComboBox pesqComboBoxCliente;
    private javax.swing.JFormattedTextField pesqDataVenda;
    private javax.swing.JTextField pesqNomeCliente;
    private javax.swing.JFormattedTextField pesqSubTotalFim;
    private javax.swing.JFormattedTextField pesqSubTotalInicio;
    private javax.swing.JFormattedTextField pesqTotalFim;
    private javax.swing.JFormattedTextField pesqTotalInicio;
    // End of variables declaration//GEN-END:variables




}