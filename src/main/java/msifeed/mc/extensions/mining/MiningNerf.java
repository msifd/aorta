package msifeed.mc.extensions.mining;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.JsonConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public enum MiningNerf {
    INSTANCE;

    private final JsonConfig<ConfigSection> config = ConfigBuilder.of(ConfigSection.class, "mining_stamina.json")
            .sync()
            .create();

    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        AttributeHandler.registerAttribute(MiningAttribute.INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.entityPlayer.getHeldItem() == null)
            return;

        final MiningInfo info = updateStamina(event.entityPlayer, true);
        event.newSpeed *= config.get().globalSpeedModifier;
        event.newSpeed *= info.stamina;
    }

    public MiningInfo updateStamina(EntityPlayer player, boolean isMining) {
        final ConfigSection config = this.config.get();
        final MiningInfo info = MiningAttribute.require(player);

        final long now = System.currentTimeMillis();
        final double secFromUpdate = (now - info.lastUpdate) / 1000d;
        final double secFromMining = (now - info.lastMining) / 1000d;
        final double diff = secFromUpdate * (secFromMining > 1 ? config.restPerSec : config.costPerSec);
        // Если прошло больше секунды, то стамина восстанавливается

        info.lastUpdate = now;
        if (isMining)
            info.lastMining = now;
        info.stamina = MathHelper.clamp_double(info.stamina + diff, 0, 1);
        MiningAttribute.INSTANCE.set(player, info);

        return info;
    }

    public MiningInfo setStamina(EntityPlayer player, int percents) {
        final MiningInfo info = MiningAttribute.require(player);
        info.lastSync = 0;
        info.lastUpdate = System.currentTimeMillis();
        info.stamina = MathHelper.clamp_double(percents / 100d, 0, 1);

        MiningAttribute.INSTANCE.set(player, info);

        return info;
    }

    public static class ConfigSection {
        double globalSpeedModifier = 0.5;
        double restPerSec = 0.001;
        double costPerSec = -0.01;
    }
}
