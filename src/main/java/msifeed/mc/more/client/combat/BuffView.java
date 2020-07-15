package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.FillLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.button.FlatButtonLabel;
import msifeed.mc.mellow.widgets.scroll.ScrollArea;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatRpc;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import net.minecraft.entity.EntityLivingBase;

class BuffView extends Widget {
    private final EntityLivingBase entity;
    private final ScrollArea scroll = new ScrollArea();

    BuffView(EntityLivingBase entity) {
        this.entity = entity;

        setSizeHint(150, 150);
        setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.PREFERRED);
        setLayout(FillLayout.INSTANCE);

        scroll.getMargin().set(1, 0, 0, 1);
        scroll.setSpacing(2);
        addChild(scroll);

        refill();
    }

    public void refill() {
        scroll.clearChildren();

        final CombatContext com = CombatAttribute.get(entity).orElse(null);
        if (com == null)
            return;

        for (int i = 0; i < com.buffs.size(); i++) {
            final int index = i;
            final Buff buff = com.buffs.get(index);
            final FlatButtonLabel btn = new FlatButtonLabel(buff.encode());
            btn.setClickCallback(() -> getTopParent().addChild(new RemoveBuffDialog(this, entity, index)));
            scroll.addChild(btn);
        }

        final ButtonLabel addBuff = new ButtonLabel("Add buff");
        addBuff.setClickCallback(() -> getTopParent().addChild(new AddBuffDialog(this, entity)));
        scroll.addChild(addBuff);
    }

    private static class RemoveBuffDialog extends Window {
        RemoveBuffDialog(Widget origin, EntityLivingBase entity, int index) {
            super(origin);

            setTitle("Remove buff?");
            setFocused(this);

            final Widget content = getContent();
            content.setLayout(ListLayout.HORIZONTAL);

            final ButtonLabel removeBtn = new ButtonLabel("Remove!");
            removeBtn.setClickCallback(() -> {
                CombatRpc.removeBuff(entity.getEntityId(), index);
                close();
            });
            content.addChild(removeBtn);

            final ButtonLabel cancelBtn = new ButtonLabel("No, plz mercy!");
            cancelBtn.setClickCallback(() -> close());
            content.addChild(cancelBtn);
        }

        private void close() {
            getParent().removeChild(this);
        }
    }
}
