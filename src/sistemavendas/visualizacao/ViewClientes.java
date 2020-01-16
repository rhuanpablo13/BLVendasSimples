/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.abstratas.View;
import infra.comunicacao.Message;
import infra.comunicacao.Sucess;
import java.util.List;
import infra.comunicacao.Warning;
import infra.listagem.DefaultTableModelClientes;
import infra.operacoes.Operacao;
import infra.utilitarios.Mask;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import sistemavendas.controle.CCliente;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NUsuario;


public class ViewClientes extends View<NCliente> {

    private static final long serialVersionUID = 1L;
    private CCliente clienteController;
    private NCliente clienteOld;
    
    public ViewClientes() {
        initComponents();
        initializeView();
        initViewClientes();
        initMasksValidators();
        configuraListagem();
    }

    
    private void carregarCodigo() {
        CCliente controller = new CCliente();
        try {
            NCliente produto = controller.recuperaUltimoRegistro();
            this.fCodigo.setText(Integer.toString(produto.getCodigo()+1));
        } catch (Exception ex) {
            this.fCodigo.setText("1");
        }
    }
    

    
    
    protected void initViewClientes() {
        clienteController = new CCliente();
        clienteOld = new NCliente();
        
        this.btSalvar.setEnabled(false);
        this.btEditar.setEnabled(false);
        this.btExcluir.setEnabled(false);
        this.btPesquisa.setEnabled(false);
        
        this.fId.setVisible(false);
        this.setName("view_clientes");
        this.jLabelTituloCadastro.setText("Cadastro de Cliente");

        jTabbedPaneCadastroCliente.setTitleAt(0, "Cadastrar");
        jTabbedPaneCadastroCliente.setTitleAt(1, "Pesquisar");

        //preencheCampos();
        carregarCodigo();
        NUsuario usuarioLogado = ViewPrincipal.getUsuarioLogado();
        configurarOperacoesAutorizadas(usuarioLogado.operacoesAutorizadas(jLabelTituloCadastro.getText()));
    }
    
    
    public void model2View(NCliente model) {
        clienteOld = model;
        setValuesModel2View(model);
    }

    
    public NCliente view2Model() {
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
    
    
    private void limparCampos() {
        String codigo = fCodigo.getText();
        cleanViewFields();
        fCodigo.setText(codigo);
    }
    
    
    private NCliente recebeDadosPesquisar() {
        return view2Model();
    }



    private NCliente getClienteSelecionado() throws Exception {
        
        int row = jTableListagem.getSelectedRow();
        if (row > -1) {
            //pega o codigo do cliente na linha selecionada
            int codigo = (Integer) jTableListagem.getValueAt(row, 1);
            try {
                return clienteController.buscarPorCodigo(codigo);
            } catch (Exception e) {
                throw new Message("Nenhum cliente encontrado", "Atenção");
            }
        }
        throw new Warning("Selecione um cliente da lista", "Atenção");
    }   
    
    
    private void mudarAba(int index, String label) {
        jTabbedPaneCadastroCliente.setSelectedIndex(index);
        jTabbedPaneCadastroCliente.setTitleAt(index, label);
        //carregarCodigo();
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
    
        btNovoRegistro.addActionListener((ActionEvent e) -> {
            onClickNovoRegistro();
        });

    }

    
    private void clearTableList() {
        DefaultTableModel modelo = (DefaultTableModel) jTableListagem.getModel();
        modelo.setNumRows(0);
    }
    
    

    protected void refreshTableList(List<NCliente> dataList) {
        try {            
            DefaultTableModelClientes modelo = (DefaultTableModelClientes) jTableListagem.getModel();
            modelo.setNumRows(0);
            //modelo.addClientes(dataList);
            
            dataList.stream().forEach((nCliente) -> {
                String ativo = ativo2View(nCliente.isAtivo());
                
                modelo.addRow(new Object[] {
                    nCliente.getId(),
                    nCliente.getCodigo(),
                    nCliente.getNome(),
                    Utils.setMask(nCliente.getCpf(), Mask.CPF),
                    Utils.setMask(nCliente.getCelular(), Mask.CELULAR),
                    nCliente.getEmail(),
                    ativo
                });
            });
            
        } catch (Exception e) {}
    }

    
    private String ativo2View(boolean ativo) {
        return ativo ? "Sim" : "Não";
    }
    
    
    private boolean isCadastro() {
        String titleLocal = jTabbedPaneCadastroCliente.getTitleAt(0);
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
            NCliente c = view2Model();
            
            if (operacao == Operacao.CADASTRO) {
                try {
                    clienteController.inserir(c);
                    new Sucess("Registro salvo com sucesso!", "Sucesso").show();
                } catch (Exception ex) {
                   out.show(ex);
                   return;
                }
            } else {
                try {
                    clienteController.alterar(c);
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
            NCliente cliente = getClienteSelecionado();
            model2View(cliente);
            mudarAba(0, "Alterar");
            setOperacao(Operacao.ALTERAR);
        } catch (Exception ex) {
            out.show(ex);
        }
    }
    
    
    private void onClickExcluir() {
        List<NCliente> list = new ArrayList();
        
        try {
            NCliente c = getClienteSelecionado();
            if (c != null) {
                int i = Utils.pedeConfirmacao("Deseja excluir o registro: " + c.getCodigo(), this);
                if (i==0) {
                    clienteController.excluir(c.getCodigo());
                }
                list = clienteController.getLista();
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
        NCliente cliente = recebeDadosPesquisar();
        List<NCliente> lista = new ArrayList();
        try {
            lista = clienteController.pesquisar(cliente);            
            refreshTableList(lista);
        } catch (Exception ex) {
            out.show(ex);
        }
        if (lista == null || lista.isEmpty()) {
            new Message("Nenhum cliente encontrado", "Sem Registros").show();
        }
    }



    private void onClickNovoRegistro() {
        setOperacao(Operacao.CADASTRO);
        limparCampos();
        mudarAba(0, "Cadastrar");
    }
    
    ////////////////////////////////////////////////////////////////////////////
     
    
    private void configuraListagem() {
    
        DefaultTableModelClientes modelo = new DefaultTableModelClientes();
        modelo.addColumn("#");
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("CPF");
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
        // cpf
        jTableListagem.getColumnModel().getColumn(3).setPreferredWidth(20); 
        // celular
        jTableListagem.getColumnModel().getColumn(4).setPreferredWidth(20);
        // email
        jTableListagem.getColumnModel().getColumn(5).setPreferredWidth(150);
        // ativo
        jTableListagem.getColumnModel().getColumn(6).setPreferredWidth(40);
        jTableListagem.getColumnModel().getColumn(6).setMaxWidth(40);
        
    }
    
        
        
    private void initMasksValidators() {
        validator.fieldCep(fCep, clienteOld.getCep(), fEndereco, fBairro, fCidade, fUf);
        validator.fieldCpf(fCpf, clienteOld.getCpf(), true, "isCpfValid", new Validador(), String.class);
        String oldDate = "";
        if (clienteOld.getDtNascimento() != null) {
            oldDate = Utils.date2View(clienteOld.getDtNascimento());
        }
        validator.fieldDate(fDtNascimento, oldDate, true, true, "Data inválida");
        validator.required(fCodigo);
        validator.required(fNome);
        validator.fieldPhone(fTelefone, false);
        validator.fieldCellPhone(fCelular, false);
        validator.fieldWithMask(pesqCpf, "###.###.###-##", "", true, false, "");
    }    
    
    
    /**
     * **************************************************************
     * MÉTODOS QUE VÃO RECEBER OS DADOS DA MODEL PARA MOSTRAR NA TELA
     * **************************************************************
     */
    
    public void setAtivo(Boolean ativo) {
        this.fAtivo.setSelected(ativo);
    }

    public void setBairro(String bairro) {
        this.fBairro.setText(bairro);
    }

    public void setCelular(String celular) {
        String tel = Utils.setMask(celular, Mask.CELULAR);
        if (tel == null) this.fCelular.setText("");
        else this.fCelular.setText(tel);
    }

    public void setCep(String cep) {
        this.fCep.setText(cep);
    }

    public void setCidade(String cidade) {
        this.fCidade.setText(cidade);
    }

    public void setCodigo(Integer codigo) {
        this.fCodigo.setText(Integer.toString(codigo));
    }

    public void setCpf(String cpf) {
        String cpf2 = Utils.setMask(cpf, Mask.CPF);
        if (cpf2 == null) this.fCpf.setText("");
        else this.fCpf.setText(cpf2);
    }

    public void setDtNascimento(Date dtNascimento) {
        String d = Utils.date2View(dtNascimento);
        this.fDtNascimento.setText(d);
    }

    public void setEmail(String email) {
        this.fEmail.setText(email);
    }

    public void setEndereco(String endereco) {
        this.fEndereco.setText(endereco);
    }

    public void setId(Integer id) {
        this.fId.setText(Integer.toString(id));
    }

    public void setNome(String nome) {
        this.fNome.setText(nome);
    }

    public void setSexo(String sexo) {
        this.fSexo.setSelectedItem(sexo);
    }

    public void setTelefone(String telefone) {
        String tel = Utils.setMask(telefone, Mask.TELEFONE);
        if (tel == null) this.fTelefone.setText("");
        else this.fTelefone.setText(tel);
    }

    public void setUf(String uf) {
        this.fUf.setText(uf);
    }

    
    
    /**
     * ***********************************************************
     *  MÉTODOS QUE VÃO PEGAR OS DADOS DA TELA PARA SETAR NA MODEL
     * ***********************************************************
     */
    
    public Integer getCodigo() {
        if (validator.canRemoveText(fCodigo)) {
            return null;
        }
        return Integer.parseInt(this.fCodigo.getText());
    }

    public Boolean isAtivo() {
        return this.fAtivo.isSelected();
    }

    public String getBairro() {
        return fBairro.getText();
    }

    public String getCelular() {
        //return parseUtf8(fCelular.getText());
        return fCelular.getText();
    }

    public String getCep() {
        return fCep.getText();
    }

    public String getCidade() {
        return fCidade.getText();
    }

    public String getCpf() {
        return fCpf.getText();
    }

    public Date getDtNascimento() {
        try {
            return Utils.view2Date(fDtNascimento.getText());
        } catch (Exception ex) {
            //new Erro("Erro ao converter data em: getDtNascimento\n" + ex.toString(), "Falha").show();
        }
        return null;
    }

    public String getEmail() {
        return fEmail.getText();
    }

    public Integer getId() {
        return Integer.parseInt(fId.getText());
    }

    public String getNome() {
        return fNome.getText();
    }

    public String getSexo() {
        return fSexo.getSelectedItem().toString();
    }

    public String getTelefone() {
        return fTelefone.getText();
    }

    public String getUf() {
        return fUf.getText();
    }
    
    public String getEndereco() {
        return fEndereco.getText();
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

    public void setPesqBairro(String pesqBairro) {
        this.pesqBairro.setText(pesqBairro);
    }

    public String getPesqCidade() {
        return pesqCidade.getText();
    }

    public void setPesqCidade(String pesqCidade) {
        this.pesqCidade.setText(pesqCidade);
    }

    public int getPesqCodigo() {
        return Integer.parseInt(pesqCodigo.getText());
    }

    public void setPesqCodigo(int pesqCodigo) {
        this.pesqCodigo.setText(Integer.toString(pesqCodigo));
    }

    public String getPesqCpf() {
        return Utils.removeCaracteresEspeciais(pesqCpf.getText());
    }

    public void setPesqCpf(String pesqCpf) {
        this.pesqCpf.setText(pesqCpf);
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

        jTabbedPaneCadastroCliente = new javax.swing.JTabbedPane();
        panelCadCliente = new javax.swing.JPanel();
        panelCadClienteInterno = new javax.swing.JPanel();
        jLabelTituloCadastro = new javax.swing.JLabel();
        fCep = new javax.swing.JFormattedTextField();
        labelAtivo = new javax.swing.JLabel();
        fAtivo = new javax.swing.JCheckBox();
        fCodigo = new javax.swing.JTextField();
        labelCodigo = new javax.swing.JLabel();
        fNome = new javax.swing.JTextField();
        labelNome = new javax.swing.JLabel();
        labelDadosUsuario = new javax.swing.JLabel();
        labelNasc = new javax.swing.JLabel();
        fDtNascimento = new javax.swing.JFormattedTextField();
        jLabelUsuario2 = new javax.swing.JLabel();
        fCpf = new javax.swing.JFormattedTextField();
        fSexo = new javax.swing.JComboBox<>();
        EnderecoCliente = new javax.swing.JLabel();
        labelCidade = new javax.swing.JLabel();
        fCidade = new javax.swing.JTextField();
        fEndereco = new javax.swing.JTextField();
        labelEnde = new javax.swing.JLabel();
        labelBairro = new javax.swing.JLabel();
        fBairro = new javax.swing.JTextField();
        labelUf = new javax.swing.JLabel();
        fUf = new javax.swing.JTextField();
        labelEmail = new javax.swing.JLabel();
        labelTel = new javax.swing.JLabel();
        ContatoCliente = new javax.swing.JLabel();
        fCelular = new javax.swing.JFormattedTextField();
        fTelefone = new javax.swing.JFormattedTextField();
        fId = new javax.swing.JTextField();
        btNovoRegistro = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        labelSexo = new javax.swing.JLabel();
        labelCep = new javax.swing.JLabel();
        labelCel = new javax.swing.JLabel();
        fEmail = new javax.swing.JTextField();
        btCancelar = new javax.swing.JButton();
        panelPesqCliente = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btExcluir = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        jLabelUsuario19 = new javax.swing.JLabel();
        pesqCodigo = new javax.swing.JTextField();
        jPanelListagem = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListagem = new javax.swing.JTable();
        jLabelUsuario12 = new javax.swing.JLabel();
        pesqNome = new javax.swing.JTextField();
        jLabelUsuario13 = new javax.swing.JLabel();
        pesqCpf = new javax.swing.JFormattedTextField();
        jLabelUsuario25 = new javax.swing.JLabel();
        pesqCidade = new javax.swing.JTextField();
        jLabelUsuario14 = new javax.swing.JLabel();
        pesqBairro = new javax.swing.JTextField();
        jLabelUsuario17 = new javax.swing.JLabel();
        pesqAtivo = new javax.swing.JComboBox<>();
        jLabelUsuario16 = new javax.swing.JLabel();
        btPesquisa = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 246, 250));
        setBorder(null);
        setAlignmentX(0.0F);
        setAlignmentY(0.0F);
        setAutoscrolls(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(0, 0));
        setMinimumSize(null);
        setVisible(true);

        jTabbedPaneCadastroCliente.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTabbedPaneCadastroCliente.setAlignmentX(0.0F);
        jTabbedPaneCadastroCliente.setAlignmentY(0.0F);
        jTabbedPaneCadastroCliente.setFocusable(false);
        jTabbedPaneCadastroCliente.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jTabbedPaneCadastroCliente.setMaximumSize(new java.awt.Dimension(0, 0));
        jTabbedPaneCadastroCliente.setMinimumSize(new java.awt.Dimension(920, 790));
        jTabbedPaneCadastroCliente.setPreferredSize(new java.awt.Dimension(920, 790));

        panelCadCliente.setBackground(new java.awt.Color(245, 246, 250));
        panelCadCliente.setAlignmentX(0.0F);
        panelCadCliente.setAlignmentY(0.0F);
        panelCadCliente.setMaximumSize(new java.awt.Dimension(0, 0));
        panelCadCliente.setMinimumSize(new java.awt.Dimension(0, 0));
        panelCadCliente.setPreferredSize(new java.awt.Dimension(920, 790));
        panelCadCliente.setLayout(new java.awt.GridBagLayout());

        panelCadClienteInterno.setBackground(new java.awt.Color(245, 246, 250));
        panelCadClienteInterno.setMaximumSize(new java.awt.Dimension(920, 790));
        panelCadClienteInterno.setPreferredSize(new java.awt.Dimension(920, 790));

        jLabelTituloCadastro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloCadastro.setText("Cadastro de Cliente ");
        jLabelTituloCadastro.setAlignmentY(0.0F);

        fCep.setBackground(new java.awt.Color(245, 246, 250));
        fCep.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            fCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fCep.setAlignmentX(0.0F);
        fCep.setAlignmentY(0.0F);
        fCep.setMaximumSize(new java.awt.Dimension(118, 20));
        fCep.setMinimumSize(new java.awt.Dimension(118, 20));
        fCep.setPreferredSize(new java.awt.Dimension(118, 20));

        labelAtivo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelAtivo.setForeground(new java.awt.Color(153, 153, 153));
        labelAtivo.setText("Ativo");

        fAtivo.setBackground(new java.awt.Color(245, 246, 250));
        fAtivo.setSelected(true);
        fAtivo.setAlignmentY(0.0F);
        fAtivo.setMaximumSize(new java.awt.Dimension(25, 25));
        fAtivo.setMinimumSize(new java.awt.Dimension(25, 25));

        fCodigo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigo.setAlignmentX(0.0F);
        fCodigo.setAlignmentY(0.0F);
        fCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigo.setMaximumSize(new java.awt.Dimension(118, 20));
        fCodigo.setMinimumSize(new java.awt.Dimension(118, 20));
        fCodigo.setPreferredSize(new java.awt.Dimension(118, 20));

        labelCodigo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelCodigo.setForeground(new java.awt.Color(153, 153, 153));
        labelCodigo.setText("Código");

        fNome.setBackground(new java.awt.Color(245, 246, 250));
        fNome.setAlignmentX(0.0F);
        fNome.setAlignmentY(0.0F);
        fNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNome.setMaximumSize(new java.awt.Dimension(409, 20));
        fNome.setMinimumSize(new java.awt.Dimension(409, 20));
        fNome.setName(""); // NOI18N
        fNome.setPreferredSize(new java.awt.Dimension(409, 20));

        labelNome.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelNome.setForeground(new java.awt.Color(153, 153, 153));
        labelNome.setText("Nome");

        labelDadosUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        labelDadosUsuario.setForeground(new java.awt.Color(51, 51, 51));
        labelDadosUsuario.setText("Dados do Cliente");
        labelDadosUsuario.setAlignmentY(0.0F);

        labelNasc.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelNasc.setForeground(new java.awt.Color(153, 153, 153));
        labelNasc.setText("Nascimento");

        fDtNascimento.setBackground(new java.awt.Color(245, 246, 250));
        fDtNascimento.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fDtNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM))));
        fDtNascimento.setAlignmentX(0.0F);
        fDtNascimento.setAlignmentY(0.0F);
        fDtNascimento.setMaximumSize(new java.awt.Dimension(111, 20));
        fDtNascimento.setMinimumSize(new java.awt.Dimension(111, 20));
        fDtNascimento.setPreferredSize(new java.awt.Dimension(111, 20));

