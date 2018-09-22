package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.composer.parser.SpeechToken;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UmallanObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        final String letters = getShuffledLetters(tokens);

        final StringBuilder sb = new StringBuilder();
        int offset = 0;
        for (SpeechToken part : tokens) {
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

    private static String getShuffledLetters(List<SpeechToken> parts) {
        final List<Integer> codes = parts.stream()
                .filter(SpeechToken::isWord)
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
