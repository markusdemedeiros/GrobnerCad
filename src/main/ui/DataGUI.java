package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

// ALl constants for the GUI
public interface DataGUI {
    // COLOUR SCHEME
    // These values are grabbed directly from the theme I've chosen
    Color WHITE    = new Color(0xadadad);
    Color LGT_GREY = new Color(0xbbbbbb);
    Color MLT_GREY = new Color(0x45494a);
    Color MED_GREY = new Color(0x3c3f41);
    Color DRK_GREY = new Color(0x303234);


    // TOOLBAR BUTTONS
    int TOOLBAR_BUTTON_SIZE = 29; // Size of toolbar buttons in pixels
    Icon TEST_ICON = new ImageIcon("./res/icons/toolbar/AddPoint3.png");
    Icon TEST_ICON2 = new ImageIcon("./res/icons/toolbar/AddLine2.png");
    Icon TEST_ICON3 = new ImageIcon("./res/icons/toolbar/ppdst1.png");
    Icon TEST_ICON4 = new ImageIcon("./res/icons/toolbar/pset1.png");

    // BACKGROUND IMAGE
    File backgroundImageFile = new File("./res/210Background3.png");

    // LINE DRAWING
    Stroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
    Stroke bigStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);

    // CLICK SENSITIVITY
    int CLICK_TOLERANCE = 10;
    int CLICK_SENS_TOLERANCE = 2;

    int ORIGIN_SIZE = 5;

}
