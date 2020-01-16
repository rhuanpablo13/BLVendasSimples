/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.comunicacao.Message;
import infra.comunicacao.Warning;
import infra.operacoes.Operacao;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import sistemavendas.controle.CFornecedor;
import sistemavendas.negocio.NFornecedor;
import infra.abstratas.View;
import infra.comunicacao.Sucess;
import infra.listagem.DefaultTableModelFornecedor;
import infra.utilitarios.Mask;
import javax.swing.table.DefaultTableModel;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NUsuario;

public class ViewFornecedor extends View<NFornecedor> {

    private static final long serialVersionUID = 1L;
    private CFornecedor fornecedorController;
    private NFornecedor fornecedorOld;

    public ViewFornecedor() {
        initComponents();
        initViewFornecedor();
        initializeView();
        initMasksValidators();
        configuraListagem();
    }

    protected void initViewFornecedor() {
        fornecedorController = new CFornecedor();
        fornecedorOld = new NFornecedor();
        this.fId.setVisible(false);
        
        this.btSalvar.setEnabled(false);
        this.btEditar.setEnabled(false);
        this.btExcluir.setEnabled(false);
        this.btPesquisa.setEnabled(false);        
        
        this.setName("view_fornecedor");
        this.jLabelTituloCadastro.setText("Cadastro de Fornecedor");
        jTabbedPaneCadastroFornecedor.setTitleAt(0, "Cadastrar");
        jTabbedPaneCadastroFornecedor.setTitleAt(1, "Pesquisar");
        carregarCodigo();
        NUsuario usuarioLogado = ViewPrincipal.getUsuarioLogado();
        configurarOperacoesAutorizadas(usuarioLogado.operacoesAutorizadas(jLabelTituloCadastro.getText()));
        
    }

    public void model2View(NFornecedor model) {
        fornecedorOld = model;
        setValuesModel2View(model);
    }

    public NFornecedor view2Model() {
        return setValuesView2Model();
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
        CFornecedor controller = new CFornecedor();
        try {
            NFornecedor fornecedor = controller.recuperarUltimoRegistro();
            this.fCodigo.setText(Integer.toString(fornecedor.getCodigo()+1));
        } catch (Exception ex) {
            this.fCodigo.setText("1");
        }
    }
        
    private void configuraListagem() {
        DefaultTableModelFornecedor modelo = new DefaultTableModelFornecedor();
        modelo.addColumn("#");
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("CNPJ");
        modelo.addColumn("Celular");
        modelo.addColumn("Email");
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
        // cnpj
        jTableListagem.getColumnModel().getColumn(3).setPreferredWidth(30);
        // celular
        jTableListagem.getColumnModel().getColumn(4).setPreferredWidth(20);
        // email
        jTableListagem.getColumnModel().getColumn(5).setPreferredWidth(150);
//        jTableListagem.getColumnModel().getColumn(5).setMinWidth(200);
        // ativo
        jTableListagem.getColumnModel().getColumn(6).setPreferredWidth(40);
        jTableListagem.getColumnModel().getColumn(6).setMaxWidth(40);
    }

    private void limparCampos() {
        String codigo = fCodigo.getText();
        cleanViewFields();
        fCodigo.setText(codigo);
    }


    private NFornecedor recebeDadosPesquisar() {
        return view2Model();
    }

    private NFornecedor getFornecedorSelecionado() throws Message {
        int row = jTableListagem.getSelectedRow();
        if (row > -1) {
            //pega o codigo do fornecedor na linha selecionada
            int codigo = (Integer) jTableListagem.getValueAt(row, 1);
            try {
                return fornecedorController.buscarPorCodigo(codigo);
            } catch (Exception e) {
                throw new Message("Nenhum fornecedor encontrado", "Atenção");
            }
        }
        return null;
    }

    private void mudarAba(int index, String label) {
        jTabbedPaneCadastroFornecedor.setSelectedIndex(index);
        jTabbedPaneCadastroFornecedor.setTitleAt(index, label);
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

    }

