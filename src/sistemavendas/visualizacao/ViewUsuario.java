/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.abstratas.View;
import infra.comunicacao.Erro;
import infra.comunicacao.Message;
import infra.comunicacao.Sucess;
import java.util.List;
import infra.comunicacao.Warning;
import infra.listagem.DefaultTableModelClientes;
import infra.operacoes.Operacao;
import infra.utilitarios.Mask;
import infra.utilitarios.Utils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import sistemavendas.controle.CUsuario;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NUsuario;


public class ViewUsuario extends View<NUsuario> {

    private static final long serialVersionUID = 1L;
    private CUsuario usuarioController;
    private NUsuario usuarioOld;
    private KeyListener validarSenhaKeyListener;
    
    public ViewUsuario() {
        initComponents();
        initViewUsuario();
        initializeView();        
    }




    
    
    
    protected void initViewUsuario() {
        usuarioController = new CUsuario();
        usuarioOld = new NUsuario();
        this.fId.setVisible(false);
        
        NUsuario usuarioLogado = ViewPrincipal.getUsuarioLogado();        
        if (!usuarioLogado.isAdministrador()) {
            this.btSalvar.setEnabled(false);
            this.btEditar.setEnabled(false);
            this.btExcluir.setEnabled(false);
            this.btPesquisa.setEnabled(false);        
            configurarOperacoesAutorizadas(usuarioLogado.operacoesAutorizadas(jLabelTituloCadastro.getText()));
        }
        
        this.setName("view_usuario");
        this.msgStatusSenha.setText("");
        jTabbedPaneCadastroUsuario.setTitleAt(0, "Cadastrar");
        jTabbedPaneCadastroUsuario.setTitleAt(1, "Pesquisar");
        initCampoSenha();
        initMasksValidators();
        configuraListagem();
        carregarCodigo();  
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
        CUsuario controller = new CUsuario();
        try {
            NUsuario usuario = controller.recuperaUltimoRegistro();
            this.fCodigo.setText(Integer.toString(usuario.getCodigo()+1));
        } catch (Exception ex) {
            this.fCodigo.setText("1");
        }
    }
    
    
    
    
    private void initCampoSenha() {
    
        Character echo = fSenha.getEchoChar();
        fMostraSenha.addActionListener((ActionEvent e ) -> {
            
            if (fMostraSenha.isSelected()) {
                fSenha.setEchoChar((char)0);
                fConfirmaSenha.setEchoChar((char)0);
            } else {
                fSenha.setEchoChar(echo);
                fConfirmaSenha.setEchoChar(echo);
            }
            
        });
    }

    
    private void configuraListagem() {
    
        DefaultTableModelClientes modelo = new DefaultTableModelClientes();
        modelo.addColumn("#");
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("CPF");
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
        // email
        jTableListagem.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // ativo
        jTableListagem.getColumnModel().getColumn(5).setPreferredWidth(40);
        jTableListagem.getColumnModel().getColumn(5).setMaxWidth(40);
    }
        
    
    public void model2View(NUsuario model) {
        usuarioOld = model;
        setValuesModel2View(model);       
        apagarSenhas();
        
        
        if (operacao == Operacao.ALTERAR) {
            msgStatusSenha.removeKeyListener(validarSenhaKeyListener);
            fConfirmaSenha.setVisible(true);
            labelConfirmaSenha.setText("Nova Senha");
            labelSenha.setText("Senha Antiga");
        } else {
            msgStatusSenha.addKeyListener(validarSenhaKeyListener);
            fConfirmaSenha.setVisible(false);
            labelConfirmaSenha.setText("Confirma Senha");
            labelSenha.setText("Senha");
        }
        
    }

    
    public NUsuario view2Model() {
        
        NUsuario usuario = setValuesView2Model();
        if (operacao == Operacao.CADASTRO) {
            msgStatusSenha.setVisible(true);
            usuario.setConfirmaSenha(recuperaConfirmaSenha());
            usuario.setSenha(recuperaSenha());
            return usuario;
        }
        
        if (operacao == Operacao.ALTERAR) {                     
            
            String senhaAntiga = recuperaSenha();
            String novaSenha = recuperaConfirmaSenha();
            
            CUsuario controller = new CUsuario();
            
            try {
                NUsuario usuarioTemp = controller.buscarPorId(usuario.getId());
                String senhaTemp = usuarioTemp.desencriptarSenha();
                if (senhaAntiga.equalsIgnoreCase(senhaTemp)) {
                    usuario.setSenha(novaSenha);
                    usuario.setConfirmaSenha(novaSenha);
                }
            } catch (Erro ex) {
                ex.show();
                return null;
            }
        }
        return usuario;
    }


