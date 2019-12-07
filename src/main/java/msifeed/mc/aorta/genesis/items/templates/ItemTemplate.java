package msifeed.mc.aorta.genesis.items.templates;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.genesis.GenesisTrait;
import msifeed.mc.aorta.genesis.items.IItemTemplate;
import msifeed.mc.aorta.genesis.items.ItemCommons;
import msifeed.mc.aorta.genesis.items.ItemGenesisUnit;
import msifeed.mc.aorta.logs.Logs;
import msifeed.mc.aorta.sys.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemTemplate extends Item implements IItemTemplate {
    private final ItemGenesisUnit unit;

    public ItemTemplate(ItemGenesisUnit unit) {
        this.unit = unit;
        setUnlocalizedName(unit.id);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        final String name = unit.title != null
                ? unit.title
                : super.getItemStackDisplayName(itemStack);
        return unit.rarity.color.toString() + name;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean debug) {
        ItemCommons.addInformation(unit, itemStack, lines);
    }

    @Override
    public int getDamage(ItemStack stack) {
        if (unit.maxUsages > 0 && super.getDamage(stack) == 0)
            stack.setItemDamage(unit.maxUsages);
        return super.getDamage(stack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return unit.hasTrait(GenesisTrait.action_bow) ? EnumAction.bow : EnumAction.none;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        if (unit.maxUsages > 0 || unit.hasTrait(GenesisTrait.reusable))
            return 32;
        else if (unit.hasTrait(GenesisTrait.action_bow))
            return 72000;
        else
            return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return !unit.hasTrait(GenesisTrait.infinite_uses) && unit.hasTrait(GenesisTrait.reusable)
                && unit.maxUsages > 0 && itemStack.getItemDamage() < unit.maxUsages;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("needsReload") ?
                1 : 1 - (double)itemStack.getItemDamage() / unit.maxUsages;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        final int duration = getMaxItemUseDuration(itemStack);
        if (duration > 0)
            player.setItemInUse(itemStack, duration);
        return itemStack;
    }

    private String getUseText(EntityPlayer player, ItemStack itemStack, boolean special) {
        if (unit.hasTrait(GenesisTrait.reusable))
            if (unit.maxUsages == 0)
                return special ? "aorta.gen.attack_special" : "aorta.gen.attack";
            else
                if (itemStack.getTagCompound().getInteger("usages") == unit.maxUsages)
                    return "aorta.gen.reload";
                else
                    return special ? "aorta.gen.shot_special" : "aorta.gen.shot";

        return "aorta.gen.used";
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if (unit.maxUsages > 0) {
            final int u = itemStack.getItemDamage();
            boolean special = false;

            if (unit.specialAttackCost > 0 && player.isSneaking() && u >= unit.specialAttackCost)
                special = true;

            int cost = special ? unit.specialAttackCost : 1;

            if (u > cost) {
                itemStack.setItemDamage(u - cost);
            }
            else {
                if (!unit.hasTrait(GenesisTrait.reusable)) {
                    itemStack.stackSize--;

                    if (itemStack.stackSize > 0)
                        itemStack.setItemDamage(unit.maxUsages);
                } else {
                    if (unit.hasTrait(GenesisTrait.infinite_uses)) {
                        itemStack.setItemDamage(unit.maxUsages);
                    } else {
                        itemStack.setItemDamage(1);

                        if (!itemStack.hasTagCompound()) {
                            NBTTagCompound compound = new NBTTagCompound();
                            compound.setBoolean("needsReload", true);
                            itemStack.setTagCompound(compound);
                        } else {
                            boolean needsReload = itemStack.getTagCompound().getBoolean("needsReload");
                            itemStack.getTagCompound().setBoolean("needsReload", !needsReload);

                            if (needsReload)
                                itemStack.setItemDamage(unit.maxUsages);
                        }
                    }
                }
            }
            if (!world.isRemote) {
                final ChatMessage m = Composer.makeMessage(SpeechType.LOG, player, L10n.fmt(getUseText(player, itemStack, special), itemStack.getDisplayName()));
                m.speaker = player.getDisplayName();
                ChatHandler.sendSystemChatMessage(player, m);
                Logs.log(player, "log", m.text);

                if (unit.maxUsages > 0 && itemStack.getTagCompound().getInteger("usages") == 0)
                    player.addChatMessage(new ChatComponentText("§f" + L10n.fmt("aorta.gen.needs_reload")));
            }
        }
        return itemStack;
    }

    @Override
    public ItemGenesisUnit getUnit() {
        return unit;
    }
}
