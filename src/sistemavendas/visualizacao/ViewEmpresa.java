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
import infra.comunicacao.Warning;
import infra.listagem.DefaultTableModelEmpresa;
import infra.operacoes.Operacao;
import infra.utilitarios.Mask;
import infra.utilitarios.Utils;
import infra.validadores.Validador;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import sistemavendas.arquivos.Arquivo;
import sistemavendas.arquivos.Imagem;
import sistemavendas.controle.CEmpresa;
import sistemavendas.negocio.CRT;
import sistemavendas.negocio.NEmpresa;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NUsuario;

/**
 *
 * @author rhuan
 */
public class ViewEmpresa extends View<NEmpresa> {

    
    private NEmpresa empresaOld;
    
    
    
    /**
     * Creates new form ViewEmpresa
     */
    public ViewEmpresa() {
        initComponents();
        initViewProduto();
        initializeView();
        initMasksValidators();
        carregarCodigo();
    }

    
    
    private void initViewProduto() {
        empresaOld = new NEmpresa();
        this.fId.setVisible(false);         
        this.fLogomarca.setVisible(false);
        
        this.btSalvar.setEnabled(false);
        this.btEditar.setEnabled(false);
        this.btExcluir.setEnabled(false);
        this.btPesquisa.setEnabled(false);
        
        
        this.setName("view_empresa");
        this.jLabelTituloCadastro.setText("Cadastro de Empresa");
        jTabbedPaneCadastroEmpresa.setTitleAt(0, "Cadastrar");
        jTabbedPaneCadastroEmpresa.setTitleAt(1, "Pesquisar");        
        configuraListagem();
        
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
        CEmpresa controller = new CEmpresa();
        try {
            NEmpresa produto = controller.recuperarUltimoRegistro();
            this.fCodigo.setText(Integer.toString(produto.getCodigo()+1));
        } catch (Exception ex) {
            this.fCodigo.setText("1");
        }
    }
    
    
    
    private void configuraListagem() { 
        DefaultTableModelEmpresa modelo = new DefaultTableModelEmpresa();
        modelo.addColumn("#");
        modelo.addColumn("Código");
        modelo.addColumn("Nome Fantasia");
        modelo.addColumn("Razão Social");
        modelo.addColumn("Produtor Rural");
        modelo.addColumn("CPF/CNPJ");        
        jTableListagem.setModel(modelo);
        
        // id
        jTableListagem.getColumnModel().getColumn(0).setPreferredWidth(40);
        jTableListagem.getColumnModel().getColumn(0).setMaxWidth(40);
        // codigo
        jTableListagem.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableListagem.getColumnModel().getColumn(1).setMaxWidth(60);
        // nome fantasia
        jTableListagem.getColumnModel().getColumn(2).setPreferredWidth(150);
        // razao social
        jTableListagem.getColumnModel().getColumn(3).setPreferredWidth(150);
        // produtor rural
        jTableListagem.getColumnModel().getColumn(4).setPreferredWidth(60);
        // cnpj
        jTableListagem.getColumnModel().getColumn(5).setPreferredWidth(60);
    }
    
    
    
    public void model2View(NEmpresa empresa) {
        limparCampos();
        empresaOld = empresa;
        setValuesModel2View(empresa);
        carregarImagemSelecionada(empresa.getLogomarca());
        try {
            String senhaDesenc = empresa.desencriptarSenha();
            fSenha.setText(senhaDesenc);
        } catch (Erro ex) {
            ex.show();
        }
    }
    
    
    public NEmpresa view2Model() {
        NEmpresa empresa = (NEmpresa) setValuesView2Model();
        if (!fLogomarca.getText().isEmpty()) {
            empresa.setLogomarca(fLogomarca.getText());
        }
        return empresa;
    }
    
    
    private void carregarImagemSelecionada() {
        panelImagem.repaint();
        fLogomarca.setVisible(false);
        srcImgLogo.setText("");
        Arquivo arq = new Arquivo();
        File origem = arq.getPathOriginFile();
        if (origem != null) {
            fLogomarca.setText(origem.getAbsolutePath());
            srcImgLogo.setIcon(new ImageIcon(Imagem.loadImage(origem, 100, 100)));
            panelImagem.repaint();
        } else {
            srcImgLogo.setIcon(null);
        }
    }
    
    
    private void carregarImagemSelecionada(String caminhoOrigem) {
        srcImgLogo.setText("");
        if (caminhoOrigem != null && !caminhoOrigem.isEmpty()) {
            File origem = new File(caminhoOrigem);
            fLogomarca.setText(origem.getAbsolutePath());
            srcImgLogo.setIcon(new ImageIcon(Imagem.loadImage(origem, 100, 100)));        
            //panelImagem.add(srcImgLogo);
            panelImagem.repaint();            
        }
    }


