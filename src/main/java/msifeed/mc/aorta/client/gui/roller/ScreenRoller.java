package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.input.TextInput;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenRoller extends MellowGuiScreen {
    static int lastModifier = 0;
    static long lastRolled = 0;

    public ScreenRoller(EntityLivingBase entity) {
        final Window window = new Window();
        window.setTitle(L10n.tr("aorta.gui.roller"));
        scene.addChild(window);

        final Widget windowContent = window.getContent();

        final TabArea tabArea = new TabArea();
        windowContent.addChild(tabArea);

        tabArea.addTab("Feats", new FeatureRollsView(entity));
        tabArea.addTab("Fight", new FightRollView(entity));
        tabArea.addTab("Other", new OtherRollView(entity));

        final Widget modifiers = new Widget();
        modifiers.setLayout(new GridLayout());
        windowContent.addChild(modifiers);

        modifiers.addChild(new Label("Modifier:"));

        final TextInput modInput = new TextInput();
        modInput.getSizeHint().x = 29;
        if (lastModifier != 0)
            modInput.setText(Integer.toString(lastModifier));
        modInput.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
        modInput.setCallback(this::parseNumber);
        modifiers.addChild(modInput);
    }

    private void parseNumber(String s) {
        lastModifier = (s.isEmpty() || s.equals("-")) ? 0 : Integer.parseInt(s);
    }
}
