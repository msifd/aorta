package msifeed.mc.extensions.chat.obfuscation;

import msifeed.mc.extensions.chat.composer.parser.SpeechToken;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MachineObfuscator implements LangObfuscator {
    private static final String[] BEEPS = {
            "бип", "беп", "бап", "боп", "буп",
            "пип", "пеп", "пап", "поп", "пуп",
            "бииип", "бааап", "боооп", "буууп",
            "бип-бип", "бип-беп", "бип-бап", "бип-боп", "бип-буп"
    };

    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.isWord() ? randomBeep(part.text) : part.text)
                .collect(Collectors.joining());
    }

    private static String randomBeep(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        return BEEPS[random.nextInt(BEEPS.length)];
    }
}
