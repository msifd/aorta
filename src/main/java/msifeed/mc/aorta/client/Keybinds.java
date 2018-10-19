package msifeed.mc.aorta.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import msifeed.mc.aorta.Aorta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public enum Keybinds {
    INSTANCE;

    private final KeyBinding rollerScreen = new KeyBinding("key.aorta.roller", Keyboard.KEY_C, "key.aorta");
    private final KeyBinding fighterScreen = new KeyBinding("key.aorta.fighter", Keyboard.KEY_V, "key.aorta");
    private final HashMap<KeyBinding, Runnable> callbacks = new HashMap<>();

    public void init() {
        callbacks.put(rollerScreen, Aorta.GUI_HANDLER::toggleRoller);
        callbacks.put(fighterScreen, () -> Aorta.GUI_HANDLER.toggleFightHelper(Minecraft.getMinecraft().thePlayer));

        KeyBinding[] keyBindings = new KeyBinding[callbacks.size()];
        callbacks.keySet().toArray(keyBindings);
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(keyBindings, Minecraft.getMinecraft().gameSettings.keyBindings);

        FMLCommonHandler.instance().bus().register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        for (HashMap.Entry<KeyBinding, Runnable> e : callbacks.entrySet()) {
            if (e.getKey().isPressed())
                e.getValue().run();
        }
    }
}
