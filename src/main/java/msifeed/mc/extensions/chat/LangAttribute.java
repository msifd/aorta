package msifeed.mc.extensions.chat;

import msifeed.mc.Bootstrap;
import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.attributes.MissingRequiredAttributeException;
import msifeed.mc.sys.attributes.PlayerAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.stream.Stream;

public class LangAttribute extends PlayerAttribute<Language> {
    public static final LangAttribute INSTANCE = new LangAttribute();
    private static final String PROP_NAME = Bootstrap.MODID + ".chat.lang";

    public static Optional<Language> get(Entity e) {
        return INSTANCE.getValue(e);
    }

    public static Language require(Entity e) {
        return INSTANCE.getValue(e).orElseThrow(() -> new MissingRequiredAttributeException(INSTANCE, e));
    }

    private LangAttribute() {
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }

    @Override
    public Language init(Entity entity, World world, Language currentValue) {
        if (currentValue == null || currentValue == Language.VANILLA) {
            return findAnyKnownLang(entity).orElse(Language.VANILLA);
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
    public Language loadNBTData(Language value, NBTTagCompound root) {
        if (root.hasKey(PROP_NAME)) {
            final byte ord = root.getByte(PROP_NAME);
            return Language.values()[ord];
        }
        return null;
    }

    @Override
    public Optional<Language> getValue(Entity entity) {
        final Optional<Language> result = super.getValue(entity);

        if (!result.isPresent())
            return Optional.empty();

        if (!isLangKnown(entity, result.get())) {
            final Optional<Language> replacement = findAnyKnownLang(entity);
            replacement.ifPresent(language -> set(entity, language));
            return replacement;
        }

        return result;
    }

    private boolean isLangKnown(Entity entity, Language language) {
        final Character character = CharacterAttribute.get(entity).orElse(null);
        return character != null && character.traits.contains(language.trait);
    }

    private Optional<Language> findAnyKnownLang(Entity entity) {
        return CharacterAttribute.get(entity)
                .flatMap(c -> Stream.of(Language.values())
                    .filter(l -> c.traits.contains(l.trait))
                    .findAny());
    }
}
