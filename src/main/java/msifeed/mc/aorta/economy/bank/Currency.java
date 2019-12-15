package msifeed.mc.aorta.economy.bank;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

public enum Currency {
    BUNDLE_5K("item.moneybundle_leg", 5000),
    BUNDLE_2K("item.moneybundle_ep", 2000),
    BUNDLE_1K("item.moneybundle_ra", 1000),
    BUNDLE_500("item.moneybundle_un", 500),
    BUNDLE_250("item.moneybundle", 250),
    BANKNOTE_100("item.currency_elvel_p100", 100),
    BANKNOTE_50("item.currency_elvel_p50", 50),
    BANKNOTE_10("item.currency_elvel_p10", 10),
    BANKNOTE_1("item.currency_elvel_p", 1);

    public final String itemId;
    public final int value;

    Currency(String itemId, int value) {
        this.itemId = itemId;
        this.value = value;
    }

    public boolean equals(ItemStack is) {
        return is.getUnlocalizedName().equals(itemId);
    }

    // //

    private static final HashMap<String, Integer> values = new HashMap<>();
    static {
        for (Currency c : values())
            values.put(c.itemId, c.value);
    }

    public static boolean isCurrency(ItemStack is) {
        if (is == null) return false;
        return values.containsKey(is.getUnlocalizedName());
    }

    public static int valueOf(ItemStack is) {
        if (is == null) return 0;
        final Integer v = values.get(is.getUnlocalizedName());
        return v != null ? v : 0;
    }
}
