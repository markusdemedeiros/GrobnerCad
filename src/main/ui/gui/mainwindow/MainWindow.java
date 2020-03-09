package ui.gui.mainwindow;

import com.formdev.flatlaf.*;
import model.calculational.FullSystem;
import model.persistence.Reader;
import ui.gui.mainwindow.panel.DataPanel;
import ui.gui.mainwindow.panel.DrawingEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;


// Handles layout of components for main drawing window
// Uses flat Darcula LAF

public class MainWindow extends JFrame {

    private FullSystem currentSystem;

    public static final Dimension MIN_SIZE = new Dimension(700, 500);
    public static final Dimension DEF_SIZE = new Dimension(1000, 600);
    private DrawingEditorPanel dep;

    // Adds components to main UI
    @SuppressWarnings("checkstyle:MethodLength")
    private void initUI() {
        setLayout(new GridBagLayout());

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        this.setJMenuBar(menubar());

        // Drawing editor
        dep = new DrawingEditorPanel();
        add(dep, new GridBagConstraints(0, 0, 1, 1,
                1, 1,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        // Panels
        DataPanel cp = new DataPanel(currentSystem);
        add(cp, new GridBagConstraints(1, 0, 1, 1,
                0, 1,
                GridBagConstraints.LINE_END, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));



        // ================================
        // WINDOW PARAMATERS
        setTitle("Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(MIN_SIZE);
        setSize(DEF_SIZE);
    }


    // Returns a JMenubar for the GUI Menu
    private JMenuBar menubar() {
        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenuItem repaintItem = new JMenuItem("Repaint");
        JMenuItem moveXItem = new JMenuItem("Increase x offset by 1");

        fileMenu.add(quitItem);
        fileMenu.add(repaintItem);

        menubar.add(fileMenu);

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Default close operation from JFrame attached to MainWindow
                MainWindow.super.dispose();
            }
        });

        repaintItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do nothing else for now
                dep.redrawAll();
            }
        });


        return menubar;
    }



    public MainWindow() {
        try {
            currentSystem = Reader.readSystem(new File("./data/allconstraints.sys"));
        } catch (IOException e) {
            // Just crash for now, I will handle this later
            throw new UncheckedIOException(e);
        }

        initUI();
    }
}


