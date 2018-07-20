package msifeed.mc.aorta.core.props;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import msifeed.mc.aorta.props.ExtProp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class TraitsProperty extends ExtProp {
    static final String PROP_NAME = Aorta.MODID + ".core.traits";

    public Set<Trait> traits = null;

    public static TraitsProperty get(EntityLivingBase entity) {
        return (TraitsProperty) entity.getExtendedProperties(PROP_NAME);
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public void init(Entity entity, World world) {
        if (entity instanceof EntityPlayer && traits == null) {
            traits = new HashSet<>();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound root) {
        if (traits == null)
            return;

        int[] array = traits.stream().mapToInt(t -> t.code).toArray();
        root.setTag(PROP_NAME, new NBTTagIntArray(array));
    }

    @Override
    public void loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return;

        final int[] codes = root.getIntArray(PROP_NAME);
        traits = TraitDecoder.decode(codes);
    }

    public boolean has(Trait trait) {
        return traits.contains(trait);
    }

    public boolean toggle(Trait trait) {
        final boolean removed = traits.remove(trait);
        if (!removed)
            traits.add(trait);
        return !removed;
    }
}
