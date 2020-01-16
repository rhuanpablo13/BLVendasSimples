package sistemavendas.visualizacao;

import infra.abstratas.View;
import infra.comunicacao.Erro;
import infra.comunicacao.Message;
import infra.comunicacao.Sucess;
import infra.comunicacao.Warning;
import infra.listagem.DefaultTableModelProduto;
import infra.operacoes.Operacao;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import infra.visualizacao.NFornecedorCellRenderer;
import infra.visualizacao.NFornecedorComboBox;
import infra.visualizacao.NGrupoCellRenderer;
import infra.visualizacao.NGrupoComboBox;
import infra.visualizacao.NMedidaCellRenderer;
import infra.visualizacao.NMedidaComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import sistemavendas.arquivos.Arquivo;
import sistemavendas.arquivos.Imagem;
import sistemavendas.cadastrosrapidos.CRFornecedor;
import sistemavendas.cadastrosrapidos.CRGrupo;
import sistemavendas.cadastrosrapidos.CRMedida;
import sistemavendas.controle.CFornecedor;
import sistemavendas.controle.CGrupo;
import sistemavendas.controle.CMedida;
import sistemavendas.controle.CProduto;
import sistemavendas.negocio.NFornecedor;
import sistemavendas.negocio.NGrupo;
import sistemavendas.negocio.NMedida;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NProduto;
import sistemavendas.negocio.NUsuario;


