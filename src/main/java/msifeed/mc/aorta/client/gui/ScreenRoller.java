package msifeed.mc.aorta.client.gui;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.net.RollRequests;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.TextInput;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;

public class ScreenRoller extends MellowGuiScreen {
    private static int lastModifier = 0;
    private static long lastRolled = 0;

    public ScreenRoller() {
        final Window window = new Window();
        window.setTitle("Dice Roller");
        scene.addChild(window);

        final Widget windowContent = window.getContent();

        final Widget buttons = new Widget();
        buttons.setLayout(new GridLayout());
        windowContent.addChild(buttons);

        for (Feature f : Feature.values()) {
            final ButtonLabel b = new ButtonLabel(f.toString());
            b.setClickCallback(() -> roll(f, lastModifier));
            buttons.addChild(b);
        }

        final Widget modifiers = new Widget();
        modifiers.setLayout(new GridLayout());
        windowContent.addChild(modifiers);

        modifiers.addChild(new Label("Modifier:"));

        final TextInput modInput = new TextInput();
        modInput.getSizeHint().x = 29;
        if (lastModifier != 0)
            modInput.setText(Integer.toString(lastModifier));
        modInput.setFilter(s -> s.length() < 5 && TextInput.isDigitPart(s));
        modInput.setCallback(this::parseNumber);
        modifiers.addChild(modInput);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void roll(Feature feature, int mod) {
        if (System.currentTimeMillis() - lastRolled < 1000)
            return;

        RollRequests.rollFeature(feature, mod);
        lastRolled = System.currentTimeMillis();
    }

    private void parseNumber(String s) {
        lastModifier = (s.isEmpty() || s.equals("-")) ? 0 : Integer.parseInt(s);
    }
}
