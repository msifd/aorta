package msifeed.mc.aorta.core.attributes;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.EntityLivingAttribute;
import msifeed.mc.aorta.core.status.CharStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StatusAttribute extends EntityLivingAttribute<CharStatus> {
    public static final StatusAttribute INSTANCE = new StatusAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".core.status";

    private StatusAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public CharStatus init(Entity entity, World world, CharStatus health) {
        if (entity instanceof EntityPlayer && health == null)
            return new CharStatus();
        return health;
    }

    @Override
    public void saveNBTData(CharStatus health, NBTTagCompound root) {
        if (health == null)
            return;

//        int[] array = health.stream().mapToInt(t -> t.code).toArray();
//        root.setTag(PROP_NAME, new NBTTagIntArray(array));


    }

    @Override
    public CharStatus loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return null;

//        final int[] codes = root.getIntArray(PROP_NAME);
//        return TraitDecoder.decode(codes);
        return new CharStatus();
    }
}
