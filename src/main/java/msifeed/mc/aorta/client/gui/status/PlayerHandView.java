package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.CharHand;
import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.render.RenderItemStack;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.render.RenderUtils;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

public class PlayerHandView extends Widget {
    private final EntityPlayer player;

    PlayerHandView(EntityPlayer player) {
        this.player = player;

        setSizeHint(18 * 9, 18 * 2 + 2);
        setSizePolicy(SizePolicy.FIXED);
        setLayout(FreeLayout.INSTANCE);

        CharRpc.requestHand(player.getEntityId());

        int offsetX = 0;
        for (int i = 0; i < 9; i++) {
            final ItemPreview ip = new HandItemPreview(i);
            ip.setPos(offsetX, 0);
            addChild(ip);
            offsetX += 18;
        }

        offsetX = 0;
        for (int i = 0; i < 4; i++) {
            final ItemPreview ip = new InvItemPreview(i, player.inventory.armorInventory);
            ip.setPos(offsetX, 18);
            addChild(ip);
            offsetX += 18;
        }
    }

    private class HandItemPreview extends ItemPreview {
        HandItemPreview(int index) {
            super(index);
        }

        @Override
        protected ItemStack getItem() {
            return CharHand.getHandItem(player.getEntityId(), index);
        }
    }

    private class InvItemPreview extends ItemPreview {
        private final ItemStack[] inv;

        InvItemPreview(int index, ItemStack[] inv) {
            super(index);
            this.inv = inv;
        }

        @Override
        protected ItemStack getItem() {
            return inv[index];
        }
    }

    private abstract class ItemPreview extends Widget {
        protected final int index;

        ItemPreview(int index) {
            this.index = index;
            setSizeHint(16, 16);
            setSizePolicy(SizePolicy.FIXED);
            setZLevel(1);
        }

        protected abstract ItemStack getItem();

        @Override
        protected void renderSelf() {
            final Geom geom = getGeometry();
            RenderShapes.rect(geom, 0x20394f, 0x4f);

            final ItemStack item = getItem();
            if (item == null)
                return;
            RenderItemStack.itemStack(item, geom.x, geom.y, geom.z);

            if (isHovered()) {
                final Minecraft mc = Minecraft.getMinecraft();
                final int scaleFactor = RenderUtils.getScreenScaleFactor();
                final int displayHeight = mc.displayHeight / scaleFactor;
                RenderItemStack.tooltip(item, player, Mouse.getX() / scaleFactor, displayHeight - Mouse.getY() / scaleFactor, geom.z);
            }
        }
    }
}
