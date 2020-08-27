package msifeed.mc.core;

import msifeed.mc.more.crabs.character.Character;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

public class JourneymapProxyClient extends JourneymapProxy {
    @Override
    public boolean isVisibleOnMap(EntityLivingBase entity) {
        if (entity == null)
            return false;

        if (!CharacterAttribute.require(Minecraft.getMinecraft().thePlayer).visibleOnMap)
            return false;

        final Character c = CharacterAttribute.get(entity).orElse(null);
        return c == null || c.visibleOnMap;
    }
}
