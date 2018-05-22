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

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        super.keyTyped(p_73869_1_, p_73869_2_);
    }
}
