package msifeed.mc.aorta.chat.gm;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.traits.Trait;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;

public enum GmSpeech {
    INSTANCE;

    private HashMap<String, GmsaySettings> settings = new HashMap<>();

    public static GmsaySettings get(String username) {
        INSTANCE.settings.putIfAbsent(username, new GmsaySettings());
        return INSTANCE.settings.get(username);
    }

    public static boolean shouldUseGmsay(EntityPlayerMP player) {
        if (!CharacterAttribute.has(player,Trait.gm))
            return false;
        final GmsaySettings s = INSTANCE.settings.get(player.getCommandSenderName());
        return s != null && s.replaceSpeech;
    }
}
