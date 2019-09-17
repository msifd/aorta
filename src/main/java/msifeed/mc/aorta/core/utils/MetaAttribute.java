package msifeed.mc.aorta.core.utils;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.sys.attributes.EntityLivingAttribute;
import msifeed.mc.aorta.sys.attributes.MissingRequiredAttributeException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Optional;

public class MetaAttribute extends EntityLivingAttribute<MetaInfo> {
    public static final MetaAttribute INSTANCE = new MetaAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".core.meta";

    public static Optional<MetaInfo> get(Entity e) {
        return INSTANCE.getValue(e);
    }

    public static MetaInfo require(Entity e) {
        return INSTANCE.getValue(e).orElseThrow(() -> new MissingRequiredAttributeException(INSTANCE, e));
    }

    private MetaAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public MetaInfo init(Entity entity, World world, MetaInfo meta) {
        if (meta != null)
            return meta;
        else if (entity instanceof EntityPlayer)
            return new MetaInfo();
        else
            return null;
    }

    @Override
    public void saveNBTData(MetaInfo meta, NBTTagCompound root) {
        if (meta == null)
            return;
        root.setTag(PROP_NAME, meta.toNBT());
    }

    @Override
    public MetaInfo loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return null;
        final MetaInfo meta = new MetaInfo();
        meta.fromNBT(root.getCompoundTag(PROP_NAME));
        return meta;
    }
}
