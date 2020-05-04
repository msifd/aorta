package msifeed.mc.extensions.chat;

import msifeed.mc.commons.traits.Trait;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;

public enum GmSpeech {
    INSTANCE;

    private HashMap<String, Preferences> settings = new HashMap<>();

    public static Preferences get(String username) {
        return INSTANCE.settings.computeIfAbsent(username, k -> new Preferences());
    }

    public static boolean shouldUseGmsay(EntityPlayerMP player) {
        if (!CharacterAttribute.has(player,Trait.gm))
            return false;
        final Preferences s = INSTANCE.settings.get(player.getCommandSenderName());
        return s != null && s.replaceSpeech;
    }

    public static class Preferences {
        public boolean replaceSpeech = false;
        public int range = 15;
        public EnumChatFormatting color = EnumChatFormatting.DARK_PURPLE;
        public String prefix = "";
    }
}
