/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.arquivos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import sistemavendas.controle.CProduto;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




/**
 *
 * @author rhuan
 */
public class Imagem {
    
    private BufferedImage img;    
    // "https://lojamultilaser.vteximg.com.br/arquivos/ids/169222-1000-1000/HO032_01.jpg?v=636613740964770000"
    
    
    public BufferedImage getNewImage(URL url, Dimension dimension) {
        try {
            img = ImageIO.read(url);       
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem do link informado. " + e.getMessage());
        }
        return processaImagem(img, dimension);
    }
    
    public BufferedImage getNewImage(File path, Dimension dimension) {
        try {
            img = ImageIO.read(path);            
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem do caminho informado. " + e.getMessage());
        }
        return processaImagem(img, dimension);
    }
    
    public BufferedImage getNewImage(File path, Dimension dimension, boolean circleImage) {
        try {
            img = ImageIO.read(path);            
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem do caminho informado. " + e.getMessage());
        }
        return processaImagem(img, dimension, circleImage);
    }
    
    public Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
    
    public BufferedImage cropImage(BufferedImage src, int width, int height) {
        BufferedImage dest = src.getSubimage(0, 0, width, height);
        return dest; 
    }

    public BufferedImage cropImage(BufferedImage src, Dimension dimension) {
        BufferedImage dest = src.getSubimage(0, 0, dimension.width, dimension.height);
        return dest;
    }

    public boolean saveImage(BufferedImage bufferedImage, String name, String extension, File pathOutput) throws IOException {
        String path = tratarExtensao(name, extension);
        String newPath = pathOutput.getAbsolutePath() + "\\" + path;
        System.out.println(newPath);
        pathOutput = new File(newPath);
        return ImageIO.write(bufferedImage, extension, pathOutput);
    }    
    
    
    
    
    /**
     * Método que vai processar o tamanho da imagem
     * @param img
     * @param larguraFinal
     * @param alturaFinal
     * @return 
     */
    private BufferedImage processaImagem(BufferedImage img, Dimension dimension) {
        int largura = img.getWidth();
        int altura = img.getHeight();
        BufferedImage tmp = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = tmp.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, (int)dimension.getWidth(), (int)dimension.getHeight(), null);
        g2.dispose();
        BufferedImage newImage = tmp.getSubimage(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
        return cropImage(newImage, dimension);
    }

    

    /**
     * Método que vai processar o tamanho da imagem
     * @param img
     * @param larguraFinal
     * @param alturaFinal
     * @return 
     */
    private BufferedImage processaImagem(BufferedImage img, Dimension dimension, boolean circleFormat) {
        int largura = img.getWidth();
        int altura = img.getHeight();
        BufferedImage tmp = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tmp.createGraphics();
        g2.setClip(new Ellipse2D.Float(0, 0, img.getWidth(), img.getWidth()));
        g2.drawImage(tmp, 0, 0, img.getWidth(), img.getWidth(), null);
        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.dispose();
        BufferedImage newImage = tmp.getSubimage(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
        return cropImage(newImage, dimension);
    }    
    
    
    
    private String tratarExtensao(String name, String extension) {
        name = name.replace("\\", "/");

        String[] explode = name.split("/");
        String nomeArq = explode[explode.length-1];
        
        String[] arNomeArq = nomeArq.split("\\.");
        
        String extensao = arNomeArq[arNomeArq.length-1];
        if (extensao.length() == 3 || extensao.length() == 4) {
            if (extensao.contains("jpg") || extensao.contains("jpeg") || extensao.contains("png")) {
                return nomeArq.replace("/", "\\");
            }
        }          
        return arNomeArq[0] + "." + extension;
    }
    
    
    public static String getNomeArquivo(String arq) {
        arq = arq.replace("\\", "/");
        String[] explode = arq.split("/");
        String nomeArq = explode[explode.length-1];
        return nomeArq;
    }
    
    
    
    public static BufferedImage loadImage(File caminho, int h, int w, boolean circleImage) {
        Imagem imagem = new Imagem();
        return imagem.getNewImage(caminho, new Dimension(h, w));
    }
        
        
    public static BufferedImage loadImage(File caminho, int h, int w) {
        Imagem imagem = new Imagem();
        return imagem.getNewImage(caminho, new Dimension(h, w));
    }
    
    
    public static BufferedImage loadImage(File caminho) {
        return loadImage(caminho, 200, 200);
    }
    
    
    public static BufferedImage loadImage(String caminho) {
        return loadImage(new File(caminho), 200, 200);
    }

    
    public static BufferedImage loadImage(String caminho, int h, int w) {
        return loadImage(new File(caminho), h, w);
    }
    
    
    public static String save(File origem, File destino) { 
        Imagem imagem = new Imagem();
        String imagemNome = imagem.tratarExtensao(origem.getAbsolutePath(), "jpg");
        
        BufferedImage bimg = imagem.getNewImage(origem, new Dimension(200, 200));
        try {
            imagem.saveImage(bimg, imagemNome, "jpg", destino);
        } catch (IOException ex) {
            Logger.getLogger(CProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return destino.getAbsolutePath() + "\\" + imagemNome;
    }
    
    
}


class Circle {

    int x, y, width, height;

    public Circle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 10, 10);

        g2d.setColor(Color.GRAY);
        g2d.fill(circle);
    }

}
