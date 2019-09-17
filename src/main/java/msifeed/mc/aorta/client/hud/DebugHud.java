package msifeed.mc.aorta.client.hud;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.character.BodyPart;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.LangAttribute;
import msifeed.mc.aorta.tools.ItemDebugTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
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
        final FontRenderer fr = RenderManager.instance.getFontRenderer();
        final EntityPlayer player = mc.thePlayer;

        if (fr == null)
            return;

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
        addCharProps(lines, entity);
        addLang(lines, entity);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        int y = 5;
        for (String l : lines) {
            fr.drawString(l, 5, y, 0xed5050);
            y += fr.FONT_HEIGHT;
        }
        GL11.glPopMatrix();
    }

    private void addCharProps(ArrayList<String> lines, EntityLivingBase entity) {
        CharacterAttribute.get(entity).ifPresent(character -> {
            lines.add("Character {");

            lines.add("  name: '" + character.name + "'");
            lines.add("  wiki: '" + character.wikiPage + "'");

            lines.add("  features: {");
            for (EnumMap.Entry<Feature, Integer> e : character.features.entrySet())
                lines.add("    " + e.getKey().toString().toLowerCase() + ": " + e.getValue());
            lines.add("  }");

            lines.add("  bodyParts: {");
            for (BodyPart bp : character.bodyParts.values())
                lines.add("    " + bp.toString());
            lines.add("  }");

            lines.add("  traits [");
            final StringBuilder sb = new StringBuilder("    ");
            for (Trait t : character.traits) {
                sb.append(t);
                sb.append(", ");
                if (sb.length() > 50) {
                    lines.add(sb.toString());
                    sb.setLength(0);
                    sb.append("    ");
                }
            }
            if (sb.length() > 0) {
                lines.add(sb.toString());
            }
            lines.add("  ]");

            lines.add("  vitaity rate: " + character.vitalityRate);
            final int vitalityThreshold = character.countVitalityThreshold();
            final int vitality = character.countVitality(vitalityThreshold);
            final double vitalityPercents = vitality / (double) vitalityThreshold;
            lines.add(String.format("  vitality: %d/%d (%f)", vitality, vitalityThreshold, vitalityPercents));

            lines.add("  shield: " + character.shield);
            lines.add("  sanity: " + character.sanity);
            lines.add(String.format("  psionics: %d/%d", character.psionics, character.maxPsionics));

            lines.add("}");
        });
    }

    private void addLang(ArrayList<String> lines, EntityLivingBase entity) {
        LangAttribute.get(entity).ifPresent(lang -> lines.add("Language: " + lang.name()));
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
