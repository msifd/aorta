package msifeed.mc.aorta.props;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSerializable {
    NBTTagCompound toNBT();

    void fromNBT(NBTTagCompound compound);
}
