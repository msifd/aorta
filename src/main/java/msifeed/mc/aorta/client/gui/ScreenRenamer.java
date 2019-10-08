package msifeed.mc.aorta.client.gui;

import msifeed.mc.aorta.genesis.rename.RenameProvider;
import msifeed.mc.aorta.genesis.rename.RenameRpc;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.mellow.widgets.text.TextInputArea;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.stream.Collectors;

public class ScreenRenamer extends MellowGuiScreen {
    private final TextInput titleInput = new TextInput();
    private final TextInputArea descInput = new TextInputArea();

    public ScreenRenamer(EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();
        if (heldItem == null)
            return;

        final Window window = new Window();
        window.setTitle(L10n.tr("aorta.gui.renamer"));
        scene.addChild(window);

        final Widget content = window.getContent();
        content.setLayout(ListLayout.VERTICAL);

        // // // //

        content.addChild(new Label(L10n.tr("aorta.gui.renamer.title")));
        content.addChild(titleInput);
        titleInput.setMaxLineWidth(120);
//        titleInput.getSizeHint().x = 120;
        titleInput.setText(RenameProvider.intoAmpersandFormatting(heldItem.getDisplayName()));
        titleInput.setFilter(s -> !s.startsWith(" "));

        content.addChild(new Label(L10n.tr("aorta.gui.renamer.desc")));
        content.addChild(descInput);
        descInput.setLines(RenameProvider.getDescription(heldItem)); // first line has title
        descInput.setSizeHint(120, 60);
        descInput.setSizePolicy(SizePolicy.Policy.MINIMUM, SizePolicy.Policy.MINIMUM);
        descInput.setMaxLineWidth(120);
        descInput.setLineLimit(4);
        descInput.getController().setMaxLines(8);
        descInput.setColor(descInput.getColor());

        // // // //

        final ButtonLabel applyBtn = new ButtonLabel(L10n.tr("aorta.gui.apply"));
        applyBtn.setClickCallback(() -> {
            RenameRpc.rename(titleInput.getText(), descInput.getLines().collect(Collectors.toList()));
        });
        content.addChild(applyBtn);
    }
}
