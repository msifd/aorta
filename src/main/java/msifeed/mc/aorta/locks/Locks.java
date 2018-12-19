package msifeed.mc.aorta.locks;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.locks.items.AccessTunerItem;
import msifeed.mc.aorta.locks.items.KeyItem;
import msifeed.mc.aorta.locks.items.LockItem;
import msifeed.mc.aorta.locks.items.LockpickItem;

import java.util.stream.Stream;

public enum Locks {
    INSTANCE;

    public final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Aorta.MODID + ".locks");
    private static final HashFunction hasher = Hashing.murmur3_128(3364);

    public static void init() {
        Stream.of(LockType.values())
                .skip(1)
                .forEach(t -> GameRegistry.registerItem(new LockItem(t), LockItem.ID_BASE + t.toString().toLowerCase()));
        GameRegistry.registerItem(new KeyItem(), KeyItem.ID);
        GameRegistry.registerItem(new LockpickItem(), LockpickItem.ID);
        GameRegistry.registerItem(new AccessTunerItem(), AccessTunerItem.ID);

        GameRegistry.registerTileEntity(LockTileEntity.class, LockTileEntity.ID);

        INSTANCE.CHANNEL.registerMessage(DigitalLockMessage.class, DigitalLockMessage.class, 0x00, Side.SERVER);
    }

    public static int makeKeyHash(String input) {
        return hasher.hashUnencodedChars(input).asInt();
    }
}
