package msifeed.mc.aorta.chat.obfuscation;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GurhkObfuscator implements LangObfuscator {
    private static final String LETTERS = "укгхашщирко";

    @Override
    public List<String> obfuscate(List<String> parts) {
        return parts.stream()
                .map(part -> ObfuscationUtils.isWordPart(part) ? randomLetters(part) : part)
                .collect(Collectors.toList());
    }

    private static String randomLetters(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            final int ci = random.nextInt(LETTERS.length());
            sb.appendCodePoint(LETTERS.codePointAt(ci));
        }
        return sb.toString();
    }
}
