package msifeed.mc.aorta.core.meta;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.Modifiers;
import net.minecraft.nbt.NBTTagCompound;

public class MetaInfo {
    public Modifiers modifiers = new Modifiers();

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setInteger("rmod", modifiers.rollMod);
        c.setIntArray("fmod", modifiers.featureMods.values().stream().mapToInt(Integer::intValue).toArray());
        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        modifiers.rollMod = c.getInteger("rmod");
        modifiers.featureMods.clear();
        final int[] featureMods = c.getIntArray("fmod");
        final Feature[] features = Feature.values();
        for (int i = 0; i < featureMods.length; i++)
            modifiers.featureMods.put(features[i], featureMods[i]);
    }
}
