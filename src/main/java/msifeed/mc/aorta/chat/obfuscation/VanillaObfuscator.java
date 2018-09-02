package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.parser.SpeechToken;

import java.util.List;
import java.util.stream.Collectors;

public class VanillaObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(List<SpeechToken> tokens) {
        return tokens.stream()
                .map(part -> part.text)
                .collect(Collectors.joining());
    }
}
