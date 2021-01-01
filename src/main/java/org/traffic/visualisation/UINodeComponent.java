package org.traffic.visualisation;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class UINodeComponent extends JPanel {

    static int tableCellSize = 20;  //width and height of a cell

    public UINodeComponent() {

        super();
        setSize(150, 150);

        //  x - turn left, y - drive ahead, z - turn right
        Object[][] tableData = {{"z", "y", "x", " ", "z"}, {" ", " ", " ", " ", "y"}, {"x", " ", " ", " ", "x"}, {"y", " ", " ", " ", " "}, {"z", " ", "x", "y", "z"}};
        String[] columnNames = {"", "", "", "", ""};
        JTable nodeTable = new JTable(tableData, columnNames);
        nodeTable.setRowHeight(tableCellSize);

        for (int i = 0; i < nodeTable.getColumnModel().getColumnCount(); i++) {
            TableColumn column = nodeTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(tableCellSize);
        }

        add(nodeTable);
        setVisible(true);
    }
}
