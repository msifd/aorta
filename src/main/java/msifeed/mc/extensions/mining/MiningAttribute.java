package msifeed.mc.extensions.mining;

import msifeed.mc.Bootstrap;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import msifeed.mc.sys.attributes.PlayerAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class MiningAttribute extends PlayerAttribute<MiningInfo> {
    public static final MiningAttribute INSTANCE = new MiningAttribute();
    private static final String PROP_NAME = Bootstrap.MODID + ".mining";
    private static final long SYNC_PERIOD_MS = 5000;

    public static MiningInfo require(EntityPlayer e) {
        return INSTANCE.getValue(e).orElseThrow(() -> new MissingRequiredAttributeException(INSTANCE, e));
    }

    private MiningAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public MiningInfo init(Entity entity, World world, MiningInfo currentValue) {
        return currentValue != null ? currentValue : new MiningInfo();
    }

    @Override
    public void saveNBTData(MiningInfo value, NBTTagCompound root) {
        root.setTag(PROP_NAME, value.toNBT());
    }

    @Override
    public MiningInfo loadNBTData(MiningInfo value, NBTTagCompound root) {
        if (root.hasKey(PROP_NAME))
            value.fromNBT(root.getCompoundTag(PROP_NAME));
        return value;
    }

    @Override
    public boolean shouldSync(MiningInfo info) {
        final long now = System.currentTimeMillis();
        final boolean sync = now - info.lastSync > SYNC_PERIOD_MS;
        if (sync)
            info.lastSync = now;
        return sync;
    }
}
