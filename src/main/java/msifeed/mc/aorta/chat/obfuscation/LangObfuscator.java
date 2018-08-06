package msifeed.mc.aorta.chat.obfuscation;

import java.util.List;

public interface LangObfuscator {
    List<String> obfuscate(List<String> parts);
}