        jLabelUsuario2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario2.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario2.setText("CPF");

        fCpf.setBackground(new java.awt.Color(245, 246, 250));
        fCpf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCpf.setMaximumSize(new java.awt.Dimension(118, 20));
        fCpf.setMinimumSize(new java.awt.Dimension(118, 20));
        fCpf.setPreferredSize(new java.awt.Dimension(118, 20));

        fSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        fSexo.setPreferredSize(new java.awt.Dimension(90, 20));

        EnderecoCliente.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        EnderecoCliente.setForeground(new java.awt.Color(51, 51, 51));
        EnderecoCliente.setText("Endereço do Cliente");
        EnderecoCliente.setAlignmentY(0.0F);

        labelCidade.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelCidade.setForeground(new java.awt.Color(153, 153, 153));
        labelCidade.setText("Cidade");

        fCidade.setBackground(new java.awt.Color(245, 246, 250));
        fCidade.setAlignmentX(0.0F);
        fCidade.setAlignmentY(0.0F);
        fCidade.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCidade.setMaximumSize(new java.awt.Dimension(118, 20));
        fCidade.setMinimumSize(new java.awt.Dimension(118, 20));
        fCidade.setPreferredSize(new java.awt.Dimension(118, 20));

        fEndereco.setBackground(new java.awt.Color(245, 246, 250));
        fEndereco.setAlignmentX(0.0F);
        fEndereco.setAlignmentY(0.0F);
        fEndereco.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEndereco.setMaximumSize(new java.awt.Dimension(409, 20));
        fEndereco.setMinimumSize(new java.awt.Dimension(409, 20));
        fEndereco.setPreferredSize(new java.awt.Dimension(409, 20));

