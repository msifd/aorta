package msifeed.mc.aorta.chat.obfuscation;

import java.util.List;

public class VanillaObfuscator implements LangObfuscator {
    @Override
    public List<String> obfuscate(List<String> parts) {
        return parts;
    }
}
