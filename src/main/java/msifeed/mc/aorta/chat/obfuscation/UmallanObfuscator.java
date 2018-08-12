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

        final StringBuilder sb = new StringBuilder();
        int offset = 0;
        for (SpeechPart part : parts) {
            if (part.isWord()) {
                final String sub = letters.substring(offset, offset + part.text.length());
                offset += sub.length();
                sb.append(sub);
            } else {
                sb.append(part.text);
            }
        }

        return sb.toString();
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
                .toString()
                .toLowerCase();
    }
}
