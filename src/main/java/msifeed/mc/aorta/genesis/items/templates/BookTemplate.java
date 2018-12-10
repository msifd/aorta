package msifeed.mc.aorta.genesis.items.templates;

import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.genesis.items.ItemGenesisUnit;
import msifeed.mc.aorta.genesis.items.data.BookData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BookTemplate extends ItemTemplate {
    private final BookData data;

    public BookTemplate(ItemGenesisUnit unit, BookData data) {
        super(unit);
        this.data = data;
    }

    public BookData getData() {
        return data;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        Aorta.GUI_HANDLER.toggleBookViewer(player);
        return itemStack;
    }
}
