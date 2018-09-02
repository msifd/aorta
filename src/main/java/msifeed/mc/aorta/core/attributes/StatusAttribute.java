package msifeed.mc.aorta.core.attributes;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.flavors.EntityLivingAttribute;
import msifeed.mc.aorta.core.status.CharStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Optional;

public class StatusAttribute extends EntityLivingAttribute<CharStatus> {
    public static final StatusAttribute INSTANCE = new StatusAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".core.status";

    public static Optional<CharStatus> get(Entity e) {
        return INSTANCE.getValue(e);
    }

    private StatusAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public CharStatus init(Entity entity, World world, CharStatus status) {
        if (status != null)
            return status;
        if (entity instanceof EntityPlayer)
            return new CharStatus();
        return null;
    }

    @Override
    public void saveNBTData(CharStatus status, NBTTagCompound root) {
        if (status == null)
            return;
        root.setTag(PROP_NAME, status.toNBT());
    }

    @Override
    public CharStatus loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return null;
        final CharStatus status = new CharStatus();
        status.fromNBT(root.getCompoundTag(PROP_NAME));
        return status;
    }
}
