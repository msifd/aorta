package msifeed.mc.aorta.client.gui.roller;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.meta.MetaRpc;
import msifeed.mc.aorta.core.rolls.Modifiers;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

class ModifiersView extends Widget {
    private final EntityLivingBase entity;
    private final MetaInfo meta;
    private final Modifiers modifiers;

    ModifiersView(EntityLivingBase entity) {
        this.entity = entity;

        final Optional<MetaInfo> metaOpt = MetaAttribute.get(entity);
        if (metaOpt.isPresent()) {
            meta = metaOpt.get();
            modifiers = meta.modifiers;
        } else {
            meta = null;
            modifiers = null;
            return;
        }

        setLayout(new GridLayout());
        getSizeHint().x = 120;

        addChild(new Label(L10n.tr("aorta.gui.roller.mods.roll")));
        final TextInput modInput = new TextInput();
        modInput.getSizeHint().x = 29;
        if (modifiers.rollMod != 0)
            modInput.setText(Integer.toString(modifiers.rollMod));
        modInput.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
        modInput.setCallback(this::updateRollMod);
        addChild(modInput);

        for (final Feature f : Feature.mainFeatures()) {
            addChild(new Label(f.tr() + ":"));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 20;
            final int modValue = modifiers.featureMods.getOrDefault(f, 0);
            if (modValue != 0)
                input.setText(Integer.toString(modValue));
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
            System.out.println("remove " + f.trShort());
            modifiers.featureMods.remove(f);
        }
        else {
            System.out.println("update " + f.trShort());
            modifiers.featureMods.put(f, Integer.parseInt(s));
        }
        syncModifiers();
    }

    private void syncModifiers() {
        MetaRpc.updateMeta(entity.getEntityId(), meta);
    }
}
