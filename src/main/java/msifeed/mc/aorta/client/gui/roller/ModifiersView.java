package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.Modifiers;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.WrapWidget;
import msifeed.mc.mellow.widgets.input.TextInput;
import msifeed.mc.mellow.widgets.spoiler.Spoiler;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

class ModifiersView extends Widget {
    private final EntityLivingBase entity;
    private final Modifiers modifiers;

    ModifiersView(EntityLivingBase entity) {
        this.entity = entity;
        final Optional<CharStatus> optStatus = StatusAttribute.get(entity);
        if (optStatus.isPresent()) {
            modifiers = optStatus.get().modifiers;
        } else {
            modifiers = null;
            return;
        }
        setLayout(ListLayout.VERTICAL);

        final Widget rollMod = new Widget();
        rollMod.setLayout(new GridLayout());
        rollMod.addChild(new Label("Roll mod:"));
        final TextInput modInput = new TextInput();
        modInput.getSizeHint().x = 29;
        modInput.setText(Integer.toString(modifiers.rollMod));
        modInput.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
        modInput.setCallback(this::updateRollMod);
        rollMod.addChild(modInput);
        addChild(new WrapWidget(rollMod));

        final Widget featMods = new Widget();
        featMods.setLayout(new GridLayout());
        for (Feature f : Feature.values()) {
            featMods.addChild(new Label(f.toString() + ":"));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 29;
            input.setText(Integer.toString(modifiers.featureMods.getOrDefault(f, 0)));
            input.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
            input.setCallback(s -> updateFeatMods(f, s));
            featMods.addChild(input);
        }
        addChild(new Spoiler("Feat mods", featMods));
    }

    private void updateRollMod(String s) {
        modifiers.rollMod = (s.isEmpty() || s.equals("-")) ? 0 : Integer.parseInt(s);
        syncModifiers();
    }

    private void updateFeatMods(Feature f, String s) {
        if (s.isEmpty() || s.equals("-")) {
            modifiers.featureMods.remove(f);
        } else {
            modifiers.featureMods.put(f, Integer.parseInt(s));
        }
        syncModifiers();
    }

    private void syncModifiers() {
        System.out.println("send mods");
        RollRpc.updateMods(entity.getEntityId(), modifiers);
    }
}
