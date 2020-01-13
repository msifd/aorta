package msifeed.mc.extensions.chat.obfuscation;

import msifeed.mc.extensions.chat.composer.parser.SpeechToken;

import java.util.List;

public interface LangObfuscator {
    String obfuscate(List<SpeechToken> tokens);
}
