package msifeed.mc.aorta.core.character;

import net.minecraft.entity.EntityLivingBase;

public class CharacterFactory {
    public static Character forEntity(EntityLivingBase entity) {
        final Character c = new Character();

        final Feature[] featureEnum = Feature.values();
        for (Feature f : featureEnum) {
            c.features.put(f, Grade.NORMAL);
        }

        return c;
    }
}
