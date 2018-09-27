package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.composer.parser.SpeechToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AistemiaObfuscator implements LangObfuscator {
    private static final ArrayList<Integer> VOWELS = new ArrayList<>(ObfuscationUtils.VOWELS);
    private static final ArrayList<Integer> CONSONANTS = new ArrayList<>(ObfuscationUtils.CONSONANTS);

    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.isWord() ? shuffleWord(part.text) : part.text)
                .collect(Collectors.joining());
    }

    private static String shuffleWord(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final StringBuilder sb = new StringBuilder();

        for (int code : word.toLowerCase().codePoints().toArray()) {
            sb.appendCodePoint(code);
            if (ObfuscationUtils.VOWELS.contains(code))
                sb.appendCodePoint(CONSONANTS.get(random.nextInt(CONSONANTS.size())));
            else if (ObfuscationUtils.CONSONANTS.contains(code))
                sb.appendCodePoint(VOWELS.get(random.nextInt(VOWELS.size())));
        }

        return sb.toString();
    }
}