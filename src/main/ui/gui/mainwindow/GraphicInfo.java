package ui.gui.mainwindow;

import model.geometric.Geometry;
import ui.gui.mainwindow.component.Drawable;

public abstract class GraphicInfo {
    protected String name;
    protected String type;

    // This constructor helpful for writes
    public GraphicInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // This "constructor" helpful for reads
    public static GraphicInfo getGraphicFromLine(String readLine) {
        String type = readType(readLine);
        String name = readName(readLine);
        if (type.equals(Geometry.TYPE_POINT)) {
            return PointGraphicInfo.read(name, readContent(readLine));
        } else {
            return LineGraphicInfo.read(name, readContent(readLine));
        }
    }

    public String writeAsString() {
        return "[" + type + ":" + name + ";" + getContentString() + "]";
    }

    private static String readType(String toRead) {
        return toRead.substring(1, toRead.indexOf(":"));
    }

    private static String readName(String toRead) {
        return toRead.substring(toRead.indexOf(":") + 1, toRead.indexOf(";"));
    }

    private static String readContent(String toRead) {
        return toRead.substring(toRead.indexOf(";"), toRead.indexOf("]"));
    }

    protected abstract String getContentString();

    public String getName() {
        return name;
    }

}
