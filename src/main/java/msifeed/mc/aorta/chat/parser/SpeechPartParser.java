package msifeed.mc.aorta.chat.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpeechPartParser {
    private static final Set<Integer> IGNORE_CODES = "*".codePoints().boxed().collect(Collectors.toSet());

    public static List<SpeechPart> parse(String text) {
        final ArrayList<SpeechPart> parts = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();

        boolean ignoreBlock = false;
        SpeechPart.PartType prevType = null;
        for (int code : text.codePoints().toArray()) {
            final SpeechPart.PartType currCodeType = getPart(code);
            if (prevType != SpeechPart.PartType.IGNORE_CODE && currCodeType == SpeechPart.PartType.IGNORE_CODE)
                ignoreBlock = !ignoreBlock;

            if (prevType != currCodeType && sb.length() > 0) {
                parts.add(new SpeechPart(sb.toString(), prevType));
                sb.setLength(0);
            }
            sb.appendCodePoint(code);
            prevType = ignoreBlock && currCodeType != SpeechPart.PartType.IGNORE_CODE ? SpeechPart.PartType.IGNORE : currCodeType;
        }

        if (sb.length() > 0)
            parts.add(new SpeechPart(sb.toString(), prevType));

        return parts;
    }

    private static SpeechPart.PartType getPart(int codePoint) {
        if (Character.isLetter(codePoint))
            return SpeechPart.PartType.WORD;
        if (Character.isWhitespace(codePoint))
            return SpeechPart.PartType.WHITESPACE;
        else if (IGNORE_CODES.contains(codePoint))
            return SpeechPart.PartType.IGNORE_CODE;
        else
            return SpeechPart.PartType.PUNCTUATION;
    }

    private static String joinParts(List<SpeechPart> parts) {
        return parts.stream()
                .map(part -> part.text)
                .collect(Collectors.joining());
    }

}
