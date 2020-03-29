package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.EntityLivingBase;

public class RollAbilityView extends Widget {
    private final EntityLivingBase entity;

    RollAbilityView(EntityLivingBase entity) {
        this.entity = entity;

        setLayout(new GridLayout());

        refill();
    }

    private void refill() {
        if (!CharacterAttribute.get(entity).isPresent())
            return;

        for (Ability ability : Ability.values()) {
            final String label = L10n.tr("more.ability.short." + ability.name().toLowerCase());
            final ButtonLabel btn = new ButtonLabel(label);
            btn.setClickCallback(() -> {
                CharRpc.rollAbility(entity.getEntityId(), ability);
            });
            addChild(btn);
        }
    }
}
