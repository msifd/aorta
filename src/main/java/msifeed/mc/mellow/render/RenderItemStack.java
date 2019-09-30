package msifeed.mc.mellow.render;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.theme.Part;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

public class RenderItemStack {
    private static final RenderItem itemRender = MellowGuiScreen.getRenderItem();

    public static void itemStack(ItemStack itemStack, int x, int y, int z) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        itemRender.zLevel = z + 50;

        FontRenderer font = null;
        if (itemStack != null)
            font = itemStack.getItem().getFontRenderer(itemStack);
        if (font == null)
            font = Minecraft.getMinecraft().fontRenderer;

        final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        itemRender.renderItemAndEffectIntoGUI(font, textureManager, itemStack, x, y);
//        itemRender.renderItemOverlayIntoGUI(font, textureManager, itemStack, x, y -  8, null);

        itemRender.zLevel = z;

        OpenGlHelper.glBlendFunc(770, 771, 1, 0); // fix after item glint
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    public static void tooltip(ItemStack itemStack, EntityPlayer player, int x, int y, int z) {
        final List<String> lines = itemStack.getTooltip(player, false);
        if (lines.isEmpty())
            return;

        for (int i = 0; i < lines.size(); ++i) {
            if (i == 0)
                lines.set(i, itemStack.getRarity().rarityColor + lines.get(i));
            else
                lines.set(i, EnumChatFormatting.GRAY + lines.get(i));
        }

        final FontRenderer fr = getFontRenderer(itemStack);

        int maxLineWidth = 0;
        for (String s : lines) {
            final int l = fr.getStringWidth(s);
            if (l > maxLineWidth) {
                maxLineWidth = l;
            }
        }

        int offsetX = x + 9;
        int offsetY = y + 4;
        int offsetZ = z + 500;
        int width = maxLineWidth + 5;
        int height = 13;

        if (lines.size() > 1)
            height += 1 + (lines.size() - 1) * 10;

        final ScaledResolution scale = RenderUtils.getScaledResolution();
        final int displayWidth = scale.getScaledWidth();
        final int displayHeight = scale.getScaledHeight();

        if (offsetX + maxLineWidth > displayWidth)
            offsetX -= 28 + maxLineWidth;
        if (offsetY + height + 6 > displayHeight)
            offsetY = displayHeight - height - 6;

        final Part tooltipPart = Mellow.getPart("tooltip");
        RenderParts.nineSlice(tooltipPart, offsetX, offsetY, offsetZ, width, height);

        offsetX += 3;
        offsetY += 1;
        for (int i = 0; i < lines.size(); ++i) {
            RenderWidgets.string(lines.get(i), offsetX, offsetY, offsetZ + 1, -1, fr);

            offsetY += (i == 0 ? 11 : 10);
        }
    }

    private static FontRenderer getFontRenderer(ItemStack itemStack) {
        final FontRenderer fr = itemStack.getItem().getFontRenderer(itemStack);
        return fr != null ? fr : Minecraft.getMinecraft().fontRenderer;
    }
}
