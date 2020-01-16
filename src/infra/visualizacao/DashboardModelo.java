/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.visualizacao;

import infra.comunicacao.Erro;
import infra.utilitarios.Utils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import sistemavendas.controle.CCliente;
import sistemavendas.controle.CProduto;
import sistemavendas.controle.CVendas;
import sistemavendas.dashboard.PanelCliente;
import sistemavendas.dashboard.PanelProduto;
import sistemavendas.dashboard.PanelVendas;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NProduto;
import sistemavendas.negocio.NVendas;

/**
 *
 * @author rhuan
 */
public class DashboardModelo extends javax.swing.JInternalFrame {

    /**
     * Creates new form DashboardModelo
     */
    public DashboardModelo() {
        initComponents();
        removeTopBar();
    }

    
    private void removeTopBar() {
        //remove a barra superior da window
        Utils.removeBarTopWindow(this);
        ((javax.swing.plaf.basic.BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    }
    
    public void carregarDashboard() {        
        jLabelultimaVenda.setText(ultimaVendaRegistrada().getText());
        jLabelProdEstoque.setText(getTotalProdutosEstoque().getText());
        jLabelTotalEstoque.setText(getTotalValorVendaEmEstoque().getText());
        
        jLabelClientesAtivos.setText(getTotalClientesAtivos().getText());
        jlabelClientesInativos.setText(getTotalClientesInativos().getText());
        jlabelClientesTotal.setText(getTotalClientes().getText());
        jLabelNomeClienteMaisFreq.setText(getClienteCompraMaisFrequencia().getText());
        
        jLabelNomeClienteMaisVendas.setText(getClienteCompraMaisFrequencia().getText());
        jLabelNomeClienteMenosVendas.setText(getClienteCompraMenosFrequencia().getText());
        
        jLabelClienteMaiorVenda.setText(getClienteCompraMaiorValor().getText());
        jLabelMaiorVenda.setText(getCompraDeMaiorValor().getText());
        
        jLabelTotalProdutos.setText(getTotalProdutosCadastrados().getText());
        jLabelProdutosMaisVendido.setText(getProdutoMaisVendido().getText());
        jLabelProdutosMenosVendido.setText(getProdutoMenosVendido().getText());
        jLabelProdutosAtivos.setText(getTotalProdutosAtivos().getText());
        jLabelProdutosInativos.setText(getTotalProdutosInativos().getText());

        jLabelEstoque.setText(getTotalProdutosEstoque().getText());
        jLabelTotalProdutosValor.setText(getTotalValorVendaEmEstoque().getText());
        jLabelConcluirCadastro.setText(getTotalCadastroRapido().getText());
    }
    
    
    
    
    
    private JLabel getTotalProdutosEstoque() {
        CProduto controllerProduto = new CProduto();
        Integer cls = controllerProduto.totalProdutosEmEstoque();
        if (cls != null) {
            return new JLabel(cls+"");
        }
        return new JLabel(0+""); 
    }
    
    
    private JLabel getTotalProdutosCadastrados() {
        CProduto controllerProduto = new CProduto();
        Integer cls;
        try {
            cls = controllerProduto.getLista().size();
            return new JLabel(cls+"");
        } catch (Erro ex) {
            Logger.getLogger(DashboardModelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel(0+""); 
    }
    
    
    private JLabel getTotalValorVendaEmEstoque() {
        CProduto controllerProduto = new CProduto();
        Double cls = controllerProduto.totalValorVendaEmEstoque();
        if (cls != null) {
            return new JLabel(doubleToView(cls));
        }
        return new JLabel("R$ 0,00");
    }

    
    private JLabel getProdutoMaisVendido() {
        CProduto controllerProduto = new CProduto();
        NProduto cls = controllerProduto.maisVendido();
        if (!cls.isEmpty()) {
            return new JLabel("" + cls.getCodigo() + "-" + cls.getNome());
        }
        return new JLabel("Nenhum");
    }
    
    
    private JLabel getProdutoMenosVendido() {
        CProduto controllerProduto = new CProduto();
        NProduto cls = controllerProduto.menosVendido();
        if (!cls.isEmpty()) {
            return new JLabel("" + cls.getCodigo() + "-" + cls.getNome());
        }
        return new JLabel("Nenhum");
    }
    
    
    private JLabel getTotalProdutosAtivos() {
        CProduto controllerProduto = new CProduto();
        NProduto c1 = new NProduto();
        c1.setAtivo(true);
        try {
            List<NProduto> cls = controllerProduto.pesquisar(c1);
            if (cls != null) {
                return new JLabel("" + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("" + 0); 
    }
    
    private JLabel getTotalProdutosInativos() {
        CProduto controllerProduto = new CProduto();
        NProduto c1 = new NProduto();
        c1.setAtivo(false);
        try {
            List<NProduto> cls = controllerProduto.pesquisar(c1);
            if (cls != null) {
                return new JLabel("" + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("" + 0); 
    }
    
    private JLabel getTotalCadastroRapido() { 
        CProduto controllerProduto = new CProduto();
        Integer cls = controllerProduto.totalProdutosCadastroRapido();
        if (cls != null) {
            return new JLabel("" + cls);
        }
        return new JLabel("" + 0); 
    }
    
    
    public JLabel getClienteCompraMaisFrequencia() {
        CVendas controller = new CVendas();
        NCliente cliente = controller.getClienteCompraMaisFrequencia();
        if (!cliente.isEmpty()) {
            return new JLabel(cliente.getCodigo() + " - " + cliente.getNome());
        }
        return new JLabel("Nenhum");
    }
    
    
    public JLabel getClienteCompraMaiorValor() {
        CVendas controller = new CVendas();
        NCliente cliente = controller.getClienteCompraMaiorValor();
        if (!cliente.isEmpty()) {
            return new JLabel(cliente.getCodigo() + " - " + cliente.getNome());
        }
        return new JLabel("Nenhum");
    }
    
    public JLabel getCompraDeMaiorValor() { 
        CVendas controller = new CVendas();
        Double v = controller.getCompraDeMaiorValor();
        return new JLabel(doubleToView(v));        
    }
    
    
    public JLabel getClienteCompraMenosFrequencia() {
        CVendas controller = new CVendas();
        NCliente cliente = controller.getClienteCompraMenosFrequencia();
        if (!cliente.isEmpty()) {
            return new JLabel(cliente.getCodigo() + " - " + cliente.getNome());
        }
        return new JLabel("Nenhum");
    }
    
    
    public JLabel ultimaVendaRegistrada() {
        CVendas controller = new CVendas();
        try {
            NVendas venda = controller.recuperaUltimoRegistro();
            if (!venda.isEmpty()) {
                return new JLabel(doubleToView(venda.getValorTotal()));
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelVendas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("Nenhuma");
    }
    
    
    
    
    private JLabel getTotalClientes() {
        CCliente controllerCliente = new CCliente();
        
        // clientes
        Integer totalClientes = controllerCliente.count();
        if (totalClientes != null) {
            return new JLabel("" + totalClientes);
        } else {
            return new JLabel("" + 0);
        }
    }
    
    
    
    private JLabel getTotalClientesAtivos() {
        CCliente controllerCliente = new CCliente();
        NCliente c1 = new NCliente();
        c1.setAtivo(true);
        try {
            List<NCliente> cls = controllerCliente.pesquisar(c1);
            if (cls != null) {
                return new JLabel("" + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("" + 0); 
    }
    
    
    private JLabel getTotalClientesInativos() {
        CCliente controllerCliente = new CCliente();
        NCliente c1 = new NCliente();
        c1.setAtivo(false);
        try {
            List<NCliente> cls = controllerCliente.pesquisar(c1);
            if (cls != null) {
                return new JLabel("" + cls.size());
            }
        } catch (Erro ex) {
            Logger.getLogger(PanelCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JLabel("" + 0); 
    }    
    
    
    protected String doubleToView(Double doubleValue) {
        if (doubleValue == null) return null;
        return converterDoubleString(doubleValue);
    }
    

    
    protected static String converterDoubleString(double precoDouble) {
        Locale ptBr = new Locale("pt", "BR");
        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(precoDouble);
        return valorString;
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

        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelultimaVenda = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelProdEstoque = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelTotalEstoque = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabelClientesAtivos = new javax.swing.JLabel();
        jlabelClientesInativos = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabelNomeClienteMaisFreq = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jlabelClientesTotal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabelNomeClienteMaisVendas = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabelNomeClienteMenosVendas = new javax.swing.JLabel();
        jLabelMaiorVenda = new javax.swing.JLabel();
        jLabelClienteMaiorVenda = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabelTotalProdutos = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabelProdutosInativos = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabelProdutosAtivos = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabelConcluirCadastro = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabelTotalProdutosValor = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabelEstoque = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabelProdutosMaisVendido = new javax.swing.JLabel();
        jLabelProdutosMenosVendido = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(240, 248, 253));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1050, 700));
        setMinimumSize(new java.awt.Dimension(1050, 700));
        setPreferredSize(new java.awt.Dimension(1050, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(240, 248, 253));
        jPanel1.setMaximumSize(new java.awt.Dimension(1050, 700));
        jPanel1.setMinimumSize(new java.awt.Dimension(1050, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(1050, 700));

        jPanel7.setBackground(new java.awt.Color(46, 204, 113));
        jPanel7.setToolTipText("Vendas");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Última venda realizada");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/45x45/Untitled-2.png"))); // NOI18N

        jLabelultimaVenda.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabelultimaVenda.setForeground(new java.awt.Color(255, 255, 255));
        jLabelultimaVenda.setText("R$ 3.899, 00");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelultimaVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabelultimaVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(41, 128, 185));
        jPanel2.setToolTipText("Estoque");
        jPanel2.setPreferredSize(new java.awt.Dimension(270, 140));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/45x45/warehouse.png"))); // NOI18N

        jLabelProdEstoque.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabelProdEstoque.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProdEstoque.setText("1256 ");

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Produtos em estoque");

        jLabelTotalEstoque.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelTotalEstoque.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotalEstoque.setText("R$ 3.899, 00");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Total em estoque");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelProdEstoque)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTotalEstoque)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabelProdEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jLabelTotalEstoque)
                .addGap(5, 5, 5)
                .addComponent(jLabel8)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(59, 101, 143));
        jPanel3.setToolTipText("Clientes");
        jPanel3.setPreferredSize(new java.awt.Dimension(270, 140));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/45x45/cliente.png"))); // NOI18N

        jLabelClientesAtivos.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabelClientesAtivos.setForeground(new java.awt.Color(153, 255, 153));
        jLabelClientesAtivos.setText("63");

        jlabelClientesInativos.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jlabelClientesInativos.setForeground(new java.awt.Color(255, 102, 102));
        jlabelClientesInativos.setText("12");

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Ativos");

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Inativos");

        jLabelNomeClienteMaisFreq.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelNomeClienteMaisFreq.setForeground(new java.awt.Color(51, 204, 255));
        jLabelNomeClienteMaisFreq.setText("02 - Maria da Silva Ribeiro");

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Cliente mais frequente");

        jLabel24.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Total");

        jlabelClientesTotal.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jlabelClientesTotal.setForeground(new java.awt.Color(255, 255, 255));
        jlabelClientesTotal.setText("12");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelClientesAtivos)
                            .addComponent(jLabel12))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jlabelClientesInativos))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlabelClientesTotal)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabelNomeClienteMaisFreq))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(73, 73, 73))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelClientesAtivos, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlabelClientesInativos, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlabelClientesTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addComponent(jLabelNomeClienteMaisFreq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Vendas");

        jLabel5.setText("Cliente com mais compras registradas");

        jLabelNomeClienteMaisVendas.setFont(new java.awt.Font("Dialog", 1, 17)); // NOI18N
        jLabelNomeClienteMaisVendas.setForeground(new java.awt.Color(51, 102, 0));
        jLabelNomeClienteMaisVendas.setText("02 - Maria da Silva Ribeiro");

        jLabel7.setText("Cliente com menos compras registradas");

        jLabelNomeClienteMenosVendas.setFont(new java.awt.Font("Dialog", 1, 17)); // NOI18N
        jLabelNomeClienteMenosVendas.setForeground(new java.awt.Color(204, 51, 0));
        jLabelNomeClienteMenosVendas.setText("15 - João Pedro Rodrigues");

        jLabelMaiorVenda.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabelMaiorVenda.setForeground(new java.awt.Color(0, 204, 0));
        jLabelMaiorVenda.setText("R$ 15.240, 00");

        jLabelClienteMaiorVenda.setForeground(new java.awt.Color(102, 102, 102));
        jLabelClienteMaiorVenda.setText("02 - Maria da Silva Ribeiro");

        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Maior venda até o momento");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNomeClienteMenosVendas)
                            .addComponent(jLabel7)
                            .addComponent(jLabelNomeClienteMaisVendas)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelMaiorVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelClienteMaiorVenda)
                            .addComponent(jLabel21))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeClienteMaisVendas)
                    .addComponent(jLabelMaiorVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelClienteMaiorVenda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelNomeClienteMenosVendas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel11.setBackground(new java.awt.Color(51, 51, 51));
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Produtos");

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 204, 204));
        jLabel14.setText("Total");

        jLabelTotalProdutos.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabelTotalProdutos.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTotalProdutos.setText("1256");

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(204, 204, 204));
        jLabel16.setText("Inativos");

