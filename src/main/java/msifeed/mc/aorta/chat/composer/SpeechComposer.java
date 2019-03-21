package msifeed.mc.aorta.chat.composer;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.chat.ChatComponentComposer;
import msifeed.mc.aorta.chat.Language;
import msifeed.mc.aorta.chat.composer.parser.SpeechToken;
import msifeed.mc.aorta.chat.composer.parser.SpeechTokenParser;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.chat.obfuscation.LangObfuscator;
import msifeed.mc.aorta.chat.usage.LangAttribute;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

import java.util.List;

public class SpeechComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.SPEECH;
        message.language = LangAttribute.get(player).orElse(Language.VANILLA);
        message.radius = getSpeechRadius(text);
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final String text;
        if (!isMyNameIs(self, message.speaker) && !doIKnowLanguage(self, message.language)) {
            text = obfuscateWith(message.language.obfuscator, message.text);
        } else {
            final String langPrefix = message.language.shortTr();
            final String finalPrefix = langPrefix.isEmpty() ? "" : "[" + langPrefix + "] ";
            text = finalPrefix + message.text;
        }

        final ChatComponentText root = new ChatComponentText("");
        root.appendText(text);

        if (!message.speaker.isEmpty())
            ChatComponentComposer.addNamePrefix(root, message.speaker, isMyNameIs(self, message.speaker));

        return root;
    }

    private static boolean isMyNameIs(EntityPlayer self, String name) {
        return self.getDisplayName().equals(name);
    }

    private static boolean doIKnowLanguage(EntityPlayer self, Language language) {
        return CharacterAttribute.has(self, language.trait);
    }

    private static String obfuscateWith(LangObfuscator obfuscator, String text) {
        final List<SpeechToken> parts = SpeechTokenParser.parse(text);
        return obfuscator.obfuscate(parts);
    }

    private static int getSpeechRadius(String text) {
        final int[] speechRadius = Aorta.DEFINES.get().chat.speechRadius;
        int loudness = (speechRadius.length - 1) / 2;

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

        loudness = MathHelper.clamp_int(loudness, 0, speechRadius.length - 1);

        return speechRadius[loudness];
    }
}
