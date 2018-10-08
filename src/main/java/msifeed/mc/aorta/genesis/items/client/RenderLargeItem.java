package msifeed.mc.aorta.genesis.items.client;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderLargeItem implements IItemRenderer {
    private boolean renderFlag = false;

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return !renderFlag && (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        renderFlag = true;
        GL11.glPushMatrix();
        GL11.glScalef(1.5f, 1.5f, 1.5f);

        GL11.glRotatef(335.0F, 0.0F, 0.2F, -1.0F);
        GL11.glTranslatef(0.7375F, 0.0F, 0.0F);
        GL11.glRotatef(50.0F, 0.0F, -1.0F, 0.0F);


        final EntityLivingBase entity = (EntityLivingBase) data[1];
        if (item.getItem().requiresMultipleRenderPasses()) {
            for (int i = 0; i < item.getItem().getRenderPasses(item.getItemDamage()); ++i)
                renderItem(item, entity, i);
        } else {
            renderItem(item, entity, 0);
        }

        GL11.glPopMatrix();
        renderFlag = false;
    }

    private void renderItem(ItemStack item, EntityLivingBase entity, int pass) {
        final int c = item.getItem().getColorFromItemStack(item, pass);
        final float f12 = (c >> 16 & 255) / 255.0F;
        final float f3 = (c >> 8 & 255) / 255.0F;
        final float f4 = (c & 255) / 255.0F;
        GL11.glColor4f(f12, f3, f4, 1.0F);
        RenderManager.instance.itemRenderer.renderItem(entity, item, pass);
    }
}
