package msifeed.mc.aorta.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.aorta.Aorta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public enum Keybinds {
    INSTANCE;

    private final KeyBinding statusScreen = new KeyBinding("aorta.gui.status", Keyboard.KEY_C, Bootstrap.MODID);
    private final KeyBinding rollerScreen = new KeyBinding("aorta.gui.roller", Keyboard.KEY_V, Bootstrap.MODID);
    private final KeyBinding langScreen = new KeyBinding("aorta.gui.lang_selector", Keyboard.KEY_Y, Bootstrap.MODID);
    private final HashMap<KeyBinding, Runnable> callbacks = new HashMap<>();

    public void init() {
        callbacks.put(statusScreen, () -> Aorta.GUI_HANDLER.toggleStatusEditor(Minecraft.getMinecraft().thePlayer));
        callbacks.put(rollerScreen, () -> Aorta.GUI_HANDLER.toggleRoller(Minecraft.getMinecraft().thePlayer));
        callbacks.put(langScreen, () -> Aorta.GUI_HANDLER.toggleLangSelector(Minecraft.getMinecraft().thePlayer));

        KeyBinding[] keyBindings = new KeyBinding[callbacks.size()];
        callbacks.keySet().toArray(keyBindings);
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(keyBindings, Minecraft.getMinecraft().gameSettings.keyBindings);

        FMLCommonHandler.instance().bus().register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        for (HashMap.Entry<KeyBinding, Runnable> e : callbacks.entrySet()) {
            if (Keyboard.isKeyDown(e.getKey().getKeyCode()))
                e.getValue().run();
        }
    }
}
