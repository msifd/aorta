package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.chat.ChatComponentComposer;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.composer.parser.SpeechToken;
import msifeed.mc.aorta.chat.composer.parser.SpeechTokenParser;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.chat.usage.LangAttribute;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

import java.util.List;

class SpeechComposer extends ChatMessageComposer {
    static SpeechComposer INSTANCE = new SpeechComposer();
    private static final int[] LOUDNESS_RADIUS = {2, 5, 15, 30, 60};

    @Override
    ChatMessage compose(SpeechType type, EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.SPEECH;
        message.language = LangAttribute.get(player).orElse(Language.MENALA);
        message.radius = getSpeechRadius(text);
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    IChatComponent format(ChatMessage message) {
        final String text;

        if (!isMyNameIs(message.speaker) && !doIKnowLanguage(message.language))
            text = obfuscateWith(message.language.obfuscator, message.text);
        else
            text = message.text;

        final ChatComponentText root = new ChatComponentText("");
        root.appendText(text);

        if (!message.speaker.isEmpty())
            ChatComponentComposer.addNamePrefix(root, message.speaker, isMyNameIs(message.speaker));

        return root;
    }

    private static boolean isMyNameIs(String name) {
        return Minecraft.getMinecraft().thePlayer.getCommandSenderName().equalsIgnoreCase(name);
    }

    private static boolean doIKnowLanguage(Language language) {
        return CharacterAttribute.has(Minecraft.getMinecraft().thePlayer, language.trait);
    }

    private static String obfuscateWith(LangObfuscator obfuscator, String text) {
        final List<SpeechToken> parts = SpeechTokenParser.parse(text);
        return obfuscator.obfuscate(parts);
    }

    private static int getSpeechRadius(String text) {
        int loudness = (LOUDNESS_RADIUS.length - 1) / 2;

        int exclamations = 0;
        for (int i = text.length() - 1; i >= 0; --i) {
            switch (text.charAt(i)) {
                case '!':
                    exclamations++;
                case '?':
                    continue;
            }
            break;
        }
        loudness += exclamations;

        if (exclamations == 0) {
            int silencers = 0;
            for (int i = 0; i < text.length(); ++i) {
                if (text.charAt(i) == '(' && text.charAt(text.length() - i - 1) == ')')
                    silencers++;
                else
                    break;
            }
            loudness -= silencers;
        }

        loudness = MathHelper.clamp_int(loudness, 0, LOUDNESS_RADIUS.length - 1);

        return LOUDNESS_RADIUS[loudness];
    }
}
