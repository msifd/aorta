package msifeed.mc.extensions.chat.composer;

import msifeed.mc.extensions.chat.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.util.EnumMap;

public class Composer {
    private static EnumMap<SpeechType, ChatComposer> COMPOSERS = new EnumMap<>(SpeechType.class);

    static {
        COMPOSERS.put(SpeechType.SPEECH, new SpeechComposer());
        COMPOSERS.put(SpeechType.OFFTOP, new OfftopComposer());
        COMPOSERS.put(SpeechType.GLOBAL, new GlobalComposer());
        COMPOSERS.put(SpeechType.GM, new GmsayComposer());
        COMPOSERS.put(SpeechType.GM_GLOBAL, new GmGlobalComposer());
        COMPOSERS.put(SpeechType.LOG, new LogComposer());
        COMPOSERS.put(SpeechType.ENV, new EnvComposer());
        COMPOSERS.put(SpeechType.ROLL, new RollComposer());
        COMPOSERS.put(SpeechType.COMBAT, new CombatComposer());
    }

    public static ChatMessage makeMessage(SpeechType type, EntityPlayer player, ChatComponentTranslation comp) {
        return makeMessage(type, player, getTextFromTranslation(comp));
    }

    public static ChatMessage makeMessage(SpeechType type, EntityPlayer player, String text) {
        return COMPOSERS.getOrDefault(type, COMPOSERS.get(SpeechType.SPEECH)).compose(player, text);
    }

    public static IChatComponent formatMessage(EntityPlayer self, ChatMessage message) {
        return COMPOSERS.getOrDefault(message.type, COMPOSERS.get(SpeechType.SPEECH)).format(self, message);
    }

    public static boolean canReceiveMessage(EntityPlayer self, ChatMessage message) {
        return COMPOSERS.getOrDefault(message.type, COMPOSERS.get(SpeechType.SPEECH)).canReceiveMessage(self, message);
    }

    private static String getTextFromTranslation(ChatComponentTranslation chatComponent) {
        final Object[] args = chatComponent.getFormatArgs();
        if (args.length == 0)
            return "";
        final Object formatObj = args[args.length - 1]; // pick last "text" arg
        if (formatObj instanceof IChatComponent)
            return ((IChatComponent) formatObj).getUnformattedText();
        else if (formatObj instanceof String)
            return (String) formatObj;
        else
            return chatComponent.getUnformattedText();
    }
}
