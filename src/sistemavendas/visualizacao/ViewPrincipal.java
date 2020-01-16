/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sistemavendas.visualizacao;

import infra.visualizacao.DashboardModelo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import sistemavendas.negocio.NUsuario;
import sistemavendas.relatorios.cliente.RelCliente;



public class ViewPrincipal extends javax.swing.JFrame {

    //private JScrollPane scroll;
    private ViewClientes viewClientes;
    private ViewProduto viewProdutos;
    private ViewFornecedor viewFornecedor;
    private ViewUsuario viewUsuario;
    private ViewVenda viewVendas;
    private static DashboardModelo dashboard;
    private ViewControleAcesso controleAcesso;
    private static NUsuario usuarioLogado;
    
    
    
    
    public ViewPrincipal(NUsuario usuarioLogado) {
        initComponents();
        MyLookAndFeel look = new MyLookAndFeel(this);
        look.setWindowsApperance(true);
        this.setName("view_principal");
        ViewPrincipal.usuarioLogado = usuarioLogado;
        iniciarMenuLateral();
        iniciarMenuSuperior();
        maximizeView();
        this.labelNomeUsuario.setText(usuarioLogado.getNome());
    }

    
    
    public static void showDashBoard() {
        if (usuarioLogado.operacoesAutorizadas("Dashboard").size() > 0) {
            dashboard = new DashboardModelo();
            dashboard.carregarDashboard();
            gerenciaTelas(dashboard);            
        }
    }
    
    
    
