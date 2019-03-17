package msifeed.mc.aorta.logs;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Logs {
    INSTANCE;

    static Logger LOGGER = LogManager.getLogger("Aorta.Logs");
    private static DBHandler dbHandler = new DBHandler();

    public static void init() {
        if (FMLCommonHandler.instance().getSide().isServer())
            MinecraftForge.EVENT_BUS.register(dbHandler);
    }

    public static void log(ICommandSender sender, String type, String message) {
        dbHandler.logCommand(sender, type, message);
        LOGGER.info("{}/{}: {}", sender.getCommandSenderName(), type, message);
    }
}
