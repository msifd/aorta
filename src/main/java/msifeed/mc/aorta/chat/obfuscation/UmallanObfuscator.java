package msifeed.mc.aorta.chat.obfuscation;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UmallanObfuscator implements LangObfuscator {
    @Override
    public List<String> obfuscate(List<String> parts) {
        final String letters = getShuffledLetters(parts);
        final int[] offset = {0};
        return parts.stream()
                .map(part -> {
                    if (ObfuscationUtils.isWordPart(part)) {
                        final String sub = letters.substring(offset[0], part.length());
                        offset[0] += part.length();
                        return sub;
                    } else {
                        return part;
                    }
                })
                .collect(Collectors.toList());
    }

    private static String getShuffledLetters(List<String> parts) {
        final List<Integer> codes = parts.stream()
                .filter(ObfuscationUtils::isWordPart)
                .map(CharSequence::codePoints)
                .flatMap(IntStream::boxed)
                .collect(Collectors.toList());

        final Random random = ObfuscationUtils.codesSeededRandom(codes);
        Collections.shuffle(codes, random);

        return codes.stream()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
