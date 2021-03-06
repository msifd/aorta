package msifeed.mc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import msifeed.mc.more.More;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.config.ConfigManager;

@Mod(modid = Bootstrap.MODID, name = Bootstrap.NAME, version = Bootstrap.VERSION)
public class Bootstrap {
    public static final String MODID = "more";
    public static final String NAME = "More";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:openmods";

    @Mod.Instance
    public static Bootstrap INSTANCE;

    @SidedProxy(
            serverSide = "msifeed.mc.more.More",
            clientSide = "msifeed.mc.more.MoreClient"
    )
    public static More MORE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AttributeHandler.preInit();
        ConfigManager.preInit(event);
        MORE.preInit();
        ConfigManager.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MORE.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MORE.postInit();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        MORE.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
//        ConfigManager.save();
    }
}
