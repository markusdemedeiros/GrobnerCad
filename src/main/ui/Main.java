package ui;

import ui.gui.mainWindow.MainWindow;

public class Main {
    public static void main(String[] args) {
        //SolverApp application = new SolverApp();

        MainWindow mw = new MainWindow();
        mw.setLocationRelativeTo(null);
        mw.setVisible(true);
    }
}
