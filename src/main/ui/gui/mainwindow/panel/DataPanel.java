package ui.gui.mainwindow.panel;

import model.algebraic.Constraint;
import model.calculational.FullSystem;
import model.geometric.Geometry;
import ui.DataGUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

import static javax.swing.BorderFactory.createEmptyBorder;


public class DataPanel extends JPanel {


    // TODO: Add Click on icon to show polynomial tooltip and functionality
    private static final int NUM_COLS = 3;
    private static final Object[] COLNAMESCON = {"", "Type", "Name"};
    private final int[] COLUMNWIDTHSCON = {25, 80, 100};

    private final int HALFMODULEPADDING = 5;


    public DataPanel(FullSystem currentSystem) {
        setLayout(new GridBagLayout());

        // Constraint Table
        JTable constraintTable = new JTable(generateConstraintTableData(currentSystem.getAlgebraAsArray()), COLNAMESCON);
        for (int i = 0; i < NUM_COLS; i++) {
            TableColumn c = constraintTable.getColumnModel().getColumn(i);
            c.setPreferredWidth(COLUMNWIDTHSCON[i]);
            c.setMaxWidth(COLUMNWIDTHSCON[i]);
            c.setMinWidth(COLUMNWIDTHSCON[i]);
        }

        // Geometry Table
        JTable geometryTable = new JTable(generateGeometryTableData(currentSystem.getGeometryAsArray()), COLNAMESCON);
        for (int i = 0; i < NUM_COLS; i++) {
            TableColumn c = geometryTable.getColumnModel().getColumn(i);
            c.setPreferredWidth(COLUMNWIDTHSCON[i]);
            c.setMaxWidth(COLUMNWIDTHSCON[i]);
            c.setMinWidth(COLUMNWIDTHSCON[i]);
        }


        // Titles
        JLabel constraintTitle = new JLabel("Constraints");
        JLabel geometryTitle = new JLabel("Geometry");


        // Add Constraint elements
        JScrollPane scrollPane;
        scrollPane = new JScrollPane(constraintTable);
        scrollPane.setBorder(createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(tableWidth(), 200));
        add(scrollPane, new GridBagConstraints(0, 1, 1, 1,
                0.5, 1.0,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 0, 0));
        add(constraintTitle, new GridBagConstraints(0, 0, 1, 1,
                1.0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 0, 0));

        // Add Constraint buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        JButton removeConstraint = new JButton("-");
        removeConstraint.setPreferredSize(new Dimension(DataGUI.TOOLBAR_BUTTON_SIZE, DataGUI.TOOLBAR_BUTTON_SIZE));
        buttonPane.add(removeConstraint);
        JButton seePolynomial = new JButton("P");
        seePolynomial.setPreferredSize(new Dimension(DataGUI.TOOLBAR_BUTTON_SIZE, DataGUI.TOOLBAR_BUTTON_SIZE));
        buttonPane.add(seePolynomial);
        add(buttonPane, new GridBagConstraints(0, 2, 1, 1,
                0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 0, 0));



        add(geometryTitle, new GridBagConstraints(1, 0, 1, 1,
                1.0, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.BOTH,
                new Insets(0,HALFMODULEPADDING,0,HALFMODULEPADDING), 0, 0));

        // Add Geo elements
        JScrollPane geoPane;
        geoPane = new JScrollPane(geometryTable);
        geoPane.setBorder(createEmptyBorder());
        geoPane.setPreferredSize(new Dimension(tableWidth(), 200));
        add(geoPane, new GridBagConstraints(1, 1, 1, 1,
                0.5, 1.0,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0,HALFMODULEPADDING,0,HALFMODULEPADDING), 0, 0));


        // Add Geo buttons
        JPanel buttonPane1 = new JPanel();
        buttonPane1.setLayout(new FlowLayout());
        JButton removeConstraint1 = new JButton("-");
        removeConstraint1.setPreferredSize(new Dimension(DataGUI.TOOLBAR_BUTTON_SIZE, DataGUI.TOOLBAR_BUTTON_SIZE));
        buttonPane1.add(removeConstraint1);
        add(buttonPane1, new GridBagConstraints(1, 2, 1, 1,
                0, 0,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 0, 0));

    }


    private Object[][] generateConstraintTableData(Constraint[] constraints) {
        Object[][] tableData = new Object[constraints.length][3];
        for (int i = 0; i < constraints.length; i++) {
            tableData[i][0] = "[*]";
            tableData[i][1] = constraints[i].getType();
            tableData[i][2] = constraints[i].getName();
        }
        return tableData;
    }

    private Object[][] generateGeometryTableData(Geometry[] geometries) {
        Object[][] tableData = new Object[geometries.length][3];
        for (int i = 0; i < geometries.length; i++) {
            tableData[i][0] = "[*]";
            tableData[i][1] = geometries[i].getType();
            tableData[i][2] = geometries[i].getName();
        }
        return tableData;
    }


    private int tableWidth() {
        int output = 0;
        for (int i = 0; i < NUM_COLS; i++) {
            output += COLUMNWIDTHSCON[i];
        }
        return output;
    }




}
