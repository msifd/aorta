package msifeed.mc.more.crabs.meta;

import msifeed.mc.more.crabs.character.Ability;
import msifeed.mc.more.crabs.rolls.Modifiers;
import net.minecraft.nbt.NBTTagCompound;

public class MetaInfo {
    public Modifiers modifiers = new Modifiers();
    public boolean receiveGlobal = true;

    public MetaInfo() {
    }

    public MetaInfo(MetaInfo m) {
        this.modifiers = new Modifiers(m.modifiers);
        this.receiveGlobal = m.receiveGlobal;
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setInteger("rmod", modifiers.roll);

        final Ability[] feats = Ability.values();
        final int[] featsArray = new int[feats.length];
        for (int i = 0; i < feats.length; i++)
            featsArray[i] = modifiers.toAbility(feats[i]);
        c.setIntArray("fmod", featsArray);

        c.setBoolean("recglob", receiveGlobal);

        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        modifiers.roll = c.getInteger("rmod");

        modifiers.abilities.clear();
        final Ability[] feats = Ability.values();
        final int[] featsArray = c.getIntArray("fmod");
        for (int i = 0; i < feats.length; i++)
            modifiers.abilities.put(feats[i], featsArray[i]);

        receiveGlobal = c.getBoolean("recglob");
    }
}
