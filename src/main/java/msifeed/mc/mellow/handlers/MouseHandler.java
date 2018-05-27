package msifeed.mc.mellow.handlers;

public final class MouseHandler {
    public interface Press {
        void onPress(int xMouse, int yMouse, int button);
    }

    public interface Move {
        void onMove(int xMouse, int yMouse, int button);
    }

    public interface Release {
        void onRelease(int xMouse, int yMouse, int button);
    }

    public interface Click {
        void onClick(int xMouse, int yMouse, int button);
    }
}