        jLabelProdutosInativos.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelProdutosInativos.setForeground(new java.awt.Color(204, 51, 0));
        jLabelProdutosInativos.setText("123");

        jLabel17.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(204, 204, 204));
        jLabel17.setText("Ativos");

        jLabelProdutosAtivos.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelProdutosAtivos.setForeground(new java.awt.Color(51, 204, 0));
        jLabelProdutosAtivos.setText("1156");

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(204, 204, 204));
        jLabel20.setText("Concluir cadastro");

        jLabelConcluirCadastro.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelConcluirCadastro.setForeground(new java.awt.Color(255, 204, 0));
        jLabelConcluirCadastro.setText("23");

        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(204, 204, 204));
        jLabel19.setText("Total em Estoque");

        jLabelTotalProdutosValor.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelTotalProdutosValor.setForeground(new java.awt.Color(0, 153, 255));
        jLabelTotalProdutosValor.setText("R$ 25.896,66");

        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(204, 204, 204));
        jLabel18.setText("Estoque");

        jLabelEstoque.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        jLabelEstoque.setForeground(new java.awt.Color(51, 51, 51));
        jLabelEstoque.setText("1156");

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(204, 204, 204));
        jLabel22.setText("Mais vendido");

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(204, 204, 204));
        jLabel23.setText("Menos vendido");

        jLabelProdutosMaisVendido.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jLabelProdutosMaisVendido.setForeground(new java.awt.Color(0, 153, 255));
        jLabelProdutosMaisVendido.setText("08 - Camiseta Amarela Tamanho");

        jLabelProdutosMenosVendido.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jLabelProdutosMenosVendido.setForeground(new java.awt.Color(0, 153, 255));
        jLabelProdutosMenosVendido.setText("08 - Camiseta Amarela Tamanho");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabelTotalProdutos)
                            .addComponent(jLabelTotalProdutosValor)
                            .addComponent(jLabel19)
                            .addComponent(jLabel11))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel17)
                                                    .addComponent(jLabelProdutosAtivos))
                                                .addGap(77, 77, 77)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel16)
                                                    .addComponent(jLabelProdutosInativos))
                                                .addGap(62, 62, 62)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabelEstoque)
                                                    .addComponent(jLabel18)))
                                            .addComponent(jLabel22)))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelConcluirCadastro)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(29, 29, 29))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelProdutosMaisVendido, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelProdutosMenosVendido, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23))
                                .addGap(16, 16, 16))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(4, 4, 4)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTotalProdutos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelProdutosAtivos)
                            .addComponent(jLabelProdutosInativos)
                            .addComponent(jLabelEstoque))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelProdutosMaisVendido)
                            .addComponent(jLabelProdutosMenosVendido))
                        .addGap(24, 24, 24)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTotalProdutosValor)
                    .addComponent(jLabelConcluirCadastro)
                    .addComponent(jLabel20))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 293, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
