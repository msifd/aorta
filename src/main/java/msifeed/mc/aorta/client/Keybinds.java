package msifeed.mc.aorta.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;

public class Keybinds {
//    public static final KeyBinding gmsayScreen = new KeyBinding("key.aorta.gmsay", Keyboard.KEY_LEFT, "key.aorta");

    public static void register() {
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(
                new KeyBinding[]{},
                Minecraft.getMinecraft().gameSettings.keyBindings);
    }
}
