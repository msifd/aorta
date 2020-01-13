package msifeed.mc.genesis;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.utils.AlphanumComparator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenesisCreativeTab extends CreativeTabs {
    private static final Item iconAorta = makeIcon("tab_aorta");
    private static final Item iconBlocks = makeIcon("tab_blocks");
    private static final Item iconItems = makeIcon("tab_items");
    private static final Item iconTools = makeIcon("tab_tools");

    public static final GenesisCreativeTab BLOCKS = new GenesisCreativeTab(Bootstrap.MODID + ".blocks", iconBlocks);
    public static final GenesisCreativeTab ITEMS = new GenesisCreativeTab(Bootstrap.MODID + ".items", iconItems);
    public static final GenesisCreativeTab TOOLS = new GenesisCreativeTab(Bootstrap.MODID + ".tools", iconTools);

    private final Item icon;
    private ArrayList<ItemStack> cache = new ArrayList<>();

    public GenesisCreativeTab(String name, Item icon) {
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

    public static void init() {
        // static init
    }

    public static Item makeIcon(String name) {
        final Item item = new Item();
        item.setTextureName(Bootstrap.MODID + ":" + name);
        GameRegistry.registerItem(item, name);
        return item;
    }
}
