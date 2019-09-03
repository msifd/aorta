package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.CharStatus;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.Modifiers;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
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
        setLayout(new GridLayout());

        addChild(new Label(L10n.tr("aorta.gui.roller.mods.roll")));
        final TextInput modInput = new TextInput();
        modInput.getSizeHint().x = 29;
        modInput.setText(Integer.toString(modifiers.rollMod));
        modInput.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
        modInput.setCallback(this::updateRollMod);
        addChild(modInput);

        for (Feature f : Feature.values()) {
            addChild(new Label(f.tr() + ":"));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 20;
            input.setText(Integer.toString(modifiers.featureMods.getOrDefault(f, 0)));
            input.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
            input.setCallback(s -> updateFeatMods(f, s));
            addChild(input);
        }
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
