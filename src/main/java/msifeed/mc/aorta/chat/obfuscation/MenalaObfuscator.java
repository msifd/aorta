package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.parser.SpeechPart;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MenalaObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(List<SpeechPart> parts) {
        return parts.stream()
                .map(part -> part.isWord() ? shuffleWord(part.text) : part.text)
                .collect(Collectors.joining());
    }

    private static String shuffleWord(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final List<Integer> codes = word.codePoints().boxed().collect(Collectors.toList());
        Collections.shuffle(codes, random);
        return codes.stream()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toLowerCase();
    }
}