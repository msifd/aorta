package msifeed.mc.aorta.core.chat;

import net.minecraft.util.ChatComponentText;

import java.util.List;

public interface LangProcessor {
    List<ChatComponentText> process(List<ChatComponentText> components);
}
