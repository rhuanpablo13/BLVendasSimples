/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import sistemavendas.negocio.NCliente;
import sistemavendas.negocio.NProduto;

/**
 *
 * @author rhuan
 */
public class TableViewFormat {



    public void formatDate(TableColumn columnDate) {
            columnDate.setCellFactory(column -> {
            TableCell<NCliente, Date> cell = new TableCell<NCliente, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(format.format(item));
                    }
                }
            };
            return cell;
        });
    }
    
    public void formatTCpf(TableColumn columnCpf) {
            columnCpf.setCellFactory(column -> {
            TableCell<NCliente, String> cell = new TableCell<NCliente, String>() {
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    
                    item = setMask(item, "###.###.###-##");                    
                    super.updateItem(item, empty);
                    
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(item);
                    }
                }
            };
            return cell;
        });
    }    
    
    
    public void formatCep(TableColumn columnCep) {
            columnCep.setCellFactory(column -> {
            TableCell<NCliente, String> cell = new TableCell<NCliente, String>() {
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    
                    item = setMask(item, "#####-###");
                    super.updateItem(item, empty);
                    
                    if(empty) {
                        setText(null);
                    }
                    else {
                        setText(item);
                    }
                }
            };
            return cell;
        });
    }    
    
    
    public void formatMonetary(TableColumn columnMoney) {
            columnMoney.setCellFactory(column -> {
            TableCell<NProduto, Double> cell = new TableCell<NProduto, Double>() {
                
                @Override
                protected void updateItem(Double item, boolean empty) {

                    if (item != null) {
                        
                        //DecimalFormat twoDig = new DecimalFormat("##,###,##0.00");
                        //String suaVariavel = "1.200,00";
                        //double num = twoDig.parse(suaVariavel).doubleValue(); //converte de string para double
                        //String suaString = twoDig.format(num);//converte de double para string
                        //System.out.println(suaString);
                        
                        
                        DecimalFormat format = new DecimalFormat("###,###.00");
                        Double numero = Double.parseDouble(format.format(item));
                        super.updateItem(numero, empty);

                        if(empty) {
                            setText(null);
                        }
                        else {
                            setText(Double.toString(numero));
                        }
                    }
                }
            };
            return cell;
        });
    }    
    
    
    private static String setMask(String value, String mask) {
        try {
            MaskFormatter format = new MaskFormatter(mask);
            JFormattedTextField textField = new JFormattedTextField();
            format.install(textField);
            textField.setText(value);
            return textField.getText();
        } catch (ParseException ex) {
            Logger.getLogger(TableViewFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
