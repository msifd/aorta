package msifeed.mc.aorta.client.gui;

import msifeed.mc.aorta.genesis.items.IItemTemplate;
import msifeed.mc.aorta.genesis.items.data.ItemRenderData;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ScreenItemDesigner extends MellowGuiScreen {
    public ScreenItemDesigner() {
        final Window window = new Window();
        window.setTitle("Designer tool");
        scene.addChild(window);

        final Widget content = window.getContent();
        content.setLayout(ListLayout.VERTICAL);

        final ItemStack stack = findCustomRenderItem();
        if (stack == null) {
            content.addChild(new Label("No custom render item"));
            return;
        }

        final Item item = stack.getItem();
        final ItemRenderData renderData = ((IItemTemplate) item).getUnit().renderData;
        content.addChild(new Label(item.getUnlocalizedName()));

        final Widget paramsGrid = new Widget();
        paramsGrid.setLayout(new GridLayout());
        content.addChild(paramsGrid);

        paramsGrid.addChild(new Label("scale"));
        final TextInput scaleInput = new TextInput();
        scaleInput.setText(String.valueOf(renderData.scale));
        scaleInput.setFilter(TextInput::isUnsignedFloat);
        scaleInput.getSizeHint().x = 40;
        paramsGrid.addChild(scaleInput);

        paramsGrid.addChild(new Label("thickness"));
        final TextInput thicknessInput = new TextInput();
        thicknessInput.setText(String.valueOf(renderData.thickness));
        thicknessInput.setFilter(TextInput::isUnsignedFloat);
        thicknessInput.getSizeHint().x = 40;
        paramsGrid.addChild(thicknessInput);

        paramsGrid.addChild(new Label("offset"));
        final TextInput offsetInput = new TextInput();
        offsetInput.setText(String.valueOf(renderData.offset));
        offsetInput.setFilter(TextInput::isSignedFloat);
        offsetInput.getSizeHint().x = 40;
        paramsGrid.addChild(offsetInput);

        paramsGrid.addChild(new Label("recess"));
        final TextInput recessInput = new TextInput();
        recessInput.setText(String.valueOf(renderData.recess));
        recessInput.setFilter(TextInput::isSignedFloat);
        recessInput.getSizeHint().x = 40;
        paramsGrid.addChild(recessInput);

        paramsGrid.addChild(new Label("rotation"));
        final TextInput rotationInput = new TextInput();
        rotationInput.setText(String.valueOf(renderData.rotation));
        rotationInput.setFilter(TextInput::isSignedFloat);
        rotationInput.getSizeHint().x = 40;
        paramsGrid.addChild(rotationInput);

        final ButtonLabel applyBtn = new ButtonLabel("Apply");
        applyBtn.setClickCallback(() -> {
            renderData.scale = scaleInput.getFloat();
            renderData.thickness = thicknessInput.getFloat();
            renderData.offset = offsetInput.getFloat();
            renderData.recess = recessInput.getFloat();
            renderData.rotation = rotationInput.getFloat();
        });
        content.addChild(applyBtn);
    }

    private ItemStack findCustomRenderItem() {
        final InventoryPlayer inv = Minecraft.getMinecraft().thePlayer.inventory;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = inv.mainInventory[i];
            if (stack == null || !(stack.getItem() instanceof IItemTemplate))
                continue;
            if (((IItemTemplate) stack.getItem()).getUnit().renderData != null) {
                inv.currentItem = i;
                return stack;
            }
        }
        return null;
    }
}
