package msifeed.mc.aorta.core.attributes;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitType;
import msifeed.mc.aorta.sys.attributes.EntityLivingAttribute;
import msifeed.mc.aorta.sys.attributes.MissingRequiredAttributeException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

public class CharacterAttribute extends EntityLivingAttribute<Character> {
    public static final CharacterAttribute INSTANCE = new CharacterAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".core.char";

    public static Optional<Character> get(Entity e) {
        return INSTANCE.getValue(e);
    }

    public static Character require(Entity e) {
        return INSTANCE.getValue(e).orElseThrow(() -> new MissingRequiredAttributeException(INSTANCE, e));
    }

    private CharacterAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public Character init(Entity entity, World world, Character character) {
        if (character != null)
            return character;
        else if (entity instanceof EntityPlayer)
            return new Character();
        else
            return null;
    }

    @Override
    public void saveNBTData(Character character, NBTTagCompound root) {
        if (character == null)
            return;
        root.setTag(PROP_NAME, character.toNBT());
    }

    @Override
    public Character loadNBTData(NBTTagCompound root) {
        if (!root.hasKey(PROP_NAME))
            return null;
        final Character character = new Character();
        character.fromNBT(root.getCompoundTag(PROP_NAME));

        // Add default lang trait if has no one
        if (character.traits.stream().noneMatch(TraitType.LANG::is))
            character.traits.add(Trait.lang_common);

        return character;
    }

    public static boolean has(EntityLivingBase entity, Trait trait) {
        return INSTANCE.getValue(entity).map(c -> c.has(trait)).orElse(false);
    }

    public static boolean toggle(EntityLivingBase entity, Trait trait) {
        final Optional<Character> charOpt = INSTANCE.getValue(entity);
        if (charOpt.isPresent()) {
            final Set<Trait> traits = charOpt.get().traits;
            final boolean removed = traits.remove(trait);
            if (!removed)
                traits.add(trait);
            INSTANCE.broadcast(entity.worldObj, entity);
            return !removed;
        }
        return false;
    }
}
