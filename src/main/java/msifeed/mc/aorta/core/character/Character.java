package msifeed.mc.aorta.core.character;

import msifeed.mc.commons.traits.Trait;
import msifeed.mc.commons.traits.TraitDecoder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;

import java.util.*;

public class Character {
    public String name = "";
    public String wikiPage = "";

    public Map<Feature, Integer> features = new EnumMap<>(Feature.class);
    public Map<String, BodyPart> bodyParts = new LinkedHashMap<>();
    public Map<String, Integer> addictions = new HashMap<>();
    public Set<Trait> traits = new HashSet<>();
    public byte vitalityRate = 40;
    public byte maxPsionics = 0;

    public BodyShield shield = new BodyShield();
    public Illness illness = new Illness();

    public byte load = 0;
    public byte sanity = 100;
    public byte psionics = 0;

    public Character() {
        for (Feature f : Feature.mainFeatures())
            features.put(f, 5);
    }

    public Character(Character c) {
        name = c.name;
        wikiPage = c.wikiPage;
        for (Map.Entry<Feature, Integer> e : c.features.entrySet())
            features.put(e.getKey(), e.getValue());
        for (BodyPart bp : c.bodyParts.values())
            bodyParts.put(bp.name, new BodyPart(bp));
        for (Map.Entry<String, Integer> e : c.addictions.entrySet())
            addictions.put(e.getKey(), e.getValue());
        traits.addAll(c.traits);
        vitalityRate = c.vitalityRate;
        maxPsionics = c.maxPsionics;
        shield.unpack(c.shield.pack());
        illness.unpack(c.illness.pack());
        load = c.load;
        sanity = c.sanity;
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

    public int countVitality() {
        final int currentHealth = bodyParts.values().stream().mapToInt(BodyPart::getHealth).sum();
        final int vitalityThreshold = Math.round((100 - vitalityRate) * countMaxHealth() / 100f);
        return Math.max(0, currentHealth - vitalityThreshold);
    }

    public int countMaxVitality() {
        return Math.round(vitalityRate * countMaxHealth() / 100f);
    }

    public int vitalityLevel() {
        if (isDeadByVitalPart())
            return 4;
        return vitalityLevel(countVitality(), countMaxVitality());
    }

    public int vitalityLevel(int vitality, int maxVitality) {
        if (maxVitality <= 0)
            return 0;
        final int percent = 100 - (vitality * 100) / maxVitality;
        return MathHelper.clamp_int(percent / 25, 0, 4);
    }

    public int sanityLevel() {
        final int s = MathHelper.clamp_int(sanity, 1, 125);
        return Math.floorDiv(s - 1, 25);
    }

    public int psionicsLevel() {
        if (maxPsionics <= 0)
            return 0;
        final int p = MathHelper.clamp_int(psionics, 0, maxPsionics);
        final int percent = 100 - (p * 100) / maxPsionics;
        return MathHelper.clamp_int(percent / 25, 0, 4);
    }

    public boolean isDeadByVitalPart() {
        return bodyParts.values().stream().anyMatch(BodyPart::isVitalGone);
    }

    public NBTTagCompound toNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setString(Tags.name, name);
        c.setString(Tags.wiki, wikiPage);

        c.setIntArray(Tags.features, features.entrySet().stream()
                .filter(e -> e.getKey().isMain())
                .mapToInt(Map.Entry::getValue).toArray());

        final NBTTagList bodyParts = new NBTTagList();
        for (BodyPart p : this.bodyParts.values())
            bodyParts.appendTag(p.toNBT());
        c.setTag(Tags.bodyParts, bodyParts);

        final NBTTagList addictions = new NBTTagList();
        for (Map.Entry<String, Integer> e : this.addictions.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("drugType", e.getKey());
            tag.setInteger("addictionValue", e.getValue());
            addictions.appendTag(tag);
        }
        c.setTag(Tags.addictions, addictions);

        c.setIntArray(Tags.traits, traits.stream().mapToInt(t -> t.code).toArray());

        c.setByte(Tags.vitality, vitalityRate);
        c.setByte(Tags.maxPsionics, maxPsionics);

        c.setInteger(Tags.shield, shield.pack());
        c.setInteger(Tags.illness, illness.pack());

        c.setByte(Tags.load, load);
        c.setByte(Tags.sanity, sanity);
        c.setByte(Tags.psionics, psionics);

        return c;
    }

    public void fromNBT(NBTTagCompound c) {
        name = c.getString(Tags.name);
        wikiPage = c.getString(Tags.wiki);

        final int[] featuresArr = c.getIntArray(Tags.features);
        if (featuresArr.length == Feature.mainFeatures().length)
            for (Feature f : Feature.mainFeatures())
                features.put(f, featuresArr[f.ordinal()]);

        final NBTTagList bodyParts = c.getTagList(Tags.bodyParts, 10); // 10 - NBTTagCompound
        this.bodyParts.clear();
        for (int i = 0; i < bodyParts.tagCount(); i++) {
            final BodyPart bp = new BodyPart(bodyParts.getCompoundTagAt(i));
            this.bodyParts.put(bp.name, bp);
        }

        final NBTTagList addictions = c.getTagList(Tags.addictions, 10); // 10 - NBTTagCompound
        this.addictions.clear();
        for (int i = 0; i < addictions.tagCount(); i++) {
            final NBTTagCompound tag = addictions.getCompoundTagAt(i);
            this.addictions.put(tag.getString("drugType"), tag.getInteger("addictionValue"));
        }

        this.traits = TraitDecoder.decode(c.getIntArray(Tags.traits));

        this.vitalityRate = (byte) MathHelper.clamp_int(c.getByte(Tags.vitality), 0, 100);
        this.maxPsionics = c.getByte(Tags.maxPsionics);

        shield.unpack(c.getInteger(Tags.shield));
        illness.unpack(c.getInteger(Tags.illness));

        load = c.getByte(Tags.load);
        sanity = c.getByte(Tags.sanity);
        psionics = c.getByte(Tags.psionics);
    }

    private static class Tags {
        static final String name = "name";
        static final String wiki = "wiki";
        static final String features = "feats";
        static final String bodyParts = "parts";
        static final String addictions = "addictions";
        static final String traits = "traits";
        static final String vitality = "vitality";
        static final String maxPsionics = "maxPsi";
        static final String shield = "shield";
        static final String illness = "illness";
        static final String load = "load";
        static final String sanity = "sanity";
        static final String psionics = "psi";
    }
}
