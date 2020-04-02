package msifeed.mc.extensions.mining;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.JsonConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public enum MiningNerf {
    INSTANCE;

    private static final int MAX_CACHED_BLOCKS = 20;
    private static Class<?> tinkerToolClass;

    private static final Logger logger = LogManager.getLogger(MiningNerf.class);
    private final JsonConfig<ConfigSection> config = ConfigBuilder.of(ConfigSection.class, "mining_stamina.json")
            .sync()
            .create();

    private final HashSet<Block> oreBlocks = new HashSet<>();
    private final LinkedHashMap<Block, Boolean> oreBlocksCache = new CompactLinkedMap<>();

    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        AttributeHandler.registerAttribute(MiningAttribute.INSTANCE);

        try {
            tinkerToolClass = Class.forName("tconstruct.library.tools.ToolCore");
            logger.info("Tinker Construct found. Slowing it down...");
        } catch (ClassNotFoundException e) {
            logger.warn("Tinker Construct not found. Ignore.");
        }
    }

    public static void onPostInit() {
        // Bake ore set
        try {
            final Field oresListField = OreDictionary.class.getDeclaredField("idToStackUn");
            oresListField.setAccessible(true);
            final ArrayList<ArrayList<ItemStack>> ores = (ArrayList<ArrayList<ItemStack>>) oresListField.get(null);

            for (ArrayList<ItemStack> stacks : ores) {
                for (ItemStack stack : stacks) {
                    final Block block = Block.getBlockFromItem(stack.getItem());
                    if (block != Blocks.air)
                        INSTANCE.oreBlocks.add(block);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.throwing(e);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || !isTool(heldItem.getItem()))
            return;
        if (!oreBlocksCache.computeIfAbsent(event.block, oreBlocks::contains))
            return;

        final MiningInfo info = updateStamina(event.entityPlayer, true);
        event.newSpeed *= config.get().globalSpeedModifier;
        event.newSpeed *= info.stamina;
    }

    private boolean isTool(Item tool) {
        return tool instanceof ItemTool || (tinkerToolClass != null && tinkerToolClass.isInstance(tool));
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

    private static class CompactLinkedMap<K, V> extends LinkedHashMap<K, V> {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_CACHED_BLOCKS;
        }
    }
}
