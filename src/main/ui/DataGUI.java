package ui;

import javax.swing.*;
import java.awt.*;

// ALl constants for the GUI
public interface DataGUI {
    // COLOUR SCHEME, picked from the theme. TODO: Get this right from the theme .jar?
    public static final Color LGT_GREY =    new Color(0xbbbbbb);
    public static final Color MED_GREY =    new Color(0x3c3f41);
    public static final Color DRK_GREY =    new Color(0x303234);


    // TOOLBAR BUTTON INFORMATION
    public static int TOOLBAR_BUTTON_SIZE =     29; // Size of toolbar buttons in pixels
    public static final Icon TEST_ICON =        new ImageIcon("./res/icons/toolbar/AddPoint3.png");
    public static final Icon TEST_ICON2 =        new ImageIcon("./res/icons/toolbar/AddLine2.png");
    public static final Icon TEST_ICON3 =        new ImageIcon("./res/icons/toolbar/ppdst1.png");
    public static final Icon TEST_ICON4 =        new ImageIcon("./res/icons/toolbar/pset1.png");

}