        labelEnde.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelEnde.setForeground(new java.awt.Color(153, 153, 153));
        labelEnde.setText("Endereço");

        labelBairro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelBairro.setForeground(new java.awt.Color(153, 153, 153));
        labelBairro.setText("Bairro");

        fBairro.setBackground(new java.awt.Color(245, 246, 250));
        fBairro.setAlignmentX(0.0F);
        fBairro.setAlignmentY(0.0F);
        fBairro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fBairro.setPreferredSize(new java.awt.Dimension(100, 20));

        labelUf.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelUf.setForeground(new java.awt.Color(153, 153, 153));
        labelUf.setText("UF");

        fUf.setBackground(new java.awt.Color(245, 246, 250));
        fUf.setAlignmentX(0.0F);
        fUf.setAlignmentY(0.0F);
        fUf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fUf.setPreferredSize(new java.awt.Dimension(50, 20));

        labelEmail.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelEmail.setForeground(new java.awt.Color(153, 153, 153));
        labelEmail.setText("E-mail");

        labelTel.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelTel.setForeground(new java.awt.Color(153, 153, 153));
        labelTel.setText("Telefone");

        ContatoCliente.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        ContatoCliente.setForeground(new java.awt.Color(51, 51, 51));
        ContatoCliente.setText("Contatos do Cliente");
        ContatoCliente.setAlignmentY(0.0F);

