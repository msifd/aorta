package msifeed.mc.aorta.locks;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.blocks.templates.DoorTemplate;
import msifeed.mc.aorta.locks.items.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public enum Locks {
    INSTANCE;

    public final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".locks");
    private static final HashFunction hasher = Hashing.murmur3_128(3364);

    public static void init() {
        LockType.locks().forEach(t -> GameRegistry.registerItem(new LockItem(t), LockItem.getItemId(t)));
        GameRegistry.registerItem(new BlankKeyItem(), BlankKeyItem.ID);
        GameRegistry.registerItem(new KeyItem(), KeyItem.ID);
        GameRegistry.registerItem(new LockpickItem(), LockpickItem.ID);
        GameRegistry.registerItem(new AccessTunerItem(), AccessTunerItem.ID);
        GameRegistry.registerItem(new SkeletalKeyItem(), SkeletalKeyItem.ID);
        GameRegistry.registerTileEntity(LockTileEntity.class, LockTileEntity.ID);
        GameRegistry.addRecipe(new CopyKeyRecipe());

        INSTANCE.CHANNEL.registerMessage(DigitalLockMessage.class, DigitalLockMessage.class, 0x00, Side.SERVER);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.block instanceof DoorTemplate))
            return;

        final LockTileEntity lock = LockTileEntity.find(event.world, event.x, event.y, event.z);
        if (lock != null)
            event.world.spawnEntityInWorld(lock.makeEntityItem());
    }

    public static int makeKeyHash(String input) {
        return hasher.hashUnencodedChars(input).asInt();
    }
}
