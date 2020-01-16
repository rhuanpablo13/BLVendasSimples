/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import infra.abstratas.Negocio;
import java.awt.Dimension;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.swing.JPanel;

/**
 *
 * @author rhuan
 * @param <T>
 */
public class TableViewGUI2<T extends Negocio> {
    
    private JPanel container;
    private ObservableList<T> observableList;
    private TableColumn[] columns;
    private TableView<T> tableView;
    private final int width;
    private final int height;
    private JFXPanel fxPanel;


    public TableViewGUI2(ObservableList observableList, TableColumn[] columns) {
        this.width = 750;
        this.height = 350;
        this.observableList = observableList;
        this.columns = columns;
        this.fxPanel = new JFXPanel();
        this.tableView = new TableView<>();
    }
    
    
    public JPanel getTableView() {
        this.container = new JPanel();
        initAndShowGui();
        container.add(fxPanel);
        container.setVisible(true);
        return container;
    }
    
    
    private void initAndShowGui() {
        //fxPanel.setSize(new Dimension(width, height));
        fxPanel.setSize(new Dimension(750, 350));
        fxPanel.setVisible(true);
        container.add(fxPanel);
        
        Platform.runLater(() -> {
            initFx(fxPanel);
        });
    }
    

    public void initFx(JFXPanel fxPanel) {
        Scene scene = initTableView();
        fxPanel.setScene(scene);
    }
    
    private Scene initTableView() {

        observableList = FXCollections.observableArrayList();

        //initialise the TableView
        tableView.setPrefSize(width, height);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //add the columns to the table view
        tableView.getColumns().addAll(columns);

        //Load the data into the table
        tableView.setItems(observableList);
        
        Group root = new Group();
        root.getChildren().add(tableView);
        Scene scene = new Scene(root);
        
        return scene;
    }
    
    public void setColumns(TableColumn... columns) {
        this.columns = columns;
    }
    
    public void setItems(List itens) {
        this.observableList = FXCollections.observableArrayList(itens);        
    }


    
}
