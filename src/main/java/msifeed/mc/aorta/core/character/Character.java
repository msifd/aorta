package msifeed.mc.aorta.core.character;

import msifeed.mc.aorta.props.INBTSerializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.EnumMap;
import java.util.HashSet;

public class Character implements INBTSerializable {
    public EnumMap<Feature, Grade> features = new EnumMap<>(Feature.class);
    public HashSet<BodyPart> bodyParts = new HashSet<>();

    public Character() {
        final Feature[] featureEnum = Feature.values();
        for (Feature f : featureEnum) {
            features.put(f, Grade.NORMAL);
        }
    }

    @Override
    public NBTTagCompound toNBT() {
        final NBTTagCompound compound = new NBTTagCompound();

        final NBTTagCompound features = new NBTTagCompound();
        for (EnumMap.Entry<Feature, Grade> e : this.features.entrySet())
            features.setByte(e.getKey().toString().toLowerCase(), (byte) e.getValue().ordinal());
        compound.setTag("features", features);

        final NBTTagList bodyParts = new NBTTagList();
        for (BodyPart p : this.bodyParts)
            bodyParts.appendTag(p.toNBT());
        compound.setTag("bodyParts", bodyParts);

        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound compound) {
        final NBTTagCompound features = compound.getCompoundTag("features");
        final Feature[] featEnum = Feature.values();
        final Grade[] gradeEnum = Grade.values();
        for (Feature feature : featEnum) {
            final byte ord = features.getByte(feature.toString().toLowerCase());
            this.features.put(feature, gradeEnum[ord]);
        }

        final NBTTagList bodyParts = compound.getTagList("bodyParts", 10); // 10 - NBTTagCompound
        this.bodyParts.clear();
        for (int i = 0; i < bodyParts.tagCount(); i++) {
            final BodyPart part = new BodyPart();
            part.fromNBT(bodyParts.getCompoundTagAt(i));
            this.bodyParts.add(part);
        }
    }
}
