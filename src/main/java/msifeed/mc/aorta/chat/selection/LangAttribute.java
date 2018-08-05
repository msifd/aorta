package msifeed.mc.aorta.chat.selection;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.attributes.PlayerAttribute;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.core.attributes.TraitsAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitTypes;
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

    private LangAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public Language init(Entity entity, World world, Language currentValue) {
        if (currentValue == null || currentValue == Language.VANILLA) {
            final Set<Trait> traits = TraitsAttribute.INSTANCE.get(entity).orElse(Collections.emptySet());
            final Set<Trait> langTraits = TraitTypes.LANG.filter(traits);
            return Arrays.stream(Language.values())
                    .filter(language -> langTraits.contains(language.trait))
                    .findFirst().orElse(Language.VANILLA);
        }
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
    public Optional<Language> get(Entity entity) {
        Optional<Language> result = super.get(entity);

        if (!result.isPresent() || result.get() == Language.VANILLA) {
            final Set<Trait> traits = TraitsAttribute.INSTANCE.get(entity).orElse(Collections.emptySet());
            final Set<Trait> langTraits = TraitTypes.LANG.filter(traits);
            final Language firstLang = Arrays.stream(Language.values())
                    .filter(language -> langTraits.contains(language.trait))
                    .findFirst().orElse(Language.VANILLA);
            set(entity, firstLang);
            return super.get(entity);
        }

        return result;
    }
}
