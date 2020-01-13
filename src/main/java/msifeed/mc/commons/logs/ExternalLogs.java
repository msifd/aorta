package msifeed.mc.commons.logs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;

public class ExternalLogs {
    private static final DBHandler dbHandler = new DBHandler();

    public void init() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            FMLCommonHandler.instance().bus().register(this);
            MinecraftForge.EVENT_BUS.register(this);
            MinecraftForge.EVENT_BUS.register(dbHandler);
        }
    }

    public static void log(ICommandSender sender, String type, String message) {
        if (FMLCommonHandler.instance().getSide().isServer())
            dbHandler.logCommand(sender, type, message);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote)
            log(event.player, "log", "[login]");
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.player.worldObj.isRemote)
            log(event.player, "log", "[logout]");
    }
}
