package ui.gui.mainwindow;

import model.geometric.Geometry;
import ui.gui.mainwindow.component.Drawable;

public class LineGraphicInfo extends GraphicInfo {
    private String p0;
    private String p1;

    public LineGraphicInfo(String name, String p0, String p1) {
        super(name, Geometry.TYPE_LINE);
        this.p0 = p0;
        this.p1 = p1;
    }

    public static LineGraphicInfo read(String name, String content) {
        return new LineGraphicInfo(name, readp0(content), readp1(content));
    }

    @Override
    protected String getContentString() {
        return p0 + "," + p1;
    }


    private static String readp0(String content) {
        return content.substring(1, content.indexOf(","));
    }

    private static String readp1(String content) {
        return content.substring(content.indexOf(",") + 1);
    }

    public String getp0() {
        return p0;
    }

    public String getp1() {
        return p1;
    }
}
