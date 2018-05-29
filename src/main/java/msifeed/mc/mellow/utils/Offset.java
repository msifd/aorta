package msifeed.mc.mellow.utils;

public class Offset {
    public int top;
    public int right;
    public int bottom;
    public int left;

    public void setTopLeft(int x, int y) {
        this.top = y;
        this.left = x;
    }

    public void set(int a) {
        this.top = a;
        this.right = a;
        this.bottom = a;
        this.left = a;
    }

    public void set(int v, int h) {
        this.top = v;
        this.right = h;
        this.bottom = v;
        this.left = h;
    }

    public void set(int t, int r, int b, int l) {
        this.top = t;
        this.right = r;
        this.bottom = b;
        this.left = l;
    }
}
