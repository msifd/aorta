package msifeed.mc.aorta.chat.usage;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.flavors.PlayerAttribute;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class LangAttribute extends PlayerAttribute<Language> {
    public static final LangAttribute INSTANCE = new LangAttribute();
    private static final String PROP_NAME = Aorta.MODID + ".chat.lang";

    public static Optional<Language> get(Entity e) {
        return INSTANCE.getValue(e);
    }

    private LangAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public Language init(Entity entity, World world, Language currentValue) {
        if (currentValue == null || currentValue == Language.VANILLA)
            return findLang(entity);
        return currentValue;
    }

    @Override
    public void saveNBTData(Language lang, NBTTagCompound root) {
        if (lang == null)
            return;
        root.setByte(PROP_NAME, (byte) lang.ordinal());
    }

    @Override
    public Language loadNBTData(NBTTagCompound root) {
        if (root.hasKey(PROP_NAME)) {
            final byte ord = root.getByte(PROP_NAME);
            return Language.values()[ord];
        }
        return null;
    }

    @Override
    public Optional<Language> getValue(Entity entity) {
        Optional<Language> result = super.getValue(entity);

        if (!result.isPresent() || result.get() == Language.VANILLA) {
            set(entity, findLang(entity));
            return super.getValue(entity);
        }

        return result;
    }

    private Language findLang(Entity entity) {
        final Set<Trait> traits = CharacterAttribute.get(entity).map(Character::traits).orElse(Collections.emptySet());
        final Set<Trait> langTraits = TraitType.LANG.filter(traits);
        return Arrays.stream(Language.values())
                .filter(language -> langTraits.contains(language.trait))
                .findFirst().orElse(Language.VANILLA);
    }
}
