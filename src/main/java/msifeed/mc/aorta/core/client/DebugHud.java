package msifeed.mc.aorta.core.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.character.CharacterProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;

public enum DebugHud {
    INSTANCE;

    public void register() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRenderer;
        final EntityPlayer player = mc.thePlayer;
        final NBTTagCompound data = player.getEntityData();
        final CharacterProperty charProp = CharacterProperty.get(player);

        if (charProp == null)
            return;

        LinkedList<String> lines = new LinkedList<>();
        lines.add("Aorta debug:");
        lines.add(charProp.character.toString());

        int y = 5;
        for (String l : lines) {
            fr.drawString(l, 5, y, 0xed5050);
            y += fr.FONT_HEIGHT;
        }
    }
}
