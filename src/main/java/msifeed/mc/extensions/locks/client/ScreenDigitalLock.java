package msifeed.mc.extensions.locks.client;

import msifeed.mc.extensions.locks.LockObject;
import msifeed.mc.extensions.locks.LocksRpc;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.sys.rpc.Rpc;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class ScreenDigitalLock extends MellowGuiScreen {
    private final LockObject lock;

    private String input = "";
    private Mode mode = Mode.NORMAL;

    private HashMap<Character, SquareButton> keyButtons = new HashMap<>();
    private SquareButton keyPressedButton = null;
    private long keyPressedTime = 0;

    private final SquareButton resetBtn;

    public ScreenDigitalLock(LockObject lock) {
        this.lock = lock;

        final Window window = new Window();
        window.setTitle("ZX DigiLock");
        scene.addChild(window);

        final Widget content = window.getContent();

        for (int i = 7; i > 0; i -= 3) {
            final Widget line = new Widget();
            line.setLayout(ListLayout.HORIZONTAL);
            for (int j = 0; j < 3; ++j) {
                final char digit = Character.forDigit(i + j, 10);
                line.addChild(makeKeyResponsiveButton(digit, () -> selectDigit(digit)));
            }
            content.addChild(line);
        }

        final Widget bottomLine = new Widget();
        bottomLine.setLayout(ListLayout.HORIZONTAL);
        bottomLine.addChild(makeKeyResponsiveButton('C', this::clear));
        bottomLine.addChild(makeKeyResponsiveButton('0', () -> selectDigit('0')));

        resetBtn = new SquareButton('R', this::pressReset);
        updateResetBtn();
        bottomLine.addChild(resetBtn);

        content.addChild(bottomLine);
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        super.drawScreen(xMouse, yMouse, tick);

        if (keyPressedButton != null && System.currentTimeMillis() - keyPressedTime > 100) {
            if (Widget.pressedWidget == keyPressedButton)
                Widget.pressedWidget = null;
            keyPressedButton = null;
        }
    }

    @Override
    public void handleKeyboardInput() {
        Keyboard.enableRepeatEvents(false);
        if (!Keyboard.getEventKeyState())
            return;

        final char c = Keyboard.getEventCharacter();
        final int k = Keyboard.getEventKey();

        if (k == Keyboard.KEY_ESCAPE) {
            closeGui();
        } else if (keyButtons.containsKey(c)) {
            final SquareButton b = keyButtons.get(c);
            b.onClick(0, 0, 0);
            Widget.pressedWidget = b;
            keyPressedButton = b;
            keyPressedTime = System.currentTimeMillis();
        }
    }

    private SquareButton makeKeyResponsiveButton(char c, Runnable cb) {
        final SquareButton b = new SquareButton(c, cb);
        keyButtons.put(c, b);
        if (c == 'C') {
            keyButtons.put('c', b); // lower case
            keyButtons.put('С', b); // Russian
            keyButtons.put('с', b); // Russian lower case
        }
        return b;
    }

    private void selectDigit(char digit) {
        input += digit;
        switch (mode) {
            case NORMAL:
                unlock();
                break;
            case RESET_CHECK:
                if (lock.canUnlockWith(input)) {
                    input = "";
                    mode = Mode.RESET_INPUT;
                    updateResetBtn();
                }
                break;
        }
    }

    private void pressReset() {
        if (lock.isLocked())
            return;

        switch (mode) {
            case NORMAL:
                input = "";
                mode = Mode.RESET_CHECK;
                updateResetBtn();
                break;
            case RESET_INPUT:
                final TileEntity te = lock.getTileEntity();
                Rpc.sendToServer(LocksRpc.resetDigital, te.xCoord, te.yCoord, te.zCoord, input);
                lock.setSecret(input); // Update client side early
                clear();
                break;
        }
    }

    private void clear() {
        input = "";
        mode = Mode.NORMAL;
        updateResetBtn();
    }

    private void unlock() {
        if (lock.canUnlockWith(input)) {
            final TileEntity te = lock.getTileEntity();
            Rpc.sendToServer(LocksRpc.toggleDigital, te.xCoord, te.yCoord, te.zCoord, input);
            closeGui();
        }
    }

    private void updateResetBtn() {
        resetBtn.setDisabled(lock.isLocked() || mode == Mode.RESET_CHECK);
    }

    private enum Mode {
        NORMAL, RESET_CHECK, RESET_INPUT
    }

    private static class SquareButton extends ButtonLabel {
        SquareButton(char c, Runnable cb) {
            super(String.valueOf(c));
            setSizeHint(20, 20);
            setSizePolicy(SizePolicy.FIXED);
            setClickCallback(cb);
        }
    }
}
