package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.parser.SpeechToken;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TervilianObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        final LinkedList<String> words = tokens.stream()
                .filter(SpeechToken::isWord)
                .map(part -> part.text)
                .map(TervilianObfuscator::shuffleWord)
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(words, ObfuscationUtils.nonrandomRandom());

        return tokens.stream()
                .map(part -> part.isWord() ? words.removeFirst() : part.text)
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
