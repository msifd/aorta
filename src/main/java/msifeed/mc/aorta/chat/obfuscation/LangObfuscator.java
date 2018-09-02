package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.parser.SpeechToken;

import java.util.List;

public interface LangObfuscator {
    String obfuscate(List<SpeechToken> tokens);
}
