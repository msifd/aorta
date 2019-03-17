package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

public class Character {
    public EnumMap<Feature, Integer> features = new EnumMap<>(Feature.class);
    public Map<String, BodyPart> bodyParts = new LinkedHashMap<>();
    public Set<Trait> traits = new HashSet<>();
    public byte vitalityRate = 50;
    public byte psionics = 0;

    public Character() {
        final Feature[] featureEnum = Feature.values();
        for (Feature f : featureEnum) {
            features.put(f, 5);
        }
    }

    public Character(Character c) {
        for (Map.Entry<Feature, Integer> e : c.features.entrySet())
            this.features.put(e.getKey(), e.getValue());
        for (Map.Entry<String, BodyPart> e : c.bodyParts.entrySet())
            this.bodyParts.put(e.getKey(), new BodyPart(e.getValue()));
        traits.addAll(c.traits);
        vitalityRate = c.vitalityRate;
        psionics = c.psionics;
    }

    public Set<Trait> traits() {
        return this.traits;
    }

    public boolean has(Trait trait) {
        return traits.contains(trait);
    }

    public int countMaxHealth() {
        return bodyParts.values().stream().mapToInt(BodyPart::getMaxHealth).sum();
    }

    public int countVitalityThreshold() {
        return Math.floorDiv(vitalityRate * countMaxHealth(), 100);
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();

        final NBTTagCompound features = new NBTTagCompound();
        for (EnumMap.Entry<Feature, Integer> e : this.features.entrySet())
            features.setByte(e.getKey().toString().toLowerCase(), e.getValue().byteValue());
        compound.setTag(Tags.features, features);

        final NBTTagList bodyParts = new NBTTagList();
        for (BodyPart p : this.bodyParts.values())
            bodyParts.appendTag(p.toNBT());
        compound.setTag(Tags.bodyParts, bodyParts);

        int[] array = traits.stream().mapToInt(t -> t.code).toArray();
        compound.setTag(Tags.traits, new NBTTagIntArray(array));

        compound.setByte(Tags.vitality, vitalityRate);
        compound.setByte(Tags.psionics, psionics);

        return compound;
    }

    public void fromNBT(NBTTagCompound compound) {
        final NBTTagCompound features = compound.getCompoundTag(Tags.features);
        final Feature[] featEnum = Feature.values();
        for (Feature feature : featEnum) {
            final int feat = features.getByte(feature.toString().toLowerCase());
            this.features.put(feature, feat);
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

        this.vitalityRate = compound.getByte(Tags.vitality);
        this.psionics = compound.getByte(Tags.psionics);
    }

    private static class Tags {
        static final String features = "features";
        static final String bodyParts = "bodyParts";
        static final String traits = "traits";
        static final String vitality = "vitality";
        static final String psionics = "psionics";
    }
}
