package msifeed.mc.aorta.core.props;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.EntityLivingAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TraitsAttribute extends EntityLivingAttribute<Set<Trait>> {
    public static final TraitsAttribute INSTANCE = new TraitsAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".core.traits";

    private TraitsAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public Set<Trait> init(Entity entity, World world, Set<Trait> traits) {
        if (entity instanceof EntityPlayer && traits == null)
            return new HashSet<>();
        return traits;
    }

    @Override
    public void saveNBTData(Set<Trait> traits, NBTTagCompound root) {
        if (traits == null)
            return;

        int[] array = traits.stream().mapToInt(t -> t.code).toArray();
        root.setTag(PROP_NAME, new NBTTagIntArray(array));
    }

    @Override
    public Set<Trait> loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return null;

        final int[] codes = root.getIntArray(PROP_NAME);
        return TraitDecoder.decode(codes);
    }

    public boolean has(EntityLivingBase entity, Trait trait) {
        return get(entity).orElse(Collections.emptySet()).contains(trait);
    }

    public boolean toggle(EntityLivingBase entity, Trait trait) {
        final Optional<Set<Trait>> traitsOpt = get(entity);
        if (traitsOpt.isPresent()) {
            final Set<Trait> traits = traitsOpt.get();
            final boolean removed = traits.remove(trait);
            if (!removed)
                traits.add(trait);
            sync(entity);
            return !removed;
        }
        return false;
    }
}