    private void maximizeView() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        this.setSize((int) width, (int) height-40);
    }
    
    
    
    public static NUsuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    
    
    private void iniciarMenuLateral() {
        
        // Cliente
        menuCliente.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseExitedEvent(menuCliente, jLabelCliente, jPanelHLCliente);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseEnteredEvent(menuCliente, jLabelCliente, jPanelHLCliente);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciaTelas(new ViewClientes());
            }
            
        });
        
        
        // Fornecedor
        menuFornecedor.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseExitedEvent(menuFornecedor, jLabelFornecedor, jPanelHLFornecedor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseEnteredEvent(menuFornecedor, jLabelFornecedor, jPanelHLFornecedor);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciaTelas(new ViewFornecedor());
            }
        });
        

        // Produto
        menuProduto.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseExitedEvent(menuProduto, jLabelProduto, jPanelHLProduto);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseEnteredEvent(menuProduto, jLabelProduto, jPanelHLProduto);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciaTelas(new ViewProduto());
            }
        });


        // Usuario
        menuUsuario.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseExitedEvent(menuUsuario, jLabelUsuario, jPanelHLUsuario);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseEnteredEvent(menuUsuario, jLabelUsuario, jPanelHLUsuario);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciaTelas(new ViewUsuario());
            }
        });


        // Vendas
        menuRegVenda.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseExitedEvent(menuRegVenda, jLabelVenda, jPanelHLVenda);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseEnteredEvent(menuRegVenda, jLabelVenda, jPanelHLVenda);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                gerenciaTelas(new ViewVenda());
            }
        });



        // Relatório de Clientes
        menuRelCliente.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseExited(MouseEvent e) {
                setMouseExitedEvent(menuRelCliente, jLabelRelCliente, jPanelHLRelCliente);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setMouseEnteredEvent(menuRelCliente, jLabelRelCliente, jPanelHLRelCliente);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                RelCliente relCliente = new RelCliente();
                relCliente.listarTodos();
            }
        });


        // Relatório de Fornecedor
        
        

        //Relatório de Produto
        
        

        //Relatório de Vendas
        // Implementar...

    }

    
    private void iniciarMenuSuperior() {
        
        subClientes.addActionListener((ActionEvent e) -> {
            gerenciaTelas(new ViewClientes());
        });
        
        subFornecedor.addActionListener((ActionEvent e) -> {
            gerenciaTelas(new ViewFornecedor());
        });
        
        subProdutos.addActionListener((ActionEvent e) -> {
            gerenciaTelas(new ViewProduto());
        });
        
        subUsuario.addActionListener((ActionEvent e) -> {
            gerenciaTelas(new ViewUsuario());
        });
        
        subEmpresa.addActionListener((ActionEvent e) -> {
            gerenciaTelas(new ViewEmpresa());
        });
        
        JMenuItem subControleAcesso = new JCheckBoxMenuItem("Controle Acesso");
        subControleAcesso.addActionListener((ActionEvent e) -> {
            if (controleAcesso == null) {
                
                controleAcesso = new ViewControleAcesso();
                controleAcesso.setVisible(true);
                if (jDesktopPane.getAllFrames().length == 0) {
                    construct(controleAcesso);
                } else {
                    jDesktopPane.remove(0);
                    construct(controleAcesso);
                }
            }
        });
        JMenu menuControleAcesso = new JMenu("Configuração");
        menuControleAcesso.add(subControleAcesso);
        
        JMenuItem subConfigSistema = new JCheckBoxMenuItem("Configuração Sistema");
        subConfigSistema.addActionListener((ActionEvent e) -> {
            ViewConfiguracoesGeraisSistema configSistem = new ViewConfiguracoesGeraisSistema();
                configSistem.setVisible(true);
                if (jDesktopPane.getAllFrames().length == 0) {
                    construct(configSistem);
                } else {
                    jDesktopPane.remove(0);
                    construct(configSistem);
                }
        });
        menuControleAcesso.add(subConfigSistema);        
        jMenuBar.add(menuControleAcesso);
        
        
        JMenu subRelClientes = new JMenu("Clientes", true);
        menuRelatorios.add(subRelClientes);

        
        JMenuItem subRelClienteTodos = new JCheckBoxMenuItem("Todos");
        subRelClientes.add(subRelClienteTodos);
        subRelClienteTodos.addActionListener((ActionEvent e) -> {
            RelCliente relCliente = new RelCliente();
            relCliente.listarTodos();
        });
        
        
        JMenuItem subRelClienteIdadeRegiao = new JCheckBoxMenuItem("Por Idade e Região");
        subRelClientes.add(subRelClienteIdadeRegiao);
        subRelClienteIdadeRegiao.addActionListener((ActionEvent e) -> {
            RelCliente relCliente = new RelCliente();
            relCliente.listarClientesPorIdadeERegiao();
        });
        
        
        JMenuItem subRelClienteMaisRegistrosVendas = new JCheckBoxMenuItem("Mais Registros de Vendas");
        subRelClientes.add(subRelClienteMaisRegistrosVendas);
        subRelClienteMaisRegistrosVendas.addActionListener((ActionEvent e) -> {
            RelCliente relCliente = new RelCliente();
            relCliente.listarClientesMaisRegistradosEmVendas();
        });
        
        
        JMenuItem subRelClienteMaisLucrativos = new JCheckBoxMenuItem("Mais Lucrativos");
        subRelClientes.add(subRelClienteMaisLucrativos);
        subRelClienteMaisLucrativos.addActionListener((ActionEvent e) -> {
            RelCliente relCliente = new RelCliente();
            relCliente.listarClientesMaisLucrativos();
        });        
    }
    
    
    
    
    /**
     * Evento mouseexit para os itens do menu lateral
     * @param itemMenu
     * @param descricao
     * @param hl 
     */
    private void setMouseExitedEvent(JPanel itemMenu, JLabel descricao, JPanel hl) {
        setColor(itemMenu, Constantes.bkg_btn_lateral_stby);
        setColor(descricao, Color.WHITE);
        setColor(hl, Constantes.bkg_highlight_lateral_stby);
    }

    
    /**
     * Evento mouseentered para os itens do menu lateral
     * @param itemMenu
     * @param descricao
     * @param hl 
     */
    private void setMouseEnteredEvent(JPanel itemMenu, JLabel descricao, JPanel hl) {
        setColor(itemMenu, Constantes.bkg_telas_padrao);
        setColor(descricao, Color.BLACK);
        setColor(hl, Constantes.bkg_highlight_lateral_over);
    }

    
    /**
     * Gerencia as telas, mostrando e escondendo
     * @param frame 
     */
    private static void gerenciaTelas(JInternalFrame frame) {
        if (jDesktopPane.getAllFrames().length == 0) {
            construct(frame);
        } else {
            jDesktopPane.remove(0);
            construct(frame);
        }
    }
    
    
    
    
    // jDesktopPane
    private static void construct(JInternalFrame frame) {
        //scroll = new JScrollPane(frame, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);        
        
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        
        javax.swing.GroupLayout jDesktopPaneLayout = new javax.swing.GroupLayout(jDesktopPane);
        jDesktopPane.setLayout(jDesktopPaneLayout);
        jDesktopPaneLayout.setHorizontalGroup(
           jDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPaneLayout.createSequentialGroup()
               .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1116, Short.MAX_VALUE)
               .addContainerGap())
        );
        jDesktopPaneLayout.setVerticalGroup(
           jDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
           .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jDesktopPane.setLayer(scroll, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        
        scroll.setViewportView(frame);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        frame.setVisible(true);
        scroll.setVisible(true);
        jDesktopPane.setVisible(true);
    }
  
    
    
    private static JPanel getMenuCliente() {
        return menuCliente;
    }
    
    
    private void setColor(JPanel panel, Color color) {
        panel.setBackground(color);
    }    
    
    private void resetColor(JPanel panel, Color color){
        panel.setBackground(color);
    } 

    private void setColor(JLabel label, Color color) {
        label.setForeground(color);
    }        
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jPanelLateral = new javax.swing.JPanel();
        menuCliente = new javax.swing.JPanel();
        jPanelHLCliente = new javax.swing.JPanel();
        jLabelClienteIcon = new javax.swing.JLabel();
        jLabelCliente = new javax.swing.JLabel();
        menuProduto = new javax.swing.JPanel();
        jPanelHLProduto = new javax.swing.JPanel();
        jLabelProdutoIcon = new javax.swing.JLabel();
        jLabelProduto = new javax.swing.JLabel();
        menuFornecedor = new javax.swing.JPanel();
        jPanelHLFornecedor = new javax.swing.JPanel();
        jLabelFornecedorIcon2 = new javax.swing.JLabel();
        jLabelFornecedor = new javax.swing.JLabel();
        menuRegVenda = new javax.swing.JPanel();
        jPanelHLVenda = new javax.swing.JPanel();
        jLabelvendaIcon3 = new javax.swing.JLabel();
        jLabelVenda = new javax.swing.JLabel();
        menuRelCliente = new javax.swing.JPanel();
        jPanelHLRelCliente = new javax.swing.JPanel();
        jLabelRelClienteIcon = new javax.swing.JLabel();
        jLabelRelCliente = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelNomeUsuario = new javax.swing.JLabel();
        jLabelSaidas = new javax.swing.JLabel();
        jLabelSaidas1 = new javax.swing.JLabel();
        menuUsuario = new javax.swing.JPanel();
        jPanelHLUsuario = new javax.swing.JPanel();
        jLabelUsuarioIcon = new javax.swing.JLabel();
        jLabelUsuario = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelSaidas2 = new javax.swing.JLabel();
        jPanelBarraInfo = new javax.swing.JPanel();
        jDesktopPane = new javax.swing.JDesktopPane();
        scroll = new javax.swing.JScrollPane();
        jMenuBar = new javax.swing.JMenuBar();
        mArquivo = new javax.swing.JMenu();
        subSobreSistema = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        subSair = new javax.swing.JMenuItem();
        menuCadastrar = new javax.swing.JMenu();
        subClientes = new javax.swing.JMenuItem();
        subProdutos = new javax.swing.JMenuItem();
        subFornecedor = new javax.swing.JMenuItem();
        subEmpresa = new javax.swing.JMenuItem();
        subUsuario = new javax.swing.JMenuItem();
        menuVendas = new javax.swing.JMenu();
        mnuRegistrarVenda = new javax.swing.JMenuItem();
        menuRelatorios = new javax.swing.JMenu();
        menuAjuda = new javax.swing.JMenu();

        jMenu2.setText("File");
        jMenuBar2.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("jMenu4");

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BLVendas");
        setBackground(new java.awt.Color(245, 246, 250));
        setMinimumSize(new java.awt.Dimension(1366, 720));
        setPreferredSize(new java.awt.Dimension(1366, 720));

        jPanelLateral.setBackground(new java.awt.Color(72, 126, 176));
        jPanelLateral.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanelLateral.setMaximumSize(new java.awt.Dimension(300, 1080));
        jPanelLateral.setMinimumSize(new java.awt.Dimension(250, 600));
        jPanelLateral.setPreferredSize(new java.awt.Dimension(250, 600));

        menuCliente.setBackground(new java.awt.Color(72, 126, 176));
        menuCliente.setMaximumSize(new java.awt.Dimension(350, 50));
        menuCliente.setMinimumSize(new java.awt.Dimension(250, 35));
        menuCliente.setPreferredSize(new java.awt.Dimension(250, 35));

        jPanelHLCliente.setBackground(new java.awt.Color(251, 197, 49));
        jPanelHLCliente.setMaximumSize(new java.awt.Dimension(10, 50));
        jPanelHLCliente.setMinimumSize(new java.awt.Dimension(5, 35));
        jPanelHLCliente.setPreferredSize(new java.awt.Dimension(5, 35));

        javax.swing.GroupLayout jPanelHLClienteLayout = new javax.swing.GroupLayout(jPanelHLCliente);
        jPanelHLCliente.setLayout(jPanelHLClienteLayout);
        jPanelHLClienteLayout.setHorizontalGroup(
            jPanelHLClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelHLClienteLayout.setVerticalGroup(
            jPanelHLClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        jLabelClienteIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelClienteIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/menu/clientes.png"))); // NOI18N
        jLabelClienteIcon.setMaximumSize(new java.awt.Dimension(30, 30));
        jLabelClienteIcon.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabelClienteIcon.setPreferredSize(new java.awt.Dimension(25, 25));

        jLabelCliente.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCliente.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelCliente.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCliente.setText("  Clientes");

        javax.swing.GroupLayout menuClienteLayout = new javax.swing.GroupLayout(menuCliente);
        menuCliente.setLayout(menuClienteLayout);
        menuClienteLayout.setHorizontalGroup(
            menuClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuClienteLayout.createSequentialGroup()
                .addComponent(jPanelHLCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jLabelClienteIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuClienteLayout.setVerticalGroup(
            menuClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHLCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(menuClienteLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(menuClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelClienteIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        menuProduto.setBackground(new java.awt.Color(72, 126, 176));
        menuProduto.setMinimumSize(new java.awt.Dimension(250, 35));
        menuProduto.setPreferredSize(new java.awt.Dimension(250, 35));
        menuProduto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHLProduto.setBackground(new java.awt.Color(251, 197, 49));
        jPanelHLProduto.setMaximumSize(new java.awt.Dimension(10, 50));
        jPanelHLProduto.setMinimumSize(new java.awt.Dimension(5, 35));
        jPanelHLProduto.setPreferredSize(new java.awt.Dimension(5, 35));

        javax.swing.GroupLayout jPanelHLProdutoLayout = new javax.swing.GroupLayout(jPanelHLProduto);
        jPanelHLProduto.setLayout(jPanelHLProdutoLayout);
        jPanelHLProdutoLayout.setHorizontalGroup(
            jPanelHLProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHLProdutoLayout.setVerticalGroup(
            jPanelHLProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        menuProduto.add(jPanelHLProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabelProdutoIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelProdutoIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/menu/produtos.png"))); // NOI18N
        jLabelProdutoIcon.setMaximumSize(new java.awt.Dimension(30, 30));
        jLabelProdutoIcon.setMinimumSize(new java.awt.Dimension(25, 25));
        jLabelProdutoIcon.setPreferredSize(new java.awt.Dimension(25, 25));
        menuProduto.add(jLabelProdutoIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 25, 25));

        jLabelProduto.setBackground(new java.awt.Color(255, 255, 255));
        jLabelProduto.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelProduto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProduto.setText("Produtos");
        menuProduto.add(jLabelProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 5, 80, 20));

        menuFornecedor.setBackground(new java.awt.Color(72, 126, 176));
        menuFornecedor.setMinimumSize(new java.awt.Dimension(250, 35));
        menuFornecedor.setPreferredSize(new java.awt.Dimension(250, 35));
        menuFornecedor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHLFornecedor.setBackground(new java.awt.Color(251, 197, 49));
        jPanelHLFornecedor.setMaximumSize(new java.awt.Dimension(10, 50));
        jPanelHLFornecedor.setMinimumSize(new java.awt.Dimension(5, 35));
        jPanelHLFornecedor.setPreferredSize(new java.awt.Dimension(5, 35));

        javax.swing.GroupLayout jPanelHLFornecedorLayout = new javax.swing.GroupLayout(jPanelHLFornecedor);
        jPanelHLFornecedor.setLayout(jPanelHLFornecedorLayout);
        jPanelHLFornecedorLayout.setHorizontalGroup(
            jPanelHLFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHLFornecedorLayout.setVerticalGroup(
            jPanelHLFornecedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        menuFornecedor.add(jPanelHLFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabelFornecedorIcon2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelFornecedorIcon2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/menu/fornecedores.png"))); // NOI18N
        menuFornecedor.add(jLabelFornecedorIcon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 25, 25));

        jLabelFornecedor.setBackground(new java.awt.Color(255, 255, 255));
        jLabelFornecedor.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelFornecedor.setForeground(new java.awt.Color(255, 255, 255));
        jLabelFornecedor.setText("Fornecedores");
        menuFornecedor.add(jLabelFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 5, 120, 20));

        menuRegVenda.setBackground(new java.awt.Color(72, 126, 176));
        menuRegVenda.setMinimumSize(new java.awt.Dimension(250, 35));
        menuRegVenda.setName(""); // NOI18N
        menuRegVenda.setPreferredSize(new java.awt.Dimension(250, 35));
        menuRegVenda.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHLVenda.setBackground(new java.awt.Color(251, 197, 49));
        jPanelHLVenda.setMaximumSize(new java.awt.Dimension(10, 50));
        jPanelHLVenda.setMinimumSize(new java.awt.Dimension(5, 35));
        jPanelHLVenda.setPreferredSize(new java.awt.Dimension(5, 35));

        javax.swing.GroupLayout jPanelHLVendaLayout = new javax.swing.GroupLayout(jPanelHLVenda);
        jPanelHLVenda.setLayout(jPanelHLVendaLayout);
        jPanelHLVendaLayout.setHorizontalGroup(
            jPanelHLVendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHLVendaLayout.setVerticalGroup(
            jPanelHLVendaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        menuRegVenda.add(jPanelHLVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabelvendaIcon3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelvendaIcon3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/menu/carrinho.png"))); // NOI18N
        menuRegVenda.add(jLabelvendaIcon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 25, 25));

        jLabelVenda.setBackground(new java.awt.Color(255, 255, 255));
        jLabelVenda.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelVenda.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVenda.setText("Registrar Venda");
        menuRegVenda.add(jLabelVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 5, 110, 20));

        menuRelCliente.setBackground(new java.awt.Color(72, 126, 176));
        menuRelCliente.setMinimumSize(new java.awt.Dimension(250, 35));
        menuRelCliente.setPreferredSize(new java.awt.Dimension(250, 35));
        menuRelCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHLRelCliente.setBackground(new java.awt.Color(251, 197, 49));
        jPanelHLRelCliente.setMaximumSize(new java.awt.Dimension(10, 50));
        jPanelHLRelCliente.setMinimumSize(new java.awt.Dimension(5, 35));
        jPanelHLRelCliente.setPreferredSize(new java.awt.Dimension(5, 35));

        javax.swing.GroupLayout jPanelHLRelClienteLayout = new javax.swing.GroupLayout(jPanelHLRelCliente);
        jPanelHLRelCliente.setLayout(jPanelHLRelClienteLayout);
        jPanelHLRelClienteLayout.setHorizontalGroup(
            jPanelHLRelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHLRelClienteLayout.setVerticalGroup(
            jPanelHLRelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        menuRelCliente.add(jPanelHLRelCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabelRelClienteIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelRelClienteIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/menu/rel_clientes.png"))); // NOI18N
        menuRelCliente.add(jLabelRelClienteIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 25, 25));

        jLabelRelCliente.setBackground(new java.awt.Color(255, 255, 255));
        jLabelRelCliente.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelRelCliente.setForeground(new java.awt.Color(255, 255, 255));
        jLabelRelCliente.setText("Relatório de Clientes");
        menuRelCliente.add(jLabelRelCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 5, 140, 20));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/512X512/login.png"))); // NOI18N

        labelNomeUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 1, 18)); // NOI18N
        labelNomeUsuario.setForeground(new java.awt.Color(255, 255, 255));
        labelNomeUsuario.setText("Seu nome");

        jLabelSaidas.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelSaidas.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSaidas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSaidas.setText("CADASTROS");

        jLabelSaidas1.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 14)); // NOI18N
        jLabelSaidas1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSaidas1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSaidas1.setText("RELATÓRIOS");

        menuUsuario.setBackground(new java.awt.Color(72, 126, 176));
        menuUsuario.setMinimumSize(new java.awt.Dimension(250, 35));
        menuUsuario.setName(""); // NOI18N
        menuUsuario.setPreferredSize(new java.awt.Dimension(250, 35));
        menuUsuario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelHLUsuario.setBackground(new java.awt.Color(251, 197, 49));
        jPanelHLUsuario.setMaximumSize(new java.awt.Dimension(10, 50));
        jPanelHLUsuario.setMinimumSize(new java.awt.Dimension(5, 35));
        jPanelHLUsuario.setPreferredSize(new java.awt.Dimension(5, 35));

        javax.swing.GroupLayout jPanelHLUsuarioLayout = new javax.swing.GroupLayout(jPanelHLUsuario);
        jPanelHLUsuario.setLayout(jPanelHLUsuarioLayout);
        jPanelHLUsuarioLayout.setHorizontalGroup(
            jPanelHLUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHLUsuarioLayout.setVerticalGroup(
            jPanelHLUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        menuUsuario.add(jPanelHLUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabelUsuarioIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelUsuarioIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/24x24/menu/usuario.png"))); // NOI18N
        menuUsuario.add(jLabelUsuarioIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, 25, 25));

        jLabelUsuario.setBackground(new java.awt.Color(255, 255, 255));
        jLabelUsuario.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 12)); // NOI18N
        jLabelUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUsuario.setText("Usuários");
        menuUsuario.add(jLabelUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 5, 90, 20));

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelSaidas2.setFont(new java.awt.Font("Avenir LT Std 35 Light", 0, 14)); // NOI18N
        jLabelSaidas2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSaidas2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSaidas2.setText("SAÍDAS");

        javax.swing.GroupLayout jPanelLateralLayout = new javax.swing.GroupLayout(jPanelLateral);
        jPanelLateral.setLayout(jPanelLateralLayout);
        jPanelLateralLayout.setHorizontalGroup(
            jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelSaidas, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(menuCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(menuProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(menuFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(menuUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelSaidas2))
            .addComponent(menuRegVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelSaidas1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(menuRelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanelLateralLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(labelNomeUsuario))))
        );
        jPanelLateralLayout.setVerticalGroup(
            jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(6, 6, 6)
                .addComponent(labelNomeUsuario)
                .addGap(62, 62, 62)
                .addComponent(jLabelSaidas)
                .addGap(5, 5, 5)
                .addComponent(menuCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(menuProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(menuFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(menuUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabelSaidas2)
                .addGap(5, 5, 5)
                .addComponent(menuRegVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabelSaidas1)
                .addGap(5, 5, 5)
                .addComponent(menuRelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelBarraInfo.setBackground(new java.awt.Color(53, 59, 72));
        jPanelBarraInfo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanelBarraInfo.setAlignmentX(0.0F);
        jPanelBarraInfo.setAlignmentY(0.0F);
        jPanelBarraInfo.setMaximumSize(new java.awt.Dimension(1110, 80));
        jPanelBarraInfo.setMinimumSize(new java.awt.Dimension(1110, 80));
        jPanelBarraInfo.setPreferredSize(new java.awt.Dimension(1110, 80));

        javax.swing.GroupLayout jPanelBarraInfoLayout = new javax.swing.GroupLayout(jPanelBarraInfo);
        jPanelBarraInfo.setLayout(jPanelBarraInfoLayout);
        jPanelBarraInfoLayout.setHorizontalGroup(
            jPanelBarraInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelBarraInfoLayout.setVerticalGroup(
            jPanelBarraInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 78, Short.MAX_VALUE)
        );

        jDesktopPane.setBackground(new java.awt.Color(204, 204, 204));
        jDesktopPane.setForeground(new java.awt.Color(245, 246, 250));
        jDesktopPane.setAlignmentX(0.0F);
        jDesktopPane.setAlignmentY(0.0F);
        jDesktopPane.setMaximumSize(new java.awt.Dimension(9999, 9999));
        jDesktopPane.setMinimumSize(new java.awt.Dimension(700, 550));

        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout jDesktopPaneLayout = new javax.swing.GroupLayout(jDesktopPane);
        jDesktopPane.setLayout(jDesktopPaneLayout);
        jDesktopPaneLayout.setHorizontalGroup(
            jDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPaneLayout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1116, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPaneLayout.setVerticalGroup(
            jDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jDesktopPane.setLayer(scroll, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMenuBar.setAlignmentX(0.0F);

        mArquivo.setText("Arquivo");

        subSobreSistema.setText("Sobre o Sistema...");
        mArquivo.add(subSobreSistema);
        mArquivo.add(jSeparator1);

        subSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        subSair.setText("Sair");
        mArquivo.add(subSair);

        jMenuBar.add(mArquivo);

        menuCadastrar.setText("Cadastros");

        subClientes.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        subClientes.setText("Clientes");
        menuCadastrar.add(subClientes);

        subProdutos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        subProdutos.setText("Produtos");
        menuCadastrar.add(subProdutos);

        subFornecedor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK));
        subFornecedor.setText("Fornecedores");
        menuCadastrar.add(subFornecedor);

        subEmpresa.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        subEmpresa.setText("Empresa");
        menuCadastrar.add(subEmpresa);

        subUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
        subUsuario.setText("Usuário");
        menuCadastrar.add(subUsuario);

        jMenuBar.add(menuCadastrar);

        menuVendas.setText("Vendas");

        mnuRegistrarVenda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK));
        mnuRegistrarVenda.setText("Vendas");
        menuVendas.add(mnuRegistrarVenda);

        jMenuBar.add(menuVendas);

        menuRelatorios.setText("Relatórios");
        jMenuBar.add(menuRelatorios);

        menuAjuda.setText("Ajuda");
        jMenuBar.add(menuAjuda);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelLateral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelBarraInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 1126, Short.MAX_VALUE)
                    .addComponent(jDesktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelBarraInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jDesktopPane, javax.swing.GroupLayout.PREFERRED_SIZE, 520, Short.MAX_VALUE))
            .addComponent(jPanelLateral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ViewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ViewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ViewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ViewPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        java.awt.EventQueue.invokeLater(() -> {
//            ViewPrincipal view = new ViewPrincipal();
//            view.setVisible(true);
//            view.iniciarMenuLateral();
//        });
//    }


    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private static javax.swing.JDesktopPane jDesktopPane;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelCliente;
    private javax.swing.JLabel jLabelClienteIcon;
    private javax.swing.JLabel jLabelFornecedor;
    private javax.swing.JLabel jLabelFornecedorIcon2;
    private javax.swing.JLabel jLabelProduto;
    private javax.swing.JLabel jLabelProdutoIcon;
    private javax.swing.JLabel jLabelRelCliente;
    private javax.swing.JLabel jLabelRelClienteIcon;
    private javax.swing.JLabel jLabelSaidas;
    private javax.swing.JLabel jLabelSaidas1;
    private javax.swing.JLabel jLabelSaidas2;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JLabel jLabelUsuarioIcon;
    private javax.swing.JLabel jLabelVenda;
    private javax.swing.JLabel jLabelvendaIcon3;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelBarraInfo;
    private javax.swing.JPanel jPanelHLCliente;
    private javax.swing.JPanel jPanelHLFornecedor;
    private javax.swing.JPanel jPanelHLProduto;
    private javax.swing.JPanel jPanelHLRelCliente;
    private javax.swing.JPanel jPanelHLUsuario;
    private javax.swing.JPanel jPanelHLVenda;
    private javax.swing.JPanel jPanelLateral;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelNomeUsuario;
    private javax.swing.JMenu mArquivo;
    private javax.swing.JMenu menuAjuda;
    private javax.swing.JMenu menuCadastrar;
    private static javax.swing.JPanel menuCliente;
    private javax.swing.JPanel menuFornecedor;
    private javax.swing.JPanel menuProduto;
    private javax.swing.JPanel menuRegVenda;
    private javax.swing.JPanel menuRelCliente;
    private javax.swing.JMenu menuRelatorios;
    private javax.swing.JPanel menuUsuario;
    private javax.swing.JMenu menuVendas;
    private javax.swing.JMenuItem mnuRegistrarVenda;
    private static javax.swing.JScrollPane scroll;
    private javax.swing.JMenuItem subClientes;
    private javax.swing.JMenuItem subEmpresa;
    private javax.swing.JMenuItem subFornecedor;
    private javax.swing.JMenuItem subProdutos;
    private javax.swing.JMenuItem subSair;
    private javax.swing.JMenuItem subSobreSistema;
    private javax.swing.JMenuItem subUsuario;
    // End of variables declaration//GEN-END:variables
}
