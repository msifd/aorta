package msifeed.mc.aorta.logs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import msifeed.mc.aorta.sys.rpc.Rpc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;

import java.util.List;

public enum LogsClient {
    INSTANCE;

    private GuiNewChat chatGui = null;
    private int sentMessagesSize = 0;

    public static void init() {
        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

        if (chatGui == null)
            chatGui = Minecraft.getMinecraft().ingameGUI.getChatGUI();

        final List<String> sent = chatGui.getSentMessages();
        final int sentSize = sent.size();
        if (sentSize > sentMessagesSize) {
            for (int i = sentMessagesSize; i < sentSize; i++) {
                final String s = sent.get(i);
                if (s.charAt(0) == '/')
                    logCommand(s);
            }
            sentMessagesSize = sentSize;
        } else if (sentSize == 0 && sentMessagesSize > 0) {
            sentMessagesSize = 0;
        }
    }

    private void logCommand(String cmd) {
        final String sub = cmd.substring(1);
        if (!sub.startsWith("g ") && !sub.startsWith("o "))
            Rpc.sendToServer(Logs.requestLogCommand, sub);
    }
}
