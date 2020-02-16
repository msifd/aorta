package msifeed.mc.extensions.tweaks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

public enum SetMaxHealth {
    INSTANCE;

    public static void init() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    @SubscribeEvent
    public void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        calculateHealth(event.player);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        calculateHealth(event.player);
    }

    private void calculateHealth(EntityPlayer player) {
        final Character c = CharacterAttribute.require(player);
        player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(c.countMaxHealth());
    }
}
