/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.visualizacao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author RHUAN
 */
public class Constantes {
    
    static final Color bkg_telas_padrao = new java.awt.Color(245, 246, 250);
    
    // tela login  rgb(72, 126, 176)
    static final Color bkg_tela_cor = new java.awt.Color(72, 126, 176);
    static final Color bkg_btn_login_stby = new java.awt.Color(72, 126, 176);
    static final Color bkg_btn_login_over = new java.awt.Color(35, 154, 180);
    static final Color bkg_btn_login_click = new java.awt.Color(24, 121, 143);
    
    // botões gerais
    static final Color bkg_btn_padrao_stby = new java.awt.Color(220, 221, 225);
    static final Color bkg_btn_padrao_over = new java.awt.Color(236, 237, 240);
    static final Color bkg_btn_padrao_click = new java.awt.Color(216, 216, 216);
  
    
    // botão cofirmação
    static final Color bkg_btn_confirm_stby = new java.awt.Color(76, 209, 55);
    static final Color bkg_btn_confirm_over = new java.awt.Color(78, 220, 55);
    static final Color bkg_btn_confirm_click = new java.awt.Color(60, 185, 41);
     
    
    // botão cancelar
    static final Color bkg_btn_cancel_stby = new java.awt.Color(232, 65, 24);
    static final Color bkg_btn_cancel_over = new java.awt.Color(247, 98, 62);
    static final Color bkg_btn_cancel_click = new java.awt.Color(196, 49, 12);
    
    
    // botões laterais
    static final Color bkg_btn_lateral_stby = new java.awt.Color(72, 126, 176);
    static final Color bkg_btn_lateral_over = new java.awt.Color(255, 255, 255);
    static final Color bkg_btn_lateral_click = new java.awt.Color(54, 58, 67);
    
    // barrinha lateral dos botões do menu principal 
    static final Color bkg_highlight_lateral_stby = new java.awt.Color(251, 197, 49);
    static final Color bkg_highlight_lateral_over = new java.awt.Color(232, 65, 24);
    
    
    
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static final int WIDTH = (int) screenSize.getWidth();
    static final int HEIGHT = (int)screenSize.getHeight() -45; //TIRA A DIFERENÇA DA BARRA DO WINDOWS
    
    
    static final String MSG_OBRIGATORIO = "Campo obrigatório";
    static final Color VERMELHO_ERRO_TXT = new Color(231, 76, 60);
    static final Color COR_PADRAO_TXT = new Color(0,0,0);
    
    
    static final String MSG_NOME_VAZIO = "Por favor, informe um nome";
    static final String MSG_CPF_VAZIO = "Por favor, informe o CPF";
    static final String MSG_DT_NASC_VAZIO = "Por favor, informe a data de nascimento";
    static final String MSG_CEP_VAZIO = "CEP inválido";
    static final String MSG_CEP_INCORRETO = "CEP não encontrado";
    static final String MSG_ENDERECO_VAZIO = "Por favor, informe um endereço";
    static final String MSG_CIDADE_VAZIO = "Por favor, informe uma cidade";
    static final String MSG_BAIRRO_VAZIO = "Por favor, informe um bairro";
    static final String MSG_UF_VAZIO = "Por favor, informe um UF";
    static final String MSG_TEL_VAZIO = "Por favor, informe um número de telefone";
    static final String MSG_CELULAR_INVALIDO = "Número inválido";
    static final String MSG_EMAIL_VAZIO = "Por favor, informe um e-mail";
    
    
    public static final String INSERIR = "inserir";
    public static final String ALTERAR = "alterar";
    public static final String EXCLUIR = "excluir";
    public static final String PESQUISAR = "pesquisar";
    
    
    
}
