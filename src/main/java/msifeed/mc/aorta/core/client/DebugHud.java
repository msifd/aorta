package msifeed.mc.aorta.core.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.aorta.core.props.CharacterProperty;
import msifeed.mc.aorta.core.props.TraitsProperty;
import msifeed.mc.aorta.core.things.ItemDebugTool;
import msifeed.mc.aorta.core.traits.Trait;
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
import java.util.ArrayList;
import java.util.EnumMap;

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

        final ArrayList<String> lines = new ArrayList<>();
        lines.add("[Aorta debug]");
        lines.add("Entity: " + entity.getCommandSenderName());
        addCharProp(lines, entity);
        addTraits(lines, entity);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        int y = 5;
        for (String l : lines) {
            fr.drawString(l, 5, y, 0xed5050);
            y += fr.FONT_HEIGHT;
        }
        GL11.glPopMatrix();
    }

    private void addCharProp(ArrayList<String> lines, EntityLivingBase entity) {
        final CharacterProperty charProp = CharacterProperty.get(entity);
        if (charProp == null)
            return;
        final Character character = charProp.character;
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
            lines.add("    " + bodyPart.toLineString());
        }
        lines.add("  }");
        lines.add("}");
    }

    private void addTraits(ArrayList<String> lines, EntityLivingBase entity) {
        final TraitsProperty prop = TraitsProperty.get(entity);
        if (prop == null)
            return;

        lines.add("Traits [");

        final StringBuilder sb = new StringBuilder("  ");
        for (Trait t : prop.traits) {
            sb.append(t);
            sb.append(", ");
            if (sb.length() > 50) {
                lines.add(sb.toString());
                sb.setLength(0);
                sb.append("  ");
            }
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }

        lines.add("]");
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
