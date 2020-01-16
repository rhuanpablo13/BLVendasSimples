/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.cadastrosrapidos;

import infra.comunicacao.Warning;
import infra.validadores.ViewValidator;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import sistemavendas.controle.CMedida;
import sistemavendas.negocio.NMedida;

/**
 *
 * @author rhuan
 */
public class CRLote extends CadastroRapido {

    private String operacao = "cadastrar";
    
    public CRLote() {
        initComponents();
        initActions();
    }
    
    public void init() {
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // id
        jTableListagem.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableListagem.getColumnModel().getColumn(0).setMaxWidth(50);
        // codigo
        jTableListagem.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTableListagem.getColumnModel().getColumn(1).setMaxWidth(60);
        
        // data cadastro
        jTableListagem.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTableListagem.getColumnModel().getColumn(2).setMaxWidth(60);
        
        // nome
        jTableListagem.getColumnModel().getColumn(3).setPreferredWidth(140);
    }
    
    
    @Override
    protected void initMasks() {
        validator = new ViewValidator();
        validator.required(fCodigo);
        validator.required(fDescricao);
    }

    

    @Override
    protected void initActions() {
        btSalvar.addActionListener((ActionEvent e) -> {
            cadastrar();
        });
        
        btExcluir.addActionListener((ActionEvent e) -> {
            excluir();
        });
        
        btPesquisar.addActionListener((ActionEvent e) -> {
            pesquisar();
        });
        
        btEditar.addActionListener((ActionEvent e) -> {
            editar();
        });
    }

    @Override
    protected void cadastrar() {
        
        NMedida medida = new NMedida();
        if (operacao.equals("editar")) {
            medida.setId(Integer.parseInt(fId.getText()));
        }
        
        try {
            medida.setCodigo(Integer.parseInt(fCodigo.getText()));
            medida.setDescricao(fDescricao.getText());
        } catch (NumberFormatException e) {
            new Warning("Os campos código e descrição precisam ser preenchidos!", "Atenção").show();
        }

        if (operacao.equals("editar")) {
            try {
                CMedida controller = new CMedida();
                controller.alterar(medida);
            } catch (Exception e) {
                showException(e);
            }
            
        } else {
            try {
                CMedida controller = new CMedida();
                controller.inserir(medida);
            } catch (Exception e) {
                showException(e);
            }
        }
        
        try {
            CMedida controller = new CMedida();
            List<NMedida> lista = controller.getLista();
            refreshTableList(lista);
        } catch (Exception e) {
            showException(e);
        }
        
        fId.setText("");
        fCodigo.setText("");
        fDescricao.setText("");
        titulo.setText("Cadastro Medida");
        btEditar.setEnabled(true);
        btExcluir.setEnabled(true);
        btPesquisar.setEnabled(true);
        operacao = "cadastrar";
    }    
    
    
    protected void pesquisar() {
        
        operacao = "pesquisar";
        NMedida medida = new NMedida();
        try {
            if (! fCodigo.getText().isEmpty()) {
                int codigo = Integer.parseInt(fCodigo.getText());
                medida.setCodigo(codigo);
            }
            
            if (! fDescricao.getText().isEmpty()) {
                String descricao = fDescricao.getText();
                medida.setDescricao(descricao);                
            }
            
            CMedida controller = new CMedida();
            List<NMedida> lista = controller.pesquisar(medida);
            refreshTableList(lista);
        } catch (Exception e) {
            showException(e);
        }
    }
    
    protected void refreshTableList(List<NMedida> dataList) {
        try {
            DefaultTableModel modelo = (DefaultTableModel) jTableListagem.getModel();
            modelo.setNumRows(0);
            
            dataList.stream().forEach((nMedida) -> {
                modelo.addRow(new Object[] {
                    nMedida.getId(),
                    nMedida.getCodigo(),
                    nMedida.getDescricao()
                });
            });
        } catch (Exception e) {}
    }
    
