package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.composer.parser.SpeechToken;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GurhkObfuscator implements LangObfuscator {
    private static final String LETTERS = "укгхашщирко";

    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.isWord() ? randomLetters(part.text) : part.text)
                .collect(Collectors.joining());
    }

    private static String randomLetters(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            final int ci = random.nextInt(LETTERS.length());
            sb.appendCodePoint(LETTERS.codePointAt(ci));
        }
        return sb.toString().toLowerCase();
    }
}
