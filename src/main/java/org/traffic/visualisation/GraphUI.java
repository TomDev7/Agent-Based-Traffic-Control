package org.traffic.visualisation;

import javax.swing.*;
import java.awt.*;

public class GraphUI extends JFrame {

    private JTable table1;
    private JTable table2;
    private JTable table3;

    public GraphUI() {

        super("Graph UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout());

        UIEdgeComponent uiec = new UIEdgeComponent();
        add(uiec);
        UINodeComponent uinc = new UINodeComponent();
        add(uinc);

        setVisible(true);
    }
}
