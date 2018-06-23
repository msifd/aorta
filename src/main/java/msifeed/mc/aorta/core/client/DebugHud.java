package msifeed.mc.aorta.core.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.character.*;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.props.CharacterProperty;
import msifeed.mc.aorta.core.things.ItemDebugTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.LinkedList;

public enum DebugHud {
    INSTANCE;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRenderer;
        final EntityPlayer player = mc.thePlayer;

        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemDebugTool))
            return;

        Entity hitEntity = getEntityLookingAt();
        if (!(hitEntity instanceof EntityLivingBase))
            hitEntity = mc.thePlayer;
        final EntityLivingBase entity = (EntityLivingBase) hitEntity;

        final LinkedList<String> lines = new LinkedList<>();
        lines.add("[Aorta debug]");
        lines.add("Entity: " + entity.getCommandSenderName());
        addCharProp(lines, entity);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        int y = 5;
        for (String l : lines) {
            fr.drawString(l, 5, y, 0xed5050);
            y += fr.FONT_HEIGHT;
        }
        GL11.glPopMatrix();
    }

    private void addCharProp(LinkedList<String> lines, EntityLivingBase entity) {
        final CharacterProperty charProp = CharacterProperty.get(entity);
        if (charProp == null)
            return;
        final Character character = charProp.getCharacter().orElse(null);
        if (character == null)
            return;

        lines.add("Character {");
        lines.add("  features: {");
        for (EnumMap.Entry<Feature, Grade> e : character.features.entrySet()) {
            lines.add("    " + e.getKey().toString().toLowerCase() + ": " + (e.getValue().ordinal() + 1));
        }
        lines.add("  }");
        lines.add("  bodyParts: {");
        for (BodyPart bodyPart : character.bodyParts) {
            lines.add("    " + bodyPart.type.toString().toLowerCase() + ": " + bodyPart.hits + "/" + bodyPart.maxHits);
        }
        lines.add("  }");
        lines.add("}");
    }

    @Nullable
    private Entity getEntityLookingAt() {
        final Minecraft mc = Minecraft.getMinecraft();
        final MovingObjectPosition mop = mc.objectMouseOver;
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)
            return null;
        return mop.entityHit;
    }
}
