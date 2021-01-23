package org.traffic.visualisation;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class UIEdgeComponent extends JPanel {

    static int tableCellSize = 20;

    public UIEdgeComponent() {

        super();
        setSize(150, 150);

        Object[][] tableData = {{" ", "|", " ", "|", " "}, {" ", "down", " ", "x", " "}, {" ", "|", " ", "|", " "}, {" ", "x", " ", "up", " "}, {" ", "|", " ", "|", " "}};
        String[] columnNames = {"", "", "", "", ""};
        JTable nodeTable = new JTable(tableData, columnNames);
        nodeTable.setRowHeight(tableCellSize);

        for (int i = 0; i < nodeTable.getColumnModel().getColumnCount(); i++) {
            TableColumn column = nodeTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(tableCellSize);
        }

        add(nodeTable);
        setVisible(true);

        setVisible(true);
    }
}
