package msifeed.mc.aorta.things;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.aorta.utils.AlphanumComparator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AortaCreativeTab extends CreativeTabs {
    public static final AortaCreativeTab BLOCKS = new AortaCreativeTab("aorta.blocks");
    public static final AortaCreativeTab ITEMS = new AortaCreativeTab("aorta.items");

    private static Item iconItem = new Item();

    static {
        iconItem.setTextureName("aorta:aorta_tab");
        GameRegistry.registerItem(iconItem, "aorta_tab");
    }

    private ArrayList<ItemStack> cache = new ArrayList<>();

    private AortaCreativeTab(String name) {
        super(name);
    }

    @Override
    public Item getTabIconItem() {
        return iconItem;
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void displayAllReleventItems(List list) {
        cache.clear();
        for (Object anItemRegistry : Item.itemRegistry) {
            Item item = (Item) anItemRegistry;
            if (item == null) continue;

            for (CreativeTabs tab : item.getCreativeTabs()) {
                if (tab == this) {
                    item.getSubItems(item, this, cache);
                }
            }
        }

        cache.sort(Comparator.comparing(ItemStack::getUnlocalizedName, new AlphanumComparator()));
        list.addAll(cache);

        if (this.func_111225_m() != null) {
            this.addEnchantmentBooksToList(list, this.func_111225_m());
        }
    }
}
