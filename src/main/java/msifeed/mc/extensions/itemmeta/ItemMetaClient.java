package msifeed.mc.extensions.itemmeta;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.commons.traits.Trait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.Arrays;

public class ItemMetaClient {
    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltip(ItemTooltipEvent event) {
        if (!isGameMaster(event.entityPlayer) && !isOwner(event.entityPlayer, event.itemStack))
            return;

        final String owner = ItemMetaProvider.getOwner(event.itemStack);
        final String[] lines = ItemMetaProvider.getLines(event.itemStack);

        if (owner.isEmpty() && lines.length == 0)
            return;

//        final String header = " = Meta: " + "(owner: " + (owner.isEmpty() ? "???" : owner) + ')';
        final String header = String.format("# Meta (%s)", (owner.isEmpty() ? "???" : owner));
        event.toolTip.add(getRandomFormattingColor() + header);
        event.toolTip.addAll(Arrays.asList(lines));
    }

    private boolean isGameMaster(EntityPlayer player) {
        return CharacterAttribute.has(player, Trait.gm);
    }

    private boolean isOwner(EntityPlayer player, ItemStack itemStack) {
        return ItemMetaProvider.getOwner(itemStack).equalsIgnoreCase(player.getCommandSenderName());
    }

    private String getRandomFormattingColor() {
        final int second = (int) (System.currentTimeMillis() / 1000);
        final int minStyleOrd = 1;
        final int maxStyleOrd = 15;
        final int ord = minStyleOrd + second % (maxStyleOrd - minStyleOrd);
        final char code = EnumChatFormatting.values()[ord].getFormattingCode();
        return "\u00A7" + code;
    }
}
