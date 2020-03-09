package ui.gui.mainWindow;

import com.formdev.flatlaf.*;
import ui.gui.mainWindow.panel.DrawingEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// Handles layout of components for main drawing window
// Uses flat Darcula LAF

public class MainWindow extends JFrame {




    private DrawingEditorPanel dp;

    public static final Dimension DEF_SIZE = new Dimension(700, 400);
    private JPanel content;

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
        DrawingEditorPanel dep = new DrawingEditorPanel();
        //dc.setPreferredSize(new Dimension(300, 300));
        add(dep, new GridBagConstraints(0, 0, 1, 1,
                1.0, 1.0,
                GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        // ================================
        // WINDOW PARAMATERS
        setTitle("Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEF_SIZE);
    }


    // Returns a JMenubar for the GUI Menu
    private JMenuBar menubar() {
        JMenuBar menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenuItem repaintItem = new JMenuItem("Repaint");

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
                repaint();
            }
        });

        return menubar;
    }




    public MainWindow() {
        initUI();
    }
}