    private void clearTableList() {
        DefaultTableModel modelo = (DefaultTableModel) jTableListagem.getModel();
        modelo.setNumRows(0);
    }

    protected void refreshTableList() {
        try {
            List<NFornecedor> dataList = fornecedorController.getLista();
            refreshTableList(dataList);
        } catch (Exception e) {
            out.show(e);
        }
    }

    protected void refreshTableList(List<NFornecedor> dataList) {
        try {
            clearTableList();
            DefaultTableModelFornecedor modelo = (DefaultTableModelFornecedor) jTableListagem.getModel();
            modelo.setNumRows(0);

            dataList.stream().forEach((nFornecedor) -> {
                String cnpj = Utils.setMask(nFornecedor.getCnpj(), Mask.CNPJ);
                String celular = Utils.setMask(nFornecedor.getCelular(), Mask.CELULAR);
                String ativo = ativo2View(nFornecedor.isAtivo());
                
                modelo.addRow(new Object[] {
                    nFornecedor.getId(),
                    nFornecedor.getCodigo(),
                    nFornecedor.getNome(),
                    cnpj,
                    celular,
                    nFornecedor.getEmail(),
                    ativo
                });
            });

        } catch (Exception e) {
        }
    }

    private String ativo2View(boolean ativo) {
        return ativo ? "Sim" : "Não";
    }
        
