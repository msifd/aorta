package msifeed.mc.aorta.core.client.gui;

import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Button;
import msifeed.mc.mellow.widgets.Label;
import msifeed.mc.mellow.widgets.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenCharEditor extends MellowGuiScreen {
    private final EntityLivingBase entity;

    public ScreenCharEditor(EntityLivingBase entity) {
        this.entity = entity;

        final Layout sceneLayout = scene.getLayout();

        final Window window = new Window(scene);
        window.setSizeHint(200, 200);
        window.setTitle("Char Editor");
        sceneLayout.addWidget(window);

        final VerticalLayout windowLayout = new VerticalLayout(window);
        windowLayout.getMargin().set(2);
        window.setLayout(windowLayout);


        final Label nameLabel = new Label(window, "Char: " + entity.getCommandSenderName());
        windowLayout.addWidget(nameLabel);



        final Button btn = new Button(window, "Kill");
        btn.setSizeHint(100, 50);
        btn.setClickCallback(entity::setDead);
        windowLayout.addWidget(btn);
    }

}
