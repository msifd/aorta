package msifeed.mc.aorta.client.gui.chareditor;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;

    private final Widget content;
    private final TabArea tabArea = new TabArea();
    private final Button submitBtn = new ButtonLabel("Submit");

    private Character character = null;
    private CharStatus charStatus = null;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle("Char edit: " + entity.getCommandSenderName());
        scene.addChild(window);

        content = window.getContent();
        content.setLayout(ListLayout.VERTICAL);

        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            else if (character != null && charStatus != null) {
                charStatus.cleanup(character);
                CharacterAttribute.INSTANCE.set(entity, character);
                StatusAttribute.INSTANCE.set(entity, charStatus);
            }
        });

        refill();
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        if (!entity.isEntityAlive()) {
            closeGui();
            return;
        }
        super.drawScreen(xMouse, yMouse, tick);
    }

    private void refill() {
        CharacterAttribute.get(entity).ifPresent(c -> this.character = new Character(c));
        StatusAttribute.get(entity).ifPresent(s -> this.charStatus = new CharStatus(s));

        content.clearChildren();
        if (character != null && charStatus != null) {
            tabArea.clearChildren();
            tabArea.addTab("Params", new EditParamsView(character));
            tabArea.addTab("Feats", new EditFeaturesView(character));
            tabArea.addTab("Body", new EditBodypartsView(character, charStatus));
            content.addChild(tabArea);

            content.addChild(new Separator());
            if (!(entity instanceof EntityPlayer))
                content.addChild(new ClearDataButton());
            content.addChild(submitBtn);
        } else {
            final Button addDataBtn = new ButtonLabel("Add data");
            addDataBtn.setClickCallback(() -> {
                character = new Character();
                charStatus = new CharStatus();
                refill();
            });
            content.addChild(addDataBtn);
        }
    }

    private class ClearDataButton extends ButtonLabel {
        ClearDataButton() {
            super("Clear all data");
            setClickCallback(() -> {
                CharacterAttribute.INSTANCE.set(entity, null);
                StatusAttribute.INSTANCE.set(entity, null);
                character = null;
                charStatus = null;
                refill();
            });
        }
    }
}
