/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.listagem;

/**
 *
 * @author rhuan
 */
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JTableListSelectionListener {

  public static void main(String[] a) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final JTable table;

    String[] columnTitles = { "A", "B", "C", "D" };
    Object[][] rowData = { { "11", "12", "13", "14" }, { "21", "22", "23", "24" },
        { "31", "32", "33", "34" }, { "41", "42", "44", "44" } };

    table = new JTable(rowData, columnTitles);

    table.setCellSelectionEnabled(true);
    ListSelectionModel cellSelectionModel = table.getSelectionModel();
    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        String selectedData = null;

        int[] selectedRow = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();

        for (int i = 0; i < selectedRow.length; i++) {
          for (int j = 0; j < selectedColumns.length; j++) {
            selectedData = (String) table.getValueAt(selectedRow[i], selectedColumns[j]);
          }
        }
        System.out.println("Selected: " + selectedData);
      }

    });

    frame.add(new JScrollPane(table));

    frame.setSize(300, 200);
    frame.setVisible(true);
  }

}
