/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import infra.comunicacao.Sucess;
import infra.comunicacao.Warning;
import infra.operacoes.Operacao;
import infra.utilitarios.Utils;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import sistemavendas.controle.CEmpresa;
import sistemavendas.controle.CSistema;
import sistemavendas.controle.CUsuario;
import sistemavendas.negocio.NEmpresa;
import sistemavendas.negocio.NSistema;
import sistemavendas.negocio.NUsuario;

/**
 *
 * @author rhuan
 */
public class ViewConfiguracoesGeraisSistema extends JInternalFrame {

    
    private Operacao operacao = Operacao.ALTERAR;
    
    
    /**
     * Creates new form ViewControleAcesso
     */ 
    public ViewConfiguracoesGeraisSistema() {
        initComponents();
        initActions();
        removeTopBar();
        init();
        initCampoSenha();
        fId.setVisible(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    
    private void removeTopBar() {
        //remove a barra superior da window
        Utils.removeBarTopWindow(this);
        ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    }
    
    
    private void init() {
        
        List<String> emails = new ArrayList();
        
        try {
            CEmpresa controller = new CEmpresa();
            List<NEmpresa> empresas = controller.getLista();
            if (empresas != null && !empresas.isEmpty()) {
                empresas.stream().forEach((NEmpresa emp) -> {
                    if (!emp.getEmail().isEmpty()) {
                        emails.add(emp.getEmail());
                    }
                });                
            }
            
            CUsuario controllerUsuario = new CUsuario();
            List<NUsuario> lista = controllerUsuario.getLista();
            if (lista != null && !lista.isEmpty()) {
                lista.stream().forEach((NUsuario usa) -> {
                    if (usa.isAdministrador() && !usa.getEmail().isEmpty()) {
                        emails.add(usa.getEmail());
                    }
                });
            }
        } catch (Exception ex) { }
        
        
        CSistema controllerSistema = new CSistema();
        NSistema configVigente = controllerSistema.buscarConfiguracaoVigente();
        if (configVigente != null) {
            emails.add(configVigente.getEmail());
            try {
                String senhaDec = configVigente.desencriptarSenha();
                fSenha.setText(senhaDec);
                fId.setText(configVigente.getId().toString());
            } catch (Erro ex) {
                ex.show();
                return;
            }
            jComboBoxEmail.setSelectedItem(configVigente.getEmail());
        } else {
            operacao = Operacao.CADASTRO;
        }
        
        
        if (emails.isEmpty()) {
            new Warning("Nenhum e-mail cadastrado no sistema, por favor, cadastre um e-mail válido no cadastro de Empresa ou no Cadastro de Usuaário administrador", "").show();
        } else {
            emails.stream().forEach((String email) -> {
                jComboBoxEmail.addItem((String) email);
            });
        }
        
    }
    
    
    
    protected void initActions() {
        
        btSalvar.addActionListener((ActionEvent e) -> {
            onClickSalvar();
        });
        
        btCancelar.addActionListener((ActionEvent e) -> {
            onClickCancelar();
        });
    }
    
    private void onClickSalvar() {
        String email = (String) jComboBoxEmail.getSelectedItem();
        String senha = String.copyValueOf(fSenha.getPassword());
        
        if (senha.isEmpty()) {
            new Erro("Campo senha é obrigatório").show();
            return;
        }
        
        NSistema negocio = new NSistema();
        negocio.setEmail(email);
        negocio.setSenha(senha);
        try {
            negocio.encriptarSenha();
        } catch (Erro ex) {
            ex.show();
            return;
        }
        
        CSistema controller = new CSistema();
        if (operacao == Operacao.CADASTRO) {
            try {
                controller.inserir(negocio);
                new Sucess("Registro salvo com sucesso!", "").show();
                limparCampos();
            } catch (Exception ex) {
                new Erro("Não foi possível salvar esta configuração!" + StackDebug.getLineNumber(this.getClass()), "").show();
            }            
        }
        
        if (operacao == Operacao.ALTERAR) {
            negocio.setId(Integer.parseInt(fId.getText()));
            try {
                controller.alterar(negocio);
                new Sucess("Registro alterado com sucesso!", "").show();
                limparCampos();
            } catch (Exception ex) {
                new Erro("Não foi possível alterar esta configuração!" + StackDebug.getLineNumber(this.getClass()), "").show();
            }
        }
    }
    
    
    private void onClickCancelar() {
        ViewPrincipal.showDashBoard();
    }
    
    
    private void initCampoSenha() {
        Character echo = fSenha.getEchoChar();
        fMostraSenha.addActionListener((ActionEvent e ) -> {
            
            if (fMostraSenha.isSelected()) {
                fSenha.setEchoChar((char)0);
            } else {
                fSenha.setEchoChar(echo);
            }
            
        });
    }
    
    
    private void limparCampos() {
        this.fSenha.setText("");
        this.fId.setText("");
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
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        label32 = new javax.swing.JLabel();
        fSenha = new javax.swing.JPasswordField();
        fMostraSenha = new javax.swing.JCheckBox();
        jLabelUsuario1 = new javax.swing.JLabel();
        labelSenha = new javax.swing.JLabel();
        jComboBoxEmail = new javax.swing.JComboBox();
        fId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel16.setBackground(new java.awt.Color(245, 246, 250));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jLabelTitulo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitulo.setText("Configurações Gerais");
        jLabelTitulo.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(44, 32, 0, 0);
        jPanel16.add(jLabelTitulo, gridBagConstraints);

        jLabelSubtitulo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 14)); // NOI18N
        jLabelSubtitulo.setForeground(new java.awt.Color(51, 51, 51));
        jLabelSubtitulo.setText("Configuração de E-mail");
        jLabelSubtitulo.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 20, 0, 0);
        jPanel16.add(jLabelSubtitulo, gridBagConstraints);

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 36, 75, 0);
        jPanel16.add(btCancelar, gridBagConstraints);

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 10, 75, 19);
        jPanel16.add(btSalvar, gridBagConstraints);

        label32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label32.setForeground(new java.awt.Color(153, 153, 153));
        label32.setText("Email do sistema (Empresa)");
        label32.setToolTipText("E-mail que vai ser utilizado pelo sistema para operações como [Enviar email's de recuperação de senha]");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 20, 0, 0);
        jPanel16.add(label32, gridBagConstraints);

        fSenha.setBackground(new java.awt.Color(245, 246, 250));
        fSenha.setAlignmentX(0.0F);
        fSenha.setAlignmentY(0.0F);
        fSenha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fSenha.setMinimumSize(null);
        fSenha.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 185;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 0);
        jPanel16.add(fSenha, gridBagConstraints);

        fMostraSenha.setBackground(new java.awt.Color(245, 246, 250));
        fMostraSenha.setAlignmentY(0.0F);
        fMostraSenha.setMaximumSize(new java.awt.Dimension(25, 25));
        fMostraSenha.setMinimumSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 0);
        jPanel16.add(fMostraSenha, gridBagConstraints);

        jLabelUsuario1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario1.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario1.setText("Mostrar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 18, 0, 0);
        jPanel16.add(jLabelUsuario1, gridBagConstraints);

        labelSenha.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        labelSenha.setForeground(new java.awt.Color(153, 153, 153));
        labelSenha.setText("Senha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 20, 0, 0);
        jPanel16.add(labelSenha, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 157;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 20, 0, 0);
        jPanel16.add(jComboBoxEmail, gridBagConstraints);

        fId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(57, 20, 0, 0);
        jPanel16.add(fId, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btSalvar;
    private javax.swing.JTextField fId;
    private javax.swing.JCheckBox fMostraSenha;
    private javax.swing.JPasswordField fSenha;
    private javax.swing.JComboBox jComboBoxEmail;
    private javax.swing.JLabel jLabelSubtitulo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUsuario1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JLabel label32;
    private javax.swing.JLabel labelSenha;
    // End of variables declaration//GEN-END:variables
}
