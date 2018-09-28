package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.composer.parser.SpeechToken;

import java.util.List;
import java.util.stream.Collectors;

public class UnderwaterObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.isWord() ? shuffleWord(part.text) : part.text)
                .collect(Collectors.joining());
    }

    private static String shuffleWord(String word) {
        word = word.toLowerCase();
        final StringBuilder sb = new StringBuilder();
        for (int code : word.codePoints().toArray()) {
            if (!ObfuscationUtils.CONSONANTS_SET.contains(code))
                sb.appendCodePoint(code);
        }
        return sb.toString();
    }
}