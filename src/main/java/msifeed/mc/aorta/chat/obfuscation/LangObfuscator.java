package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.parser.SpeechPart;

import java.util.List;

public interface LangObfuscator {
    String obfuscate(List<SpeechPart> parts);
}
