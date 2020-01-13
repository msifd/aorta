package msifeed.mc.extensions.chat.obfuscation;

import msifeed.mc.extensions.chat.composer.parser.SpeechToken;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TranscriptorObfuscator implements LangObfuscator {
    private static final List<Integer> REPLACEMENTS = "жшчщ".chars().boxed().collect(Collectors.toList());

    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        final List<SpeechToken> words = tokens.stream().filter(SpeechToken::isWord).collect(Collectors.toList());
        Collections.shuffle(words);
        final Iterator<SpeechToken> wordIter = words.iterator();
        return tokens.stream()
                .map(part -> part.isWord() ? shuffleWord(wordIter.next().text) : part.text)
                .collect(Collectors.joining());
    }

    private static String shuffleWord(String word) {
        word = word.toLowerCase();

        if (word.length() <= 3)
            return word;

        final Random random = ObfuscationUtils.stringSeededRandom(word);
        final StringBuilder sb = new StringBuilder();

        int charsToReplace = word.length() / 2 - 1;
        final int probability = word.length() / charsToReplace;

        for (int code : word.codePoints().toArray()) {
            if (charsToReplace > 0 && random.nextInt(word.length()) <= probability) {
                sb.appendCodePoint(REPLACEMENTS.get(random.nextInt(REPLACEMENTS.size())));
                charsToReplace--;
            }
            else
                sb.appendCodePoint(code);
        }

        return sb.toString();
    }
}