package msifeed.mc.mellow.mc;

import msifeed.mc.mellow.widgets.Scene;
import net.minecraft.client.gui.GuiScreen;

public class MellowGuiScreen extends GuiScreen {
    protected Scene scene = new Scene();

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        scene.render();
    }
}
