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

    public interface Click extends Press, Release {
        void onClick(int xMouse, int yMouse, int button);

        @Override
        default void onPress(int xMouse, int yMouse, int button) {
        }

        @Override
        default void onRelease(int xMouse, int yMouse, int button) {
        }
    }

    public interface AllBasic extends Press, Move, Release {
    }

    public interface All extends AllBasic, Click {
    }
}
