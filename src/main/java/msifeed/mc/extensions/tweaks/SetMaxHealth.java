package msifeed.mc.extensions.tweaks;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SetMaxHealth {
    public static void init() {
        MinecraftForge.EVENT_BUS.register(new SetMaxHealth());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (!e.world.isRemote && e.entity instanceof EntityPlayer)
            calculateHealth((EntityPlayer) e.entity);
    }

    private void calculateHealth(EntityPlayer player) {
        final Character c = CharacterAttribute.require(player);
        player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(c.countMaxHealth());
    }
}
