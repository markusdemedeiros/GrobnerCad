package ui.gui.mainwindow.graphicpersistence;

import model.geometric.Geometry;
import ui.gui.mainwindow.component.Drawable;
import ui.gui.mainwindow.component.GraphicalPoint;

// Represents a named Point with graphic information
public class PointGraphicInfo extends GraphicInfo  {
    private int coordx;
    private int coordy;

    public PointGraphicInfo(String name, int coordx, int coordy) {
        super(name, Geometry.TYPE_POINT);
        this.coordx = coordx;
        this.coordy = coordy;
    }

    public static PointGraphicInfo read(String name, String content) {
        return new PointGraphicInfo(name, readx(content), ready(content));
    }

    public int getCoordx() {
        return coordx;
    }

    public int getCoordy() {
        return coordy;
    }

    @Override
    protected String getContentString() {
        return coordx + "," + coordy;
    }

    public Drawable generate() {
        GraphicalPoint output = new GraphicalPoint();
        output.setOffset(coordx, coordy);
        return output;
    }

    private static int readx(String content) {
        return Integer.parseInt(content.substring(1, content.indexOf(",")));
    }

    private static int ready(String content) {
        return Integer.parseInt(content.substring(content.indexOf(",") + 1));
    }


}
