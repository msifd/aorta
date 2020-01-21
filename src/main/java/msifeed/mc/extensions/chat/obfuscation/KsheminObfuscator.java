package msifeed.mc.extensions.chat.obfuscation;

import msifeed.mc.extensions.chat.composer.parser.SpeechToken;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KsheminObfuscator implements LangObfuscator {
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
                sb.appendCodePoint(ObfuscationUtils.CONSONANTS.get(random.nextInt(ObfuscationUtils.CONSONANTS.size())));
            else if (ObfuscationUtils.CONSONANTS_SET.contains(code))
                sb.appendCodePoint(ObfuscationUtils.VOWELS.get(random.nextInt(ObfuscationUtils.VOWELS.size())));
            else
                sb.appendCodePoint(code);
        }

        return sb.toString();
    }
}