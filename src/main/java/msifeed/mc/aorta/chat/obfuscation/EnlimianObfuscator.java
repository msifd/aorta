package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.composer.parser.SpeechToken;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EnlimianObfuscator implements LangObfuscator {
    private static final List<Integer> VOWELS_REPLACEMENT = "ауие".chars().boxed().collect(Collectors.toList());
    private static final List<Integer> CONSONANTS_REPLACEMENT = "лмкзв".chars().boxed().collect(Collectors.toList());

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

        for (int code : word.toLowerCase().codePoints().toArray()) {
            if (ObfuscationUtils.VOWELS_SET.contains(code))
                sb.appendCodePoint(VOWELS_REPLACEMENT.get(random.nextInt(VOWELS_REPLACEMENT.size())));
            else if (ObfuscationUtils.CONSONANTS_SET.contains(code))
                sb.appendCodePoint(CONSONANTS_REPLACEMENT.get(random.nextInt(CONSONANTS_REPLACEMENT.size())));
            else
                sb.appendCodePoint(code);
        }

        return sb.toString();
    }
}