//            java.util.logging.Logger.getLogger(DashboardModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DashboardModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DashboardModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DashboardModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new DashboardModelo().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelClienteMaiorVenda;
    private javax.swing.JLabel jLabelClientesAtivos;
    private javax.swing.JLabel jLabelConcluirCadastro;
    private javax.swing.JLabel jLabelEstoque;
    private javax.swing.JLabel jLabelMaiorVenda;
    private javax.swing.JLabel jLabelNomeClienteMaisFreq;
    private javax.swing.JLabel jLabelNomeClienteMaisVendas;
    private javax.swing.JLabel jLabelNomeClienteMenosVendas;
    private javax.swing.JLabel jLabelProdEstoque;
    private javax.swing.JLabel jLabelProdutosAtivos;
    private javax.swing.JLabel jLabelProdutosInativos;
    private javax.swing.JLabel jLabelProdutosMaisVendido;
    private javax.swing.JLabel jLabelProdutosMenosVendido;
    private javax.swing.JLabel jLabelTotalEstoque;
    private javax.swing.JLabel jLabelTotalProdutos;
    private javax.swing.JLabel jLabelTotalProdutosValor;
    private javax.swing.JLabel jLabelultimaVenda;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel jlabelClientesInativos;
    private javax.swing.JLabel jlabelClientesTotal;
    // End of variables declaration//GEN-END:variables
}
