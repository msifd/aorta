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
    private static final Item iconAorta = new Item();
    private static final Item iconTools = new Item();

    public static final AortaCreativeTab BLOCKS = new AortaCreativeTab("aorta.blocks", iconAorta);
    public static final AortaCreativeTab ITEMS = new AortaCreativeTab("aorta.items", iconAorta);
    public static final AortaCreativeTab TOOLS = new AortaCreativeTab("aorta.tools", iconTools);

    static {
        iconAorta.setTextureName("aorta:tab_aorta");
        iconTools.setTextureName("aorta:tab_tools");
        GameRegistry.registerItem(iconAorta, "tab_aorta");
        GameRegistry.registerItem(iconTools, "tab_tools");
    }

    private final Item icon;
    private ArrayList<ItemStack> cache = new ArrayList<>();

    private AortaCreativeTab(String name, Item icon) {
        super(name);
        this.icon = icon;
    }

    @Override
    public Item getTabIconItem() {
        return icon;
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
