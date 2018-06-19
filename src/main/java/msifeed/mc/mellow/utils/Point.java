package msifeed.mc.mellow.utils;

public class Point {
    public int x;
    public int y;

    public Point() {
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void translate(Point p) {
        translate(p.x, p.y);
    }
}
