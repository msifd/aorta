package msifeed.mc.aorta.client.gui.morph;

import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ScreenMorph extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private Character character;

    private final Widget content;

    public ScreenMorph(EntityLivingBase entity) {
        this.entity = entity;
        this.character = CharacterAttribute.get(entity).map(Character::new).orElse(null);

        final Window window = new Window();
        window.setTitle("Morph: " + entity.getCommandSenderName());
        scene.addChild(window);

        content = window.getContent();
        content.setLayout(ListLayout.HORIZONTAL);

        refill();
    }

    private void refill() {
        content.clearChildren();

        final Widget mainColumn = new Widget();
        mainColumn.setLayout(ListLayout.VERTICAL);
        mainColumn.getSizeHint().x = 100;
        content.addChild(mainColumn);

        if (character == null) {
            final Button addDataBtn = new ButtonLabel("Add data");
            addDataBtn.setClickCallback(() -> {
                character = new Character();
                refill();
            });
            mainColumn.addChild(addDataBtn);
        } else {
            mainColumn.addChild(new EditParamsView(character));
            if (!(entity instanceof EntityPlayer))
                mainColumn.addChild(new ClearDataButton());
        }

        final Button submitBtn = new ButtonLabel("Submit");
        submitBtn.setClickCallback(() -> {
            if (character != null)
                CharRpc.updateChar(entity.getEntityId(), character);
        });
        mainColumn.addChild(submitBtn);

        if (character != null) {
            final TabArea tabArea = new TabArea();
            tabArea.getSizeHint().x = 100;
            tabArea.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.PREFERRED);
            content.addChild(tabArea);

            tabArea.addTab("Features", new EditFeaturesView(character));
            tabArea.addTab("Body", new EditBodypartsView(character));
        }
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        if (!entity.isEntityAlive()) {
            closeGui();
            return;
        }
        super.drawScreen(xMouse, yMouse, tick);
    }

    private class ClearDataButton extends ButtonLabel {
        ClearDataButton() {
            super("Clear all data");
            setClickCallback(() -> {
                CharRpc.clearEntity(entity.getEntityId());
                character = null;
                refill();
            });
        }
    }
}
