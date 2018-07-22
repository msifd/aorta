package msifeed.mc.aorta.chat.obfuscation;

import net.minecraft.util.ChatComponentText;

import java.util.List;

public interface LangObfuscator {
    void obfuscate(List<ChatComponentText> components);
}
