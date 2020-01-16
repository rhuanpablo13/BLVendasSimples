/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

import infra.abstratas.Negocio;
import java.awt.Dimension;
import java.util.Arrays;
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
public class TableViewGUI<T extends Negocio> {
    
    private final JPanel container;
    private ObservableList<T> observableList;
    private TableColumn[] columns;
    private final TableView<T> tableView;
    private final int width;
    private final int height;
    private JFXPanel fxPanel;
    private Scene scene;

    public TableViewGUI(JPanel container, TableColumn[] columns) {
        this.container = container;
        this.columns = columns;
        this.width = container.getPreferredSize().width -35;
        this.height = container.getPreferredSize().height;
        this.fxPanel = new JFXPanel();
        this.tableView = new TableView<>();
    }
    
    
    public void initAndShowGui() {
        fxPanel.setSize(new Dimension(width, height));
        fxPanel.setVisible(true);
        if (container.getComponents().length > 0) {
            fxPanel = (JFXPanel) container.getComponent(0);
            container.remove(0);
        }
        container.add(fxPanel);
        
        Platform.runLater(() -> {
            initFx(fxPanel);
        });
    }


    private void initFx(JFXPanel fxPanel) {
        scene = initTableView();
        if (fxPanel.isShowing()) {
            System.out.println("remove fxpanel " + this.getClass().getCanonicalName());
                    
            fxPanel.removeAll();
        }
        fxPanel.setScene(scene);
    }
    
    
    private Scene initTableView() {
            
        if (observableList == null) {
            observableList = FXCollections.observableArrayList();
            //add the columns to the table view
            tableView.getColumns().addAll(columns);

            //Load the data into the table
            tableView.setItems(observableList);

            //initialise the TableView
            tableView.setPrefSize(width, height);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            System.out.println("novo observableList");
        }

        Group root = new Group();
        root.getChildren().add(tableView);
        return new Scene(root);
//        return scene;
    }
    
    public void setColumns(TableColumn... columns) {
        this.columns = columns;
    }
    
    public void setItems(List itens) {
        this.observableList = FXCollections.observableArrayList(itens);        
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    
}
