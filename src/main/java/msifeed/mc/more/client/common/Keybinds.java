package msifeed.mc.more.client.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;

public enum Keybinds {
    INSTANCE;

    private final KeyBinding statusScreen = new KeyBinding("more.gui.status", Keyboard.KEY_I, Bootstrap.MODID);
    private final KeyBinding combatScreen = new KeyBinding("more.gui.combat", Keyboard.KEY_O, Bootstrap.MODID);
    private final HashMap<KeyBinding, Runnable> callbacks = new HashMap<>();

    public void init() {
        callbacks.put(statusScreen, () -> More.GUI_HANDLER.toggleStatusEditor(Minecraft.getMinecraft().thePlayer));
        callbacks.put(combatScreen, () -> More.GUI_HANDLER.toggleCombat(Minecraft.getMinecraft().thePlayer));

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
