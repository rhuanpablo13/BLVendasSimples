/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemavendas.relatorios.cliente;

import infra.comunicacao.Erro;
import infra.comunicacao.StackDebug;
import infra.conexao.ConnectionFactory;
import infra.conexao.Schema;
import java.io.InputStream;
import java.sql.Connection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author rhuan
 */
public class RelCliente {
    
    
    public void listarClientesPorIdadeERegiao()  {

        // Define o caminho do template.
        String path = "clientes_por_idade_grafico.jrxml";
        
        // Compilar o relatório
        JasperPrint jp = compilarRelatorio(path);
                
        // Exportar o relatório para PDF.
        JasperViewer.viewReport(jp, false);
    }
    
    
    
    public void listarClientesMaisRegistradosEmVendas()  {

        // Define o caminho do template.
        String path = "clientes_mais_registrados_vendas.jrxml";
        
        // Compilar o relatório
        JasperPrint jp = compilarRelatorio(path);
                
        // Exportar o relatório para PDF.
        JasperViewer.viewReport(jp, false);
    }
    
    
    
    public void listarClientesMaisLucrativos()  {

        // Define o caminho do template.
        String path = "clientes_mais_lucrativos.jrxml";
        
        // Compilar o relatório
        JasperPrint jp = compilarRelatorio(path);
                
        // Exportar o relatório para PDF.
        JasperViewer.viewReport(jp, false);
    }
    
    
    
    public void listarTodos() {

        // Define o caminho do template.
        String path = "clientes_todos.jrxml";
        
        // Compilar o relatório
        JasperPrint jp = compilarRelatorio(path);
        
        // Exportar o relatório para PDF.
        JasperViewer.viewReport(jp, false);
    }
    
    
    
    private JasperPrint compilarRelatorio(String path) {
        
        try {
            InputStream jasperTemplate = RelCliente.class.getResourceAsStream(path);
            
            // Compila o template.
            JasperReport jr = JasperCompileManager.compileReport(jasperTemplate);
            
            // Cria a conexão com o banco de dados.
            Connection conexao = ConnectionFactory.getConnection(Schema.MYSQL_SISVENDASSIMPLES);
            
            // Passagem dos parâmetros e preenchimento do relatório - informamos um
            // datasource vazio, pois a query do relatório irá trazer os dados.
            JasperPrint jp = JasperFillManager.fillReport(jr, null, conexao);
            
            return jp;
        } catch (JRException ex) {
            new Erro("Falha ao gerar relatório!" + StackDebug.getLineNumber(this.getClass()), "").show();
            ex.printStackTrace();
        }
        return null;
    }
}