    private void limparCampos() {        
        String codigo = fCodigo.getText();
        cleanViewFields();
        fCodigo.setText(codigo);
    }
    



    private NUsuario recebeDadosPesquisar() {
        return view2Model();
    }



    private NUsuario getUsuarioSelecionado() throws Exception {
        int row = jTableListagem.getSelectedRow();
        if (row > -1) {
            //pega o codigo do cliente na linha selecionada
            int codigo = (Integer) jTableListagem.getValueAt(row, 1);
            try {
                return usuarioController.buscarPorCodigo(codigo);
            } catch (Exception e) {
                throw new Message("Nenhum usuário encontrado", "Atenção");
            }
        }
        throw new Warning("Selecione um cliente da lista", "Atenção");
    }   
    
    
    private void mudarAba(int index, String label) {
        jTabbedPaneCadastroUsuario.setSelectedIndex(index);
        jTabbedPaneCadastroUsuario.setTitleAt(index, label);
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
        
        
        validarSenhaKeyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String senha1 = String.copyValueOf(fSenha.getPassword());
                String senha2 = String.copyValueOf(fConfirmaSenha.getPassword());
                if (senha1.equalsIgnoreCase(senha2)) {
                    msgStatusSenha.setForeground(new Color(0,153,0));
                    msgStatusSenha.setText("Senhas compatíveis");
                } else {
                    msgStatusSenha.setForeground(Color.RED);
                    msgStatusSenha.setText("Senhas diferentes");
                }
            }
        };        
        msgStatusSenha.addKeyListener(validarSenhaKeyListener);
    }

    
    

    
    
    private void clearTableList() {
        DefaultTableModel modelo = (DefaultTableModel) jTableListagem.getModel();
        modelo.setNumRows(0);
    }
    
    
    

    
    protected void refreshTableList(List<NUsuario> dataList) {
        try {
            clearTableList();
            DefaultTableModelClientes modelo = (DefaultTableModelClientes) jTableListagem.getModel();
            modelo.setNumRows(0);
            
            dataList.stream().forEach((nUsuario) -> {
                String cpf = Utils.setMask(nUsuario.getCpf(), Mask.CPF);
                String ativo = ativo2View(nUsuario.isAtivo());
                
                modelo.addRow(new Object[] {
                    nUsuario.getId(),
                    nUsuario.getCodigo(),
                    nUsuario.getNome(),
                    cpf,
                    nUsuario.getEmail(),
                    ativo
                });
            });
            
        } catch (Exception e) {}
    }
    

    private String ativo2View(boolean ativo) {
        return ativo ? "Sim" : "Não";
    }

    
    private boolean isCadastro() {
        String titleLocal = jTabbedPaneCadastroUsuario.getTitleAt(0);
        return titleLocal.equalsIgnoreCase("Cadastrar");
    }
    
    
    private void apagarSenhas() {
        this.fSenha.setText("");
        this.fConfirmaSenha.setText("");
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
            NUsuario c = view2Model();
            if (operacao == Operacao.CADASTRO) {
                try {
                    usuarioController.inserir(c);
                    new Sucess("Registro salvo com sucesso!", "Sucesso").show();
                    limparCampos();
                } catch (Erro ex) {
                    ex.show();
                    apagarSenhas();
                    return;
                }
                mudarAba(0, "Cadastrar");
            } else {
                try {
                    usuarioController.alterar(c);
                    new Sucess("Registro alterado com sucesso!", "Sucesso").show();
                } catch (Erro ex) {
                    ex.show();
                    apagarSenhas();
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
            setOperacao(Operacao.ALTERAR);
            NUsuario Usuario = getUsuarioSelecionado();
            model2View(Usuario);
            mudarAba(0, "Alterar");
        } catch (Exception ex) {
            out.show(ex);
        }
    }
    
    private void onClickExcluir() {
        List<NUsuario> list = new ArrayList();
        
        try {
            NUsuario c = getUsuarioSelecionado();
            setOperacao(Operacao.EXCLUIR);
            if (c != null) {
                int i = Utils.pedeConfirmacao("Deseja excluir o registro: " + c.getCodigo(), this);
                if (i==0) {
                    usuarioController.excluir(c.getCodigo());
                }
                list = usuarioController.getLista();
            } else {
                new Warning("Você deve selecionar um registro!", "Falha").show();
            }
        } catch (Exception ex) {
            out.show(ex);
        }
        refreshTableList(list);
    }    

    private void onClickPesquisar() {
        apagarSenhas();
        setOperacao(Operacao.PESQUISAR);
        NUsuario Usuario = recebeDadosPesquisar();
        List<NUsuario> lista = new ArrayList();
        try {
            clearTableList();
            lista = usuarioController.pesquisar(Usuario);
            refreshTableList(lista);
        } catch (Exception ex) {
            out.show(ex);
        }
        if (lista == null || lista.isEmpty()) {
            new Message("Nenhum usuário encontrado", "Sem Registros").show();
        }
    }

    
    ////////////////////////////////////////////////////////////////////////////

    
    
    private void initMasksValidators() {
        validator.required(fCodigo);
        validator.required(fNome);
        validator.required(fEmail);
        validator.required(fUsuario);
        validator.requiredSenha(fSenha);
        validator.requiredSenha(fConfirmaSenha);
    }    
    
    
    /**
     * **************************************************************
     * MÉTODOS QUE VÃO RECEBER OS DADOS DA MODEL PARA MOSTRAR NA TELA
     * **************************************************************
     */
    
    public void setAtivo(Boolean ativo) {
        this.fMostraSenha.setSelected(ativo);
    }
    
    public void setAdministrador(Boolean admin) {
        this.fAdministrador.setSelected(admin);
    }

    public void setBairro(String bairro) {
        this.fEmail.setText(bairro);
    }
    
    public void setCodigo(Integer codigo) {
        this.fCodigo.setText(Integer.toString(codigo));
    }

    public void setEmail(String email) {
        this.fEmail.setText(email);
    }

    public void setId(Integer id) {
        this.fId.setText(Integer.toString(id));
    }

    public void setNome(String nome) {
        this.fNome.setText(nome);
    }
    
    public void setUsuario(String usuario) {
        this.fUsuario.setText(usuario);
    }
    
    public void setSenha(String fSenha) {
        this.fSenha.setText(fSenha);
    }
    
    public void setCpf(String fCpf) {
        if (fCpf == null) this.fCpf.setText("");
        else {
            String cpf2 = Utils.setMask(fCpf, Mask.CPF);
            this.fCpf.setText(cpf2);
        }
    }
    
    /**
     * ***********************************************************
     *  MÉTODOS QUE VÃO PEGAR OS DADOS DA TELA PARA SETAR NA MODEL
     * ***********************************************************
     */
    
        
    public String recuperaConfirmaSenha() {
        char[] f = fConfirmaSenha.getPassword();
        return String.copyValueOf(f);
    }  
    
    public String recuperaSenha() {
        char[] f = fSenha.getPassword();
        return String.copyValueOf(f);
    }
    
    public Integer getCodigo() {
        return Integer.parseInt(this.fCodigo.getText());
    } 

    public Boolean isAtivo() {
        return this.fMostraSenha.isSelected();
    }

    public Boolean isAdministrador() {
        return this.fAdministrador.isSelected();
    }
    
    public String getBairro() {
        return fEmail.getText();
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

    public String getUsuario() {
        return fUsuario.getText();
    }
    
    public String getCpf() {
        return fCpf.getText();
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

        jTabbedPaneCadastroUsuario = new javax.swing.JTabbedPane();
        panelCadUsuario = new javax.swing.JPanel();
        jPanelCadastroUsuario = new javax.swing.JPanel();
        jLabelTituloCadastro = new javax.swing.JLabel();
        jLabelUsuario21 = new javax.swing.JLabel();
        jLabelUsuario = new javax.swing.JLabel();
        fNome = new javax.swing.JTextField();
        jLabelUsuario15 = new javax.swing.JLabel();
        jLabelUsuario7 = new javax.swing.JLabel();
        fUsuario = new javax.swing.JTextField();
        fEmail = new javax.swing.JTextField();
        jLabelUsuario11 = new javax.swing.JLabel();
        fMostraSenha = new javax.swing.JCheckBox();
        jLabelUsuario1 = new javax.swing.JLabel();
        btCancelar = new javax.swing.JButton();
        fCodigo = new javax.swing.JTextField();
        jLabelUsuario20 = new javax.swing.JLabel();
        fId = new javax.swing.JTextField();
        btSalvar = new javax.swing.JButton();
        labelSenha = new javax.swing.JLabel();
        labelConfirmaSenha = new javax.swing.JLabel();
        msgStatusSenha = new javax.swing.JLabel();
        fAtivo = new javax.swing.JCheckBox();
        jLabelUsuario2 = new javax.swing.JLabel();
        fConfirmaSenha = new javax.swing.JPasswordField();
        fSenha = new javax.swing.JPasswordField();
        fCpf = new javax.swing.JFormattedTextField();
        jLabelUsuario18 = new javax.swing.JLabel();
        fAdministrador = new javax.swing.JCheckBox();
        jLabelUsuario3 = new javax.swing.JLabel();
        panelPesqUsuario = new javax.swing.JPanel();
        jPanelPesquisaUsuario = new javax.swing.JPanel();
        jLabelUsuario19 = new javax.swing.JLabel();
        jLabelUsuario12 = new javax.swing.JLabel();
        pesqCodigo = new javax.swing.JTextField();
        jLabelUsuario13 = new javax.swing.JLabel();
        pesqNome = new javax.swing.JTextField();
        jLabelUsuario25 = new javax.swing.JLabel();
        pesqCpf = new javax.swing.JFormattedTextField();
        jLabelUsuario14 = new javax.swing.JLabel();
        btPesquisa = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        jPanelListagemUsuario = new javax.swing.JPanel();
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
        setMinimumSize(new java.awt.Dimension(920, 700));
        setPreferredSize(new java.awt.Dimension(920, 700));
        setRequestFocusEnabled(false);

        jTabbedPaneCadastroUsuario.setBackground(new java.awt.Color(245, 246, 250));
        jTabbedPaneCadastroUsuario.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jTabbedPaneCadastroUsuario.setAlignmentX(0.0F);
        jTabbedPaneCadastroUsuario.setAlignmentY(0.0F);
        jTabbedPaneCadastroUsuario.setFocusable(false);
        jTabbedPaneCadastroUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jTabbedPaneCadastroUsuario.setMaximumSize(new java.awt.Dimension(0, 0));
        jTabbedPaneCadastroUsuario.setMinimumSize(new java.awt.Dimension(920, 700));
        jTabbedPaneCadastroUsuario.setPreferredSize(new java.awt.Dimension(920, 700));

        panelCadUsuario.setBackground(new java.awt.Color(245, 246, 250));
        panelCadUsuario.setAlignmentX(0.0F);
        panelCadUsuario.setAlignmentY(0.0F);
        panelCadUsuario.setMaximumSize(new java.awt.Dimension(0, 0));
        panelCadUsuario.setMinimumSize(new java.awt.Dimension(850, 300));
        panelCadUsuario.setPreferredSize(new java.awt.Dimension(850, 600));
        panelCadUsuario.setLayout(new java.awt.GridBagLayout());

        jPanelCadastroUsuario.setBackground(new java.awt.Color(245, 246, 250));
        jPanelCadastroUsuario.setMaximumSize(null);
        jPanelCadastroUsuario.setMinimumSize(new java.awt.Dimension(920, 790));
        jPanelCadastroUsuario.setName(""); // NOI18N
        jPanelCadastroUsuario.setPreferredSize(new java.awt.Dimension(920, 790));

        jLabelTituloCadastro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro.setText("Cadastro de Usuário ");
        jLabelTituloCadastro.setAlignmentY(0.0F);

        jLabelUsuario21.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario21.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario21.setText("Dados do Usuário");
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

        jLabelUsuario15.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario15.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario15.setText("Dados de Login");
        jLabelUsuario15.setAlignmentY(0.0F);

        jLabelUsuario7.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario7.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario7.setText("Usuário");

        fUsuario.setBackground(new java.awt.Color(245, 246, 250));
        fUsuario.setAlignmentX(0.0F);
        fUsuario.setAlignmentY(0.0F);
        fUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fUsuario.setMinimumSize(null);
        fUsuario.setPreferredSize(new java.awt.Dimension(100, 20));

        fEmail.setBackground(new java.awt.Color(245, 246, 250));
        fEmail.setAlignmentX(0.0F);
        fEmail.setAlignmentY(0.0F);
        fEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEmail.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario11.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario11.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario11.setText("E-mail");

        fMostraSenha.setBackground(new java.awt.Color(245, 246, 250));
        fMostraSenha.setAlignmentY(0.0F);
        fMostraSenha.setMaximumSize(new java.awt.Dimension(25, 25));
        fMostraSenha.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario1.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario1.setText("Mostrar");

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));

        fCodigo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigo.setAlignmentX(0.0F);
        fCodigo.setAlignmentY(0.0F);
        fCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        jLabelUsuario20.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario20.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario20.setText("Código");

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));

        labelSenha.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelSenha.setForeground(new java.awt.Color(153, 153, 153));
        labelSenha.setText("Senha");

        labelConfirmaSenha.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelConfirmaSenha.setForeground(new java.awt.Color(153, 153, 153));
        labelConfirmaSenha.setText("Confirma Senha");

        msgStatusSenha.setText("Senha válida!");

        fAtivo.setBackground(new java.awt.Color(245, 246, 250));
        fAtivo.setSelected(true);
        fAtivo.setAlignmentY(0.0F);
        fAtivo.setMaximumSize(new java.awt.Dimension(25, 25));
        fAtivo.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario2.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario2.setText("Ativo");

        fConfirmaSenha.setBackground(new java.awt.Color(245, 246, 250));
        fConfirmaSenha.setAlignmentX(0.0F);
        fConfirmaSenha.setAlignmentY(0.0F);
        fConfirmaSenha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fConfirmaSenha.setMinimumSize(null);
        fConfirmaSenha.setPreferredSize(new java.awt.Dimension(100, 20));

        fSenha.setBackground(new java.awt.Color(245, 246, 250));
        fSenha.setAlignmentX(0.0F);
        fSenha.setAlignmentY(0.0F);
        fSenha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fSenha.setMinimumSize(null);
        fSenha.setPreferredSize(new java.awt.Dimension(100, 20));

        fCpf.setBackground(new java.awt.Color(245, 246, 250));
        fCpf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            fCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("### . ### . ### - ##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fCpf.setAlignmentX(0.0F);
        fCpf.setAlignmentY(0.0F);
        fCpf.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario18.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario18.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario18.setText("CPF");

        fAdministrador.setBackground(new java.awt.Color(245, 246, 250));
        fAdministrador.setAlignmentY(0.0F);
        fAdministrador.setMaximumSize(new java.awt.Dimension(25, 25));
        fAdministrador.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario3.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario3.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario3.setText("Administrador");

        javax.swing.GroupLayout jPanelCadastroUsuarioLayout = new javax.swing.GroupLayout(jPanelCadastroUsuario);
        jPanelCadastroUsuario.setLayout(jPanelCadastroUsuarioLayout);
        jPanelCadastroUsuarioLayout.setHorizontalGroup(
            jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addComponent(fUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUsuario3)))
                    .addComponent(jLabelUsuario21)
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelSenha)
                            .addComponent(fSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelConfirmaSenha)
                            .addComponent(fConfirmaSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fMostraSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUsuario1)))
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelUsuario20, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUsuario15)
                            .addComponent(jLabelUsuario7)
                            .addComponent(jLabelUsuario18, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fCpf, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(fCodigo))
                        .addGap(34, 34, 34)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario)
                                .addGap(400, 400, 400)
                                .addComponent(jLabelUsuario2))
                            .addComponent(jLabelUsuario11)
                            .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                    .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(msgStatusSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(fEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(fNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGap(33, 33, 33)
                                    .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(316, 316, 316)
                        .addComponent(jLabelTituloCadastro)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCadastroUsuarioLayout.setVerticalGroup(
            jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTituloCadastro))
                .addGap(58, 58, 58)
                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario21)
                                .addGap(21, 21, 21)
                                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario)
                                    .addComponent(jLabelUsuario2)))
                            .addComponent(jLabelUsuario20))
                        .addGap(4, 4, 4)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(24, 24, 24)
                        .addComponent(jLabelUsuario11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(jLabelUsuario15)
                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fAdministrador, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelSenha)
                                    .addComponent(labelConfirmaSenha)
                                    .addComponent(jLabelUsuario1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fConfirmaSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(fMostraSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(msgStatusSenha)))
                    .addGroup(jPanelCadastroUsuarioLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabelUsuario3)))
                .addGap(70, 70, 70)
                .addGroup(jPanelCadastroUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -40;
        gridBagConstraints.ipady = -147;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 17, 16, 21);
        panelCadUsuario.add(jPanelCadastroUsuario, gridBagConstraints);

        jTabbedPaneCadastroUsuario.addTab("", panelCadUsuario);

        panelPesqUsuario.setBackground(new java.awt.Color(245, 246, 250));
        panelPesqUsuario.setAlignmentX(0.0F);
        panelPesqUsuario.setAlignmentY(0.0F);
        panelPesqUsuario.setMaximumSize(new java.awt.Dimension(0, 0));
        panelPesqUsuario.setMinimumSize(new java.awt.Dimension(920, 790));
        panelPesqUsuario.setPreferredSize(new java.awt.Dimension(920, 790));
        panelPesqUsuario.setLayout(new java.awt.GridBagLayout());

        jPanelPesquisaUsuario.setBackground(new java.awt.Color(245, 246, 250));
        jPanelPesquisaUsuario.setMinimumSize(new java.awt.Dimension(894, 769));
        jPanelPesquisaUsuario.setPreferredSize(new java.awt.Dimension(894, 769));
        jPanelPesquisaUsuario.setRequestFocusEnabled(false);

        jLabelUsuario19.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelUsuario19.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario19.setText("Pesquisa de Usuário");
        jLabelUsuario19.setAlignmentY(0.0F);

        jLabelUsuario12.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario12.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario12.setText("Código");

        pesqCodigo.setBackground(new java.awt.Color(245, 246, 250));
        pesqCodigo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 14)); // NOI18N
        pesqCodigo.setAlignmentX(0.0F);
        pesqCodigo.setAlignmentY(0.0F);
        pesqCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        jLabelUsuario13.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario13.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario13.setText("Nome");

        pesqNome.setBackground(new java.awt.Color(245, 246, 250));
        pesqNome.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 14)); // NOI18N
        pesqNome.setAlignmentX(0.0F);
        pesqNome.setAlignmentY(0.0F);
        pesqNome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        jLabelUsuario25.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario25.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario25.setText("Ativo");

        pesqCpf.setBackground(new java.awt.Color(245, 246, 250));
        pesqCpf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            pesqCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("### . ### . ### - ##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        pesqCpf.setAlignmentX(0.0F);
        pesqCpf.setAlignmentY(0.0F);
        pesqCpf.setPreferredSize(new java.awt.Dimension(113, 20));

        jLabelUsuario14.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario14.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario14.setText("CPF");

        btPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisa.setToolTipText("Pesquisar");

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");

        jPanelListagemUsuario.setBackground(new java.awt.Color(153, 153, 153));
        jPanelListagemUsuario.setMinimumSize(new java.awt.Dimension(880, 470));
        jPanelListagemUsuario.setPreferredSize(new java.awt.Dimension(880, 470));

        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableListagem);

        javax.swing.GroupLayout jPanelListagemUsuarioLayout = new javax.swing.GroupLayout(jPanelListagemUsuario);
        jPanelListagemUsuario.setLayout(jPanelListagemUsuarioLayout);
        jPanelListagemUsuarioLayout.setHorizontalGroup(
            jPanelListagemUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListagemUsuarioLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 857, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelListagemUsuarioLayout.setVerticalGroup(
            jPanelListagemUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListagemUsuarioLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pesqAtivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Ativo", "Desativado" }));
        pesqAtivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesqAtivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPesquisaUsuarioLayout = new javax.swing.GroupLayout(jPanelPesquisaUsuario);
        jPanelPesquisaUsuario.setLayout(jPanelPesquisaUsuarioLayout);
        jPanelPesquisaUsuarioLayout.setHorizontalGroup(
            jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPesquisaUsuarioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelUsuario19)
                .addGap(372, 372, 372))
            .addGroup(jPanelPesquisaUsuarioLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPesquisaUsuarioLayout.createSequentialGroup()
                        .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPesquisaUsuarioLayout.createSequentialGroup()
                                .addComponent(jLabelUsuario14, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(103, 103, 103)
                                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario25)
                                    .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanelPesquisaUsuarioLayout.createSequentialGroup()
                                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pesqCpf, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pesqCodigo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelUsuario12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabelUsuario13, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPesquisaUsuarioLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir))
                    .addComponent(jPanelListagemUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 858, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        jPanelPesquisaUsuarioLayout.setVerticalGroup(
            jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPesquisaUsuarioLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabelUsuario19)
                .addGap(57, 57, 57)
                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuario12)
                    .addComponent(jLabelUsuario13))
                .addGap(7, 7, 7)
                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pesqCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btPesquisa)
                    .addGroup(jPanelPesquisaUsuarioLayout.createSequentialGroup()
                        .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuario14)
                            .addComponent(jLabelUsuario25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pesqCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pesqAtivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jPanelListagemUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPesquisaUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btExcluir)
                    .addComponent(btEditar))
                .addGap(25, 25, 25))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 24, 24);
        panelPesqUsuario.add(jPanelPesquisaUsuario, gridBagConstraints);

        jTabbedPaneCadastroUsuario.addTab("", panelPesqUsuario);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPaneCadastroUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPaneCadastroUsuario.getAccessibleContext().setAccessibleName("Cadastro");

        getAccessibleContext().setAccessibleName("view_usuarios");
        getAccessibleContext().setAccessibleDescription("view_usuarios");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pesqAtivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesqAtivoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pesqAtivoActionPerformed

    



    




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btPesquisa;
    private javax.swing.JButton btSalvar;
    private javax.swing.JCheckBox fAdministrador;
    private javax.swing.JCheckBox fAtivo;
    private javax.swing.JTextField fCodigo;
    private javax.swing.JPasswordField fConfirmaSenha;
    private javax.swing.JFormattedTextField fCpf;
    private javax.swing.JTextField fEmail;
    private javax.swing.JTextField fId;
    private javax.swing.JCheckBox fMostraSenha;
    private javax.swing.JTextField fNome;
    private javax.swing.JPasswordField fSenha;
    private javax.swing.JTextField fUsuario;
    private javax.swing.JLabel jLabelTituloCadastro;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JLabel jLabelUsuario1;
    private javax.swing.JLabel jLabelUsuario11;
    private javax.swing.JLabel jLabelUsuario12;
    private javax.swing.JLabel jLabelUsuario13;
    private javax.swing.JLabel jLabelUsuario14;
    private javax.swing.JLabel jLabelUsuario15;
    private javax.swing.JLabel jLabelUsuario18;
    private javax.swing.JLabel jLabelUsuario19;
    private javax.swing.JLabel jLabelUsuario2;
    private javax.swing.JLabel jLabelUsuario20;
    private javax.swing.JLabel jLabelUsuario21;
    private javax.swing.JLabel jLabelUsuario25;
    private javax.swing.JLabel jLabelUsuario3;
    private javax.swing.JLabel jLabelUsuario7;
    private javax.swing.JPanel jPanelCadastroUsuario;
    private static javax.swing.JPanel jPanelListagemUsuario;
    private javax.swing.JPanel jPanelPesquisaUsuario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneCadastroUsuario;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JLabel labelConfirmaSenha;
    private javax.swing.JLabel labelSenha;
    private javax.swing.JLabel msgStatusSenha;
    private javax.swing.JPanel panelCadUsuario;
    private javax.swing.JPanel panelPesqUsuario;
    private javax.swing.JComboBox<String> pesqAtivo;
    private javax.swing.JTextField pesqCodigo;
    private javax.swing.JFormattedTextField pesqCpf;
    private javax.swing.JTextField pesqNome;
    // End of variables declaration//GEN-END:variables

    private static class KeyAdapterImpl extends KeyAdapter {

        public KeyAdapterImpl() {
        }

        public void keyPressed(java.awt.event.KeyEvent evt) {
            
        }
    }






}
