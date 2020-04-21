package msifeed.mc.extensions.chat.composer;

import msifeed.mc.extensions.chat.ChatDefines;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.LangAttribute;
import msifeed.mc.extensions.chat.Language;
import msifeed.mc.extensions.chat.composer.parser.SpeechToken;
import msifeed.mc.extensions.chat.composer.parser.SpeechTokenParser;
import msifeed.mc.extensions.chat.obfuscation.LangObfuscator;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

import java.util.List;
import java.util.Random;

public class SpeechComposer implements ChatComposer {
    @Override
    public ChatMessage compose(EntityPlayer player, String text) {
        final ChatMessage message = new ChatMessage();
        message.type = SpeechType.SPEECH;
        message.source = player.getPlayerCoordinates();
        message.language = LangAttribute.get(player).orElse(Language.VANILLA);
        message.radius = getSpeechRadius(text);
        message.senderId = player.getEntityId();
        message.speaker = player.getDisplayName();
        message.text = text;
        return message;
    }

    @Override
    public IChatComponent format(EntityPlayer self, ChatMessage message) {
        final boolean myMessage = isMyMessage(self, message);
        final boolean knowLang = doIKnowLanguage(self, message.language);

        final ChatComponentStyle root = new ChatComponentText("");

        final String name = message.speaker.isEmpty() ? self.getDisplayName() : message.speaker;
        root.appendSibling(getNamePrefix(name, isMyMessage(self, message)));

        final String langPrefix = message.language.shortTr();
        root.appendSibling(new ChatComponentText(knowLang && !langPrefix.isEmpty()
            ? ": [" + langPrefix + "] " : ": "));

        final String rawText;
        if (!myMessage && !knowLang)
            rawText = obfuscateWith(message.language.obfuscator, message.text);
        else
            rawText = message.text;
        root.appendSibling(applyDistance(rawText, self, message));

        return root;
    }

    private static ChatComponentText applyDistance(String text, EntityPlayer player, ChatMessage message) {
        final float distance = MathHelper.sqrt_float(player.getPlayerCoordinates().getDistanceSquaredToChunkCoordinates(message.source));
        final int thresholdDistance = More.DEFINES.get().chat.garble.thresholdDistance;

        if (distance > thresholdDistance) {
            final float garblness = (distance - thresholdDistance) / (float) message.radius;
            return garbleficate(text, garblness);
        } else {
            return new ChatComponentText(text);
        }
    }

    private static ChatComponentText getNamePrefix(String name, boolean myName) {
        final ChatComponentText cName = new ChatComponentText(name);
        cName.getChatStyle().setColor(myName ? EnumChatFormatting.YELLOW : EnumChatFormatting.GREEN);
        return cName;
    }

    private static boolean isMyMessage(EntityPlayer self, ChatMessage message) {
        return self.getEntityId() == message.senderId;
    }

    private static boolean doIKnowLanguage(EntityPlayer self, Language language) {
        return CharacterAttribute.has(self, language.trait);
    }

    public static String obfuscateWith(LangObfuscator obfuscator, String text) {
        final List<SpeechToken> parts = SpeechTokenParser.parse(text);
        return obfuscator.obfuscate(parts);
    }

    private static int getSpeechRadius(String text) {
        final int[] speechRadius = More.DEFINES.get().chat.speechRadius;
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
                if (text.charAt(i) == '(') silencers++;
                else break;
            }
            loudness -= silencers;
        }

        loudness = MathHelper.clamp_int(loudness, 0, speechRadius.length - 1);

        return speechRadius[loudness];
    }

    private static ChatComponentText garbleficate(String input, float garblness) {
        final ChatDefines.GarbleSettings settings = More.DEFINES.get().chat.garble;

        final Random rand = new Random();

        final ChatComponentText root = new ChatComponentText("");
        final EnumChatFormatting[] prevColor = {null};
        final StringBuilder sb = new StringBuilder();

        input.codePoints().forEach(cp -> {
            final double r = garblness + rand.nextFloat() / 2;

            final EnumChatFormatting color;
            if (r > settings.missThreshold) {
                color = prevColor[0];
            } else if (r > settings.darkGrayThreshold) {
                color = EnumChatFormatting.DARK_GRAY;
            } else if (r > settings.grayThreshold) {
                color = EnumChatFormatting.GRAY;
            } else {
                color = null;
            }

            if (color != prevColor[0] && sb.length() > 0) {
                final ChatComponentText cc = new ChatComponentText(sb.toString());
                cc.getChatStyle().setColor(prevColor[0]);
                root.appendSibling(cc);

                sb.setLength(0);
            }

            if (Character.isLetterOrDigit(cp) && r > settings.missThreshold) {
                sb.append(' ');
            } else {
                sb.appendCodePoint(cp);
            }

            prevColor[0] = color;
        });

        if (sb.length() > 0) {
            final ChatComponentText cc = new ChatComponentText(sb.toString());
            cc.getChatStyle().setColor(prevColor[0]);
            root.appendSibling(cc);
        }

        return root;
    }
}
