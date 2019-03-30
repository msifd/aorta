package msifeed.mc.aorta.logs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Logs {
    INSTANCE;

    static Logger LOGGER = LogManager.getLogger("Aorta.Logs");
    private static DBHandler dbHandler = new DBHandler();

    public static void init() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            MinecraftForge.EVENT_BUS.register(dbHandler);
            FMLCommonHandler.instance().bus().register(INSTANCE);
        }
    }

    public static void log(ICommandSender sender, String type, String message) {
        if (FMLCommonHandler.instance().getSide().isServer())
            dbHandler.logCommand(sender, type, message);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote)
            log(event.player, "login", "[login]");
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!event.player.worldObj.isRemote)
            log(event.player, "logout", "[logout]");
    }
}
