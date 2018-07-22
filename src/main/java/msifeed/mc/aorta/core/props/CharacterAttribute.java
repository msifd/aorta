package msifeed.mc.aorta.core.props;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.EntityLivingAttribute;
import msifeed.mc.aorta.core.character.Character;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CharacterAttribute extends EntityLivingAttribute<Character> {
    public static final CharacterAttribute INSTANCE = new CharacterAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".core.char";

    private CharacterAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public Character init(Entity entity, World world, Character character) {
        if (entity instanceof EntityPlayer && character == null)
            return new Character();
        return character;
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
        return character;
    }
}
