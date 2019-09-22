package msifeed.mc.aorta.core.meta;

import msifeed.mc.aorta.core.character.Feature;
import msifeed.mc.aorta.core.rolls.Modifiers;
import net.minecraft.nbt.NBTTagCompound;

public class MetaInfo {
    public Modifiers modifiers = new Modifiers();

    public MetaInfo() {
    }

    public MetaInfo(MetaInfo m) {
        fromNBT(m.toNBT());
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setInteger("rmod", modifiers.rollMod);

        final Feature[] feats = Feature.mainFeatures();
        final int[] featsArray = new int[feats.length];
        for (int i = 0; i < feats.length; i++)
            featsArray[i] = modifiers.featureMods.getOrDefault(feats[i], 0);
        c.setIntArray("fmod", featsArray);
        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        modifiers.rollMod = c.getInteger("rmod");

        modifiers.featureMods.clear();
        final Feature[] feats = Feature.mainFeatures();
        final int[] featsArray = c.getIntArray("fmod");
        for (int i = 0; i < feats.length; i++)
            modifiers.featureMods.put(feats[i], featsArray[i]);
    }
}
