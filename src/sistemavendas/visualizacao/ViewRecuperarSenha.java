/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import infra.comunicacao.Erro;
import infra.comunicacao.Sucess;
import infra.comunicacao.Warning;
import java.awt.event.ActionEvent;
import sistemavendas.controle.CUsuario;
import sistemavendas.negocio.NUsuario;

/**
 *
 * @author rhuan
 */
public class ViewRecuperarSenha extends javax.swing.JFrame {

    /**
     * Creates new form ViewRecuperarSenha
     */
    public ViewRecuperarSenha() {
        initComponents();
        initActions();
        fEmail.setText("rhuanpablo13@hotmail.com");
        fCpf.setText("025.335.721-79");
    }

    
    private void initActions() {
        
        btRecuperar.addActionListener((ActionEvent e) -> {
            recuperarSenha();
        });
    }
    
    
    private void recuperarSenha() {
        NUsuario usuario = new NUsuario();
        if (validarEntradaDados()) {
            CUsuario controller = new CUsuario();
            try {
                usuario = controller.recuperaPorEmail(this.fEmail.getText());
                if (usuario == null) {
                    new Warning("Nenhum usuário com o email: " + this.fEmail.getText() + " foi encontrado!", "").show();
                }
                
                if (controller.recuperarSenha(usuario)) {
                    new Sucess("Sua senha foi enviada para o e-mail: " + this.fEmail.getText(), "").show();
                } else {
                    new Erro("Ocorreu um erro ao resuperar sua senha, verifique se os dados informados estão corretos e tente novamente!", "").show();
                }
                
            } catch (Erro ex) {
                ex.show();
            }
        }
    }
    
    
    
    private boolean validarEntradaDados() {
        if (this.fEmail == null || this.fEmail.getText().isEmpty()) {
            new Warning("Campo E-mail é obrigatório", "").show();
            return false;
        }
        
        if (this.fCpf == null || this.fCpf.getText().isEmpty()) {
            new Warning("Campo CPF é obrigatório", "").show();
            return false;
        }        
        return true;
    }
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fEmail = new javax.swing.JTextField();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelUsuario1 = new javax.swing.JLabel();
        jLabelSejaBemVindo = new javax.swing.JLabel();
        btCancelar = new javax.swing.JButton();
        btRecuperar = new javax.swing.JButton();
        fCpf = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        fEmail.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        fEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fEmail.setMinimumSize(new java.awt.Dimension(0, 15));

        jLabelUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 11)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario.setText("E-mail do usuário");

        jLabelUsuario1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 11)); // NOI18N
        jLabelUsuario1.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario1.setText("CPF");

        jLabelSejaBemVindo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 18)); // NOI18N
        jLabelSejaBemVindo.setText("Recuperar senha");

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/cancel.png"))); // NOI18N
        btCancelar.setToolTipText("Cancelar Operação");
        btCancelar.setAlignmentY(0.0F);
        btCancelar.setMaximumSize(new java.awt.Dimension(80, 39));
        btCancelar.setMinimumSize(new java.awt.Dimension(80, 39));
        btCancelar.setOpaque(false);
        btCancelar.setPreferredSize(new java.awt.Dimension(80, 39));

        btRecuperar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/35x35/recovery-refresh.png"))); // NOI18N
        btRecuperar.setToolTipText("Salvar Registro");
        btRecuperar.setAlignmentY(0.0F);
        btRecuperar.setMaximumSize(new java.awt.Dimension(80, 39));
        btRecuperar.setMinimumSize(new java.awt.Dimension(80, 39));
        btRecuperar.setOpaque(false);
        btRecuperar.setPreferredSize(new java.awt.Dimension(80, 39));

        fCpf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        try {
            fCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fCpf.setAlignmentX(0.0F);
        fCpf.setAlignmentY(0.0F);
        fCpf.setPreferredSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario1)
                                    .addComponent(jLabelUsuario)
                                    .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(136, 136, 136)
                                .addComponent(jLabelSejaBemVindo)))
                        .addGap(0, 112, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btRecuperar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabelSejaBemVindo)
                .addGap(43, 43, 43)
                .addComponent(jLabelUsuario)
                .addGap(4, 4, 4)
                .addComponent(fEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelUsuario1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btRecuperar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ViewRecuperarSenha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ViewRecuperarSenha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ViewRecuperarSenha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ViewRecuperarSenha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ViewRecuperarSenha().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btRecuperar;
    private javax.swing.JFormattedTextField fCpf;
    private javax.swing.JTextField fEmail;
    private javax.swing.JLabel jLabelSejaBemVindo;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JLabel jLabelUsuario1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
