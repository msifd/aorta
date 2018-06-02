package msifeed.mc.mellow.utils;

public class Rect implements Cloneable {
    public int x;
    public int y;
    public int w;
    public int h;

    public Rect() {
    }

    public Rect(int x, int y, int w, int h) {
        set(x, y, w, h);
    }

    public Rect(Rect rect) {
        set(rect);
    }

    public void set(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void set(Rect r) {
        this.x = r.x;
        this.y = r.y;
        this.w = r.w;
        this.h = r.h;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void translate(Point p) {
        translate(p.x, p.y);
    }

    public void translate(Rect p) {
        translate(p.x, p.y);
    }

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void setSize(Point p) {
        this.w = p.x;
        this.h = p.y;
    }

    public void offset(Margins o) {
        this.x += o.left;
        this.y += o.top;
        this.w -= o.left + o.right;
        this.h -= o.top + o.bottom;
    }

    public boolean contains(Point p) {
        return p.x >= this.x && p.x <= this.x + this.w
                && p.y >= this.y && p.y <= this.y + this.h;
    }
}
