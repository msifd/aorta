package msifeed.mc.aorta.chat.composer.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpeechTokenParser {
    private static final Set<Integer> IGNORE_CODES = "*".codePoints().boxed().collect(Collectors.toSet());

    public static List<SpeechToken> parse(String text) {
        final ArrayList<SpeechToken> tokens = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();

        boolean ignoreBlock = false;
        TokenType prevType = null;
        for (int code : text.codePoints().toArray()) {
            final TokenType currCodeType = getPart(code);
            if (prevType != TokenType.IGNORE_CODE && currCodeType == TokenType.IGNORE_CODE)
                ignoreBlock = !ignoreBlock;

            if (prevType != currCodeType && sb.length() > 0) {
                tokens.add(new SpeechToken(sb.toString(), prevType));
                sb.setLength(0);
            }
            sb.appendCodePoint(code);
            prevType = ignoreBlock && currCodeType != TokenType.IGNORE_CODE ? TokenType.IGNORE : currCodeType;
        }

        if (sb.length() > 0)
            tokens.add(new SpeechToken(sb.toString(), prevType));

        return tokens;
    }

    private static TokenType getPart(int codePoint) {
        if (Character.isLetter(codePoint))
            return TokenType.WORD;
        if (Character.isWhitespace(codePoint))
            return TokenType.WHITESPACE;
        else if (IGNORE_CODES.contains(codePoint))
            return TokenType.IGNORE_CODE;
        else
            return TokenType.PUNCTUATION;
    }

    private static String joinTokens(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.text)
                .collect(Collectors.joining());
    }

}
