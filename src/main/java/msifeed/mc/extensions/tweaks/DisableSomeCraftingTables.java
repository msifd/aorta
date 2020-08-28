package msifeed.mc.extensions.tweaks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class DisableSomeCraftingTables {
    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(new DisableSomeCraftingTables());
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;

        final Block block = event.world.getBlock(event.x, event.y, event.z);
        if (block instanceof BlockEnchantmentTable || block instanceof BlockBed)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.left.getItem() instanceof ItemEnchantedBook && event.right.getItem() instanceof ItemEnchantedBook)
            event.setCanceled(true);
    }
}