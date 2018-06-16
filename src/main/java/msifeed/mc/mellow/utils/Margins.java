package msifeed.mc.mellow.utils;

public class Margins {
    public int top;
    public int right;
    public int bottom;
    public int left;

    public Margins() {
    }

    public Margins(int all) {
        set(all);
    }

    public void set(int all) {
        this.top = all;
        this.right = all;
        this.bottom = all;
        this.left = all;
    }

    public void set(int h, int v) {
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

    public void set(Margins o) {
        this.top = o.top;
        this.right = o.right;
        this.bottom = o.bottom;
        this.left = o.left;
    }
}