    protected void excluir() {
        int row = jTableListagem.getSelectedRow();
        if (row > -1) {
            
            int codigo = (Integer) jTableListagem.getValueAt(row, 1);
            try {
                CMedida controller = new CMedida();
                controller.excluir(codigo);
                List<NMedida> lista = controller.getLista();
                refreshTableList(lista);
            } catch (Exception e) {
                showException(e);
            }
        }
    }
    
    
    protected void editar() {
        
        operacao = "editar";
        int row = jTableListagem.getSelectedRow();
        if (row > -1) {
            
            int codigo = (Integer) jTableListagem.getValueAt(row, 1);
            try {
                CMedida c = new CMedida();
                NMedida medida = c.buscarPorCodigo(codigo);
                fId.setText(medida.getId().toString());
                fCodigo.setText(medida.getCodigo().toString());
                fDescricao.setText(medida.getDescricao());
                titulo.setText("Editar Medida");
                btEditar.setEnabled(false);
                btExcluir.setEnabled(false);
                btPesquisar.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
                showException(e);
            }
        }
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        jLabelDescricao = new javax.swing.JLabel();
        fCodigo = new javax.swing.JTextField();
        jLabelUsuario2 = new javax.swing.JLabel();
        fDescricao = new javax.swing.JTextField();
        btSalvar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListagem = new javax.swing.JTable();
        btExcluir = new javax.swing.JButton();
        btPesquisar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        fId = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 246, 250));

        titulo.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 16)); // NOI18N
        titulo.setForeground(new java.awt.Color(51, 51, 51));
        titulo.setText("Cadastro de Medida");
        titulo.setAlignmentY(0.0F);

        jLabelDescricao.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelDescricao.setForeground(new java.awt.Color(153, 153, 153));
        jLabelDescricao.setText("Código");

        fCodigo.setBackground(new java.awt.Color(245, 246, 250));
        fCodigo.setAlignmentX(0.0F);
        fCodigo.setAlignmentY(0.0F);
        fCodigo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));

        jLabelUsuario2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario2.setForeground(new java.awt.Color(153, 153, 153));
        jLabelUsuario2.setText("Descrição");

        fDescricao.setBackground(new java.awt.Color(245, 246, 250));
        fDescricao.setAlignmentX(0.0F);
        fDescricao.setAlignmentY(0.0F);
        fDescricao.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(12, 91, 160)));
        fDescricao.setMinimumSize(new java.awt.Dimension(400, 20));
        fDescricao.setName(""); // NOI18N
        fDescricao.setPreferredSize(new java.awt.Dimension(400, 20));

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/salvar2.png"))); // NOI18N
        btSalvar.setToolTipText("Salvar Registro");
        btSalvar.setAlignmentY(0.0F);
        btSalvar.setMaximumSize(new java.awt.Dimension(80, 39));
        btSalvar.setMinimumSize(new java.awt.Dimension(80, 39));
        btSalvar.setOpaque(false);
        btSalvar.setPreferredSize(new java.awt.Dimension(80, 39));

        jTableListagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Código", "Data Cadastro", "Descrição"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableListagem);

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/deletar.png"))); // NOI18N
        btExcluir.setToolTipText("Excluir Registro");
        btExcluir.setAlignmentY(0.0F);
        btExcluir.setMaximumSize(new java.awt.Dimension(80, 39));
        btExcluir.setMinimumSize(new java.awt.Dimension(80, 39));
        btExcluir.setOpaque(false);
        btExcluir.setPreferredSize(new java.awt.Dimension(80, 39));

        btPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/buscar.png"))); // NOI18N
        btPesquisar.setToolTipText("Pesquisar");
        btPesquisar.setAlignmentY(0.0F);
        btPesquisar.setMaximumSize(new java.awt.Dimension(80, 39));
        btPesquisar.setMinimumSize(new java.awt.Dimension(80, 39));
        btPesquisar.setOpaque(false);
        btPesquisar.setPreferredSize(new java.awt.Dimension(80, 39));

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/30x30/editar.png"))); // NOI18N
        btEditar.setToolTipText("Editar Registro");
        btEditar.setAlignmentY(0.0F);
        btEditar.setMaximumSize(new java.awt.Dimension(80, 39));
        btEditar.setMinimumSize(new java.awt.Dimension(80, 39));
        btEditar.setOpaque(false);
        btEditar.setPreferredSize(new java.awt.Dimension(80, 39));

        fId.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(252, Short.MAX_VALUE)
                .addComponent(titulo)
                .addGap(234, 234, 234))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(36, 36, 36)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelUsuario2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(titulo))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(fId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDescricao)
                            .addComponent(jLabelUsuario2))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btPesquisar;
    private javax.swing.JButton btSalvar;
    private javax.swing.JTextField fCodigo;
    private javax.swing.JTextField fDescricao;
    private javax.swing.JTextField fId;
    private javax.swing.JLabel jLabelDescricao;
    private javax.swing.JLabel jLabelUsuario2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableListagem;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables


}