        fCelular.setBackground(new java.awt.Color(245, 246, 250));
        fCelular.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            fCelular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ##### - ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fCelular.setMaximumSize(new java.awt.Dimension(143, 20));
        fCelular.setMinimumSize(new java.awt.Dimension(143, 20));
        fCelular.setPreferredSize(new java.awt.Dimension(143, 20));

        fTelefone.setBackground(new java.awt.Color(245, 246, 250));
        fTelefone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            fTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fTelefone.setMaximumSize(new java.awt.Dimension(118, 20));
        fTelefone.setMinimumSize(new java.awt.Dimension(118, 20));
        fTelefone.setPreferredSize(new java.awt.Dimension(118, 20));
        fTelefone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fTelefoneActionPerformed(evt);
            }
        });

        fId.setEditable(false);

        btNovoRegistro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/novo.png"))); // NOI18N
        btNovoRegistro.setToolTipText("Novo Registro");
        btNovoRegistro.setAlignmentY(0.0F);
        btNovoRegistro.setMaximumSize(new java.awt.Dimension(80, 39));
        btNovoRegistro.setMinimumSize(new java.awt.Dimension(80, 39));
        btNovoRegistro.setOpaque(false);
        btNovoRegistro.setPreferredSize(new java.awt.Dimension(80, 39));

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));

        labelSexo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelSexo.setForeground(new java.awt.Color(153, 153, 153));
        labelSexo.setText("Sexo");

        labelCep.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelCep.setForeground(new java.awt.Color(153, 153, 153));
        labelCep.setText("CEP");

        labelCel.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelCel.setForeground(new java.awt.Color(153, 153, 153));
        labelCel.setText("Celular");

        fEmail.setBackground(new java.awt.Color(245, 246, 250));
        fEmail.setAlignmentX(0.0F);
        fEmail.setAlignmentY(0.0F);
        fEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEmail.setPreferredSize(new java.awt.Dimension(240, 20));

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar Operação");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));

        javax.swing.GroupLayout panelCadClienteInternoLayout = new javax.swing.GroupLayout(panelCadClienteInterno);
        panelCadClienteInterno.setLayout(panelCadClienteInternoLayout);
        panelCadClienteInternoLayout.setHorizontalGroup(
            panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(310, 310, 310)
                        .addComponent(jLabelTituloCadastro))
                    .addComponent(labelDadosUsuario)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(labelCodigo)
                        .addGap(112, 112, 112)
                        .addComponent(labelNome)
                        .addGap(405, 405, 405)
                        .addComponent(labelAtivo))
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario2)
                        .addGap(128, 128, 128)
                        .addComponent(labelNasc)
                        .addGap(77, 77, 77)
                        .addComponent(labelSexo))
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(fDtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(fSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(EnderecoCliente)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(labelCep)
                        .addGap(127, 127, 127)
                        .addComponent(labelEnde))
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(labelCidade)
                        .addGap(112, 112, 112)
                        .addComponent(labelBairro)
                        .addGap(326, 326, 326)
                        .addComponent(labelUf))
                    .addComponent(ContatoCliente)
                    .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                            .addComponent(fCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(fBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(fUf, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                            .addComponent(fCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(fEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addComponent(btNovoRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(318, 318, 318)
                        .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                                .addComponent(fTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(fCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                                .addComponent(labelTel)
                                .addGap(105, 105, 105)
                                .addComponent(labelCel)))
                        .addGap(37, 37, 37)
                        .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelEmail)
                            .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(288, Short.MAX_VALUE))
        );
        panelCadClienteInternoLayout.setVerticalGroup(
            panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelTituloCadastro))
                .addGap(59, 59, 59)
                .addComponent(labelDadosUsuario)
                .addGap(12, 12, 12)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCodigo)
                    .addComponent(labelNome)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(labelAtivo)))
                .addGap(6, 6, 6)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(19, 19, 19)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelUsuario2)
                    .addComponent(labelNasc)
                    .addComponent(labelSexo))
                .addGap(6, 6, 6)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCadClienteInternoLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(fDtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(EnderecoCliente)
                .addGap(12, 12, 12)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCep)
                    .addComponent(labelEnde))
                .addGap(12, 12, 12)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCidade)
                    .addComponent(labelBairro)
                    .addComponent(labelUf))
                .addGap(12, 12, 12)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(ContatoCliente)
                .addGap(12, 12, 12)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTel)
                    .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelCel)
                        .addComponent(labelEmail)))
                .addGap(12, 12, 12)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addGroup(panelCadClienteInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btNovoRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 282;
        gridBagConstraints.ipady = 130;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panelCadCliente.add(panelCadClienteInterno, gridBagConstraints);

        jTabbedPaneCadastroCliente.addTab("", panelCadCliente);

        panelPesqCliente.setBackground(new java.awt.Color(245, 246, 250));
        panelPesqCliente.setAlignmentX(0.0F);
        panelPesqCliente.setAlignmentY(0.0F);
        panelPesqCliente.setMaximumSize(new java.awt.Dimension(0, 0));
        panelPesqCliente.setMinimumSize(new java.awt.Dimension(920, 790));
        panelPesqCliente.setPreferredSize(new java.awt.Dimension(920, 790));
        panelPesqCliente.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBackground(new java.awt.Color(245, 246, 250));

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");

        jLabelUsuario19.setBackground(new java.awt.Color(153, 153, 255));
        jLabelUsuario19.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelUsuario19.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUsuario19.setText("Pesquisa de Cliente");
        jLabelUsuario19.setAlignmentY(0.0F);
        jLabelUsuario19.setFocusable(false);
        jLabelUsuario19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelUsuario19.setIconTextGap(0);
        jLabelUsuario19.setInheritsPopupMenu(false);
        jLabelUsuario19.setMaximumSize(new java.awt.Dimension(0, 0));
        jLabelUsuario19.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabelUsuario19.setPreferredSize(new java.awt.Dimension(0, 0));
        jLabelUsuario19.setRequestFocusEnabled(false);
        jLabelUsuario19.setVerifyInputWhenFocusTarget(false);

        pesqCodigo.setBackground(new java.awt.Color(245, 246, 250));
        pesqCodigo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        pesqCodigo.setAlignmentX(0.0F);
        pesqCodigo.setAlignmentY(0.0F);
        pesqCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqCodigo.setMinimumSize(new java.awt.Dimension(0, 20));

        jPanelListagem.setMaximumSize(new java.awt.Dimension(880, 470));
        jPanelListagem.setMinimumSize(new java.awt.Dimension(880, 470));
        jPanelListagem.setPreferredSize(new java.awt.Dimension(880, 470));

        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableListagem);

        javax.swing.GroupLayout jPanelListagemLayout = new javax.swing.GroupLayout(jPanelListagem);
        jPanelListagem.setLayout(jPanelListagemLayout);
        jPanelListagemLayout.setHorizontalGroup(
            jPanelListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanelListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE))
        );
        jPanelListagemLayout.setVerticalGroup(
            jPanelListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
            .addGroup(jPanelListagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        jLabelUsuario12.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario12.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario12.setText("Código");

        pesqNome.setBackground(new java.awt.Color(245, 246, 250));
        pesqNome.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        pesqNome.setAlignmentX(0.0F);
        pesqNome.setAlignmentY(0.0F);
        pesqNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqNome.setPreferredSize(new java.awt.Dimension(0, 20));

        jLabelUsuario13.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario13.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario13.setText("Nome");

        pesqCpf.setBackground(new java.awt.Color(245, 246, 250));
        pesqCpf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            pesqCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        pesqCpf.setAlignmentX(0.0F);
        pesqCpf.setAlignmentY(0.0F);
        pesqCpf.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario25.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario25.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario25.setText("Situação");

        pesqCidade.setBackground(new java.awt.Color(245, 246, 250));
        pesqCidade.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        pesqCidade.setAlignmentX(0.0F);
        pesqCidade.setAlignmentY(0.0F);
        pesqCidade.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqCidade.setPreferredSize(new java.awt.Dimension(130, 20));

        jLabelUsuario14.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario14.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario14.setText("CPF");

        pesqBairro.setBackground(new java.awt.Color(245, 246, 250));
        pesqBairro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        pesqBairro.setAlignmentX(0.0F);
        pesqBairro.setAlignmentY(0.0F);
        pesqBairro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        pesqBairro.setPreferredSize(new java.awt.Dimension(150, 20));

        jLabelUsuario17.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario17.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario17.setText("Cidade");

        pesqAtivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Ativo", "Desativado" }));

        jLabelUsuario16.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario16.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario16.setText("Bairro");

        btPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisa.setToolTipText("Pesquisar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanelListagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(349, 349, 349))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario12, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUsuario14, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuario13, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelUsuario17)
                                            .addComponent(pesqCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(39, 39, 39)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pesqBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelUsuario16))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelUsuario25)
                                            .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                                .addComponent(btPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btExcluir)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btPesquisa)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabelUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuario12)
                            .addComponent(jLabelUsuario13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuario14)
                            .addComponent(jLabelUsuario17)
                            .addComponent(jLabelUsuario16)
                            .addComponent(jLabelUsuario25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pesqCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jPanelListagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btExcluir)
                    .addComponent(btEditar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 10, 28);
        panelPesqCliente.add(jPanel3, gridBagConstraints);

        jTabbedPaneCadastroCliente.addTab("", panelPesqCliente);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPaneCadastroCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPaneCadastroCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPaneCadastroCliente.getAccessibleContext().setAccessibleName("Cadastro");

        getAccessibleContext().setAccessibleName("view_clientes");
        getAccessibleContext().setAccessibleDescription("view_clientes");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fTelefoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fTelefoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fTelefoneActionPerformed

    



    




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ContatoCliente;
    private javax.swing.JLabel EnderecoCliente;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btNovoRegistro;
    private javax.swing.JButton btPesquisa;
    private javax.swing.JButton btSalvar;
    private javax.swing.JCheckBox fAtivo;
    private javax.swing.JTextField fBairro;
    private javax.swing.JFormattedTextField fCelular;
    private javax.swing.JFormattedTextField fCep;
    private javax.swing.JTextField fCidade;
    private javax.swing.JTextField fCodigo;
    private javax.swing.JFormattedTextField fCpf;
    private javax.swing.JFormattedTextField fDtNascimento;
    private javax.swing.JTextField fEmail;
    private javax.swing.JTextField fEndereco;
    private javax.swing.JTextField fId;
    private javax.swing.JTextField fNome;
    private javax.swing.JComboBox<String> fSexo;
    private javax.swing.JFormattedTextField fTelefone;
    private javax.swing.JTextField fUf;
    private javax.swing.JLabel jLabelTituloCadastro;
    private javax.swing.JLabel jLabelUsuario12;
    private javax.swing.JLabel jLabelUsuario13;
    private javax.swing.JLabel jLabelUsuario14;
    private javax.swing.JLabel jLabelUsuario16;
    private javax.swing.JLabel jLabelUsuario17;
    private javax.swing.JLabel jLabelUsuario19;
    private javax.swing.JLabel jLabelUsuario2;
    private javax.swing.JLabel jLabelUsuario25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelListagem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneCadastroCliente;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JLabel labelAtivo;
    private javax.swing.JLabel labelBairro;
    private javax.swing.JLabel labelCel;
    private javax.swing.JLabel labelCep;
    private javax.swing.JLabel labelCidade;
    private javax.swing.JLabel labelCodigo;
    private javax.swing.JLabel labelDadosUsuario;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelEnde;
    private javax.swing.JLabel labelNasc;
    private javax.swing.JLabel labelNome;
    private javax.swing.JLabel labelSexo;
    private javax.swing.JLabel labelTel;
    private javax.swing.JLabel labelUf;
    private javax.swing.JPanel panelCadCliente;
    private javax.swing.JPanel panelCadClienteInterno;
    private javax.swing.JPanel panelPesqCliente;
    private javax.swing.JComboBox<String> pesqAtivo;
    private javax.swing.JTextField pesqBairro;
    private javax.swing.JTextField pesqCidade;
    private javax.swing.JTextField pesqCodigo;
    private javax.swing.JFormattedTextField pesqCpf;
    private javax.swing.JTextField pesqNome;
    // End of variables declaration//GEN-END:variables








}
