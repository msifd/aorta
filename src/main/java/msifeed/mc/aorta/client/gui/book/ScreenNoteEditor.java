package msifeed.mc.aorta.client.gui.book;

import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.input.TextArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.player.EntityPlayer;

public class ScreenNoteEditor extends MellowGuiScreen {
    public ScreenNoteEditor(EntityPlayer player) {
        final Window window = new Window();
        window.setTitle(L10n.tr("item.remote_note.name"));
        scene.addChild(window);

        final Widget content = window.getContent();

        final TextArea textArea = new TextArea();
        textArea.setSizeHint(305, 150);
        content.addChild(textArea);

        Widget.setFocused(textArea);
    }
}
