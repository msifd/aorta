package msifeed.mc.aorta.chat.obfuscation;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MenalaObfuscator implements LangObfuscator {
    @Override
    public List<String> obfuscate(List<String> parts) {
        return parts.stream()
                .map(part -> ObfuscationUtils.isWordPart(part) ? shuffleWord(part) : part)
                .collect(Collectors.toList());
    }

    private static String shuffleWord(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final List<Integer> codes = word.toLowerCase().codePoints().boxed().collect(Collectors.toList());
        Collections.shuffle(codes, random);
        return codes.stream().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }
}