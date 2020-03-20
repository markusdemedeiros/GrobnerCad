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
    Icon ADDPOINT_ICON = new ImageIcon("./res/icons/toolbar/AddPoint3.png");
    Icon ADDLINE_ICON = new ImageIcon("./res/icons/toolbar/AddLine2.png");
    Icon PPDST_ICON = new ImageIcon("./res/icons/toolbar/ppdst1.png");
    Icon PPCOINC_ICON = new ImageIcon("./res/icons/toolbar/pset1.png");
    Icon PPVERT_ICON = new ImageIcon("./res/icons/toolbar/ppvert.png");
    Icon PPHORIZ_ICON = new ImageIcon("./res/icons/toolbar/pphoriz.png");
    Icon PSETX_ICON = new ImageIcon("./res/icons/toolbar/psetx.png");
    Icon PSETY_ICON = new ImageIcon("./res/icons/toolbar/psety.png");

    // BACKGROUND IMAGE
    File backgroundImageFile = new File("./res/210Background3.png");

    // LINE DRAWING
    int BIG_STROKE_WIDTH = 3;
    Stroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[]{9}, 0);
    Stroke bigStroke = new BasicStroke(BIG_STROKE_WIDTH, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
    Stroke bigDashedStroke =  new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[]{9}, 0);

    // CONSTRAINT DRAWING
    int CONSTRAINT_SIZE = 10;       // Size of constraint (square of size length 20)
    int CONSTRAINT_OFFSET = 8;      // Offset from line to constraint
    int CONSTRAINT_INSET = 2;       // Inset from constraint bounding box to draw icon
    int CONSTRAINT_TEXT_SPACE = 5;  // Space between constraint label and text, if applicible
    int CONSTRAINT_OFFSET_POINT = 6;      // Offset from line to constraint


    // CLICK SENSITIVITY
    int CLICK_TOLERANCE = 10;
    int CLICK_SENS_TOLERANCE = 1;

    int ORIGIN_SIZE = 5;

    int POINT_RADIUS = 5;


}
