package msifeed.mc.aorta.client.gui;

import msifeed.mc.aorta.locks.DigitalLockAction;
import msifeed.mc.aorta.locks.DigitalLockMessage;
import msifeed.mc.aorta.locks.LockTileEntity;
import msifeed.mc.aorta.locks.Locks;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.client.Minecraft;

public class ScreenDigitalLock extends MellowGuiScreen {
    private final LockTileEntity lock;
    private final DigitalLockAction action;
    private String input = "";

    public ScreenDigitalLock(LockTileEntity lock, DigitalLockAction action) {
        this.lock = lock;
        this.action = action;

        final Window window = new Window();
        window.setTitle("DigiLock");
        scene.addChild(window);

        final Widget content = window.getContent();

        for (int i = 0; i < 3; ++i) {
            final Widget line = new Widget();
            line.setLayout(ListLayout.HORIZONTAL);
            for (int j = 0; j < 3; ++j) {
                final String digit = String.valueOf(i + j + 1);
                final ButtonLabel btn = new ButtonLabel(digit);
                btn.setClickCallback(() -> selectDigit(digit));
                line.addChild(btn);
            }
            content.addChild(line);
        }

        final Widget bottomLine = new Widget();
        bottomLine.setLayout(ListLayout.HORIZONTAL);

        final ButtonLabel clearBtn = new ButtonLabel("C");
        clearBtn.setClickCallback(this::clearInput);
        bottomLine.addChild(clearBtn);

        final ButtonLabel zeroBtn = new ButtonLabel("0");
        zeroBtn.setClickCallback(() -> selectDigit("0"));
        bottomLine.addChild(zeroBtn);

        final ButtonLabel resetBtn = new ButtonLabel("R");
//        resetBtn.setClickCallback(() -> selectDigit("R"));
        bottomLine.addChild(resetBtn);

        content.addChild(bottomLine);
    }

    private void selectDigit(String digit) {
        input += digit;
        unlock();
    }

    private void clearInput() {
        input = "";
        unlock();
    }

    private void unlock() {
        if (lock.canUnlockWith(input)) {
            final DigitalLockMessage m = new DigitalLockMessage(lock, action, input);
            Locks.INSTANCE.CHANNEL.sendToServer(m);
            closeScreen();
        }
    }

    private void closeScreen() {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
