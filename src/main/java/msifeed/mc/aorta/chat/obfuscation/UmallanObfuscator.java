package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.parser.SpeechPart;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UmallanObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(List<SpeechPart> parts) {
        final String letters = getShuffledLetters(parts);
        final int[] offset = {0};
        return parts.stream()
                .map(part -> {
                    final String text = part.text;
                    if (part.isWord()) {
                        final String sub = letters.substring(offset[0], text.length());
                        offset[0] += text.length();
                        return sub;
                    } else {
                        return text;
                    }
                })
                .collect(Collectors.joining());
    }

    private static String getShuffledLetters(List<SpeechPart> parts) {
        final List<Integer> codes = parts.stream()
                .filter(SpeechPart::isWord)
                .map(part -> part.text)
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
