package msifeed.mc.aorta.sys.config;

import cpw.mods.fml.common.FMLCommonHandler;

public enum ConfigMode {
    CLIENT(true, true), SYNC(false, true), SERVER(false, false);

    public final boolean clientSide;
    public final boolean sync;

    ConfigMode(boolean client, boolean sync) {
        this.clientSide = client;
        this.sync = sync;
    }

    public boolean doFileIO() {
        return FMLCommonHandler.instance().getSide().isServer() || clientSide;
    }
}
