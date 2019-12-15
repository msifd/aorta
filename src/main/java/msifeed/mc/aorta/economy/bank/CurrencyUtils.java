package msifeed.mc.aorta.economy.bank;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.HashMap;
import java.util.Map;

public class CurrencyUtils {
    public static int countPlayerBudget(EntityPlayerMP player) {
        int budget = 0;
        for (ItemStack is : player.inventory.mainInventory) {
            final int faceValue = Currency.valueOf(is);
            if (faceValue > 0)
                budget += faceValue * is.stackSize;
        }
        return budget;
    }

    public static boolean removeCurrencyFromPlayer(EntityPlayerMP player, int amount) {
        final HashMap<ItemStack, Integer> currencyItems = new HashMap<>(); // stack -> inv pos
        final ItemStack[] inv = player.inventory.mainInventory;
        for (int i = 0; i < inv.length; i++) {
            final ItemStack is = inv[i];
            final int value = Currency.valueOf(is);
            if (value > 0 && value <= amount)
                currencyItems.put(is, i);
        }

        final HashMap<Integer, Integer> itemsDiff = new HashMap<>(); // inv pos -> reduce by
        int amountLeft = amount;

        for (Currency currency : Currency.values()) {
            if (amountLeft == 0) break;
            if (currency.value > amountLeft) continue;
            for (Map.Entry<ItemStack, Integer> e : currencyItems.entrySet()) {
                final ItemStack is = e.getKey();
                if (!currency.equals(is)) continue;
                final int unitsLeft = amountLeft / currency.value;
                final int unitsToTake = Math.min(is.stackSize, unitsLeft);
                itemsDiff.put(e.getValue(), unitsToTake);
                amountLeft -= unitsToTake * currency.value;
                if (amountLeft == 0) break;
            }
        }

        if (amountLeft > 0) {
            player.addChatMessage(new ChatComponentText("Cant take that amount without exchange"));
            return false;
        }

        for (Map.Entry<Integer, Integer> e : itemsDiff.entrySet())
            inv[e.getKey()].stackSize -= e.getValue();

        return true;
    }
}
