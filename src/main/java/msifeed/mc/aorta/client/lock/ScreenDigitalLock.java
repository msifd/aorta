package msifeed.mc.aorta.client.lock;

import msifeed.mc.aorta.locks.LockObject;
import msifeed.mc.aorta.locks.LocksRpc;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public class ScreenDigitalLock extends MellowGuiScreen {
    private final LockObject lock;

    private String input = "";
    private boolean resetMode = false;

    private HashMap<Character, SquareButton> keyButtons = new HashMap<>();
    private SquareButton keyPressedButton = null;
    private long keyPressedTime = 0;

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
        bottomLine.addChild(makeKeyResponsiveButton('C', this::pressClear));
        bottomLine.addChild(makeKeyResponsiveButton('0', () -> selectDigit('0')));
        bottomLine.addChild(new SquareButton('R', this::pressReset));

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
            closeScreen();
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
        if (!resetMode)
            unlock();
    }

    private void pressClear() {
        input = "";
    }

    private void pressReset() {
        if (lock.isLocked())
            return;
        if (resetMode) {
            if (!input.isEmpty()) {
                lock.setSecret(input); // Update client side early
                sendActionRequest();
                resetMode = false;
            }
        } else {
            resetMode = true;
        }
        input = "";
    }

    private void unlock() {
        if (lock.canUnlockWith(input)) {
            sendActionRequest();
            closeScreen();
        }
    }

    private void sendActionRequest() {
        final TileEntity te = lock.getTileEntity();
        if (resetMode)
            Rpc.sendToServer(LocksRpc.resetDigital, te.xCoord, te.yCoord, te.zCoord, input);
        else
            Rpc.sendToServer(LocksRpc.toggleDigital, te.xCoord, te.yCoord, te.zCoord, input);
    }

    private void closeScreen() {
        Minecraft.getMinecraft().displayGuiScreen(null);
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
