package msifeed.mc.aorta.client.gui.auction;

import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.tabs.TabArea;
import msifeed.mc.mellow.widgets.window.Window;

public class ScreenAuctionTerminal extends MellowGuiScreen {
    public ScreenAuctionTerminal() {
        final Window window = new Window();
        window.setTitle("auction terminal");
        scene.addChild(window);

        final Widget content = window.getContent();

        final TabArea tabs = new TabArea();
        tabs.setSizeHint(300, 200);
        tabs.setSizePolicy(SizePolicy.FIXED);
        tabs.addTab("Open lots", new OpenLotsTab());
        tabs.addTab("My lots", new MyLotsTab());
        tabs.addTab("Add lot", new AddLotTab());
        content.addChild(tabs);
    }
}