public final class ViewProduto extends View<NProduto> {

    
    private static final long serialVersionUID = 1L;
    private NProduto produtoOld;
    private CProduto produtoController;
    private CFornecedor fornecedorController;
    
    
    private Double vlCusto = new Double(-1);
    private Double vlVenda = new Double(-1);
    private Double vlLucro = new Double(-1);
    private Double percentLucro = new Double(-1);
    
    
    public ViewProduto() {
        initComponents();
        initViewProduto();
        initMasksValidators();
        initializeView();
        carregaComboBoxFornecedores();
        carregaComboBoxGrupo();
        carregaComboBoxMedidas();
        configuraListagem();
        carregarCodigo(); 
        carregarCodigoBarras();
    }

    
    private void initViewProduto() {
        produtoOld = new NProduto();
        produtoController = new CProduto();
        fornecedorController = new CFornecedor();
        this.id.setVisible(false); 
        
        this.btSalvar.setEnabled(false);
        this.btEditar.setEnabled(false);
        this.btExcluir.setEnabled(false);
        this.btPesquisa.setEnabled(false);
        
        
        this.setName("view_produtos");
        this.jLabelTituloCadastro.setText("Cadastro de Produto");
        this.fValorCusto.setName("fValorCusto");
        this.fValorLucro.setName("fValorLucro");
        this.fValorVenda.setName("fValorVenda");
        this.fPercentLucro.setName("fPercentLucro");
        jTabbedPaneCadastroProduto.setTitleAt(0, "Cadastrar");
        jTabbedPaneCadastroProduto.setTitleAt(1, "Pesquisar");        
        //preencheCampos(); 
        
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
    
    
    
    private void carregarCodigo() {
        CProduto controller = new CProduto();
        try {
            NProduto produto = controller.recuperaUltimoRegistro();
            this.fCodigo.setText(Integer.toString(produto.getCodigo()+1));
        } catch (Exception ex) {
            this.fCodigo.setText("1");
        }
    }

    private void carregarCodigoBarras() {
        NProduto produto = new NProduto();
        Integer codigoBarras = produto.gerarCodigoBarras();
        this.fCodigoBarras.setText(codigoBarras.toString());
    }
    
    
    private void configuraListagem() { 
        DefaultTableModelProduto modelo = new DefaultTableModelProduto();
        modelo.addColumn("#");
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("R$ Custo");
        modelo.addColumn("R$ Venda");
        modelo.addColumn("% Lucro");
        modelo.addColumn("Qtd. Estoque");
        modelo.addColumn("Ativo");
        jTableListagem.setModel(modelo);
        
        // id
        jTableListagem.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTableListagem.getColumnModel().getColumn(0).setMaxWidth(40);
        // codigo
        jTableListagem.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableListagem.getColumnModel().getColumn(1).setMaxWidth(60);
        // nome
        jTableListagem.getColumnModel().getColumn(2).setPreferredWidth(150); 
        //Custo
        jTableListagem.getColumnModel().getColumn(3).setPreferredWidth(20); 
        // Venda
        jTableListagem.getColumnModel().getColumn(4).setPreferredWidth(20);
        // Lucro
        jTableListagem.getColumnModel().getColumn(5).setPreferredWidth(20);
        // Estoque
        jTableListagem.getColumnModel().getColumn(6).setPreferredWidth(30);
        // Ativo
        jTableListagem.getColumnModel().getColumn(7).setPreferredWidth(15);
    }
    
    
    public void carregaComboBoxFornecedores() {
        try {
            CFornecedor controller = new CFornecedor();
            NFornecedor parametro = new NFornecedor();
            parametro.setAtivo(true);
            List<NFornecedor> items = controller.pesquisar(parametro);
            NFornecedorComboBox cbmodel = new NFornecedorComboBox(items);
            
            if (! fCodigoFornecedor.getText().isEmpty()) {
                try {
                    int codigo = Integer.parseInt(fCodigoFornecedor.getText());
                    jComboBoxFornecedor.setModel(cbmodel);
                    cbmodel.updateForCodigo(codigo);
                } catch (NumberFormatException e) {
                    new Warning("Informe apenas o código do fornecedor", "Atenção").show();
                }
            } else {
                cbmodel.setSelectedLastElement();
            }
            jComboBoxFornecedor.setModel(cbmodel);
            jComboBoxFornecedor.setRenderer(new NFornecedorCellRenderer());
            
        } catch(Exception e) {e.printStackTrace();}
    }
    
    
    public void carregaComboBoxMedidas() {
        try {
            CMedida medidaController = new CMedida();
            List<NMedida> items = medidaController.getLista();
            NMedidaComboBox cbmodel = new NMedidaComboBox(items);
            cbmodel.setSelectedLastElement();
            jComboBoxMedida.setModel(cbmodel);
            jComboBoxMedida.setRenderer(new NMedidaCellRenderer());
        } catch(Exception e) {e.printStackTrace();}
    }

    
    public void carregaComboBoxGrupo() {
        try {
            CGrupo grupoController = new CGrupo();
            List<NGrupo> items = grupoController.getLista();
            NGrupoComboBox cbmodel = new NGrupoComboBox(items);
            
            if (! fCodigoGrupo.getText().isEmpty()) {
                
                try {
                    int codigo = Integer.parseInt(fCodigoGrupo.getText());
                    jComboBoxGrupo.setModel(cbmodel);
                    cbmodel.updateForCodigo(codigo);
                } catch (NumberFormatException e) {
                    new Warning("Informe apenas o código do grupo", "Atenção").show();
                }
                
            } else {
                cbmodel.setSelectedLastElement();
            }
            jComboBoxGrupo.setModel(cbmodel);
            jComboBoxGrupo.setRenderer(new NGrupoCellRenderer());
            
        } catch (Exception e2) {}
    }


    public void model2View(NProduto produto) {
        limparCampos();
        produtoOld = produto;
        setValuesModel2View(produto);
        
        carregarImagemSelecionada(produto.getSrcImg());
        
        // desabilitar codigo de barras
        this.fCodigoBarras.setText(produto.getCodigoBarras().toString());
        this.fCodigoBarras.setEditable(false);
        this.fGerarCodigoBarras.setSelected(false);
        this.fGerarCodigoBarras.setEnabled(false);
    }
    
    
    
    
    public NProduto view2Model() {        
        NProduto produto = (NProduto) setValuesView2Model();        
        
        if (! operacao.equals(Operacao.PESQUISAR)) {
            NFornecedor fornecedor = selectedFornecedor();
            NMedida medida = selectedMedida();
            NGrupo grupo = selectedGrupo();
            
            produto.setCodigoBarras(Integer.parseInt(fCodigoBarras.getText()));
            produto.setIdFornecedor(fornecedor.getId());
            produto.setIdUnidadeMedidaComercial(medida.getId());
            produto.setIdGrupo(grupo.getId());
            produto.setGerarCodigoBarras(fGerarCodigoBarras.isSelected());
            if (!caminhoImagem.getText().isEmpty()) {
                produto.setSrcImg(caminhoImagem.getText());
            }
        }
        return produto;
    }

    
    private NFornecedor selectedFornecedor() {
        NFornecedorComboBox localModel = (NFornecedorComboBox) jComboBoxFornecedor.getModel();
        return (NFornecedor) localModel.getSelectedItem();
    }
    
    private NMedida selectedMedida() {
        NMedidaComboBox localModel = (NMedidaComboBox) jComboBoxMedida.getModel();
        return (NMedida) localModel.getSelectedItem();
    }

    private NGrupo selectedGrupo() {
        NGrupoComboBox localModel = (NGrupoComboBox) jComboBoxGrupo.getModel();
        return (NGrupo) localModel.getSelectedItem();
    }

    
    private void limparCampos() {        
        String codigo = fCodigo.getText();
        cleanViewFields();
        fCodigo.setText(codigo);
        srcImg.setIcon(null);
        this.caminhoImagem.setVisible(false);
        //#,##0
    }
    
    
    private NProduto recebeDadosPesquisar() {
        NProduto produto = view2Model();
        return produto;
    }
    
    
    private NProduto getProdutoSelecionado() throws Exception {
        int index = jTableListagem.getSelectedRow();
        if (index > -1) {
            int idLocal = (int) jTableListagem.getValueAt(index, 0);
            CProduto controller = new CProduto();
            try {
                return controller.buscarPorId(idLocal);
            } catch (Exception ex) {
                throw new Warning("Nenhuma produto encontrado", "Atenção");
            }
        }
        throw new Warning("Selecione um produto da lista", "Atenção");
    }
    
    
    private void mudarAba(int index, String label) {
        jTabbedPaneCadastroProduto.setSelectedIndex(index);
        jTabbedPaneCadastroProduto.setTitleAt(index, label);
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

        
        btNovoCrGrupo.addActionListener((ActionEvent e) -> {
            CRGrupo crGrupo = new CRGrupo();
            crGrupo.init();
            crGrupo.addWindowListener(new java.awt.event.WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        carregaComboBoxGrupo();
                    } catch(Exception e3) { e3.printStackTrace(); }
                }
            });
        });
        
        
        btNovoCrFornecedor.addActionListener((ActionEvent e) -> {
            CRFornecedor crFornecedor = new CRFornecedor();
            crFornecedor.init();
            crFornecedor.addWindowListener(new WindowAdapter() {
                
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        System.out.println("carregaComboBoxFornecedores");
                        carregaComboBoxFornecedores();
                    } catch (Exception e2) {e2.printStackTrace();}
                }
                
            });
        });

        
        btNovoCrMedida.addActionListener((ActionEvent e) -> {
            CRMedida crMedida = new CRMedida();
            crMedida.init();
            crMedida.addWindowListener(new WindowAdapter() {
                
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        carregaComboBoxMedidas();
                    } catch (Exception e2) {e2.printStackTrace();}
                }
                
            });
        });

        
        fGerarCodigoBarras.addActionListener((ActionEvent e) -> {
            if (fGerarCodigoBarras.isSelected()) {
                this.fCodigoBarras.setEditable(false);
                carregarCodigoBarras();
                //validator.removeValidation(fCodigoBarras);
            } else {
                this.fCodigoBarras.setEditable(true);                
                validator.required(fCodigoBarras);
            }
        });
        
        
        
        /**
         * Botão de salvar imagem
         */
        btSrcImg.addActionListener((ActionEvent e) -> {
            carregarImagemSelecionada();
        });
        
        
        /**
         * Inicia as actions do combobox de fornecedor
         */
        fCodigoFornecedor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doHighlight = () -> {
                    carregaComboBoxFornecedores();
                };       
                SwingUtilities.invokeLater(doHighlight);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doHighlight = () -> {
                    carregaComboBoxFornecedores();
                };       
                SwingUtilities.invokeLater(doHighlight);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {}
        });
        
        
        
        /**
         * Inicia as actions do combobox de grupo
         */
        fCodigoGrupo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doHighlight = () -> {
                    carregaComboBoxGrupo();
                };       
                SwingUtilities.invokeLater(doHighlight);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Runnable doHighlight = () -> {
                    carregaComboBoxGrupo();
                };       
                SwingUtilities.invokeLater(doHighlight);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {}
        });
        
        
        
        
        fValorCusto.addFocusListener(new FocusAdapter() {
            
            @Override
            public void focusLost(FocusEvent e) {
                atualizaFinanceiro(fValorCusto);
            }
            
        });

        
        fValorVenda.addFocusListener(new FocusAdapter() {
            
            @Override
            public void focusLost(FocusEvent e) {
                atualizaFinanceiro(fValorVenda);
            }
            
        });
        
        
        fValorLucro.addFocusListener(new FocusAdapter() {
            
            @Override
            public void focusLost(FocusEvent e) {
                atualizaFinanceiro(fValorLucro);
            }
            
        });
        
        
        fPercentLucro.addFocusListener(new FocusAdapter() {
            
            @Override
            public void focusLost(FocusEvent e) {
                atualizaFinanceiro(fPercentLucro);
            }
            
        });
        
    }


    protected void atualizaFinanceiro(JTextField field) {
    
        System.out.println(field.getName());
        if (field.getText().toUpperCase().contains("incorreto".toUpperCase())) {
            return;
        }
        
        if (field.getText().equalsIgnoreCase(",")) {
            field.setText("0,00");
        }
        switch (field.getName()) {
            case "fValorCusto":
                try {
                    vlCusto = toDouble(fValorCusto);
                    
                    if (vlVenda > -1 && !Objects.equals(produtoOld.getValorCusto(), vlCusto)) {
                        vlVenda = toDouble(fValorVenda);
                        
                        // % Lucro
                        percentLucro = calculaPercentLucro(vlCusto, vlVenda);
                        
                        // R$ Lucro
                        vlLucro = calculaValorLucro(vlCusto, vlVenda);
                        
                        fPercentLucro.setText(doubleToView(percentLucro));
                        fValorLucro.setText(doubleToView(vlLucro));
                        
                        produtoOld.setValorCusto(vlCusto);
                        produtoOld.setValorLucro(vlLucro);
                        produtoOld.setValorVenda(vlVenda);
                        produtoOld.setPercentLucro(percentLucro);
                    }
                    
                } catch (NumberFormatException e) {System.out.println("erro fValorCusto");}
                
                break;

            case "fValorVenda":
                try {
                    vlVenda = toDouble(fValorVenda);
                    
                    if (vlCusto > -1 && !Objects.equals(produtoOld.getValorVenda(), vlVenda) && !fValorCusto.getText().isEmpty()) {
                        
                        
                        vlCusto      = toDouble(fValorCusto);
                        percentLucro = calculaPercentLucro(vlCusto, vlVenda);
                        System.out.println("aqui");
                        vlLucro      = calculaValorLucro(vlCusto, vlVenda);
                        
                        fPercentLucro.setText(doubleToView(percentLucro));
                        fValorLucro.setText(doubleToView(vlLucro));
                        
                        produtoOld.setValorCusto(vlCusto);
                        produtoOld.setValorLucro(vlLucro);
                        produtoOld.setValorVenda(vlVenda);
                        produtoOld.setPercentLucro(percentLucro);
                    }
                } catch (NumberFormatException e) {e.printStackTrace(); System.out.println("erro fValorVenda");}
                
                break;
                
                
            case "fValorLucro":
                try {
                    vlLucro = toDouble(fValorLucro);
                    
                    if (vlCusto > -1 && !Objects.equals(produtoOld.getValorLucro(), vlLucro) && !fValorCusto.getText().isEmpty()) {
                        vlCusto      = toDouble(fValorCusto);
                        vlVenda      = calculaValorVendaPorValorLucro(vlCusto, vlLucro);
                        percentLucro = calculaPercentLucro(vlCusto, vlVenda);
                        
                        fValorVenda.setText(doubleToView(vlVenda));
                        fPercentLucro.setText(doubleToView(percentLucro));
                        
                        produtoOld.setValorCusto(vlCusto);
                        produtoOld.setValorLucro(vlLucro);
                        produtoOld.setValorVenda(vlVenda);
                        produtoOld.setPercentLucro(percentLucro);
                    }
                } catch (NumberFormatException e) { e.printStackTrace(); System.out.println("erro fValorLucro");}
                
                break;

                
                
            case "fPercentLucro":
                try {
                    if (vlCusto > -1 && fPercentLucro != null && !fPercentLucro.getText().isEmpty() && !fPercentLucro.getText().isEmpty() && !fValorVenda.getText().isEmpty() && !fValorLucro.getText().isEmpty()) {
                        percentLucro = toDouble(fPercentLucro);
                        vlCusto      = toDouble(fValorCusto);
                        vlVenda      = calculaValorVendaPorPercentLucro(vlCusto, percentLucro);
                        vlLucro      = calculaValorLucro(vlCusto, vlVenda);
                        fValorVenda.setText(doubleToView(vlVenda));
                        fValorLucro.setText(doubleToView(vlLucro));
                        fPercentLucro.setText(doubleToView(percentLucro));
                        
                        produtoOld.setValorCusto(vlCusto);
                        produtoOld.setValorLucro(vlLucro);
                        produtoOld.setValorVenda(vlVenda);
                        produtoOld.setPercentLucro(percentLucro);
                    }
                } catch (NumberFormatException e) {System.out.println("erro fPercentLucro");}
                
                break;                
        }
        
    }
    
    /**
     * Calcula a porcentagem de lucro sobre um valor de custo e um valor de venda.
     * A Escala de porcentagem é em decimal. Por exemplo:
     * vlCusto = 100.00
     * vlVenda = 150.00
     * retorno: % Lucro = 50.00
     * 
     * @param vlCusto
     * @param vlVenda
     * @return double
     */
    private double calculaPercentLucro(double vlCusto, double vlVenda) {
        //return Math.round(( (vlVenda - vlCusto) / vlCusto ) * 100);
        Double lucro = ((vlVenda - vlCusto) / vlCusto ) * 100;
        return  converterDoubleDoisDecimais(lucro);
    }
    
    
    /**
     * Calcula o valor de venda tendo como referencia um valor de custo e a
     * porcentagem de lucro que se deseja ter sobre este valor de custo. Exemplo:
     * 
     * vlCusto = 100.00
     * percentLucro = 50%
     * 
     * retorno: vlVenda = 150.00
     * 
     * @param vlCusto
     * @param percentLucro
     * @return double
     */
    private double calculaValorVenda(double vlCusto, double percentLucro) {
        return (vlCusto * percentLucro) / 100;
    }
    
    
    /**
     * Calcula o valor de venda que deverá ser cobrado para se ter um vlLucro desejado.
     * Por exemplo, se desejar ter um valor de lucro de 50,00 reais sobre 100,00 reais,
     * então o produto deve ser vendido pela somatória do custo com o lucro, 
     * ou seja, 150,00 reais.
     * 
     * @param vlCusto
     * @param vlLucro
     * @return double
     */
    private double calculaValorVendaPorValorLucro(double vlCusto, double vlLucro) {
        return vlCusto + vlLucro;
    }
    
    
    /**
     * Calcula por quanto um produto precisa ser vendido para que se tenha a
     * porcentagem de lucro desejada. Exemplo:
     * 
     * vlCusto = 100,00
     * percentLucro = 50%
     * retorno: vlVenda = 150,00
     * 
     * @param vlCusto
     * @param percentLucro
     * @return double
     */
    private double calculaValorVendaPorPercentLucro(double vlCusto, double percentLucro) {
        return ((vlCusto * percentLucro) / 100) + vlCusto ;
    }
    
    
    /**
     * Calcula o valor de lucro que vai ser obtido por um produto sendo vendido
     * por um valor determinado. Exemplo:
     * 
     * vlCusto = 100,00
     * vlVenda = 150,00
     * retorno: vlLucro = 50,00
     * 
     * @param vlCusto
     * @param vlVenda
     * @return 
     */
    private double calculaValorLucro(double vlCusto, double vlVenda) {
        return vlVenda - vlCusto;
    }
    
    
    private void clearTableList() {
        DefaultTableModel modelo = (DefaultTableModel) jTableListagem.getModel();
        modelo.setNumRows(0);
    }    
    

    protected void refreshTableList(List<NProduto> dataList) {
        try {            
            DefaultTableModelProduto modelo = (DefaultTableModelProduto) jTableListagem.getModel();
            modelo.setNumRows(0);
            
            dataList.stream().forEach((nProduto) -> {
                String valorCusto = "R$ "+doubleToView(nProduto.getValorCusto());
                String valorVenda = "R$ "+doubleToView(nProduto.getValorVenda());
                String percentLucro = nProduto.getPercentLucro()+"%";
                String qtdEst = nProduto.getQtdEstoque() + " - " + estoque2View(nProduto);
                
                modelo.addRow(new Object[] {
                    nProduto.getId(),
                    nProduto.getCodigo(),
                    nProduto.getNome(),
                    valorCusto,
                    valorVenda,
                    percentLucro,
                    qtdEst,
                    ativo2View(nProduto.isAtivo())  
                });
            });
            
        } catch (Exception e) {e.printStackTrace();}
    }
    
    private String estoque2View(NProduto p) {
        int idMedida = p.getIdUnidadeMedidaComercial();
        CMedida medida = new CMedida();
        try {
            NMedida m = medida.buscarPorId(idMedida);
            return m.getDescricao();
        } catch (Erro e) {e.printStackTrace();}
        return "";
    }
    
    private String ativo2View(boolean ativo) {
        return ativo ? "Sim" : "Não";
    }
    
    
    
    private boolean isCadastro() {
        String titleLocal = jTabbedPaneCadastroProduto.getTitleAt(0);
        return titleLocal.equalsIgnoreCase("Cadastrar");
    }
    
    
    
    
    
    private void initMasksValidators() {
        validator.required(fCodigo);
        //validator.required(fCodigoBarras);
        validator.required(fNome);
        validator.fieldMonetaryValue(fValorVenda, produtoOld.getValorVenda(), false, true, "isMoneyValid", new Validador(), true, new Double(0), new Double(99999));
        validator.fieldMonetaryValue(fValorLucro, produtoOld.getValorLucro(), false, true, "isMoneyValid", new Validador(), true, new Double(0), new Double(99999));
        validator.fieldMonetaryValue(fValorCusto, produtoOld.getValorCusto(), false, true, "isMoneyValid", new Validador(), true, new Double(0), new Double(99999));
        validator.requiredValue(fQtdEstoque, true, 0, 100000000);        
    }
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    private void onClickCancelar() {
        limparCampos();
        mudarAba(0, "Cadastrar");
        carregarCodigo();
        ViewPrincipal.showDashBoard();
    }
    
    private void onClickSalvar() {
        
        String msg = "";
        if (isCadastro()) {
            setOperacao(Operacao.CADASTRO);
            msg = "Confirma o cadastro ?";
        } else {
            setOperacao(Operacao.ALTERAR);
            msg = "Confirma a alteração ?";
        }
        
        produtoController = new CProduto();
        int i = Utils.pedeConfirmacao(msg, this);

        if (i == 0) {
            NProduto c = view2Model();
            System.out.println(c.toString());
            
            if (operacao == Operacao.CADASTRO) {
                try {
                    produtoController.inserir(c);
                    new Sucess("Registro salvo com sucesso!", "Sucesso").show();
                    limparCampos();
                    fGerarCodigoBarras.setEnabled(true);
                } catch (Erro ex) {
                    ex.show();
                    return;
                }
            } else {
                try {
                    produtoController.alterar(c);
                    new Sucess("Registro alterado com sucesso!", "Sucesso").show();
                    limparCampos();
                    fGerarCodigoBarras.setEnabled(true);
                } catch (Erro ex) {
                    ex.show();
                    return;
                }
            }
            
            mudarAba(0, "Cadastrar");
            carregarCodigoBarras();
            carregarCodigo();
        }
    }
    
    private void onClickEditar() {
        try {            
            NProduto produto = getProdutoSelecionado();
            model2View(produto);
            mudarAba(0, "Alterar");
            setOperacao(Operacao.ALTERAR);
        } catch (Exception ex) {
            ex.printStackTrace();
            out.show(ex);
        }
    }
    
    
    private void onClickExcluir() {
        try {
            NProduto c = getProdutoSelecionado();
            if (c != null) {                
                CProduto controller = new CProduto();
                List<NProduto> lote = controller.getLoteProduto(c);
                
                /* Atualiza a tabela PRODUTO_CODIGO_BARRAS e atualiza o código de barras na tabela PRODUTO */
                if (!lote.isEmpty()) {                    
                    ViewExclusaoProdutos telaExclusaoMultipla = new ViewExclusaoProdutos();
                    telaExclusaoMultipla.setVisible(true);
                    telaExclusaoMultipla.adicionarProdutos(lote);
                    telaExclusaoMultipla.addWindowListener(new WindowAdapter() {
                        
                        @Override
                        public void windowClosing(WindowEvent e) {
                            System.out.println("2");
                            try {
                                clearTableList();
                                CProduto controller = new CProduto();
                                List<NProduto> list = controller.getLista();
                                refreshTableList(list);
                            } catch (Erro ex) {
                                Logger.getLogger(ViewProduto.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                    });

                } else {
                    
                    /* Se não tiver registros na tabela PRODUTO_CODIGO_BARRAS */
                    int i = Utils.pedeConfirmacao("Deseja excluir o registro: " + c.getCodigo(), this);
                    if (i==0) {                        
                        
                        try {
                            produtoController.excluir(c.getCodigo());
                            new Sucess("Produto excluido com sucesso!", "Exclusão de Produto").show();
                            controller = new CProduto();
                            List<NProduto> list = controller.getLista();
                            clearTableList();
                            refreshTableList(list);                            
                        } catch (Erro e) {
                            e.show();
                        }
                    }
                }
                
            } else {
                new Warning("Você deve selecionar um registro!", "Falha").show();
            }
        } catch (Exception ex) {
            new Warning("Falha ao excluir o registro selecionado!", "Falha").show();
        }
        carregarCodigoBarras();
        carregarCodigo();
        
    }    

    private void onClickPesquisar() {
        setOperacao(Operacao.PESQUISAR);
        NProduto produto = recebeDadosPesquisar();
        List<NProduto> lista = new ArrayList();
        try {
            produtoController = new CProduto();
            lista = produtoController.pesquisar(produto);
            refreshTableList(lista);
        } catch (Exception ex) {
            out.show(ex);
        }
        
        if (lista == null || lista.isEmpty()) {
            new Message("Nenhum produto encontrado", "Sem Registros").show();
        }
        
    }
    
    private void onClickLimparCampos() {
        limparCampos();
    }
    
    
    
    private void carregarImagemSelecionada() {
        srcImg.setText("");
        Arquivo arq = new Arquivo();
        File origem = arq.getPathOriginFile();
        if (origem != null) {
            caminhoImagem.setVisible(false);
            caminhoImagem.setText(origem.getAbsolutePath());
            srcImg.setIcon(new ImageIcon(Imagem.loadImage(origem, 200, 200, true)));
            panelImagem.add(srcImg);
            panelImagem.repaint();
        } else {
            srcImg.setIcon(null);
        }
    }


    private void carregarImagemSelecionada(String caminhoOrigem) {
        srcImg.setText("");
        if (caminhoOrigem.isEmpty()) return;
        File origem = new File(caminhoOrigem);
        caminhoImagem.setVisible(false);
        caminhoImagem.setText(origem.getAbsolutePath());
        srcImg.setIcon(new ImageIcon(Imagem.loadImage(origem, 200, 200)));        
        panelImagem.add(srcImg);
        panelImagem.repaint();
    }

    
    public void setAtivo(Boolean  ativo) {
        this.fAtivo.setSelected(ativo);
    }

    

    public void setCodigo(Integer codigo) {
        this.fCodigo.setText(Integer.toString(codigo));
    }

    

    public void setCodigoBarras(Integer codigoBarras) {
        this.fCodigoBarras.setText(Integer.toString(codigoBarras));
    }

    

    public void setCodigoFornecedor(Integer codigoFornecedor) {
        this.fCodigoFornecedor.setText(Integer.toString(codigoFornecedor));
    }

    

    public void setCodigoGrupo(Integer codigoGrupo) {
        this.fCodigoGrupo.setText(Integer.toString(codigoGrupo));
    }

    

    public void setLocalizacao(String localizacao) {
        this.fLocalizacao.setText(localizacao);
    }

    

    public void setNome(String nome) {
        this.fNome.setText(nome);
    }

    

//    public void setPercentDescontoMax(Double porcentagemDescontoMaximo) {
//        this.fPercentDescontoMax.setText(doubleToView(porcentagemDescontoMaximo));
//    }



    public void setPercentLucro(Double porcentagemLucro) {
        //this.fPercentLucro.setText("100,5");
        this.fPercentLucro.setText(doubleToView(porcentagemLucro));
    }

    

    public void setQtdEstoque(Integer qtdEstoque) {
        this.fQtdEstoque.setText(Integer.toString(qtdEstoque));
    }

    

    public void setQtdEstoqueMax(Integer qtdEstoqueMaximo) {
        this.fQtdEstoqueMax.setText(Integer.toString(qtdEstoqueMaximo));
    }

    

    public void setQtdEstoqueMin(Integer qtdEstoqueMinimo) {
        this.fQtdEstoqueMin.setText(Integer.toString(qtdEstoqueMinimo));
    }

    

    public void setValorCusto(Double valorCusto) {
        this.fValorCusto.setText(doubleToView(valorCusto));
    }

    

    public void setValorLucro(Double valorLucro) {
        this.fValorLucro.setText(doubleToView(valorLucro));
    }

    

    public void setValorVenda(Double valorVenda) {
        this.fValorVenda.setText(doubleToView(valorVenda));
    }

    

    public void setId(Integer id) {
        this.id.setText(Integer.toString(id));
    }


    
    public Boolean getAtivo() {
        return fAtivo.isSelected();
    }
    

    public Integer getCodigo() {
        return Integer.parseInt(fCodigo.getText());
    }
    

    public Integer getCodigoBarras() {
        return Integer.parseInt(fCodigoBarras.getText());
    }
    

    public Integer getCodigoFornecedor() {
        return Integer.parseInt(fCodigoFornecedor.getText());
    }
    
    
    public Integer getCodigoGrupo() {
        return Integer.parseInt(fCodigoGrupo.getText());
    }
    
    
    public String getLocalizacao() {
        return this.fLocalizacao.getText();
    }
    

    public String getNome() {
        return this.fNome.getText();
    }
    

//    public Double getPercentDescontoMax() {
//        return toDouble(this.fPercentDescontoMax);
//    }
    

    public Double getPercentLucro() {
        return toDouble(this.fPercentLucro);
    }
    

    public Integer getQtdEstoque() {
        return Integer.parseInt(this.fQtdEstoque.getText().replace(" ", ""));
    }
    

    public Integer getQtdEstoqueMax() {
        return Integer.parseInt(this.fQtdEstoqueMax.getText().replace(" ", ""));
    }
    

    public Integer getQtdEstoqueMin() {
        return Integer.parseInt(this.fQtdEstoqueMin.getText().replace(" ", ""));
    }
    

    public Double getValorCusto() {
        return toDouble(this.fValorCusto);
    }
    

    public Double getValorLucro() {
        return toDouble(this.fValorLucro);
    }
    

    public Double getValorVenda() {
        return toDouble(this.fValorVenda);
    }
    

    public Integer getId() {
        return Integer.parseInt(this.id.getText());
    }    

    
    
    
    
    
    
    /**
     * *******************************
     *  MÉTODOS USADOS PARA A PESQUISA
     * *******************************
     */
    
    public Boolean getPesqAtivo() {
        String value = pesqAtivo.getSelectedItem().toString();
        if (value.equalsIgnoreCase("Todos")) {
            return null;
        }
        if (value.equalsIgnoreCase("Ativo")) {
            return true;
        }
        return false;
    }
    

    public void setPesqAtivo(int pesqAtivo) {
        boolean b = Utils.int2Boolean(pesqAtivo);
        if (b) {
            this.pesqAtivo.setSelectedItem("Ativo");
        } else {
            this.pesqAtivo.setSelectedItem("Desativado");
        }
    }

    public int getPesqCodigo() {
        return Integer.parseInt(pesqCodigo.getText());
    }

    public void setPesqCodigo(int pesqCodigo) {
        this.pesqCodigo.setText(Integer.toString(pesqCodigo));
    }

    public String getPesqNome() {
        return pesqNome.getText();
    }

    public void setPesqNome(String pesqNome) {
        this.pesqNome.setText(pesqNome);
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

        jLabelUsuario24 = new javax.swing.JLabel();
        jTabbedPaneCadastroProduto = new javax.swing.JTabbedPane();
        panelCadProduto = new javax.swing.JPanel();
        jPanelCadastroProduto = new javax.swing.JPanel();
        jLabelTituloCadastro = new javax.swing.JLabel();
        fNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fCodigoFornecedor = new javax.swing.JTextField();
        fCodigoBarras = new javax.swing.JTextField();
        panelImagem = new javax.swing.JPanel();
        srcImg = new javax.swing.JLabel();
        btSrcImg = new javax.swing.JButton();
        jComboBoxMedida = new javax.swing.JComboBox<String>();
        btNovoCrMedida = new javax.swing.JButton();
        fAtivo = new javax.swing.JCheckBox();
        jLabelUsuario1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fCodigo = new javax.swing.JTextField();
        btNovoCrFornecedor = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        fCodigoGrupo = new javax.swing.JTextField();
        btNovoCrGrupo = new javax.swing.JButton();
        jLabelTituloCadastro1 = new javax.swing.JLabel();
        jLabelTituloCadastro2 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        btSalvar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        jComboBoxFornecedor = new javax.swing.JComboBox();
        jComboBoxGrupo = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        fQtdEstoque = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        fQtdEstoqueMin = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        fQtdEstoqueMax = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        fLocalizacao = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fValorCusto = new javax.swing.JFormattedTextField();
        fValorVenda = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        fPercentLucro = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        fValorLucro = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        labelDadosUsuario = new javax.swing.JLabel();
        fGerarCodigoBarras = new javax.swing.JCheckBox();
        jLabelUsuario3 = new javax.swing.JLabel();
        caminhoImagem = new javax.swing.JLabel();
        panelPesqProduto = new javax.swing.JPanel();
        jPanelPesquisaProduto = new javax.swing.JPanel();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        pesqCodigo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabelUsuario2 = new javax.swing.JLabel();
        pesqNome = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btPesquisa = new javax.swing.JButton();
        jLabelUsuario19 = new javax.swing.JLabel();
        jPanelListagemProduto = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListagem = new javax.swing.JTable();
        pesqAtivo = new javax.swing.JComboBox<String>();

        jLabelUsuario24.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario24.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUsuario24.setText("Salvar");

        setBackground(new java.awt.Color(245, 246, 250));
        setBorder(null);
        setTitle("Produtos");
        setMaximumSize(null);
        setMinimumSize(null);
        setNormalBounds(new java.awt.Rectangle(0, 0, 0, 0));

        jTabbedPaneCadastroProduto.setBackground(new java.awt.Color(245, 246, 250));
        jTabbedPaneCadastroProduto.setMaximumSize(new java.awt.Dimension(920, 790));
        jTabbedPaneCadastroProduto.setMinimumSize(new java.awt.Dimension(920, 790));
        jTabbedPaneCadastroProduto.setPreferredSize(new java.awt.Dimension(920, 790));

        panelCadProduto.setBackground(new java.awt.Color(245, 246, 250));
        panelCadProduto.setMaximumSize(new java.awt.Dimension(920, 790));
        panelCadProduto.setMinimumSize(new java.awt.Dimension(920, 790));
        panelCadProduto.setPreferredSize(new java.awt.Dimension(920, 790));
        panelCadProduto.setLayout(new java.awt.GridBagLayout());

        jPanelCadastroProduto.setBackground(new java.awt.Color(245, 246, 250));
        jPanelCadastroProduto.setMaximumSize(new java.awt.Dimension(920, 790));
        jPanelCadastroProduto.setMinimumSize(new java.awt.Dimension(920, 790));
        jPanelCadastroProduto.setName(""); // NOI18N
        jPanelCadastroProduto.setPreferredSize(new java.awt.Dimension(920, 790));

        jLabelTituloCadastro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro.setText("Cadastro de Produto");
        jLabelTituloCadastro.setAlignmentY(0.0F);

        fNome.setBackground(new java.awt.Color(245, 246, 250));
        fNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNome.setMinimumSize(new java.awt.Dimension(250, 20));
        fNome.setPreferredSize(new java.awt.Dimension(300, 20));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Nome:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("Código:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 153, 153));
        jLabel6.setText("Código de Barras:");

        fCodigoFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        fCodigoFornecedor.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigoFornecedor.setPreferredSize(new java.awt.Dimension(100, 20));

        fCodigoBarras.setEditable(false);
        fCodigoBarras.setBackground(new java.awt.Color(245, 246, 250));
        fCodigoBarras.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigoBarras.setPreferredSize(new java.awt.Dimension(250, 20));

        panelImagem.setBackground(new java.awt.Color(204, 204, 204));
        panelImagem.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)));
        panelImagem.setMinimumSize(new java.awt.Dimension(200, 200));
        panelImagem.setOpaque(false);
        panelImagem.setPreferredSize(new java.awt.Dimension(200, 200));

        srcImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        srcImg.setText("sem imagem...");
        srcImg.setAlignmentY(0.0F);
        srcImg.setDisplayedMnemonicIndex(0);
        srcImg.setMaximumSize(null);
        srcImg.setMinimumSize(new java.awt.Dimension(200, 200));
        srcImg.setPreferredSize(new java.awt.Dimension(200, 200));

        javax.swing.GroupLayout panelImagemLayout = new javax.swing.GroupLayout(panelImagem);
        panelImagem.setLayout(panelImagemLayout);
        panelImagemLayout.setHorizontalGroup(
            panelImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srcImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelImagemLayout.setVerticalGroup(
            panelImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srcImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btSrcImg.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btSrcImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/Folder.png"))); // NOI18N
        btSrcImg.setToolTipText("Carregar Imagem");
        btSrcImg.setAlignmentY(0.0F);
        btSrcImg.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btSrcImg.setOpaque(false);
        btSrcImg.setPreferredSize(new java.awt.Dimension(210, 40));

        jComboBoxMedida.setBackground(new java.awt.Color(245, 246, 250));
        jComboBoxMedida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxMedida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kilograma", "Unidade", "Centena" }));
        jComboBoxMedida.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxMedida.setPreferredSize(new java.awt.Dimension(100, 20));

        btNovoCrMedida.setBackground(new java.awt.Color(245, 246, 250));
        btNovoCrMedida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Add.png"))); // NOI18N
        btNovoCrMedida.setToolTipText("Nova Unidade de Medida");
        btNovoCrMedida.setMinimumSize(new java.awt.Dimension(25, 25));
        btNovoCrMedida.setPreferredSize(new java.awt.Dimension(25, 22));

        fAtivo.setBackground(new java.awt.Color(245, 246, 250));
        fAtivo.setSelected(true);
        fAtivo.setAlignmentY(0.0F);
        fAtivo.setMaximumSize(new java.awt.Dimension(25, 25));
        fAtivo.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario1.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario1.setText("Ativo");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Fornecedor:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Medida:");

        fCodigo.setEditable(false);
        fCodigo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigo.setMaximumSize(new java.awt.Dimension(118, 20));
        fCodigo.setMinimumSize(new java.awt.Dimension(118, 20));
        fCodigo.setPreferredSize(new java.awt.Dimension(118, 20));

        btNovoCrFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        btNovoCrFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Add.png"))); // NOI18N
        btNovoCrFornecedor.setToolTipText("Novo Fornecedor");
        btNovoCrFornecedor.setMinimumSize(new java.awt.Dimension(25, 22));
        btNovoCrFornecedor.setPreferredSize(new java.awt.Dimension(25, 22));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(153, 153, 153));
        jLabel15.setText("Grupo:");

        fCodigoGrupo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigoGrupo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigoGrupo.setPreferredSize(new java.awt.Dimension(100, 20));

        btNovoCrGrupo.setBackground(new java.awt.Color(245, 246, 250));
        btNovoCrGrupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Add.png"))); // NOI18N
        btNovoCrGrupo.setToolTipText("Novo Fornecedor");
        btNovoCrGrupo.setMinimumSize(new java.awt.Dimension(25, 22));
        btNovoCrGrupo.setPreferredSize(new java.awt.Dimension(25, 22));

        jLabelTituloCadastro1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro1.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro1.setText("Estoque");
        jLabelTituloCadastro1.setAlignmentY(0.0F);

        jLabelTituloCadastro2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro2.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro2.setText("Financeiro");
        jLabelTituloCadastro2.setAlignmentY(0.0F);

        id.setEditable(false);
        id.setFocusable(false);

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Salvar Registro");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));

        jComboBoxFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        jComboBoxFornecedor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxFornecedor.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxFornecedor.setPreferredSize(new java.awt.Dimension(100, 20));

        jComboBoxGrupo.setBackground(new java.awt.Color(245, 246, 250));
        jComboBoxGrupo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBoxGrupo.setMinimumSize(new java.awt.Dimension(100, 20));
        jComboBoxGrupo.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setText("Qtde em Estoque:");

        fQtdEstoque.setBackground(new java.awt.Color(245, 246, 250));
        fQtdEstoque.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fQtdEstoque.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 153, 153));
        jLabel12.setText("Estoque Mínimo: ");

        fQtdEstoqueMin.setBackground(new java.awt.Color(245, 246, 250));
        fQtdEstoqueMin.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fQtdEstoqueMin.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setText("Estoque Máximo:");

        fQtdEstoqueMax.setBackground(new java.awt.Color(245, 246, 250));
        fQtdEstoqueMax.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fQtdEstoqueMax.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(153, 153, 153));
        jLabel14.setText("Localização:");

        fLocalizacao.setBackground(new java.awt.Color(245, 246, 250));
        fLocalizacao.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fLocalizacao.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Valor de Custo: ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 153, 153));
        jLabel7.setText("Preço de Venda: ");

        fValorCusto.setBackground(new java.awt.Color(245, 246, 250));
        fValorCusto.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fValorCusto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        fValorCusto.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        fValorCusto.setPreferredSize(new java.awt.Dimension(100, 20));

        fValorVenda.setBackground(new java.awt.Color(245, 246, 250));
        fValorVenda.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fValorVenda.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        fValorVenda.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        fValorVenda.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setText("% de Lucro: ");

        fPercentLucro.setBackground(new java.awt.Color(245, 246, 250));
        fPercentLucro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fPercentLucro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        fPercentLucro.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        fPercentLucro.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 153, 153));
        jLabel9.setText("R$ de Lucro: ");

        fValorLucro.setBackground(new java.awt.Color(245, 246, 250));
        fValorLucro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fValorLucro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        fValorLucro.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        fValorLucro.setPreferredSize(new java.awt.Dimension(100, 20));

        jSeparator1.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator1.setAlignmentX(0.0F);
        jSeparator1.setAlignmentY(0.0F);

        jSeparator2.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator2.setForeground(new java.awt.Color(204, 204, 204));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setAlignmentX(0.0F);
        jSeparator2.setAlignmentY(0.0F);
        jSeparator2.setPreferredSize(new java.awt.Dimension(10, 50));
        jSeparator2.setRequestFocusEnabled(false);
        jSeparator2.setVerifyInputWhenFocusTarget(false);

        labelDadosUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        labelDadosUsuario.setForeground(new java.awt.Color(51, 51, 51));
        labelDadosUsuario.setText("Dados do Produto");
        labelDadosUsuario.setAlignmentY(0.0F);

        fGerarCodigoBarras.setBackground(new java.awt.Color(245, 246, 250));
        fGerarCodigoBarras.setSelected(true);
        fGerarCodigoBarras.setToolTipText("Gerar automaticamente um código de barras");
        fGerarCodigoBarras.setAlignmentY(0.0F);
        fGerarCodigoBarras.setMaximumSize(new java.awt.Dimension(25, 25));
        fGerarCodigoBarras.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario3.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario3.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario3.setText("Gerar");
        jLabelUsuario3.setToolTipText("Gerar automaticamente um código de barras");

        caminhoImagem.setOpaque(true);

        javax.swing.GroupLayout jPanelCadastroProdutoLayout = new javax.swing.GroupLayout(jPanelCadastroProduto);
        jPanelCadastroProduto.setLayout(jPanelCadastroProdutoLayout);
        jPanelCadastroProdutoLayout.setHorizontalGroup(
            jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addComponent(jLabelTituloCadastro2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(218, 218, 218)
                        .addComponent(jLabelTituloCadastro1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(340, 340, 340)
                        .addComponent(jLabelTituloCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(fValorCusto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(fValorVenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(fPercentLucro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(fValorLucro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(83, 83, 83)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(fLocalizacao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel12)
                                                    .addComponent(fQtdEstoqueMin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(30, 30, 30)
                                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(fQtdEstoqueMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(jLabel11)
                                            .addComponent(fQtdEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jSeparator1)
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(fCodigoGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                                        .addComponent(fCodigoFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jComboBoxFornecedor, 0, 239, Short.MAX_VALUE)
                                                        .addComponent(jComboBoxGrupo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(btNovoCrFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btNovoCrGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(31, 31, 31)
                                            .addComponent(jLabel6)
                                            .addGap(118, 118, 118)
                                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(fGerarCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabelUsuario3))))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelUsuario1)
                                        .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(fCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(31, 31, 31)
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                            .addComponent(jComboBoxMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btNovoCrMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(labelDadosUsuario))
                        .addGap(86, 86, 86)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(caminhoImagem)
                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(panelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btSrcImg, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))))
                .addContainerGap(136, Short.MAX_VALUE))
        );
        jPanelCadastroProdutoLayout.setVerticalGroup(
            jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloCadastro)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelDadosUsuario)
                    .addComponent(caminhoImagem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addComponent(panelImagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btSrcImg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario1)
                                    .addComponent(jLabelUsuario3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(fGerarCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btNovoCrMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                    .addComponent(btNovoCrFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(52, 52, 52)
                                    .addComponent(btNovoCrGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                    .addComponent(fCodigoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(21, 21, 21)
                                    .addComponent(jLabel15)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(fCodigoGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBoxGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(33, 33, 33)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloCadastro1)
                    .addComponent(jLabelTituloCadastro2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fQtdEstoqueMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fValorVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fValorCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fQtdEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(21, 21, 21)
                                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(fPercentLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(fValorLucro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fQtdEstoqueMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCadastroProdutoLayout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fLocalizacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanelCadastroProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(133, 133, 133))
        );

        fValorCusto.getAccessibleContext().setAccessibleName("fValorCusto");
        fValorCusto.getAccessibleContext().setAccessibleDescription("fValorCusto");
        fValorVenda.getAccessibleContext().setAccessibleName("fValorVenda");
        fValorVenda.getAccessibleContext().setAccessibleDescription("fValorVenda");
        fPercentLucro.getAccessibleContext().setAccessibleName("fPercentLucro");
        fPercentLucro.getAccessibleContext().setAccessibleDescription("fPercentLucro");
        fValorLucro.getAccessibleContext().setAccessibleName("fValorLucro");
        fValorLucro.getAccessibleContext().setAccessibleDescription("fValorLucro");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -34;
        gridBagConstraints.ipady = 164;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 15);
        panelCadProduto.add(jPanelCadastroProduto, gridBagConstraints);

        jTabbedPaneCadastroProduto.addTab("", panelCadProduto);

        panelPesqProduto.setBackground(new java.awt.Color(245, 246, 250));
        panelPesqProduto.setMaximumSize(new java.awt.Dimension(920, 790));
        panelPesqProduto.setMinimumSize(new java.awt.Dimension(920, 790));
        panelPesqProduto.setPreferredSize(new java.awt.Dimension(920, 790));
        panelPesqProduto.setLayout(new java.awt.GridBagLayout());

        jPanelPesquisaProduto.setBackground(new java.awt.Color(245, 246, 250));
        jPanelPesquisaProduto.setMaximumSize(new java.awt.Dimension(920, 790));
        jPanelPesquisaProduto.setMinimumSize(new java.awt.Dimension(920, 790));
        jPanelPesquisaProduto.setPreferredSize(new java.awt.Dimension(920, 790));

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");

        pesqCodigo.setBackground(new java.awt.Color(245, 246, 250));
        pesqCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqCodigo.setPreferredSize(new java.awt.Dimension(113, 19));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(153, 153, 153));
        jLabel16.setText("Código:");

        jLabelUsuario2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario2.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario2.setText("Ativo");

        pesqNome.setBackground(new java.awt.Color(245, 246, 250));
        pesqNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqNome.setMinimumSize(new java.awt.Dimension(250, 20));
        pesqNome.setPreferredSize(new java.awt.Dimension(300, 20));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(153, 153, 153));
        jLabel17.setText("Nome:");

        btPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisa.setToolTipText("Pesquisar");

        jLabelUsuario19.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelUsuario19.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario19.setText("Pesquisa de Produtos");
        jLabelUsuario19.setAlignmentY(0.0F);

        jPanelListagemProduto.setMaximumSize(new java.awt.Dimension(880, 470));
        jPanelListagemProduto.setMinimumSize(new java.awt.Dimension(880, 470));
        jPanelListagemProduto.setPreferredSize(new java.awt.Dimension(880, 470));

        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableListagem);

        javax.swing.GroupLayout jPanelListagemProdutoLayout = new javax.swing.GroupLayout(jPanelListagemProduto);
        jPanelListagemProduto.setLayout(jPanelListagemProdutoLayout);
        jPanelListagemProdutoLayout.setHorizontalGroup(
            jPanelListagemProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
        );
        jPanelListagemProdutoLayout.setVerticalGroup(
            jPanelListagemProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pesqAtivo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Ativo", "Desativado" }));

        javax.swing.GroupLayout jPanelPesquisaProdutoLayout = new javax.swing.GroupLayout(jPanelPesquisaProduto);
        jPanelPesquisaProduto.setLayout(jPanelPesquisaProdutoLayout);
        jPanelPesquisaProdutoLayout.setHorizontalGroup(
            jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addGap(117, 117, 117)
                                    .addComponent(jLabel17))
                                .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                                    .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(43, 43, 43)
                                    .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(29, 29, 29)
                            .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelUsuario2)))
                        .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                            .addGap(365, 365, 365)
                            .addComponent(jLabelUsuario19))
                        .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanelListagemProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                        .addComponent(btEditar)
                        .addGap(7, 7, 7)
                        .addComponent(btExcluir)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanelPesquisaProdutoLayout.setVerticalGroup(
            jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelUsuario19)
                .addGap(61, 61, 61)
                .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabelUsuario2))
                .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPesquisaProdutoLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(btPesquisa)
                .addGap(18, 18, 18)
                .addComponent(jPanelListagemProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPesquisaProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btEditar)
                    .addComponent(btExcluir))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 9, 0, 0);
        panelPesqProduto.add(jPanelPesquisaProduto, gridBagConstraints);

        jTabbedPaneCadastroProduto.addTab("", panelPesqProduto);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
        );

        jTabbedPaneCadastroProduto.getAccessibleContext().setAccessibleName("Cadastro");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btNovoCrFornecedor;
    private javax.swing.JButton btNovoCrGrupo;
    private javax.swing.JButton btNovoCrMedida;
    private javax.swing.JButton btPesquisa;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSrcImg;
    private javax.swing.JLabel caminhoImagem;
    private javax.swing.JCheckBox fAtivo;
    private javax.swing.JTextField fCodigo;
    private javax.swing.JTextField fCodigoBarras;
    private javax.swing.JTextField fCodigoFornecedor;
    private javax.swing.JTextField fCodigoGrupo;
    private javax.swing.JCheckBox fGerarCodigoBarras;
    private javax.swing.JFormattedTextField fLocalizacao;
    private javax.swing.JTextField fNome;
    private javax.swing.JFormattedTextField fPercentLucro;
    private javax.swing.JFormattedTextField fQtdEstoque;
    private javax.swing.JFormattedTextField fQtdEstoqueMax;
    private javax.swing.JFormattedTextField fQtdEstoqueMin;
    private javax.swing.JFormattedTextField fValorCusto;
    private javax.swing.JFormattedTextField fValorLucro;
    private javax.swing.JFormattedTextField fValorVenda;
    private javax.swing.JTextField id;
    private javax.swing.JComboBox jComboBoxFornecedor;
    private javax.swing.JComboBox jComboBoxGrupo;
    private javax.swing.JComboBox<String> jComboBoxMedida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelTituloCadastro;
    private javax.swing.JLabel jLabelTituloCadastro1;
    private javax.swing.JLabel jLabelTituloCadastro2;
    private javax.swing.JLabel jLabelUsuario1;
    private javax.swing.JLabel jLabelUsuario19;
    private javax.swing.JLabel jLabelUsuario2;
    private javax.swing.JLabel jLabelUsuario24;
    private javax.swing.JLabel jLabelUsuario3;
    private javax.swing.JPanel jPanelCadastroProduto;
    private javax.swing.JPanel jPanelListagemProduto;
    private javax.swing.JPanel jPanelPesquisaProduto;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPaneCadastroProduto;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JLabel labelDadosUsuario;
    private javax.swing.JPanel panelCadProduto;
    private javax.swing.JPanel panelImagem;
    private javax.swing.JPanel panelPesqProduto;
    private javax.swing.JComboBox<String> pesqAtivo;
    private javax.swing.JTextField pesqCodigo;
    private javax.swing.JTextField pesqNome;
    private javax.swing.JLabel srcImg;
    // End of variables declaration//GEN-END:variables

    private JLabel jl;

}