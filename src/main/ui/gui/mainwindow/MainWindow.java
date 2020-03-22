package ui.gui.mainwindow;

import com.formdev.flatlaf.FlatDarculaLaf;
import model.calculational.FullSystem;
import model.persistence.Reader;
import ui.gui.mainwindow.exceptions.NoGraphicsException;
import ui.gui.mainwindow.graphicalPersistence.GraphicalLoader;
import ui.gui.mainwindow.panel.DrawingEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;


// Handles layout of components for main drawing window
// Uses flat Darcula LAF

public class MainWindow extends JFrame {

    private FullSystem currentSystem;

    public static final Dimension MIN_SIZE = new Dimension(700, 500);
    public static final Dimension DEF_SIZE = new Dimension(1000, 600);
    private DrawingEditorPanel dep;

    // Adds components to main UI
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
        JMenuItem writeItem = new JMenuItem("Save");
        JMenuItem readItem = new JMenuItem("Open");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenu graphicMenu = new JMenu("Graphics");
        JMenuItem repaintItem = new JMenuItem("Repaint");
        JMenuItem recomputeItem = new JMenuItem("Recompute All");
        fileMenu.add(writeItem);
        fileMenu.add(readItem);
        fileMenu.add(quitItem);
        graphicMenu.add(repaintItem);
        graphicMenu.add(recomputeItem);
        menubar.add(fileMenu);
        menubar.add(graphicMenu);
        quitItem.addActionListener(quitActionListener());
        repaintItem.addActionListener(repaintActionListener());
        recomputeItem.addActionListener(recomputeActionListener());
        readItem.addActionListener(readActionListener());
        writeItem.addActionListener(saveActionListener());
        return menubar;
    }

    private ActionListener readActionListener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog("Which system would you like to load?");
                if (filename != null) {
                    File toRead = new File("./data/" + filename + ".sys");
                    try {
                        loadSystem(toRead);
                    } catch (IOException ex) {
                        miscIOErrorDisplay();
                    }
                }
            }
        });
    }

    private ActionListener recomputeActionListener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dep.recomputeAll();
            }
        });
    }

    private ActionListener repaintActionListener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do nothing else for now
                dep.redrawAll();
            }
        });
    }


    private ActionListener quitActionListener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Default close operation from JFrame attached to MainWindow
                MainWindow.super.dispose();
            }
        });
    }


    private ActionListener saveActionListener() {
        return (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSystem();
            }
        });
    }


    private void loadSystem(File toRead) throws IOException {
        dep.clearScreen();
        try {
            GraphicalLoader.loadIntoDrawingEditor(toRead, dep);
        } catch (NoGraphicsException e) {
            // TODO: loading of old or CLI generated systems by having placement subroutine
            noGraphicsDisplay();
        }
    }

    private void saveSystem() {
        String filename = JOptionPane.showInputDialog("What would you like to name the system?");
        if (filename != null) {
            File toWrite = new File("./data/" + filename + ".sys");
            try {
                PrintWriter writer = new PrintWriter(toWrite);
                dep.save(writer);
                writer.close();
            } catch (IOException ex) {
                miscIOErrorDisplay();
            }
        }
    }


    private void noGraphicsDisplay() {
        JFrame f = new JFrame("No graphics info");
        JOptionPane.showMessageDialog(f, "Selected file has no graphic info, placement not implemented. Aborting.");
        f.dispose();
    }

    private void miscIOErrorDisplay() {
        JFrame f = new JFrame("File error");
        JOptionPane.showMessageDialog(f, "A filesystem error has been encountered. Aborting.");
        f.dispose();
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