    private void carregarCertificadoDigital() {
        fCertificado.setText("");
        Arquivo arq = new Arquivo();
        File origem = arq.getPathOriginFile();
        if (origem != null) {
            fCertificado.setText(origem.getAbsolutePath());
        }
    }
    
    
    private void carregarCertificadoDigital(String caminho) {
        fCertificado.setText(caminho);
    }
    
    
    private void limparCampos() {        
        String codigo = fCodigo.getText();
        cleanViewFields();
        fCodigo.setText(codigo);
        srcImgLogo.setIcon(null);
        this.fLogomarca.setVisible(false);
        //#,##0
    }
    
    
    
    private NEmpresa recebeDadosPesquisar() {
        NEmpresa produto = view2Model();
        return produto;
    }
    
    
    private void mudarAba(int index, String label) {
        jTabbedPaneCadastroEmpresa.setSelectedIndex(index);
        jTabbedPaneCadastroEmpresa.setTitleAt(index, label);
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
        
        
        
        /**
         * Botão de salvar imagem
         */
        btSrcImg.addActionListener((ActionEvent e) -> {
            carregarImagemSelecionada();
        });
        
        
        btCarregarCertificado.addActionListener((ActionEvent e) -> {
            carregarCertificadoDigital();
        });
        
        fProdutorRural.addActionListener((ActionEvent e) -> {
            if (fProdutorRural.isSelected()) {
                fCnpj.setText("");
                fCnpj.setEnabled(false);
                fCpf.setEnabled(true);
            } else {
                fCpf.setText("");
                fCpf.setEnabled(false);
                fCnpj.setEnabled(true);
            }
        });        
    }

    
    
    private boolean isCadastro() {
        String titleLocal = jTabbedPaneCadastroEmpresa.getTitleAt(0);
        return titleLocal.equalsIgnoreCase("Cadastrar");
    }
    
    
    
    private void onClickSalvar() {
        
        String msg = "";
        CEmpresa produtoController = new CEmpresa();
        
        if (isCadastro()) {
            setOperacao(Operacao.CADASTRO);
            msg = "Confirma o cadastro ?";
        } else {
            setOperacao(Operacao.ALTERAR);
            msg = "Confirma a alteração ?";
        }
        
        
        int i = Utils.pedeConfirmacao(msg, this);

        if (i == 0) {
            NEmpresa c = view2Model();
            
            if (operacao == Operacao.CADASTRO) {
                try {
                    produtoController.inserir(c);
                    new Sucess("Registro salvo com sucesso!", "Sucesso").show();
                    limparCampos();
                } catch (Erro ex) {
                    ex.show();
                    return;
                }
            } else {
                try {
                    produtoController.alterar(c);
                    new Sucess("Registro alterado com sucesso!", "Sucesso").show();
                    limparCampos();
                } catch (Erro ex) {
                    ex.show();
                    return;
                }
            }            
            mudarAba(0, "Cadastrar");
            carregarCodigo();
        }
    }
    
    
    private void onClickEditar() {
        try {            
            NEmpresa produto = getEmpresaSelecionada();
            model2View(produto);
            mudarAba(0, "Alterar");
            setOperacao(Operacao.ALTERAR);
        } catch (Message ex) {
            out.show(ex);
        }
    }
    
    
    
    private void onClickCancelar() {
        limparCampos();
        mudarAba(0, "Cadastrar");
        carregarCodigo();
        ViewPrincipal.showDashBoard();
    }
    

    
    
