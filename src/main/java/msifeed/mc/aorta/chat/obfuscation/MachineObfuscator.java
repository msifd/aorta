package msifeed.mc.aorta.chat.obfuscation;

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
    public List<String> obfuscate(List<String> parts) {
        return parts.stream()
                .map(part -> ObfuscationUtils.isWordPart(part) ? randomBeep(part) : part)
                .collect(Collectors.toList());
    }

    private static String randomBeep(String word) {
        final Random random = ObfuscationUtils.stringSeededRandom(word);
        return BEEPS[random.nextInt(BEEPS.length)];
    }
}
