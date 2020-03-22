package msifeed.mc.genesis.items.templates;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.ChatHandler;
import msifeed.mc.extensions.chat.ChatMessage;
import msifeed.mc.extensions.chat.composer.Composer;
import msifeed.mc.extensions.chat.composer.SpeechType;
import msifeed.mc.genesis.GenesisTrait;
import msifeed.mc.genesis.items.IItemTemplate;
import msifeed.mc.genesis.items.ItemCommons;
import msifeed.mc.genesis.items.ItemGenesisUnit;
import msifeed.mc.more.crabs.action.Action;
import msifeed.mc.more.crabs.action.ActionRegistry;
import msifeed.mc.more.crabs.combat.CombatContext;
import msifeed.mc.more.crabs.combat.CombatManager;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.crabs.utils.CombatMessages;
import msifeed.mc.sys.utils.L10n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
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
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return unit.hasTrait(GenesisTrait.action_bow) ? EnumAction.bow : EnumAction.none;
    }

    @Override
    public int getDamage(ItemStack itemStack) {
        final int damage = super.getDamage(itemStack);
        return damage > 0 ? damage : unit.durData.maxDurability;
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
        return unit.durData.maxDurability > 0 && itemStack.getItemDamage() < unit.durData.maxDurability;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1 - (double)itemStack.getItemDamage() / unit.durData.maxDurability;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (unit.durData.maxDurability > 0 && itemStack.getItemDamage() <= 1) {
            if (world.isRemote)
                player.addChatMessage(new ChatComponentText("§4" + L10n.fmt("more.gen.broken")));
            return itemStack;
        }

        final int duration = getMaxItemUseDuration(itemStack);
        if (duration > 0)
            player.setItemInUse(itemStack, duration);
        return itemStack;
    }

    private String getUseText(EntityPlayer player, ItemStack itemStack, boolean special) {
        if (unit.hasTrait(GenesisTrait.reusable))
            if (unit.maxUsages == 0)
                return special ? "more.gen.attack_special" : "more.gen.attack";
            else
                if (itemStack.getTagCompound().getInteger("usages") == unit.maxUsages)
                    return "more.gen.reload";
                else
                    return special ? "more.gen.shot_special" : "more.gen.shot";

        return "more.gen.used";
    }

    private void onUse(EntityPlayer player, ItemStack itemStack, boolean special) {
        if (unit.crabsData.action != null) {
            if (!player.worldObj.isRemote) {
                final CombatContext com = CombatAttribute.require(player);
                final Action newAction = ActionRegistry.getFullAction(unit.crabsData.action);
                if (CombatManager.INSTANCE.doAction(player, com, newAction))
                    CombatMessages.actionChanged(player, player, newAction);
            }
        }
    }

    private void onReload(EntityPlayer player, ItemStack itemStack, boolean special) {

    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if (unit.maxUsages > 0 || unit.hasTrait(GenesisTrait.reusable)) {
            if (!itemStack.hasTagCompound()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setInteger("usages", unit.maxUsages);
                itemStack.setTagCompound(compound);
            }

            final int usages = itemStack.getTagCompound().getInteger("usages");
            final boolean special = player.isSneaking()
                    && (unit.specialAttackCost > 0 && usages >= unit.specialAttackCost || unit.maxUsages == 0);
            final int cost = special ? unit.specialAttackCost : 1;

            if (!world.isRemote && usages > cost
                    || unit.hasTrait(GenesisTrait.reusable) && usages != 0 && unit.durData.maxDurability > 0) {
                final int damage = special ? unit.durData.getNextSpecialDamage() : unit.durData.getNextDamage();
                final int durability = Math.max(1, itemStack.getItemDamage() - damage);
                itemStack.setItemDamage(durability);
            }

            if (usages > cost) {
                itemStack.getTagCompound().setInteger("usages", usages - cost);

                if (!world.isRemote)
                    onUse(player, itemStack, special);
            } else {
                if (unit.hasTrait(GenesisTrait.reusable)) {
                    if (unit.maxUsages > 0) {
                        if (!world.isRemote)
                            if (usages != 0)
                                onUse(player, itemStack, special);
                            else
                                onReload(player, itemStack, special);

                        itemStack.getTagCompound().setInteger("usages", usages == 0 ? unit.maxUsages : 0);
                    } else {
                        if (!world.isRemote)
                            onUse(player, itemStack, special);
                    }
                } else {
                    if (!world.isRemote)
                        onUse(player, itemStack, special);
                    itemStack.stackSize--;

                    if (itemStack.stackSize > 0)
                        itemStack.getTagCompound().setInteger("usages", unit.maxUsages);
                }
            }
            if (!world.isRemote) {
                final ChatMessage m = Composer.makeMessage(SpeechType.LOG, player, L10n.fmt(getUseText(player, itemStack, special), itemStack.getDisplayName()));
                m.speaker = player.getDisplayName();
                ChatHandler.sendSystemChatMessage(player, m);
                ExternalLogs.log(player, "log", m.text);

                if (unit.maxUsages > 0 && itemStack.getTagCompound().getInteger("usages") == 0)
                    player.addChatMessage(new ChatComponentText("§f" + L10n.fmt("more.gen.needs_reload")));
            }
        }
        return itemStack;
    }

    @Override
    public ItemGenesisUnit getUnit() {
        return unit;
    }
}
