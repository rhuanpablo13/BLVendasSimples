/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.arquivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author rhuan
 */
public class Arquivo {
    
    
    /**
     * Copia um arquivo do diretório source para o destination
     * @param source
     * @param destination
     * @throws IOException 
     */
    public void copyFile(File source, File destination) throws IOException {
        if (destination.exists()) {
            destination.delete();
        }

        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        } finally {
            if (sourceChannel != null && sourceChannel.isOpen())
                sourceChannel.close();
            if (destinationChannel != null && destinationChannel.isOpen())
                destinationChannel.close();
       }
    }


    

    /**
     * Recupera o diretório raiz do projeto
     * @return String
     */
    public static String getSrcProject() {
        return System.getProperty("user.dir");
    }


    public File getPathDestinationFile() {
        JFileChooser file = new JFileChooser(); 
        int i = file.showSaveDialog(null);
        if (i == 0){
           File arquivo = file.getSelectedFile();
           return arquivo;
        }
        return null;
    }
    
    
    public File getPathOriginFile() {
        JFileChooser filec = new JFileChooser(); 
        File file = null;
        int returnVal = filec.showOpenDialog(new JFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = filec.getSelectedFile();
            return file;
        } 
        return null;
    }

    

    
      
}