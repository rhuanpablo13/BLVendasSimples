/**
 * @author  BLSoft
 * www.Blsoft.com.br
 * Venda de software e código fonte
*/
package sistemavendas.visualizacao;

import infra.comunicacao.Erro;
import infra.comunicacao.Warning;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import sistemavendas.controle.CUsuario;

import sistemavendas.negocio.NUsuario;

public class ViewLogin extends javax.swing.JFrame {

    public ViewLogin() {
        initComponents();
        setLocationRelativeTo(null);  
        initLogin();
    }

    
    private void initLogin() {
        this.fUsuario.setText("rhuan");
        this.fSenha.setText("123");
        
        fEsqueciSenha.setText("Esqueci minha senha");
        fEsqueciSenha.setBorderPainted(false);
        fEsqueciSenha.setOpaque(false);
        fEsqueciSenha.setBackground(Color.WHITE);
        fEsqueciSenha.setToolTipText("");
        
        fEsqueciSenha.addActionListener((ActionEvent e) -> {
            ViewRecuperarSenha telaRecuperarSenha = new ViewRecuperarSenha();
            telaRecuperarSenha.setVisible(true);
        });
        
        jPanel2BtnLogar.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                autenticar();
            }
            
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) {
                setColor(jPanel2BtnLogar, Constantes.bkg_btn_login_over);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) {
                resetColor(jPanel2BtnLogar, Constantes.bkg_btn_login_stby);
            }
            @Override public void mousePressed(java.awt.event.MouseEvent evt) {                
                setColor(jPanel2BtnLogar, Constantes.bkg_btn_login_click);
        
                //melhorar essa transição de cores
                try {
                    Thread.sleep(130);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ViewLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                setColor(jPanel2BtnLogar, Constantes.bkg_btn_login_stby);
            }
        });
        
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelAzul = new javax.swing.JPanel();
        jLabel5UserIcon = new javax.swing.JLabel();
        jLabelEntrar = new javax.swing.JLabel();
        jPanelBranco = new javax.swing.JPanel();
        fSenha = new javax.swing.JPasswordField();
        fUsuario = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelSenha = new javax.swing.JLabel();
        jPanel2BtnLogar = new javax.swing.JPanel();
        jLabelLogin = new javax.swing.JLabel();
        jLabelSejaBemVindo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fEsqueciSenha = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(java.awt.Color.white);
        setMinimumSize(new java.awt.Dimension(400, 700));

        jPanelAzul.setBackground(new java.awt.Color(72, 126, 176));
        jPanelAzul.setForeground(new java.awt.Color(255, 255, 255));
        jPanelAzul.setToolTipText("");
        jPanelAzul.setMaximumSize(new java.awt.Dimension(240, 250));
        jPanelAzul.setMinimumSize(new java.awt.Dimension(240, 250));
        jPanelAzul.setPreferredSize(new java.awt.Dimension(240, 250));
        jPanelAzul.setLayout(new java.awt.GridBagLayout());

        jLabel5UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/512X512/login.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(18, 120, 88, 120);
        jPanelAzul.add(jLabel5UserIcon, gridBagConstraints);

        jLabelEntrar.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 36)); // NOI18N
        jLabelEntrar.setForeground(new java.awt.Color(255, 255, 255));
        jLabelEntrar.setText("Entrar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.insets = new java.awt.Insets(80, 120, 0, 120);
        jPanelAzul.add(jLabelEntrar, gridBagConstraints);

        jPanelBranco.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBranco.setMaximumSize(new java.awt.Dimension(240, 450));
        jPanelBranco.setMinimumSize(new java.awt.Dimension(240, 450));
        jPanelBranco.setPreferredSize(new java.awt.Dimension(240, 450));
        jPanelBranco.setRequestFocusEnabled(false);
        jPanelBranco.setLayout(new java.awt.GridBagLayout());

        fSenha.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        fSenha.setForeground(new java.awt.Color(102, 102, 102));
        fSenha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 252;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 9, 0, 56);
        jPanelBranco.add(fSenha, gridBagConstraints);

        fUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        fUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fUsuario.setMinimumSize(new java.awt.Dimension(0, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 252;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 9, 0, 56);
        jPanelBranco.add(fUsuario, gridBagConstraints);

        jLabelUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 11)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario.setText("Usuário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(42, 9, 0, 0);
        jPanelBranco.add(jLabelUsuario, gridBagConstraints);

        jLabelSenha.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 11)); // NOI18N
        jLabelSenha.setForeground(new java.awt.Color(153, 153, 153));
        jLabelSenha.setText("Senha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 9, 0, 0);
        jPanelBranco.add(jLabelSenha, gridBagConstraints);

        jPanel2BtnLogar.setBackground(new java.awt.Color(72, 126, 176));
        jPanel2BtnLogar.setMinimumSize(new java.awt.Dimension(250, 50));
        jPanel2BtnLogar.setPreferredSize(new java.awt.Dimension(250, 50));

        jLabelLogin.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelLogin.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLogin.setText("Login");
        jLabelLogin.setMaximumSize(new java.awt.Dimension(59, 40));
        jLabelLogin.setMinimumSize(new java.awt.Dimension(59, 40));
        jLabelLogin.setPreferredSize(new java.awt.Dimension(59, 40));

        javax.swing.GroupLayout jPanel2BtnLogarLayout = new javax.swing.GroupLayout(jPanel2BtnLogar);
        jPanel2BtnLogar.setLayout(jPanel2BtnLogarLayout);
        jPanel2BtnLogarLayout.setHorizontalGroup(
            jPanel2BtnLogarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2BtnLogarLayout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99))
        );
        jPanel2BtnLogarLayout.setVerticalGroup(
            jPanel2BtnLogarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2BtnLogarLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(67, 11, 0, 56);
        jPanelBranco.add(jPanel2BtnLogar, gridBagConstraints);

        jLabelSejaBemVindo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelSejaBemVindo.setText("Seja bem vindo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 27, 0, 0);
        jPanelBranco.add(jLabelSejaBemVindo, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        jLabel1.setText("Versão 1.0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(77, 10, 11, 0);
        jPanelBranco.add(jLabel1, gridBagConstraints);

        fEsqueciSenha.setBackground(new java.awt.Color(255, 255, 255));
        fEsqueciSenha.setForeground(new java.awt.Color(102, 102, 102));
        fEsqueciSenha.setText("Esqueci minha senha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 19, 0, 0);
        jPanelBranco.add(fEsqueciSenha, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelAzul, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBranco, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelAzul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelBranco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

                                      
    
    
    private void autenticar() {

        NUsuario usuario = new NUsuario();
        CUsuario controllerUsuario = new CUsuario();
        usuario.setUsuario(this.fUsuario.getText());
        usuario.setSenha(new String(this.fSenha.getPassword()));
        
        try {
            
            if (controllerUsuario.primeiroAcesso()) {
                // Abrir janela de redefinição de senha
                ViewRedefinirSenha redefinirSenha = new ViewRedefinirSenha();
                redefinirSenha.setVisible(true);
            } else {
                
                if (isAdminAdmin(usuario)) {
                    new Warning("Usuário (admin/admin) não permitido!", "Aviso").show();
                    return;
                }
                
                if (controllerUsuario.autorizado(usuario)) {                    
                    usuario = controllerUsuario.recuperarPorUsuario(usuario.getUsuario());                    
                    ViewPrincipal viewPrincipal = new ViewPrincipal(usuario);
                    viewPrincipal.setVisible(true);
                    this.dispose();
                } else {
                    new Warning("Usuário não autorizado!", "Aviso").show();
                }
            }           
            
        } catch (Erro ex) {
            ex.show();
        }
    }
    

    private boolean isAdminAdmin(NUsuario usuario) {
        return usuario.getUsuario().equalsIgnoreCase("admin") && usuario.getSenha().equalsIgnoreCase("admin");
    }
    
    
    public void setColor(JPanel panel, Color color) {
        panel.setBackground(color);
    }    
    
    public void resetColor(JPanel panel, Color color){
        panel.setBackground(color);
    }    
    
    
    
    public static void main(String args[]) {         
            
        java.awt.EventQueue.invokeLater(new Runnable() {
                        
            public void run() {
                ViewLogin v = new ViewLogin();
                v.setVisible(true);
            }
        });
    }
        
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton fEsqueciSenha;
    private javax.swing.JPasswordField fSenha;
    private javax.swing.JTextField fUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5UserIcon;
    private javax.swing.JLabel jLabelEntrar;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelSejaBemVindo;
    private javax.swing.JLabel jLabelSenha;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JPanel jPanel2BtnLogar;
    private javax.swing.JPanel jPanelAzul;
    private javax.swing.JPanel jPanelBranco;
    // End of variables declaration//GEN-END:variables
}
