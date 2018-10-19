package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Character {
    public EnumMap<Feature, Grade> features = new EnumMap<>(Feature.class);
    public HashMap<String, BodyPart> bodyParts = new HashMap<>();
    public Set<Trait> traits = new HashSet<>();

    public Character() {
        final Feature[] featureEnum = Feature.values();
        for (Feature f : featureEnum) {
            features.put(f, Grade.NORMAL);
        }
    }

    public Character(Character c) {
        features.putAll(c.features);
        bodyParts.putAll(c.bodyParts);
        traits.addAll(c.traits);
    }

    public Set<Trait> traits() {
        return this.traits;
    }

    public boolean has(Trait trait) {
        return traits.contains(trait);
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();

        final NBTTagCompound features = new NBTTagCompound();
        for (EnumMap.Entry<Feature, Grade> e : this.features.entrySet())
            features.setByte(e.getKey().toString().toLowerCase(), (byte) e.getValue().ordinal());
        compound.setTag(Tags.features, features);

        final NBTTagList bodyParts = new NBTTagList();
        for (BodyPart p : this.bodyParts.values())
            bodyParts.appendTag(p.toNBT());
        compound.setTag(Tags.bodyParts, bodyParts);

        int[] array = traits.stream().mapToInt(t -> t.code).toArray();
        compound.setTag(Tags.traits, new NBTTagIntArray(array));

        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        final NBTTagCompound features = compound.getCompoundTag(Tags.features);
        final Feature[] featEnum = Feature.values();
        final Grade[] gradeEnum = Grade.values();
        for (Feature feature : featEnum) {
            final byte ord = features.getByte(feature.toString().toLowerCase());
            this.features.put(feature, gradeEnum[ord]);
        }

        final NBTTagList bodyParts = compound.getTagList(Tags.bodyParts, 10); // 10 - NBTTagCompound
        this.bodyParts.clear();
        for (int i = 0; i < bodyParts.tagCount(); i++) {
            final BodyPart part = new BodyPart();
            part.fromNBT(bodyParts.getCompoundTagAt(i));
            this.bodyParts.put(part.name, part);
        }

        final int[] codes = compound.getIntArray(Tags.traits);
        this.traits = TraitDecoder.decode(codes);
    }

    private static class Tags {
        static final String features = "features";
        static final String bodyParts = "bodyParts";
        static final String traits = "traits";
    }
}