    private void onClickExcluir() {
        List<NEmpresa> list = new ArrayList();
        
        try {
            CEmpresa clienteController = new CEmpresa();
            NEmpresa c = getEmpresaSelecionada();
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
        List<NEmpresa> lista = new ArrayList();
        try {
            CEmpresa produtoController = new CEmpresa();
            lista = produtoController.getLista();
            refreshTableList(lista);
        } catch (Exception ex) {
            out.show(ex);
        }
        
        if (lista == null || lista.isEmpty()) {
            new Message("Nenhuma empresa encontrada", "Sem Registros").show();
        }
    }
    
    
    
    
    private NEmpresa getEmpresaSelecionada() throws Message {
        int index = jTableListagem.getSelectedRow();
        if (index > -1) {
            int idLocal = (int) jTableListagem.getValueAt(index, 0);
            CEmpresa controller = new CEmpresa();
            try {
                return controller.buscarPorId(idLocal);
            } catch (Erro ex) {
                throw new Warning("Nenhuma empresa encontrado", "Atenção");
            }
        }
        throw new Warning("Selecione uma empresa da lista", "Atenção");
    }
    
     
    protected void refreshTableList(List<NEmpresa> dataList) {
        try {            
            DefaultTableModelEmpresa modelo = (DefaultTableModelEmpresa) jTableListagem.getModel();
            modelo.setNumRows(0);
            
            dataList.stream().forEach((nProduto) -> {
                String cpfCnpj = "";
                String isProdRural = "";
                if (nProduto.isProdutorRural()) {
                    cpfCnpj = Utils.setMask(nProduto.getCpf(), Mask.CPF);
                    isProdRural = "Sim";
                } else {
                    cpfCnpj = Utils.setMask(nProduto.getCnpj(), Mask.CNPJ);
                    isProdRural = "Não";
                }
                
                modelo.addRow(new Object[] {
                    nProduto.getId(),
                    nProduto.getCodigo(),
                    nProduto.getNomeFantasia(),
                    nProduto.getRazaoSocial(),
                    isProdRural,
                    cpfCnpj
                });
            });
            
        } catch (Exception e) {e.printStackTrace();}
    }
    
    
    
    
    private void initMasksValidators() {
        validator.fieldCep(fCep, empresaOld.getCep(), fLogradouro, fBairro, fCidade, fUf);
        validator.fieldCpf(fCpf, empresaOld.getCpf(), true, "isCpfValid", new Validador(), String.class);
        validator.fieldCnpj(fCnpj, empresaOld.getCnpj(), true, "isCnpjValid", new Validador(), String.class);

        validator.required(fNomeFantasia);
        validator.required(fRazaoSocial);
        validator.required(fEmail);
        validator.fieldPhone(fTelefone, false);
        validator.fieldCellPhone(fCelular, false);        
    }    
    
    
     
    public String getBairro() {
        return fBairro.getText();
    }

    public void setBairro(String fBairro) {
        this.fBairro.setText(fBairro);
    }

    public String getCelular() {
        return fCelular.getText();
    }

    public void setCelular(String fCelular) {
        this.fCelular.setText(fCelular);
    }

    public String getCep() {
        return fCep.getText();
    }

    public void setCep(String fCep) {
        this.fCep.setText(fCep);
    }

    public String getCertificado() {
        return fCertificado.getText();
    }

    public void setCertificado(String fCertificado) {
        this.fCertificado.setText(fCertificado);
    }

    public String getCidade() {
        return fCidade.getText();
    }

    public void setCidade(String fCidade) {
        this.fCidade.setText(fCidade);
    }
    
    public String getLogomarca() {
        return fLogomarca.getText();
    }
    
    public void setLogomarca(String fLogomarca) {
        this.fLogomarca.setText(fLogomarca);
    }

    public String getCnpj() {
        return fCnpj.getText();
    }

    public void setCnpj(String fCnpj) {
        this.fCnpj.setText(fCnpj);
    }

    public Integer getCodigo() {
        return Integer.parseInt(fCodigo.getText());
    }

    public void setCodigo(Integer fCodigo) {
        this.fCodigo.setText(fCodigo.toString());
    }

    public String getCpf() {
        return fCpf.getText();
    }

    public void setCpf(String fCpf) {
        this.fCpf.setText(fCpf);
    }


    public CRT getCrt() {
        String item = (String) fCrt.getSelectedItem();
        if (item.equals("Simples Nacional")) {
            return CRT.SIMPLES_NACIONAL;
        }
        if (item.equals("Simples Nacional, excesso sublimite de receita bruta")) {
            return CRT.SIMPLES_NACIONAL_EXCESSO_RECEITA_BRUTA;
        }
        if (item.equals("Regime Normal. (v2.0)")) {
            return CRT.REGIME_NORMAL;
        }
        return null;
    }

    
    public void setCrt(CRT fCrt) {
        if (fCrt == CRT.REGIME_NORMAL) {
            this.fCrt.setSelectedItem("Regime Normal. (v2.0)");
        }
        if (fCrt == CRT.SIMPLES_NACIONAL) {
            this.fCrt.setSelectedItem("Simples Nacional");
        }
        if (fCrt == CRT.SIMPLES_NACIONAL_EXCESSO_RECEITA_BRUTA) {
            this.fCrt.setSelectedItem("Simples Nacional, excesso sublimite de receita bruta");
        }
    }

    public String getEmail() {
        return fEmail.getText();
    }

    public void setEmail(String fEmail) {
        this.fEmail.setText(fEmail);
    }

    public Integer getId() {
        return Integer.parseInt(fId.getText());
    }

    public void setId(Integer fId) {
        this.fId.setText(fId.toString());
    }

    public Integer getInscricaoEstadual() {
        return Integer.parseInt(fInscricaoEstadual.getText());
    }

    public void setInscricaoEstadual(Integer fIncricaoEstadual) {
        this.fInscricaoEstadual.setText(fIncricaoEstadual.toString());
    }

    public Integer getInscricaoMunicipal() {
        return Integer.parseInt(fInscricaoMunicipal.getText());
    }

    public void setInscricaoMunicipal(Integer fInscricaoMunicipal) {
        this.fInscricaoMunicipal.setText(fInscricaoMunicipal.toString());
    }

    public String getLogradouro() {
        return fLogradouro.getText();
    }

    public void setLogradouro(String fLogradouro) {
        this.fLogradouro.setText(fLogradouro);
    }

    public String getMunicipio() {
        return fMunicipio.getText();
    }

    public void setMunicipio(String fMunicipio) {
        this.fMunicipio.setText(fMunicipio);
    }

    public String getNomeFantasia() {
        return fNomeFantasia.getText();
    }

    public void setNomeFantasia(String fNomeFantasia) {
        this.fNomeFantasia.setText(fNomeFantasia);
    }

    public Boolean getProdutorRural() {
        return fProdutorRural.isSelected();
    }

    public void setProdutorRural(Boolean fProdutorRural) {
        this.fProdutorRural.setSelected(fProdutorRural);
    }

    public String getRazaoSocial() {
        return fRazaoSocial.getText();
    }

    public void setRazaoSocial(String fRazaoSocial) {
        this.fRazaoSocial.setText(fRazaoSocial);
    }

    public String getSenha() {
        return fSenha.getText();
    }

    public void setSenha(String fSenha) {
        this.fSenha.setText(fSenha);
    }

    public String getTelefone() {
        return fTelefone.getText();
    }

    public void setTelefone(String fTelefone) {
        this.fTelefone.setText(fTelefone);
    }

    public String getUf() {
        return fUf.getText();
    }

    public void setUf(String fUf) {
        this.fUf.setText(fUf);
    }
    
    public Integer getNumero() {
        return Integer.parseInt(this.fNumero.getText());
    }
    
    public void setNumero(Integer numero) {
        this.fNumero.setText(numero.toString());
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

        jTabbedPaneCadastroEmpresa = new javax.swing.JTabbedPane();
        panelCadEmpresa = new javax.swing.JPanel();
        jPanelCadastroEmpresa = new javax.swing.JPanel();
        jLabelTituloCadastro = new javax.swing.JLabel();
        jLabelUsuario21 = new javax.swing.JLabel();
        jLabelUsuario20 = new javax.swing.JLabel();
        fCodigo = new javax.swing.JTextField();
        fProdutorRural = new javax.swing.JCheckBox();
        jLabelUsuario1 = new javax.swing.JLabel();
        labelcpj = new javax.swing.JLabel();
        fCnpj = new javax.swing.JFormattedTextField();
        jLabelUsuario3 = new javax.swing.JLabel();
        fCpf = new javax.swing.JFormattedTextField();
        labelNome = new javax.swing.JLabel();
        fRazaoSocial = new javax.swing.JTextField();
        fInscricaoEstadual = new javax.swing.JTextField();
        labelNome1 = new javax.swing.JLabel();
        fCrt = new javax.swing.JComboBox<String>();
        labelSexo = new javax.swing.JLabel();
        fNomeFantasia = new javax.swing.JTextField();
        fInscricaoMunicipal = new javax.swing.JTextField();
        labelNome2 = new javax.swing.JLabel();
        labelNome3 = new javax.swing.JLabel();
        EnderecoCliente = new javax.swing.JLabel();
        labelCep = new javax.swing.JLabel();
        fCep = new javax.swing.JFormattedTextField();
        labelEnde = new javax.swing.JLabel();
        fLogradouro = new javax.swing.JTextField();
        labelCidade = new javax.swing.JLabel();
        fCidade = new javax.swing.JTextField();
        labelBairro = new javax.swing.JLabel();
        fBairro = new javax.swing.JTextField();
        labelUf = new javax.swing.JLabel();
        fUf = new javax.swing.JTextField();
        fMunicipio = new javax.swing.JTextField();
        labelBairro1 = new javax.swing.JLabel();
        panelImagem = new javax.swing.JPanel();
        srcImgLogo = new javax.swing.JLabel();
        btSrcImg = new javax.swing.JButton();
        jLabelUsuario23 = new javax.swing.JLabel();
        jLabelUsuario9 = new javax.swing.JLabel();
        fTelefone = new javax.swing.JFormattedTextField();
        jLabelUsuario10 = new javax.swing.JLabel();
        fCelular = new javax.swing.JFormattedTextField();
        jLabelUsuario22 = new javax.swing.JLabel();
        fEmail = new javax.swing.JTextField();
        jLabelUsuario24 = new javax.swing.JLabel();
        jLabelUsuario25 = new javax.swing.JLabel();
        fCertificado = new javax.swing.JTextField();
        btCarregarCertificado = new javax.swing.JButton();
        jLabelUsuario26 = new javax.swing.JLabel();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        fId = new javax.swing.JTextField();
        fLogomarca = new javax.swing.JLabel();
        fComplemento = new javax.swing.JTextField();
        fNumero = new javax.swing.JTextField();
        labelBairro2 = new javax.swing.JLabel();
        labelUf1 = new javax.swing.JLabel();
        fSenha = new javax.swing.JPasswordField();
        fIbge = new javax.swing.JTextField();
        panelPesqEmpresa = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanelListagemEmpresa = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListagem = new javax.swing.JTable();
        btPesquisa = new javax.swing.JButton();
        jLabelUsuario19 = new javax.swing.JLabel();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(920, 870));
        setPreferredSize(new java.awt.Dimension(920, 820));

        jTabbedPaneCadastroEmpresa.setBackground(new java.awt.Color(245, 246, 250));
        jTabbedPaneCadastroEmpresa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTabbedPaneCadastroEmpresa.setForeground(new java.awt.Color(51, 51, 51));
        jTabbedPaneCadastroEmpresa.setMinimumSize(new java.awt.Dimension(920, 820));
        jTabbedPaneCadastroEmpresa.setPreferredSize(new java.awt.Dimension(920, 820));

        panelCadEmpresa.setBackground(new java.awt.Color(245, 246, 250));
        panelCadEmpresa.setLayout(new java.awt.GridBagLayout());

        jPanelCadastroEmpresa.setBackground(new java.awt.Color(245, 246, 250));

        jLabelTituloCadastro.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTituloCadastro.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTituloCadastro.setText("Cadastro de Empresa");
        jLabelTituloCadastro.setAlignmentY(0.0F);

        jLabelUsuario21.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario21.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario21.setText("Dados da Empresa");
        jLabelUsuario21.setAlignmentY(0.0F);

        jLabelUsuario20.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario20.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario20.setText("Código");

        fCodigo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigo.setAlignmentX(0.0F);
        fCodigo.setAlignmentY(0.0F);
        fCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCodigo.setMinimumSize(new java.awt.Dimension(0, 20));
        fCodigo.setPreferredSize(new java.awt.Dimension(0, 20));

        fProdutorRural.setBackground(new java.awt.Color(245, 246, 250));
        fProdutorRural.setSelected(true);
        fProdutorRural.setAlignmentY(0.0F);
        fProdutorRural.setMaximumSize(new java.awt.Dimension(25, 25));
        fProdutorRural.setMinimumSize(new java.awt.Dimension(25, 25));

        jLabelUsuario1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario1.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario1.setText("Produtor Rural");

        labelcpj.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelcpj.setForeground(new java.awt.Color(153, 153, 153));
        labelcpj.setText("CNPJ");

        fCnpj.setBackground(new java.awt.Color(245, 246, 250));
        fCnpj.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCnpj.setMinimumSize(new java.awt.Dimension(100, 20));
        fCnpj.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario3.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario3.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario3.setText("CPF");

        fCpf.setBackground(new java.awt.Color(245, 246, 250));
        fCpf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCpf.setMaximumSize(new java.awt.Dimension(118, 20));
        fCpf.setMinimumSize(new java.awt.Dimension(118, 20));
        fCpf.setPreferredSize(new java.awt.Dimension(118, 20));

        labelNome.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelNome.setForeground(new java.awt.Color(153, 153, 153));
        labelNome.setText("Razão Social");

        fRazaoSocial.setBackground(new java.awt.Color(245, 246, 250));
        fRazaoSocial.setAlignmentX(0.0F);
        fRazaoSocial.setAlignmentY(0.0F);
        fRazaoSocial.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fRazaoSocial.setMaximumSize(new java.awt.Dimension(409, 20));
        fRazaoSocial.setMinimumSize(new java.awt.Dimension(409, 20));
        fRazaoSocial.setName(""); // NOI18N
        fRazaoSocial.setPreferredSize(new java.awt.Dimension(409, 20));

        fInscricaoEstadual.setBackground(new java.awt.Color(245, 246, 250));
        fInscricaoEstadual.setAlignmentX(0.0F);
        fInscricaoEstadual.setAlignmentY(0.0F);
        fInscricaoEstadual.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fInscricaoEstadual.setMaximumSize(new java.awt.Dimension(409, 20));
        fInscricaoEstadual.setMinimumSize(new java.awt.Dimension(409, 20));
        fInscricaoEstadual.setName(""); // NOI18N
        fInscricaoEstadual.setPreferredSize(new java.awt.Dimension(409, 20));

        labelNome1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelNome1.setForeground(new java.awt.Color(153, 153, 153));
        labelNome1.setText("Nome Fantasia");

        fCrt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simples Nacional", "Simples Nacional, excesso sublimite de receita bruta", "Regime Normal. (v2.0)" }));
        fCrt.setPreferredSize(new java.awt.Dimension(90, 20));

        labelSexo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelSexo.setForeground(new java.awt.Color(153, 153, 153));
        labelSexo.setText("CRT");

        fNomeFantasia.setBackground(new java.awt.Color(245, 246, 250));
        fNomeFantasia.setAlignmentX(0.0F);
        fNomeFantasia.setAlignmentY(0.0F);
        fNomeFantasia.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNomeFantasia.setMaximumSize(new java.awt.Dimension(409, 20));
        fNomeFantasia.setMinimumSize(new java.awt.Dimension(409, 20));
        fNomeFantasia.setName(""); // NOI18N
        fNomeFantasia.setPreferredSize(new java.awt.Dimension(409, 20));

        fInscricaoMunicipal.setBackground(new java.awt.Color(245, 246, 250));
        fInscricaoMunicipal.setAlignmentX(0.0F);
        fInscricaoMunicipal.setAlignmentY(0.0F);
        fInscricaoMunicipal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fInscricaoMunicipal.setMaximumSize(new java.awt.Dimension(409, 20));
        fInscricaoMunicipal.setMinimumSize(new java.awt.Dimension(409, 20));
        fInscricaoMunicipal.setName(""); // NOI18N
        fInscricaoMunicipal.setPreferredSize(new java.awt.Dimension(409, 20));

        labelNome2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelNome2.setForeground(new java.awt.Color(153, 153, 153));
        labelNome2.setText("Inscrição Estadual");

        labelNome3.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelNome3.setForeground(new java.awt.Color(153, 153, 153));
        labelNome3.setText("Inscrição Municipal");

        EnderecoCliente.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        EnderecoCliente.setForeground(new java.awt.Color(51, 51, 51));
        EnderecoCliente.setText("Endereço da Empresa");
        EnderecoCliente.setAlignmentY(0.0F);

        labelCep.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelCep.setForeground(new java.awt.Color(153, 153, 153));
        labelCep.setText("CEP");

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

        labelEnde.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelEnde.setForeground(new java.awt.Color(153, 153, 153));
        labelEnde.setText("Logradouro");

        fLogradouro.setBackground(new java.awt.Color(245, 246, 250));
        fLogradouro.setAlignmentX(0.0F);
        fLogradouro.setAlignmentY(0.0F);
        fLogradouro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fLogradouro.setMaximumSize(new java.awt.Dimension(409, 20));
        fLogradouro.setMinimumSize(new java.awt.Dimension(409, 20));
        fLogradouro.setPreferredSize(new java.awt.Dimension(409, 20));

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

        fMunicipio.setBackground(new java.awt.Color(245, 246, 250));
        fMunicipio.setAlignmentX(0.0F);
        fMunicipio.setAlignmentY(0.0F);
        fMunicipio.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fMunicipio.setPreferredSize(new java.awt.Dimension(100, 20));

        labelBairro1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelBairro1.setForeground(new java.awt.Color(153, 153, 153));
        labelBairro1.setText("Municipio");

        panelImagem.setBackground(new java.awt.Color(204, 204, 204));
        panelImagem.setBorder(new javax.swing.border.MatteBorder(null));
        panelImagem.setOpaque(false);

        srcImgLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        srcImgLogo.setText("Sem imagem");
        srcImgLogo.setDisplayedMnemonicIndex(0);
        srcImgLogo.setMaximumSize(new java.awt.Dimension(0, 0));
        srcImgLogo.setMinimumSize(new java.awt.Dimension(100, 100));
        srcImgLogo.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panelImagemLayout = new javax.swing.GroupLayout(panelImagem);
        panelImagem.setLayout(panelImagemLayout);
        panelImagemLayout.setHorizontalGroup(
            panelImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srcImgLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelImagemLayout.setVerticalGroup(
            panelImagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(srcImgLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btSrcImg.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btSrcImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/Folder.png"))); // NOI18N
        btSrcImg.setToolTipText("Carregar Imagem");
        btSrcImg.setAlignmentY(0.0F);
        btSrcImg.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btSrcImg.setOpaque(false);
        btSrcImg.setPreferredSize(new java.awt.Dimension(210, 40));

        jLabelUsuario23.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario23.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario23.setText("Contatos da Empresa");
        jLabelUsuario23.setAlignmentY(0.0F);

        jLabelUsuario9.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario9.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario9.setText("Telefone");

        fTelefone.setBackground(new java.awt.Color(245, 246, 250));
        fTelefone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fTelefone.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario10.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario10.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario10.setText("Celular");

        fCelular.setBackground(new java.awt.Color(245, 246, 250));
        fCelular.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCelular.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabelUsuario22.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario22.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario22.setText("E-mail");

        fEmail.setBackground(new java.awt.Color(245, 246, 250));
        fEmail.setAlignmentX(0.0F);
        fEmail.setAlignmentY(0.0F);
        fEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEmail.setPreferredSize(new java.awt.Dimension(50, 20));

        jLabelUsuario24.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario24.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario24.setText("Certificado Digital");
        jLabelUsuario24.setAlignmentY(0.0F);

        jLabelUsuario25.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario25.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario25.setText("Certificado");

        fCertificado.setEditable(false);
        fCertificado.setBackground(new java.awt.Color(245, 246, 250));
        fCertificado.setAlignmentX(0.0F);
        fCertificado.setAlignmentY(0.0F);
        fCertificado.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fCertificado.setPreferredSize(new java.awt.Dimension(50, 20));

        btCarregarCertificado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btCarregarCertificado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/Folder.png"))); // NOI18N
        btCarregarCertificado.setToolTipText("Carregar Imagem");
        btCarregarCertificado.setAlignmentY(0.0F);
        btCarregarCertificado.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btCarregarCertificado.setOpaque(false);
        btCarregarCertificado.setPreferredSize(new java.awt.Dimension(210, 40));

        jLabelUsuario26.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario26.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario26.setText("Senha");

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Salvar Registro");
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

        fId.setEditable(false);

        fComplemento.setBackground(new java.awt.Color(245, 246, 250));
        fComplemento.setAlignmentX(0.0F);
        fComplemento.setAlignmentY(0.0F);
        fComplemento.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fComplemento.setMaximumSize(new java.awt.Dimension(409, 20));
        fComplemento.setMinimumSize(new java.awt.Dimension(409, 20));
        fComplemento.setPreferredSize(new java.awt.Dimension(409, 20));

        fNumero.setBackground(new java.awt.Color(245, 246, 250));
        fNumero.setAlignmentX(0.0F);
        fNumero.setAlignmentY(0.0F);
        fNumero.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNumero.setPreferredSize(new java.awt.Dimension(50, 20));

        labelBairro2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelBairro2.setForeground(new java.awt.Color(153, 153, 153));
        labelBairro2.setText("Complemento");

        labelUf1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelUf1.setForeground(new java.awt.Color(153, 153, 153));
        labelUf1.setText("Número");

        fSenha.setBackground(new java.awt.Color(245, 246, 250));
        fSenha.setAlignmentX(0.0F);
        fSenha.setAlignmentY(0.0F);
        fSenha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fSenha.setMinimumSize(null);
        fSenha.setPreferredSize(new java.awt.Dimension(100, 20));

        fIbge.setEditable(false);

        javax.swing.GroupLayout jPanelCadastroEmpresaLayout = new javax.swing.GroupLayout(jPanelCadastroEmpresa);
        jPanelCadastroEmpresa.setLayout(jPanelCadastroEmpresaLayout);
        jPanelCadastroEmpresaLayout.setHorizontalGroup(
            jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fCrt, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelUsuario1)
                                    .addComponent(labelSexo))
                                .addGap(56, 56, 56)
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelNome2)
                                    .addComponent(fInscricaoEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, 124, Short.MAX_VALUE)))
                            .addComponent(jLabelUsuario24)
                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario25)
                                    .addComponent(fCertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btCarregarCertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                        .addComponent(fProdutorRural, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(115, 115, 115)
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(labelNome)
                                            .addComponent(fRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(fCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(labelcpj))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabelUsuario3)
                                                    .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addComponent(jLabelUsuario20, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelNome1)
                                    .addComponent(labelNome3)
                                    .addComponent(fNomeFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                        .addComponent(fInscricaoMunicipal, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                        .addGap(93, 93, 93))))
                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelUsuario9))
                                        .addGap(51, 51, 51)
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabelUsuario10))
                                        .addGap(47, 47, 47)
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(fSenha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelUsuario22)
                                            .addComponent(fEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                                            .addComponent(jLabelUsuario26))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroEmpresaLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario21)
                                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                        .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fIbge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(258, 258, 258)
                                        .addComponent(jLabelTituloCadastro))
                                    .addComponent(EnderecoCliente)
                                    .addComponent(jLabelUsuario23)
                                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                                    .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(fCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelCidade))
                                                    .addGap(34, 34, 34)
                                                    .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelBairro)
                                                        .addComponent(fBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroEmpresaLayout.createSequentialGroup()
                                                    .addComponent(fCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(34, 34, 34)
                                                    .addComponent(fLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                                .addComponent(labelCep)
                                                .addGap(127, 127, 127)
                                                .addComponent(labelEnde)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                                    .addComponent(labelBairro1)
                                                    .addGap(0, 0, Short.MAX_VALUE))
                                                .addComponent(fMunicipio, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                                .addComponent(fComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                            .addComponent(labelBairro2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(fUf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelUf)
                                            .addComponent(fNumero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelUf1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(93, 93, 93)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fLogomarca)
                            .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(panelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btSrcImg, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(25, 25, 25))))
        );
        jPanelCadastroEmpresaLayout.setVerticalGroup(
            jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloCadastro)
                    .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fIbge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(fLogomarca)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario21)
                        .addGap(27, 27, 27)
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuario1)
                            .addComponent(labelcpj)
                            .addComponent(jLabelUsuario3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario20)
                                    .addComponent(labelNome)
                                    .addComponent(labelNome1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fNomeFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(fProdutorRural, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addComponent(panelImagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btSrcImg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelSexo)
                            .addComponent(labelNome3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fCrt, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fInscricaoEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fInscricaoMunicipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(labelNome2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(EnderecoCliente)
                .addGap(12, 12, 12)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCep)
                    .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelEnde)
                        .addComponent(labelBairro2)
                        .addComponent(labelUf1)))
                .addGap(12, 12, 12)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelBairro)
                    .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelUf)
                        .addComponent(labelCidade)
                        .addComponent(labelBairro1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario23)
                        .addGap(22, 22, 22)
                        .addComponent(jLabelUsuario9)
                        .addGap(4, 4, 4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuario10)
                            .addComponent(jLabelUsuario22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario24)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelUsuario25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fCertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCadastroEmpresaLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addComponent(btCarregarCertificado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelCadastroEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 77;
        gridBagConstraints.ipady = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 16, 16, 23);
        panelCadEmpresa.add(jPanelCadastroEmpresa, gridBagConstraints);

        jTabbedPaneCadastroEmpresa.addTab("tab1", panelCadEmpresa);

        panelPesqEmpresa.setBackground(new java.awt.Color(245, 246, 250));
        panelPesqEmpresa.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(245, 246, 250));

        jPanelListagemEmpresa.setPreferredSize(new java.awt.Dimension(750, 350));

        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableListagem);

        javax.swing.GroupLayout jPanelListagemEmpresaLayout = new javax.swing.GroupLayout(jPanelListagemEmpresa);
        jPanelListagemEmpresa.setLayout(jPanelListagemEmpresaLayout);
        jPanelListagemEmpresaLayout.setHorizontalGroup(
            jPanelListagemEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
        );
        jPanelListagemEmpresaLayout.setVerticalGroup(
            jPanelListagemEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );

        btPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisa.setToolTipText("Pesquisar");

        jLabelUsuario19.setBackground(new java.awt.Color(153, 153, 255));
        jLabelUsuario19.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelUsuario19.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUsuario19.setText("Pesquisa de Empresa");
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

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(342, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btEditar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btExcluir)))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(357, 357, 357))))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jPanelListagemEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                    .addGap(13, 13, 13)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabelUsuario19, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(btPesquisa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 497, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btExcluir)
                    .addComponent(btEditar))
                .addGap(125, 125, 125))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(189, 189, 189)
                    .addComponent(jPanelListagemEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    .addGap(190, 190, 190)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 336;
        gridBagConstraints.ipady = 449;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 25);
        panelPesqEmpresa.add(jPanel2, gridBagConstraints);

        jTabbedPaneCadastroEmpresa.addTab("tab2", panelPesqEmpresa);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneCadastroEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("view_empresa");
        getAccessibleContext().setAccessibleDescription("view_empresa");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EnderecoCliente;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btCarregarCertificado;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btPesquisa;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSrcImg;
    private javax.swing.JTextField fBairro;
    private javax.swing.JFormattedTextField fCelular;
    private javax.swing.JFormattedTextField fCep;
    private javax.swing.JTextField fCertificado;
    private javax.swing.JTextField fCidade;
    private javax.swing.JFormattedTextField fCnpj;
    private javax.swing.JTextField fCodigo;
    private javax.swing.JTextField fComplemento;
    private javax.swing.JFormattedTextField fCpf;
    private javax.swing.JComboBox<String> fCrt;
    private javax.swing.JTextField fEmail;
    private javax.swing.JTextField fIbge;
    private javax.swing.JTextField fId;
    private javax.swing.JTextField fInscricaoEstadual;
    private javax.swing.JTextField fInscricaoMunicipal;
    private javax.swing.JLabel fLogomarca;
    private javax.swing.JTextField fLogradouro;
    private javax.swing.JTextField fMunicipio;
    private javax.swing.JTextField fNomeFantasia;
    private javax.swing.JTextField fNumero;
    private javax.swing.JCheckBox fProdutorRural;
    private javax.swing.JTextField fRazaoSocial;
    private javax.swing.JPasswordField fSenha;
    private javax.swing.JFormattedTextField fTelefone;
    private javax.swing.JTextField fUf;
    private javax.swing.JLabel jLabelTituloCadastro;
    private javax.swing.JLabel jLabelUsuario1;
    private javax.swing.JLabel jLabelUsuario10;
    private javax.swing.JLabel jLabelUsuario19;
    private javax.swing.JLabel jLabelUsuario20;
    private javax.swing.JLabel jLabelUsuario21;
    private javax.swing.JLabel jLabelUsuario22;
    private javax.swing.JLabel jLabelUsuario23;
    private javax.swing.JLabel jLabelUsuario24;
    private javax.swing.JLabel jLabelUsuario25;
    private javax.swing.JLabel jLabelUsuario26;
    private javax.swing.JLabel jLabelUsuario3;
    private javax.swing.JLabel jLabelUsuario9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelCadastroEmpresa;
    private static javax.swing.JPanel jPanelListagemEmpresa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneCadastroEmpresa;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JLabel labelBairro;
    private javax.swing.JLabel labelBairro1;
    private javax.swing.JLabel labelBairro2;
    private javax.swing.JLabel labelCep;
    private javax.swing.JLabel labelCidade;
    private javax.swing.JLabel labelEnde;
    private javax.swing.JLabel labelNome;
    private javax.swing.JLabel labelNome1;
    private javax.swing.JLabel labelNome2;
    private javax.swing.JLabel labelNome3;
    private javax.swing.JLabel labelSexo;
    private javax.swing.JLabel labelUf;
    private javax.swing.JLabel labelUf1;
    private javax.swing.JLabel labelcpj;
    private javax.swing.JPanel panelCadEmpresa;
    private javax.swing.JPanel panelImagem;
    private javax.swing.JPanel panelPesqEmpresa;
    private javax.swing.JLabel srcImgLogo;
    // End of variables declaration//GEN-END:variables


}
