package msifeed.mc.more.client.gui.morph;

import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.Button;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;
import msifeed.mc.more.crabs.character.CharRpc;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.meta.MetaRpc;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ScreenMorph extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private Character character;
    private MetaInfo metaInfo;

    private final Widget content;

    public ScreenMorph(EntityLivingBase entity) {
        this.entity = entity;
        this.character = CharacterAttribute.get(entity).map(Character::new).orElse(null);
        this.metaInfo = MetaAttribute.get(entity).map(MetaInfo::new).orElse(null);

        final String name = character == null || character.name.isEmpty() ? entity.getCommandSenderName() : character.name;

        final Window window = new Window();
        window.setTitle("Morpher: " + name);
        scene.addChild(window);

        content = window.getContent();
        content.setLayout(new ListLayout(ListLayout.Direction.HORIZONTAL, 2));

        refill();
    }

    private void refill() {
        content.clearChildren();

        final Widget mainColumn = new Widget();
        mainColumn.setLayout(ListLayout.VERTICAL);
        content.addChild(mainColumn);

        if (character == null) {
            final Button addDataBtn = new ButtonLabel("Add data");
            addDataBtn.setClickCallback(() -> {
                character = new Character();
                metaInfo = new MetaInfo();
                refill();
            });
            mainColumn.addChild(addDataBtn);
        } else {
            mainColumn.addChild(new EditParamsView(character));

            if (!(entity instanceof EntityPlayer)) {
                final ButtonLabel clearBtn = new ButtonLabel("Clear all data");
                clearBtn.setClickCallback(() -> {
                    character = null;
                    metaInfo = null;
                    refill();
                });
                mainColumn.addChild(clearBtn);
            }
        }

        final Button submitBtn = new ButtonLabel("Submit");
        submitBtn.setClickCallback(() -> {
            if (character != null && metaInfo != null) {
                CharRpc.updateChar(entity.getEntityId(), character);
                MetaRpc.updateMeta(entity.getEntityId(), metaInfo);
            } else {
                CharRpc.clearEntity(entity.getEntityId());
            }
        });
        mainColumn.addChild(submitBtn);
    }

    @Override
    public void drawScreen(int xMouse, int yMouse, float tick) {
        if (!entity.isEntityAlive()) {
            closeGui();
            return;
        }
        super.drawScreen(xMouse, yMouse, tick);
    }
}
