package msifeed.mc.aorta.chat.obfuscation;

import net.minecraft.util.ChatComponentText;

import java.util.List;

public interface LangObfuscator {
    List<ChatComponentText> obfuscate(List<ChatComponentText> components);
}
