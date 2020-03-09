package ui;

import model.algebraic.*;
import model.geometric.Geometry;
import model.geometric.Point;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

// ALl constants for the GUI
public interface DataGUI {
    // COLOUR SCHEME, picked from the theme. TODO: Get this right from the theme .jar?
    Color LGT_GREY = new Color(0xbbbbbb);
    Color MED_GREY = new Color(0x3c3f41);
    Color DRK_GREY = new Color(0x303234);


    // TOOLBAR BUTTON INFORMATION
    int TOOLBAR_BUTTON_SIZE = 29; // Size of toolbar buttons in pixels
    Icon TEST_ICON = new ImageIcon("./res/icons/toolbar/AddPoint3.png");
    Icon TEST_ICON2 = new ImageIcon("./res/icons/toolbar/AddLine2.png");
    Icon TEST_ICON3 = new ImageIcon("./res/icons/toolbar/ppdst1.png");
    Icon TEST_ICON4 = new ImageIcon("./res/icons/toolbar/pset1.png");

}
