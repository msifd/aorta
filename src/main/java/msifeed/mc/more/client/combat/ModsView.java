package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.meta.MetaRpc;
import msifeed.mc.more.crabs.rolls.Modifiers;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;

import java.util.Optional;

class ModsView extends Widget {
    private final EntityLivingBase entity;
    private final MetaInfo meta;
    private final Modifiers modifiers;

    ModsView(EntityLivingBase entity) {
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

        addChild(new Label(L10n.tr("misca.gui.combat.mods.roll")));
        final TextInput modInput = new TextInput();
        modInput.getSizeHint().x = 29;
        if (modifiers.roll != 0)
            modInput.setText(Integer.toString(modifiers.roll));
        modInput.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
        modInput.setCallback(this::updateRollMod);
        addChild(modInput);

        for (final Ability a : Ability.values()) {
            addChild(new Label(a.toString() + ":"));
            final TextInput input = new TextInput();
            input.getSizeHint().x = 20;
            final int modValue = modifiers.features.getOrDefault(a, 0);
            if (modValue != 0)
                input.setText(Integer.toString(modValue));
            input.setFilter(s -> s.length() < 5 && TextInput.isSignedInt(s));
            input.setCallback(s -> updateFeatMods(a, s));
            addChild(input);
        }
    }

    private void updateRollMod(String s) {
        modifiers.roll = (s.isEmpty() || s.equals("-")) ? 0 : Integer.parseInt(s);
        syncModifiers();
    }

    private void updateFeatMods(Ability a, String s) {
        if (s.isEmpty() || s.equals("-"))
            modifiers.features.remove(a);
        else
            modifiers.features.put(a, Integer.parseInt(s));
        syncModifiers();
    }

    private void syncModifiers() {
        MetaRpc.updateMeta(entity.getEntityId(), meta);
    }
}