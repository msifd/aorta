package msifeed.mc.mellow.utils;

public class Rect implements Cloneable {
    public int x;
    public int y;
    public int w;
    public int h;

    public void set(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void translate(Rect p) {
        this.x += p.x;
        this.y += p.y;
    }

    public void size(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void resize(int w, int h) {
        this.w += w;
        this.h += h;
    }

    public void resize(Point p) {
        this.w += p.x;
        this.h += p.y;
    }
    
    public boolean contains(Point p) {
        return p.x >= this.x && p.x <= this.x + this.w
                && p.y >= this.y && p.y <= this.y + this.h;
    }
}
