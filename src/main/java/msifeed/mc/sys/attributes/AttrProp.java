package msifeed.mc.sys.attributes;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

class AttrProp<T> implements IExtendedEntityProperties {
    T value = null;
    private final EntityAttribute<T> attribute;

    public AttrProp(EntityAttribute<T> attribute) {
        this.attribute = attribute;
    }

    @Override
    public final void init(Entity entity, World world) {
        value = attribute.init(entity, world, value);
    }

    @Override
    public void saveNBTData(NBTTagCompound root) {
        attribute.saveNBTData(value, root);
    }

    @Override
    public void loadNBTData(NBTTagCompound root) {
        final T tmp = attribute.loadNBTData(root);
        if (tmp != null)
            value = tmp;
    }
}
