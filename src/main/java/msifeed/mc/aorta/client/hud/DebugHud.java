package msifeed.mc.aorta.client.hud;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.chat.usage.LangAttribute;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.character.Grade;
import msifeed.mc.aorta.core.status.BodyPartHealth;
import msifeed.mc.aorta.core.status.StatusCalc;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.environment.EnvironmentManager;
import msifeed.mc.aorta.environment.WorldEnv;
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
import java.util.Map;

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
        addWeather(lines, EnvironmentManager.getEnv(mc.theWorld.provider.dimensionId));

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

            lines.add("  features: {");
            for (EnumMap.Entry<Feature, Grade> e : character.features.entrySet()) {
                lines.add("    " + e.getKey().toString().toLowerCase() + ": " + (e.getValue().ordinal() + 1));
            }
            lines.add("  }");

            lines.add("  bodyParts: {");
            character.bodyParts.values().stream().sorted().forEach(bp -> {
                lines.add("    " + bp.toLineString());
            });
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

            lines.add("}");

            addStatusProp(lines, entity, character);
        });
    }

    private void addStatusProp(ArrayList<String> lines, EntityLivingBase entity, Character character) {
        StatusAttribute.get(entity).ifPresent(status -> {
            lines.add("Status {");

            lines.add("  health to death: " + StatusCalc.damageToDeath(character, status));

            lines.add("  health: {");
            for (Map.Entry<String, BodyPartHealth> e : status.health.entrySet()) {
                lines.add("    " + e.getKey().toLowerCase() + ": " + e.getValue().toString());
            }
            lines.add("  }");

            lines.add("  shield: " + status.shield);
            lines.add("  sanity: " + status.sanity);

            lines.add("}");
        });
    }

    private void addLang(ArrayList<String> lines, EntityLivingBase entity) {
        LangAttribute.get(entity).ifPresent(lang -> lines.add("Language: " + lang));
    }

    private void addWeather(ArrayList<String> lines, WorldEnv env) {
        final WorldEnv.Rain r = env.rain;
        lines.add("Rain {");
        lines.add("  acc: " + r.accumulated);
        lines.add("  min: " + r.minThreshold);
        lines.add("  max: " + r.maxThreshold);
        lines.add("  thunder: " + r.thunderThreshold);
        lines.add("  +/-: " + String.format("%d/%d", r.income, r.outcome));
        lines.add("  dice: d" + r.rainfallDice);
        lines.add("}");    }

    @Nullable
    private Entity getEntityLookingAt() {
        final Minecraft mc = Minecraft.getMinecraft();
        final MovingObjectPosition mop = mc.objectMouseOver;
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)
            return null;
        return mop.entityHit;
    }
}
