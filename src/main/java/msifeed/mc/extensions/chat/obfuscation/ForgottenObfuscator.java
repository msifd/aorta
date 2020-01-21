package msifeed.mc.extensions.chat.obfuscation;

import msifeed.mc.extensions.chat.composer.parser.SpeechToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ForgottenObfuscator implements LangObfuscator {
    private static final ArrayList<Integer> RUS_CHARS = new ArrayList<>(ObfuscationUtils.RUS_CHARS);

    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.isWord() ? shuffleWord(part.text) : part.text)
                .collect(Collectors.joining());
    }

    private static String shuffleWord(String word) {
        word = word.toLowerCase();
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final StringBuilder sb = new StringBuilder();

        for (int code : word.codePoints().toArray()) {
            if (ObfuscationUtils.VOWELS_SET.contains(code))
                sb.appendCodePoint(RUS_CHARS.get(random.nextInt(RUS_CHARS.size())));
            else
                sb.appendCodePoint(code);
        }

        return sb.toString();
    }
}