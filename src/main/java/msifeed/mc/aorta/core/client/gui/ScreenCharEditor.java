package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Button;
import msifeed.mc.mellow.widgets.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Layout sceneLayout = scene.getLayout();

        final Window window = new Window(scene);
        window.setSizeHint(200, 100);
        window.setTitle("Title goes here");
        sceneLayout.addWidget(window);

        final VerticalLayout windowLayout = new VerticalLayout(window);
        windowLayout.getMargin().set(2);
        window.setLayout(windowLayout);

        final Button btn = new Button(window, "pew");
        btn.setSizeHint(100, 50);
        btn.setClickCallback(() -> System.out.println("meow!"));
        windowLayout.addWidget(btn);
    }

}
