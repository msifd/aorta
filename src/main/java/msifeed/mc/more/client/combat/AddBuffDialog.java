package msifeed.mc.more.client.combat;

import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.action.effects.ActionEffects;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.action.parser.EffectStringParser;
import msifeed.mc.more.crabs.combat.CombatRpc;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;

class AddBuffDialog extends Window {
    private final EntityLivingBase entity;
    private final ButtonLabel submitBtn = new ButtonLabel("Submit");

    AddBuffDialog(Widget origin, EntityLivingBase entity) {
        super(origin);
        this.entity = entity;

        final Buff buff = new Buff();
        buff.effect = new ActionEffects.Damage();

        setTitle("Add buff");
        setFocused(this);

        final Widget content = getContent();
        content.setLayout(new GridLayout());

        content.addChild(new Label("Pause"));
        final TextInput pauseInput = new TextInput();
        pauseInput.setText(String.valueOf(buff.pause));
        pauseInput.setFilter(TextInput::isUnsignedInt);
        pauseInput.setCallback(s -> buff.pause = Integer.parseInt(s));
        content.addChild(pauseInput);

        content.addChild(new Label("Steps"));
        final TextInput stepsInput = new TextInput();
        stepsInput.setText(String.valueOf(buff.steps));
        stepsInput.setFilter(TextInput::isUnsignedInt);
        stepsInput.setCallback(s -> buff.steps = Integer.parseInt(s));
        content.addChild(stepsInput);

        content.addChild(new Label("Merge"));
        final DropList<Buff.MergeMode> mergeDrop = new DropList<>(Arrays.asList(Buff.MergeMode.values()));
        mergeDrop.setSelectCallback(m -> buff.mergeMode = m);
        mergeDrop.selectItem(buff.mergeMode.ordinal());
        content.addChild(mergeDrop);

        content.addChild(new Label("Effect"));
        final TextInput effectInput = new TextInput();
        effectInput.getSizeHint().x = 100;
//        effectInput.setText(buff.effect.encode());
        effectInput.setCallback(s -> {
            try {
                buff.effect = EffectStringParser.parseEffect(s);
                submitBtn.setDisabled(false);
            } catch (Exception e) {
                submitBtn.setDisabled(true);
            }
        });
        content.addChild(effectInput);

        submitBtn.setClickCallback(() -> {
            CombatRpc.addBuff(entity.getEntityId(), buff);
            close();
        });
        submitBtn.setDisabled(true);
        content.addChild(submitBtn);

        final ButtonLabel cancelBtn = new ButtonLabel("Cancel");
        cancelBtn.setClickCallback(() -> close());
        content.addChild(cancelBtn);
    }

    private void close() {
        getParent().removeChild(this);
    }
}
