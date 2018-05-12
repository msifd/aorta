package msifeed.mc.aorta.core.character;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;

public class CharacterFactory {
    public static Character forEntity(Entity entity) {
        final Character c = new Character();

        final Feature[] featureEnum = Feature.values();
        for (Feature f : featureEnum) {
            c.features.put(f, Grade.NORMAL);
        }

        if (entity instanceof EntityPlayer || entity instanceof EntityZombie) {
            addHumanoidParts(c);
        }

        return c;
    }

    private static void addHumanoidParts(Character c) {
        c.bodyParts.add(new BodyPart(BodyPart.Type.HEAD, 2));
        c.bodyParts.add(new BodyPart(BodyPart.Type.BODY, 5));
        c.bodyParts.add(new BodyPart(BodyPart.Type.HAND, 3));
        c.bodyParts.add(new BodyPart(BodyPart.Type.HAND, 3));
        c.bodyParts.add(new BodyPart(BodyPart.Type.LEG, 3));
        c.bodyParts.add(new BodyPart(BodyPart.Type.LEG, 3));
    }
}
