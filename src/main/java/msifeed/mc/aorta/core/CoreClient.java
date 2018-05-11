package msifeed.mc.aorta.core;

import msifeed.mc.aorta.core.client.DebugHud;

public class CoreClient extends Core {
    @Override
    public void init() {
        super.init();

        DebugHud.INSTANCE.register();
    }
}
