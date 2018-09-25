package msifeed.mc.aorta.config;

import cpw.mods.fml.common.FMLCommonHandler;

public enum ConfigMode {
    CLIENT(true, true), SYNC(false, true), SERVER(false, false);

    public final boolean clientConfig;
    public final boolean sync;

    ConfigMode(boolean cc, boolean s) {
        this.clientConfig = cc;
        this.sync = s;
    }

    public boolean useConfigFile() {
        return FMLCommonHandler.instance().getSide().isServer() || clientConfig;
    }
}
