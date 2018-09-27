package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.composer.parser.SpeechToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CommonObfuscator implements LangObfuscator {
    private static final int MIN_WORD_SIZE = 3;
    private static final int MAX_WORD_SIZE = 8;

    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return makeGroups(tokens).stream()
                .map(group -> {
                    if (isGroupWithWords(group)) return splitToWords(joinText(group));
                    else return joinText(group);
                })
                .collect(Collectors.joining());
    }

    private static List<List<SpeechToken>> makeGroups(List<SpeechToken> tokens) {
        final List<List<SpeechToken>> groups = new ArrayList<>();
        final List<SpeechToken> currentGroup = new ArrayList<>();
        boolean groupWithWords = false;

        for (SpeechToken token : tokens) {
            if (groupWithWords && token.isWhitespace())
                continue;

            if (groupWithWords != token.isWord() && !currentGroup.isEmpty()) {
                groups.add(new ArrayList<>(currentGroup));
                currentGroup.clear();
            }
            currentGroup.add(token);
            groupWithWords = token.isWord();
        }

        if (!currentGroup.isEmpty())
            groups.add(new ArrayList<>(currentGroup));

        return groups;
    }

    private static boolean isGroupWithWords(List<SpeechToken> tokens) {
        return tokens.stream().anyMatch(SpeechToken::isWord);
    }

    private static String joinText(List<SpeechToken> tokens) {
        return tokens.stream().map(part -> part.text).collect(Collectors.joining());
    }

    private static String splitToWords(String str) {
        if (str.length() < MIN_WORD_SIZE * 2)
            return str;

        final Random random = ObfuscationUtils.stringSeededRandom(str);
        final StringBuilder sb = new StringBuilder();
        int offset = 0;

        while (offset < str.length()) {
            int remainderBefore = str.length() - offset;
            int nextLength = Math.min(MIN_WORD_SIZE + random.nextInt(MAX_WORD_SIZE - MIN_WORD_SIZE), remainderBefore);
            int remainderAfter = remainderBefore - nextLength;

            if (remainderAfter > 0 && remainderAfter < MIN_WORD_SIZE) {
                final int required = MIN_WORD_SIZE - remainderAfter;
                final int available = nextLength - MIN_WORD_SIZE;
                if (nextLength + remainderAfter <= MAX_WORD_SIZE)
                    nextLength += remainderAfter;
                else
                    nextLength -= Math.min(required, available);
            }

            final String s = str.substring(offset, offset + nextLength);
            sb.append(s);
            offset += nextLength;
            if (offset < str.length())
                sb.append(' ');
        }

        return sb.toString().toLowerCase();
    }
}