    private boolean isCadastro() {
        String titleLocal = jTabbedPaneCadastroFornecedor.getTitleAt(0);
        return titleLocal.equalsIgnoreCase("Cadastrar");
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
            NFornecedor c = view2Model();

            if (operacao == Operacao.CADASTRO) {
                try {
                    fornecedorController.inserir(c);
                    new Sucess("Registro salvo com sucesso!", "Sucesso").show();
                } catch (Exception ex) {
                    out.show(ex);
                    return;
                }
            } else {
                try {
                    fornecedorController.alterar(c);
                    new Sucess("Registro alterado com sucesso!", "Sucesso").show();
                } catch (Exception ex) {
                    out.show(ex);
                    return;
                }
            }
            mudarAba(0, "Cadastrar");
            limparCampos();
            carregarCodigo();
        }
    }

    private void onClickCancelar() {
        limparCampos();
        mudarAba(0, "Cadastrar");
        carregarCodigo();
        ViewPrincipal.showDashBoard();
    }

    private void onClickEditar() {
        try {
            NFornecedor fornecedor = getFornecedorSelecionado();
            model2View(fornecedor);
            mudarAba(0, "Alterar");
            setOperacao(Operacao.ALTERAR);
        } catch (Message ex) {
            ex.show();
        }
    }

    private void onClickExcluir() {
        List<NFornecedor> list = new ArrayList();

        try {
            NFornecedor c = getFornecedorSelecionado();
            if (c != null) {
                int i = Utils.pedeConfirmacao("Deseja excluir o registro: " + c.getCodigo(), this);
                if (i == 0) {
                    fornecedorController.excluir(c.getCodigo());
                }
                list = fornecedorController.getLista();
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
        NFornecedor fornecedor = recebeDadosPesquisar();
        List<NFornecedor> lista = new ArrayList();
        try {
            clearTableList();
            lista = fornecedorController.pesquisar(fornecedor);
            refreshTableList(lista);
        } catch (Exception ex) {
            out.show(ex);
        }
        
        if (lista == null || lista.isEmpty()) {
            new Message("Nenhum fornecedor encontrado", "Sem Registros").show();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    private void initMasksValidators() {
        validator.fieldCep(fCep, fornecedorOld.getCep(), fEndereco, fBairro, fCidade, fUf);
        validator.fieldCnpj(fCnpj, fornecedorOld.getCnpj(), true, "isCnpjValid", new Validador(), String.class);
        validator.required(fCodigo);
        validator.required(fNome);
        fTelefone.setName("fTelefone");
        validator.fieldPhone(fTelefone, false);
        fCelular.setName("fCelular");
        validator.fieldCellPhone(fCelular, false);
        validator.fieldWithMask(pesqCnpj, "##.###.###/####-##", "", true, false, "");
    }

    /**
     * *******************************
     * MÉTODOS USADOS PARA A PESQUISA *******************************
     */
    public void setPesqBairro(String pesqBairro) {
        this.pesqBairro.setText(pesqBairro);
    }

    public void setPesqCidade(String pesqCidade) {
        this.pesqCidade.setText(pesqCidade);
    }

    public void setPesqCnpj(String pesqCnpj) {
        this.pesqCnpj.setText(pesqCnpj);
    }

    public void setPesqCodigo(Integer pesqCodigo) {
        this.pesqCodigo.setText(Integer.toString(pesqCodigo));
    }

    public void setPesqNome(String pesqNome) {
        this.pesqNome.setText(pesqNome);
    }

    public Boolean getPesqAtivo() {
        String value = pesqAtivo.getSelectedItem().toString();
        if (value.equalsIgnoreCase("Todos")) {
            return null;
        }
        return value.equalsIgnoreCase("Ativo");
    }

    public void setPesqAtivo(int pesqAtivo) {
        boolean b = Utils.int2Boolean(pesqAtivo);
        if (b) {
            this.pesqAtivo.setSelectedItem("Ativo");
        } else {
            this.pesqAtivo.setSelectedItem("Desativado");
        }
    }

    public String getPesqBairro() {
        return pesqBairro.getText();
    }

    public String getPesqCidade() {
        return pesqCidade.getText();
    }

    public String getPesqCnpj() {
        return Utils.removeCaracteresEspeciais(pesqCnpj.getText());
    }

    public Integer getPesqCodigo() {
        return Integer.parseInt(pesqCodigo.getText());
    }

    public String getPesqNome() {
        return pesqNome.getText();
    }

    /**
     * **************************************************************
     * MÉTODOS QUE VÃO RECEBER OS DADOS DA MODEL PARA MOSTRAR NA TELA
     * **************************************************************
     */
    public void setAtivo(Boolean boAtivo) {
        this.fAtivo.setSelected(boAtivo);
    }

    public void setBairro(String fBairro) {
        this.fBairro.setText(fBairro);
    }

    public void setCelular(String fCelular) {
        String tel = Utils.setMask(fCelular, Mask.CELULAR);
        if (tel == null) {
            this.fCelular.setText("");
        } else {
            this.fCelular.setText(tel);
        }
    }

    public void setCep(String fCep) {
        String cep = Utils.setMask(fCep, Mask.CEP);
        if (cep == null) {
            this.fCep.setText("");
        } else {
            this.fCep.setText(cep);
        }
    }

    public void setCidade(String fCidade) {
        this.fCidade.setText(fCidade);
    }

    public void setCnpj(String fCnpj) {
        String cnpj = Utils.setMask(fCnpj, Mask.CNPJ);
        if (cnpj == null) {
            this.fCnpj.setText("");
        } else {
            this.fCnpj.setText(cnpj);
        }
    }

    public void setCodigo(Integer fCodigo) {
        this.fCodigo.setText(Integer.toString(fCodigo));
    }

    public void setEmail(String fEmail) {
        this.fEmail.setText(fEmail);
    }

    public void setEndereco(String fEndereco) {
        this.fEndereco.setText(fEndereco);
    }

    public void setId(Integer fId) {
        this.fId.setText(Integer.toString(fId));
    }

    public void setNome(String fNome) {
        this.fNome.setText(fNome);
    }

    public void setTelefone(String fTelefone) {
        String tel = Utils.setMask(fTelefone, Mask.TELEFONE);
        if (tel == null) {
            this.fTelefone.setText("");
        } else {
            this.fTelefone.setText(tel);
        }

    }

    public void setUf(String fUf) {
        this.fUf.setText(fUf);
    }

    /**
     * ***********************************************************
     * MÉTODOS QUE VÃO PEGAR OS DADOS DA TELA PARA SETAR NA MODEL
     * ***********************************************************
     */
    public Boolean isAtivo() {
        return this.fAtivo.isSelected();
    }

    public String getBairro() {
        return fBairro.getText();
    }

    public String getCelular() {
        return fCelular.getText();
    }

    public String getCep() {
        return fCep.getText();
    }

    public String getCidade() {
        return fCidade.getText();
    }

    public String getCnpj() {
        return fCnpj.getText();
    }

    public Integer getCodigo() {
        return Integer.parseInt(fCodigo.getText());
    }

    public String getEmail() {
        return fEmail.getText();
    }

    public String getEndereco() {
        return fEndereco.getText();
    }

    public Integer getId() {
        return Integer.parseInt(fId.getText());
    }

//    public String getId() {
//        return fId.getText();
//    }
    public String getNome() {
        return fNome.getText();
    }

    public String getTelefone() {
        return fTelefone.getText();
    }

    public String getUf() {
        return fUf.getText();
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

        jTabbedPaneCadastroFornecedor = new javax.swing.JTabbedPane();
        panelCadFornecedor = new javax.swing.JPanel();
        jPanelCadastroFornecedor = new javax.swing.JPanel();
        jLabelTituloCadastro = new javax.swing.JLabel();
        jLabelUsuario21 = new javax.swing.JLabel();
        jLabelUsuario = new javax.swing.JLabel();
        fNome = new javax.swing.JTextField();
        jLabelUsuario2 = new javax.swing.JLabel();
        fCnpj = new javax.swing.JFormattedTextField();
        jLabelUsuario15 = new javax.swing.JLabel();
        jLabelUsuario6 = new javax.swing.JLabel();
        fCep = new javax.swing.JFormattedTextField();
        jLabelUsuario7 = new javax.swing.JLabel();
        fCidade = new javax.swing.JTextField();
        jLabelUsuario23 = new javax.swing.JLabel();
        jLabelUsuario9 = new javax.swing.JLabel();
        fTelefone = new javax.swing.JFormattedTextField();
        fCelular = new javax.swing.JFormattedTextField();
        jLabelUsuario10 = new javax.swing.JLabel();
        fEmail = new javax.swing.JTextField();
        jLabelUsuario22 = new javax.swing.JLabel();
        fBairro = new javax.swing.JTextField();
        jLabelUsuario11 = new javax.swing.JLabel();
        fEndereco = new javax.swing.JTextField();
        jLabelUsuario5 = new javax.swing.JLabel();
        jLabelUsuario8 = new javax.swing.JLabel();
        fUf = new javax.swing.JTextField();
        fAtivo = new javax.swing.JCheckBox();
        jLabelUsuario1 = new javax.swing.JLabel();
        fCodigo = new javax.swing.JTextField();
        jLabelUsuario20 = new javax.swing.JLabel();
        fId = new javax.swing.JTextField();
        btSalvar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        panelPesqFornecedor = new javax.swing.JPanel();
        jPanelPesquisaFornecedor = new javax.swing.JPanel();
        jLabelUsuario19 = new javax.swing.JLabel();
        jLabelUsuario12 = new javax.swing.JLabel();
        pesqCodigo = new javax.swing.JTextField();
        jLabelUsuario13 = new javax.swing.JLabel();
        pesqNome = new javax.swing.JTextField();
        jLabelUsuario25 = new javax.swing.JLabel();
        pesqCnpj = new javax.swing.JFormattedTextField();
        jLabelUsuario14 = new javax.swing.JLabel();
        pesqCidade = new javax.swing.JTextField();
        jLabelUsuario17 = new javax.swing.JLabel();
        pesqBairro = new javax.swing.JTextField();
        jLabelUsuario16 = new javax.swing.JLabel();
        btPesquisa = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        jPanelListagemFornecedor = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListagem = new javax.swing.JTable();
        pesqAtivo = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(245, 246, 250));
        setBorder(null);
        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setAutoscrolls(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(0, 0));
        setMinimumSize(new java.awt.Dimension(920, 820));
        setPreferredSize(new java.awt.Dimension(920, 820));
        setRequestFocusEnabled(false);

        jTabbedPaneCadastroFornecedor.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTabbedPaneCadastroFornecedor.setAlignmentX(0.0F);
        jTabbedPaneCadastroFornecedor.setAlignmentY(0.0F);
        jTabbedPaneCadastroFornecedor.setFocusable(false);
        jTabbedPaneCadastroFornecedor.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jTabbedPaneCadastroFornecedor.setMaximumSize(new java.awt.Dimension(0, 0));
        jTabbedPaneCadastroFornecedor.setMinimumSize(new java.awt.Dimension(920, 820));
        jTabbedPaneCadastroFornecedor.setPreferredSize(new java.awt.Dimension(920, 820));

        panelCadFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        panelCadFornecedor.setAlignmentX(0.0F);
        panelCadFornecedor.setAlignmentY(0.0F);
        panelCadFornecedor.setMaximumSize(new java.awt.Dimension(0, 0));
        panelCadFornecedor.setMinimumSize(new java.awt.Dimension(800, 720));
        panelCadFornecedor.setPreferredSize(new java.awt.Dimension(800, 720));
        panelCadFornecedor.setLayout(new java.awt.GridBagLayout());

        jPanelCadastroFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        jPanelCadastroFornecedor.setMinimumSize(new java.awt.Dimension(750, 650));
        jPanelCadastroFornecedor.setPreferredSize(new java.awt.Dimension(750, 650));

        jLabelTituloCadastro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro.setText("Cadastro de Fornecedor");
        jLabelTituloCadastro.setAlignmentY(0.0F);

        jLabelUsuario21.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario21.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario21.setText("Dados do Fornecedor");
        jLabelUsuario21.setAlignmentY(0.0F);

        jLabelUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario.setText("Nome");

        fNome.setBackground(new java.awt.Color(245, 246, 250));
        fNome.setAlignmentX(0.0F);
        fNome.setAlignmentY(0.0F);
        fNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNome.setMinimumSize(new java.awt.Dimension(400, 20));
        fNome.setName(""); // NOI18N
        fNome.setPreferredSize(new java.awt.Dimension(400, 20));

        jLabelUsuario2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario2.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario2.setText("CNPJ");

        fCnpj.setBackground(new java.awt.Color(245, 246, 250));
        fCnpj.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCnpj.setMinimumSize(new java.awt.Dimension(100, 20));
        fCnpj.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario15.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario15.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario15.setText("Endereço do Fornecedor");
        jLabelUsuario15.setAlignmentY(0.0F);

        jLabelUsuario6.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario6.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario6.setText("CEP");

        fCep.setBackground(new java.awt.Color(245, 246, 250));
        fCep.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCep.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario7.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario7.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario7.setText("Cidade");

        fCidade.setBackground(new java.awt.Color(245, 246, 250));
        fCidade.setAlignmentX(0.0F);
        fCidade.setAlignmentY(0.0F);
        fCidade.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCidade.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario23.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario23.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario23.setText("Contatos do Fornecedor");
        jLabelUsuario23.setAlignmentY(0.0F);

        jLabelUsuario9.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario9.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario9.setText("Telefone");

        fTelefone.setBackground(new java.awt.Color(245, 246, 250));
        fTelefone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fTelefone.setPreferredSize(new java.awt.Dimension(100, 20));

        fCelular.setBackground(new java.awt.Color(245, 246, 250));
        fCelular.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCelular.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario10.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario10.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario10.setText("Celular");

        fEmail.setBackground(new java.awt.Color(245, 246, 250));
        fEmail.setAlignmentX(0.0F);
        fEmail.setAlignmentY(0.0F);
        fEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEmail.setPreferredSize(new java.awt.Dimension(50, 20));

        jLabelUsuario22.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario22.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario22.setText("E-mail");

        fBairro.setBackground(new java.awt.Color(245, 246, 250));
        fBairro.setAlignmentX(0.0F);
        fBairro.setAlignmentY(0.0F);
        fBairro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fBairro.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario11.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario11.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario11.setText("Bairro");

        fEndereco.setBackground(new java.awt.Color(245, 246, 250));
        fEndereco.setAlignmentX(0.0F);
        fEndereco.setAlignmentY(0.0F);
        fEndereco.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEndereco.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario5.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario5.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario5.setText("Endereço");

        jLabelUsuario8.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario8.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario8.setText("UF");

        fUf.setBackground(new java.awt.Color(245, 246, 250));
        fUf.setAlignmentX(0.0F);
        fUf.setAlignmentY(0.0F);
        fUf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fUf.setPreferredSize(new java.awt.Dimension(50, 20));

        fAtivo.setBackground(new java.awt.Color(245, 246, 250));
        fAtivo.setSelected(true);
        fAtivo.setAlignmentY(0.0F);
        fAtivo.setMaximumSize(new java.awt.Dimension(25, 25));
        fAtivo.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario1.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario1.setText("Ativo");

        fCodigo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigo.setAlignmentX(0.0F);
        fCodigo.setAlignmentY(0.0F);
        fCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigo.setMinimumSize(new java.awt.Dimension(0, 20));
        fCodigo.setPreferredSize(new java.awt.Dimension(0, 20));

        jLabelUsuario20.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario20.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario20.setText("Código");

        fId.setEditable(false);
        fId.setFocusable(false);

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

        javax.swing.GroupLayout jPanelCadastroFornecedorLayout = new javax.swing.GroupLayout(jPanelCadastroFornecedor);
        jPanelCadastroFornecedor.setLayout(jPanelCadastroFornecedorLayout);
        jPanelCadastroFornecedorLayout.setHorizontalGroup(
            jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelUsuario21)
                    .addComponent(jLabelUsuario15)
                    .addComponent(jLabelUsuario6)
                    .addComponent(jLabelUsuario7)
                    .addComponent(jLabelUsuario23)
                    .addComponent(jLabelUsuario2)
                    .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fCidade, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(fCep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelUsuario5)
                                .addComponent(fEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelUsuario11))
                            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                                .addComponent(fBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario8)
                                    .addComponent(fUf, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                        .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(319, 319, 319)
                        .addComponent(jLabelTituloCadastro))
                    .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario20, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(fCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fCnpj, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)))
                        .addGap(50, 50, 50)
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                                .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelUsuario1))))
                    .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                            .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                            .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelUsuario9))
                            .addGap(51, 51, 51)
                            .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelUsuario10))
                            .addGap(47, 47, 47)
                            .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelUsuario22)
                                .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanelCadastroFornecedorLayout.setVerticalGroup(
            jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloCadastro)
                    .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62)
                .addComponent(jLabelUsuario21)
                .addGap(25, 25, 25)
                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabelUsuario))
                            .addComponent(jLabelUsuario20, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelUsuario2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroFornecedorLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabelUsuario15)
                                .addGap(21, 21, 21)
                                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario6)
                                    .addComponent(jLabelUsuario5))
                                .addGap(9, 9, 9)
                                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fCep, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario7)
                                    .addComponent(jLabelUsuario11)
                                    .addComponent(jLabelUsuario8))
                                .addGap(12, 12, 12)
                                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fUf, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addComponent(jLabelUsuario23)
                                .addGap(22, 22, 22)
                                .addComponent(jLabelUsuario9)
                                .addGap(4, 4, 4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroFornecedorLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario10)
                                    .addComponent(jLabelUsuario22))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanelCadastroFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelUsuario1))
                .addGap(122, 122, 122))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 136;
        gridBagConstraints.ipady = 113;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 16, 17);
        panelCadFornecedor.add(jPanelCadastroFornecedor, gridBagConstraints);

        jTabbedPaneCadastroFornecedor.addTab("", panelCadFornecedor);

        panelPesqFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        panelPesqFornecedor.setAlignmentX(0.0F);
        panelPesqFornecedor.setAlignmentY(0.0F);
        panelPesqFornecedor.setMaximumSize(new java.awt.Dimension(0, 0));
        panelPesqFornecedor.setMinimumSize(new java.awt.Dimension(800, 720));
        panelPesqFornecedor.setPreferredSize(new java.awt.Dimension(800, 720));
        panelPesqFornecedor.setLayout(new java.awt.GridBagLayout());

        jPanelPesquisaFornecedor.setBackground(new java.awt.Color(245, 246, 250));
        jPanelPesquisaFornecedor.setMinimumSize(new java.awt.Dimension(750, 650));
        jPanelPesquisaFornecedor.setPreferredSize(new java.awt.Dimension(750, 650));
        jPanelPesquisaFornecedor.setRequestFocusEnabled(false);

        jLabelUsuario19.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelUsuario19.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario19.setText("Pesquisa de Fornecedor");
        jLabelUsuario19.setAlignmentY(0.0F);

        jLabelUsuario12.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario12.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario12.setText("Código");

        pesqCodigo.setBackground(new java.awt.Color(245, 246, 250));
        pesqCodigo.setAlignmentX(0.0F);
        pesqCodigo.setAlignmentY(0.0F);
        pesqCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        jLabelUsuario13.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario13.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario13.setText("Nome");

        pesqNome.setBackground(new java.awt.Color(245, 246, 250));
        pesqNome.setAlignmentX(0.0F);
        pesqNome.setAlignmentY(0.0F);
        pesqNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        jLabelUsuario25.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario25.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario25.setText("Ativo");

        pesqCnpj.setBackground(new java.awt.Color(245, 246, 250));
        pesqCnpj.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            pesqCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        pesqCnpj.setAlignmentX(0.0F);
        pesqCnpj.setAlignmentY(0.0F);
        pesqCnpj.setPreferredSize(new java.awt.Dimension(113, 20));

        jLabelUsuario14.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario14.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario14.setText("CNPJ");

        pesqCidade.setBackground(new java.awt.Color(245, 246, 250));
        pesqCidade.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        pesqCidade.setAlignmentX(0.0F);
        pesqCidade.setAlignmentY(0.0F);
        pesqCidade.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqCidade.setPreferredSize(new java.awt.Dimension(130, 20));

        jLabelUsuario17.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario17.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario17.setText("Cidade");

        pesqBairro.setBackground(new java.awt.Color(245, 246, 250));
        pesqBairro.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        pesqBairro.setAlignmentX(0.0F);
        pesqBairro.setAlignmentY(0.0F);
        pesqBairro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqBairro.setPreferredSize(new java.awt.Dimension(150, 20));

        jLabelUsuario16.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario16.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario16.setText("Bairro");

        btPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisa.setToolTipText("Pesquisar");

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");

        jPanelListagemFornecedor.setPreferredSize(new java.awt.Dimension(750, 350));

        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableListagem);

        javax.swing.GroupLayout jPanelListagemFornecedorLayout = new javax.swing.GroupLayout(jPanelListagemFornecedor);
        jPanelListagemFornecedor.setLayout(jPanelListagemFornecedorLayout);
        jPanelListagemFornecedorLayout.setHorizontalGroup(
            jPanelListagemFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanelListagemFornecedorLayout.setVerticalGroup(
            jPanelListagemFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );

        pesqAtivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Ativo", "Desativado" }));

        javax.swing.GroupLayout jPanelPesquisaFornecedorLayout = new javax.swing.GroupLayout(jPanelPesquisaFornecedor);
        jPanelPesquisaFornecedor.setLayout(jPanelPesquisaFornecedorLayout);
        jPanelPesquisaFornecedorLayout.setHorizontalGroup(
            jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelListagemFornecedor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPesquisaFornecedorLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btExcluir))
                    .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                        .addGap(332, 332, 332)
                        .addComponent(jLabelUsuario19)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario14, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario17)
                            .addComponent(pesqCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario16)
                            .addComponent(pesqBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario25)
                            .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 198, Short.MAX_VALUE)
                        .addComponent(btPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario12, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario13, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(284, 284, 284))
                            .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                                .addComponent(pesqNome)
                                .addGap(331, 331, 331)))))
                .addContainerGap())
        );
        jPanelPesquisaFornecedorLayout.setVerticalGroup(
            jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                .addComponent(jLabelUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuario12)
                    .addComponent(jLabelUsuario13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario14)
                            .addComponent(jLabelUsuario17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelUsuario16, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(3, 3, 3)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pesqCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pesqCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pesqBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btPesquisa)
                            .addGroup(jPanelPesquisaFornecedorLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)))))
                .addGap(24, 24, 24)
                .addComponent(jPanelListagemFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPesquisaFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btExcluir)
                    .addComponent(btEditar))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 135;
        gridBagConstraints.ipady = 112;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 15, 14, 18);
        panelPesqFornecedor.add(jPanelPesquisaFornecedor, gridBagConstraints);

        jTabbedPaneCadastroFornecedor.addTab("", panelPesqFornecedor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPaneCadastroFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPaneCadastroFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneCadastroFornecedor.getAccessibleContext().setAccessibleName("Cadastro");

        getAccessibleContext().setAccessibleName("view_clientes");
        getAccessibleContext().setAccessibleDescription("view_clientes");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btPesquisa;
    private javax.swing.JButton btSalvar;
    private javax.swing.JCheckBox fAtivo;
    private javax.swing.JTextField fBairro;
    private javax.swing.JFormattedTextField fCelular;
    private javax.swing.JFormattedTextField fCep;
    private javax.swing.JTextField fCidade;
    private javax.swing.JFormattedTextField fCnpj;
    private javax.swing.JTextField fCodigo;
    private javax.swing.JTextField fEmail;
    private javax.swing.JTextField fEndereco;
    private javax.swing.JTextField fId;
    private javax.swing.JTextField fNome;
    private javax.swing.JFormattedTextField fTelefone;
    private javax.swing.JTextField fUf;
    private javax.swing.JLabel jLabelTituloCadastro;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JLabel jLabelUsuario1;
    private javax.swing.JLabel jLabelUsuario10;
    private javax.swing.JLabel jLabelUsuario11;
    private javax.swing.JLabel jLabelUsuario12;
    private javax.swing.JLabel jLabelUsuario13;
    private javax.swing.JLabel jLabelUsuario14;
    private javax.swing.JLabel jLabelUsuario15;
    private javax.swing.JLabel jLabelUsuario16;
    private javax.swing.JLabel jLabelUsuario17;
    private javax.swing.JLabel jLabelUsuario19;
    private javax.swing.JLabel jLabelUsuario2;
    private javax.swing.JLabel jLabelUsuario20;
    private javax.swing.JLabel jLabelUsuario21;
    private javax.swing.JLabel jLabelUsuario22;
    private javax.swing.JLabel jLabelUsuario23;
    private javax.swing.JLabel jLabelUsuario25;
    private javax.swing.JLabel jLabelUsuario5;
    private javax.swing.JLabel jLabelUsuario6;
    private javax.swing.JLabel jLabelUsuario7;
    private javax.swing.JLabel jLabelUsuario8;
    private javax.swing.JLabel jLabelUsuario9;
    private javax.swing.JPanel jPanelCadastroFornecedor;
    private static javax.swing.JPanel jPanelListagemFornecedor;
    private javax.swing.JPanel jPanelPesquisaFornecedor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneCadastroFornecedor;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JPanel panelCadFornecedor;
    private javax.swing.JPanel panelPesqFornecedor;
    private javax.swing.JComboBox<String> pesqAtivo;
    private javax.swing.JTextField pesqBairro;
    private javax.swing.JTextField pesqCidade;
    private javax.swing.JFormattedTextField pesqCnpj;
    private javax.swing.JTextField pesqCodigo;
    private javax.swing.JTextField pesqNome;
    // End of variables declaration//GEN-END:variables

}
