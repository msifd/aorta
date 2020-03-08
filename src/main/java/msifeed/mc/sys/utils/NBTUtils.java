package msifeed.mc.sys.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTUtils {
    public static NBTTagCompound itemStackToNBT(ItemStack stack) {
        final NBTTagCompound tag = new NBTTagCompound();
        stack.writeToNBT(tag);
        return tag;
    }

    public static ItemStack itemStackFromNBT(NBTTagCompound tag) {
        if (tag == null)
            return null;
        return ItemStack.loadItemStackFromNBT(tag);
    }
}
