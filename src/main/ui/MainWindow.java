package ui;

import javax.swing.*;

public class MainWindow extends JFrame {

    private void initUI() {
        //DrawingPannel dp = new DrawingPannel();
        //add(dp);

        setSize(300, 500);
        setTitle("Drawing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }



    public MainWindow() {
        initUI();
    }
}
