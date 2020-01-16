/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.comunicacao.Erro;
import infra.comunicacao.Sucess;
import infra.comunicacao.Warning;
import infra.utilitarios.Utils;
import infra.visualizacao.NProgramaCellRenderer;
import infra.visualizacao.NProgramaComboBox;
import infra.visualizacao.NUsuarioCellRenderer;
import infra.visualizacao.NUsuarioComboBox;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import sistemavendas.controle.CPrograma;
import sistemavendas.controle.CUsuario;
import sistemavendas.negocio.NOperacao;
import sistemavendas.negocio.NPrograma;
import sistemavendas.negocio.NUsuario;

/**
 *
 * @author rhuan
 */
public class ViewControleAcesso extends JInternalFrame {

    /**
     * Creates new form ViewControleAcesso
     */
    public ViewControleAcesso() {
        initComponents();
        initActions();
        carregaComboBoxUsuario();
        carregaComboBoxPrograma();
        removeTopBar();
        desabilitarTudo();
        fIdPrograma.setVisible(false);
        fIdUsuario.setVisible(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    
    private void removeTopBar() {
        //remove a barra superior da window
        Utils.removeBarTopWindow(this);
        ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    }
    
    public void carregaComboBoxPrograma(String nome, int idUsuario) {
        
        CPrograma controller = new CPrograma();
        List<NPrograma> items = controller.pesquisar(nome, idUsuario);
        
        if (!items.isEmpty()) {
            NProgramaComboBox cbmodel = new NProgramaComboBox(items);
            jComboBoxPrograma.setModel(cbmodel);
            NPrograma c = items.get(items.size()-1);
            this.fNomePrograma.setText(c.getNome());
            jComboBoxPrograma.setRenderer(new NProgramaCellRenderer());
            cbmodel.setSelectedLastElement();
        } else {
            NProgramaComboBox cbmodel = new NProgramaComboBox(controller.getList());
            cbmodel.setSelectedLastElement();
            jComboBoxPrograma.setModel(cbmodel);
            jComboBoxPrograma.setRenderer(new NProgramaCellRenderer());
        }
    }
    
    public void carregaComboBoxPrograma() {
        
        CPrograma controller = new CPrograma();        
        List<NPrograma> items = controller.getList();
        NProgramaComboBox cbmodel = new NProgramaComboBox(items);

        if (! fNomePrograma.getText().isEmpty()) {
            try {
                NPrograma c = (NPrograma) cbmodel.getSelectedItem();
                this.fNomePrograma.setText(c.getNome());
                this.fIdPrograma.setText(c.getId().toString());

            } catch (NumberFormatException | NullPointerException e) {
                this.fNomePrograma.setText("");
                this.fIdPrograma.setText("");
            }
        } else {
            cbmodel.setSelectedLastElement();
        }
        jComboBoxPrograma.setModel(cbmodel);
        jComboBoxPrograma.setRenderer(new NProgramaCellRenderer());
        
    }
    
    private NPrograma selectedPrograma() {
        NProgramaComboBox modelSelected = (NProgramaComboBox) jComboBoxPrograma.getModel();
        NPrograma programa = (NPrograma) modelSelected.getSelectedItem();
        NUsuario usuario = selectedUsuario();
        if (usuario != null) {
            CPrograma controller = new CPrograma();
            programa = controller.pesquisar(programa.getId(), usuario.getId());
            System.out.println("selectedPrograma: " + programa.toString());
        }
        return programa;
    }
    
   
    
    public void carregaComboBoxUsuario(String nome) {
        
        CUsuario controller = new CUsuario();
        List<NUsuario> items = new ArrayList();
        try {
            NUsuario usuarioPesquisa = new NUsuario();
            usuarioPesquisa.setNome(nome);            
            items = controller.pesquisar(usuarioPesquisa);
        } catch (Erro ex) {
            Logger.getLogger(ViewControleAcesso.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!items.isEmpty()) {
            NUsuarioComboBox cbmodel = new NUsuarioComboBox(items);
            jComboBoxUsuario.setModel(cbmodel);
            NUsuario c = items.get(items.size()-1);
            this.fNomeUsuario.setText(c.getNome());
            jComboBoxUsuario.setRenderer(new NUsuarioCellRenderer());            
            cbmodel.setSelectedLastElement();
        } else {
            try {
                NUsuarioComboBox cbmodel = new NUsuarioComboBox(controller.getLista());
                cbmodel.setSelectedLastElement();
                jComboBoxUsuario.setModel(cbmodel);
                jComboBoxUsuario.setRenderer(new NUsuarioCellRenderer());
            } catch (Erro ex) {
                Logger.getLogger(ViewControleAcesso.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void carregaComboBoxUsuario() {
        
        CUsuario controller = new CUsuario();
        List<NUsuario> items = new ArrayList();
        try {
            items = controller.getLista();
        } catch (Erro ex) {
            ex.show();
        }

        NUsuarioComboBox cbmodel = new NUsuarioComboBox(items);
        if (! fNomeUsuario.getText().isEmpty()) {
            try {
                NUsuario c = (NUsuario) cbmodel.getSelectedItem();
                this.fNomeUsuario.setText(c.getNome());
                this.fIdUsuario.setText(c.getId().toString());


            } catch (NumberFormatException | NullPointerException e) {
                this.fNomeUsuario.setText("");
                this.fIdUsuario.setText("");
            }
        } else {
            cbmodel.setSelectedLastElement();
        }
        jComboBoxUsuario.setModel(cbmodel);
        jComboBoxUsuario.setRenderer(new NUsuarioCellRenderer());
    }
    
    private NUsuario selectedUsuario() {
        NUsuarioComboBox modelSelected = (NUsuarioComboBox) jComboBoxUsuario.getModel();
        return (NUsuario) modelSelected.getSelectedItem();
    }
    
    
    
    
    private void carregarOperacaoParaTela(NPrograma p, NUsuario u) {
        
        CPrograma controller = new CPrograma();
        p = controller.pesquisar(p.getId(), u.getId());
        
        csGerarRelatorio.setSelected(false);
        csAutorizarTudo.setSelected(false);
        csCadastro.setSelected(false);
        csEditar.setSelected(false);
        csExcluir.setSelected(false);
        csPesquisar.setSelected(false);
        csVisualizacao.setSelected(false);
        
        
        if (p != null) {
            List<NOperacao> oprs = p.getOperacoes();
            
            // relatorio
            if (oprs != null && oprs.size() == 1 && oprs.get(0).getId() == 5) {
                csGerarRelatorio.setEnabled(true);
                csGerarRelatorio.setSelected(true);
                csAutorizarTudo.setEnabled(false);
                csCadastro.setEnabled(false);
                csEditar.setEnabled(false);
                csExcluir.setEnabled(false);
                csPesquisar.setEnabled(false);
                csVisualizacao.setEnabled(false);
                return;
            }
            
            // dashboard
            if (oprs != null && oprs.size() == 1 && oprs.get(0).getId() == 6) {
                desabilitarTudo();
                csVisualizacao.setEnabled(true);
                return;
            }
            
            if (oprs != null) {                
                for (NOperacao opr : oprs) {
                    if (opr.getId() == 1) {
                        csCadastro.setEnabled(true);
                        csCadastro.setSelected(true);
                    }
                    if (opr.getId() == 2) {
                        csEditar.setEnabled(true);
                        csEditar.setSelected(true);
                    }
                    if (opr.getId() == 3) {
                        csExcluir.setEnabled(true);
                        csExcluir.setSelected(true);
                    }
                    if (opr.getId() == 4) {
                        csPesquisar.setEnabled(true);
                        csPesquisar.setSelected(true);
                    }
                }
            }            
            csAutorizarTudo.setEnabled(true);
            csGerarRelatorio.setEnabled(false);
            csGerarRelatorio.setSelected(false);
            csVisualizacao.setSelected(false);
            csVisualizacao.setEnabled(false);
        }
    }
    
    
    
    protected void initActions() {
        
        btSalvar.addActionListener((ActionEvent e) -> {
            onClickSalvar();
        });
        
        btCancelar.addActionListener((ActionEvent e) -> {
            onClickCancelar();
        });
        

        
        
        jComboBoxPrograma.addActionListener((ActionEvent e) -> {
            NPrograma programaSelecionado = selectedPrograma();
            if (programaSelecionado != null) {
                fNomePrograma.setText(programaSelecionado.getNome());                
            }
        });
        
        btPesquisaPrograma.addActionListener((ActionEvent e) -> {
            NPrograma programaSelecionado = selectedPrograma();
            NUsuario usuarioSelecionado = selectedUsuario();
            if (programaSelecionado != null) {
                carregaComboBoxPrograma(fNomePrograma.getText(), usuarioSelecionado.getId());                                
            }
        });
        
        
        
        
        jComboBoxPrograma.addActionListener((ActionEvent e) -> {
            NPrograma programaSelecionado = selectedPrograma();
            NUsuario usuarioSelecionado = selectedUsuario();
            if (programaSelecionado != null && usuarioSelecionado != null) {
                fNomePrograma.setText(programaSelecionado.getNome());
                carregarOperacaoParaTela(programaSelecionado, usuarioSelecionado);
            }
        });
        
        btPesquisaPrograma.addActionListener((ActionEvent e) -> {
            NPrograma programaSelecionado = selectedPrograma();
            NUsuario usuarioSelecionado = selectedUsuario();
            if (programaSelecionado != null && usuarioSelecionado != null) {
                carregaComboBoxPrograma(fNomePrograma.getText(), usuarioSelecionado.getId());                
                carregarOperacaoParaTela(programaSelecionado, usuarioSelecionado);
            }
        });
        
        
        btPesquisaUsuario.addActionListener((ActionEvent e) -> {
            carregaComboBoxUsuario(fNomeUsuario.getText());
        });
        
        
        
        
        csAutorizarTudo.addActionListener((ActionEvent e) -> {
            habilitarTudo();
            if (csAutorizarTudo.isSelected() && csAutorizarTudo.isEnabled()) {
                csGerarRelatorio.setSelected(false);
                csGerarRelatorio.setEnabled(false);
                csVisualizacao.setSelected(false);
                csVisualizacao.setEnabled(false);
                csCadastro.setSelected(true);
                csEditar.setSelected(true);
                csExcluir.setSelected(true);
                csPesquisar.setSelected(true);
            } else {
                csGerarRelatorio.setSelected(false);
                csGerarRelatorio.setEnabled(true);
                csVisualizacao.setSelected(false);
                csVisualizacao.setEnabled(true);
                csCadastro.setSelected(false);
                csEditar.setSelected(false);
                csExcluir.setSelected(false);
                csPesquisar.setSelected(false);
            }
        });
    }
    
    private void desabilitarTudo() {
        csGerarRelatorio.setSelected(false);
        csGerarRelatorio.setEnabled(false);
        csVisualizacao.setSelected(false);
        csVisualizacao.setEnabled(false);
        csCadastro.setEnabled(false);
        csCadastro.setSelected(false);
        csEditar.setEnabled(false);
        csEditar.setSelected(false);
        csExcluir.setEnabled(false);
        csExcluir.setSelected(false);
        csPesquisar.setEnabled(false);
        csPesquisar.setSelected(false);
    }
    
    
    private void habilitarTudo() {
        csGerarRelatorio.setEnabled(true);
        csVisualizacao.setEnabled(true);
        csCadastro.setEnabled(true);
        csEditar.setEnabled(true);
        csExcluir.setEnabled(true);
        csPesquisar.setEnabled(true);
    }
    
    
    private NPrograma recuperarProgramaEOperacoes(NPrograma programaSelecionado, List<NOperacao> operacoesSelecionadas) {
        
        // se na tela não tiver nenhuma operação selecionada, então retira as operações do programa
        if (operacoesSelecionadas.isEmpty()) {
            programaSelecionado.setOperacoes(new ArrayList());
            return programaSelecionado;
        }
        
        // se no objeto programa não tiver nenhuma operação selecionada mas na tela sim, então busca no banco as operações
        if (programaSelecionado.getOperacoes().isEmpty() && !operacoesSelecionadas.isEmpty()) {
            List<NOperacao> operacoesBanco = new ArrayList();
            CPrograma controller = new CPrograma();            
            for (NOperacao operacoesSelecionada : operacoesSelecionadas) {
                NOperacao o = controller.getOperacaoPorNome(operacoesSelecionada.getDescricao());
                if (o != null) {
                    operacoesBanco.add(o);
                }
            }
            programaSelecionado.setOperacoes(operacoesBanco);
            return programaSelecionado;
        }
        
        // Se no objeto programa tiver operações e o objeto operacoes selecionadas tbm, faz o vimdiff
        List<NOperacao> novasOprs = new ArrayList();
        for (NOperacao operacaoPrograma : programaSelecionado.getOperacoes()) { //contem o id da operação
            for (NOperacao operacoesSelecionada : operacoesSelecionadas) { // não contem o id da operação pois está vindo da tela
                if (operacaoPrograma.getDescricao().equalsIgnoreCase(operacoesSelecionada.getDescricao())) {
                    novasOprs.add(operacaoPrograma);
                }
            }
        }
        programaSelecionado.setOperacoes(novasOprs);
        return programaSelecionado;
    }
    
    
    private void onClickSalvar() {
        NPrograma programa = selectedPrograma();
        NUsuario usuario = selectedUsuario();
        
        if (programa == null || usuario == null) {
            new Warning("Selecione um usuário e programa para poder dar continuidade ao cadastro!", "Erro").show();
        }
        programa = recuperarProgramaEOperacoes(programa, getOperacoesTela());        

        CPrograma controller = new CPrograma();
        if (controller.salvar(programa)) {
            new Sucess("Alteração salva com sucesso", "").show();
        }        
    }
    
    
    private void onClickCancelar() {
        limparCampos();        
    }
    
    
    private void limparCampos() {
        this.fNomePrograma.setText("");
        this.fNomeUsuario.setText("");
        this.csAutorizarTudo.setSelected(false);
        this.csCadastro.setSelected(false);
        this.csEditar.setSelected(false);
        this.csExcluir.setSelected(false);
        this.csGerarRelatorio.setSelected(false);
        this.csPesquisar.setSelected(false);
    }
    
    
    private List<NOperacao> getOperacoesTela() {
        List<NOperacao> operacoes = new ArrayList();
        if (csAutorizarTudo.isSelected()) {
            operacoes.add(new NOperacao(NOperacao.ALTERAR));
            operacoes.add(new NOperacao(NOperacao.EXCLUIR));
            operacoes.add(new NOperacao(NOperacao.INCLUIR));
            operacoes.add(new NOperacao(NOperacao.PESQUISAR));
        } else {
            if (csCadastro.isSelected()) {
                operacoes.add(new NOperacao(NOperacao.INCLUIR));
            }
            if (csEditar.isSelected()) {
                operacoes.add(new NOperacao(NOperacao.ALTERAR));
            }
            if (csExcluir.isSelected()) {
                operacoes.add(new NOperacao(NOperacao.EXCLUIR));
            }
            if (csPesquisar.isSelected()) {
                operacoes.add(new NOperacao(NOperacao.PESQUISAR));
            }
            if (csGerarRelatorio.isSelected()) {
                operacoes.add(new NOperacao(NOperacao.GERAR_RELATORIO));
            }
        }
        System.out.println("getOperacoesTela: " + operacoes);
                
        return operacoes;
    }
    
    
    private String getTipoPrograma() {
        if (csGerarRelatorio.isEnabled() || csGerarRelatorio.isSelected())
            return "Relatório";
        return "Cadastro";
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

        jPanel16 = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelSubtitulo = new javax.swing.JLabel();
        jPanelPrograma = new javax.swing.JPanel();
        fNomePrograma = new javax.swing.JTextField();
        jComboBoxPrograma = new javax.swing.JComboBox();
        label31 = new javax.swing.JLabel();
        fIdPrograma = new javax.swing.JTextField();
        btPesquisaPrograma = new javax.swing.JButton();
        csCadastro = new javax.swing.JCheckBox();
        csEditar = new javax.swing.JCheckBox();
        csExcluir = new javax.swing.JCheckBox();
        csPesquisar = new javax.swing.JCheckBox();
        jLabelUsuario31 = new javax.swing.JLabel();
        csGerarRelatorio = new javax.swing.JCheckBox();
        csAutorizarTudo = new javax.swing.JCheckBox();
        label32 = new javax.swing.JLabel();
        fNomeUsuario = new javax.swing.JTextField();
        btPesquisaUsuario = new javax.swing.JButton();
        jComboBoxUsuario = new javax.swing.JComboBox();
        fIdUsuario = new javax.swing.JTextField();
        csVisualizacao = new javax.swing.JCheckBox();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel16.setBackground(new java.awt.Color(245, 246, 250));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jLabelTitulo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitulo.setText("Controle de Acesso");
        jLabelTitulo.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(49, 108, 0, 0);
        jPanel16.add(jLabelTitulo, gridBagConstraints);

        jLabelSubtitulo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelSubtitulo.setForeground(new java.awt.Color(51, 51, 51));
        jLabelSubtitulo.setText("Programas autorizados");
        jLabelSubtitulo.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 20, 0, 0);
        jPanel16.add(jLabelSubtitulo, gridBagConstraints);

        jPanelPrograma.setBackground(new java.awt.Color(245, 246, 250));
        jPanelPrograma.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Programas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanelPrograma.setAlignmentX(0.0F);
        jPanelPrograma.setAlignmentY(0.0F);

        fNomePrograma.setBackground(new java.awt.Color(245, 246, 250));
        fNomePrograma.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNomePrograma.setPreferredSize(new java.awt.Dimension(100, 20));

        label31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label31.setForeground(new java.awt.Color(153, 153, 153));
        label31.setText("Programa");

        fIdPrograma.setEditable(false);
        fIdPrograma.setFocusable(false);

        btPesquisaPrograma.setBackground(new java.awt.Color(245, 246, 250));
        btPesquisaPrograma.setForeground(new java.awt.Color(245, 246, 250));
        btPesquisaPrograma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Find.png"))); // NOI18N
        btPesquisaPrograma.setToolTipText("Pesquisa Programa");
        btPesquisaPrograma.setMinimumSize(new java.awt.Dimension(25, 22));
        btPesquisaPrograma.setPreferredSize(new java.awt.Dimension(25, 22));

        csCadastro.setText("Cadastrar");

        csEditar.setText("Editar");

        csExcluir.setText("Excluir");

        csPesquisar.setText("Pesquisar");

        jLabelUsuario31.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelUsuario31.setForeground(new java.awt.Color(51, 51, 51));
        jLabelUsuario31.setText("Operações");
        jLabelUsuario31.setAlignmentY(0.0F);

        csGerarRelatorio.setText("Gerar Relatório");

        csAutorizarTudo.setText("Autorizar Todas as Operações");

        label32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label32.setForeground(new java.awt.Color(153, 153, 153));
        label32.setText("Usuário");

        fNomeUsuario.setBackground(new java.awt.Color(245, 246, 250));
        fNomeUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fNomeUsuario.setPreferredSize(new java.awt.Dimension(100, 20));

        btPesquisaUsuario.setBackground(new java.awt.Color(245, 246, 250));
        btPesquisaUsuario.setForeground(new java.awt.Color(245, 246, 250));
        btPesquisaUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/18x18/Find.png"))); // NOI18N
        btPesquisaUsuario.setToolTipText("Pesquisa Cliente");
        btPesquisaUsuario.setMinimumSize(new java.awt.Dimension(25, 22));
        btPesquisaUsuario.setPreferredSize(new java.awt.Dimension(25, 22));

        fIdUsuario.setEditable(false);
        fIdUsuario.setFocusable(false);

        csVisualizacao.setText("Visualizar");

        javax.swing.GroupLayout jPanelProgramaLayout = new javax.swing.GroupLayout(jPanelPrograma);
        jPanelPrograma.setLayout(jPanelProgramaLayout);
        jPanelProgramaLayout.setHorizontalGroup(
            jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProgramaLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelProgramaLayout.createSequentialGroup()
                        .addComponent(jLabelUsuario31, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(csAutorizarTudo))
                    .addGroup(jPanelProgramaLayout.createSequentialGroup()
                        .addComponent(csCadastro)
                        .addGap(10, 10, 10)
                        .addComponent(csEditar)
                        .addGap(18, 18, 18)
                        .addComponent(csExcluir)
                        .addGap(18, 18, 18)
                        .addComponent(csPesquisar)
                        .addGap(18, 18, 18)
                        .addComponent(csGerarRelatorio)
                        .addGap(18, 18, 18)
                        .addComponent(csVisualizacao))
                    .addGroup(jPanelProgramaLayout.createSequentialGroup()
                        .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelProgramaLayout.createSequentialGroup()
                                .addComponent(fNomeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btPesquisaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelProgramaLayout.createSequentialGroup()
                                .addComponent(fNomePrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btPesquisaPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fIdPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelProgramaLayout.setVerticalGroup(
            jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProgramaLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(label32)
                .addGap(4, 4, 4)
                .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProgramaLayout.createSequentialGroup()
                        .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fNomeUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btPesquisaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addComponent(label31)
                        .addGap(4, 4, 4)
                        .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fNomePrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btPesquisaPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelProgramaLayout.createSequentialGroup()
                        .addComponent(jComboBoxUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jComboBoxPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelProgramaLayout.createSequentialGroup()
                        .addComponent(fIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGap(2, 2, 2)
                        .addComponent(fIdPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelUsuario31)
                    .addComponent(csAutorizarTudo))
                .addGap(30, 30, 30)
                .addGroup(jPanelProgramaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(csCadastro)
                    .addComponent(csEditar)
                    .addComponent(csExcluir)
                    .addComponent(csPesquisar)
                    .addComponent(csGerarRelatorio)
                    .addComponent(csVisualizacao)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 20, 0, 51);
        jPanel16.add(jPanelPrograma, gridBagConstraints);

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 57, 79, 0);
        jPanel16.add(btCancelar, gridBagConstraints);

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 79, 51);
        jPanel16.add(btSalvar, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btPesquisaPrograma;
    private javax.swing.JButton btPesquisaUsuario;
    private javax.swing.JButton btSalvar;
    private javax.swing.JCheckBox csAutorizarTudo;
    private javax.swing.JCheckBox csCadastro;
    private javax.swing.JCheckBox csEditar;
    private javax.swing.JCheckBox csExcluir;
    private javax.swing.JCheckBox csGerarRelatorio;
    private javax.swing.JCheckBox csPesquisar;
    private javax.swing.JCheckBox csVisualizacao;
    private javax.swing.JTextField fIdPrograma;
    private javax.swing.JTextField fIdUsuario;
    private javax.swing.JTextField fNomePrograma;
    private javax.swing.JTextField fNomeUsuario;
    private javax.swing.JComboBox jComboBoxPrograma;
    private javax.swing.JComboBox jComboBoxUsuario;
    private javax.swing.JLabel jLabelSubtitulo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUsuario31;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanelPrograma;
    private javax.swing.JLabel label31;
    private javax.swing.JLabel label32;
    // End of variables declaration//GEN-END:variables
}
