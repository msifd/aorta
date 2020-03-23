package msifeed.mc.more.commands;

import msifeed.mc.commons.traits.Trait;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public class ItemAttrCommand extends ExtCommand {
    private static final UUID OVERRIDE_ID = UUID.fromString("a352d97b-0483-4855-a597-626b76d0cfa3");

    @Override
    public String getCommandName() {
        return "itemattr";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/itemattr <g|s|c> <attribute> [value]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "Sender is not a player");
            return;
        }

        final EntityPlayer player = (EntityPlayer) sender;
        if (!CharacterAttribute.hasAny(player, Trait.__admin, Trait.gm)) {
            error(sender, "Not cool enough");
            return;
        }

        if (args.length < 2) {
            title(sender, getCommandUsage(sender));
            info(sender, "g|s|c - for get, set, clear resp.");
            info(sender, "attributes: all, damage");
            info(sender, "value: a floating point number");
            return;
        }

        final ItemStack item = player.getHeldItem();
        if (item == null) {
            error(sender, "You must hold an item");
            return;
        }

        final boolean set = args[0].equalsIgnoreCase("s");
        final boolean clear = args[0].equalsIgnoreCase("c");
        final String attr = args[1];

        switch (attr) {
            case "all":
                if (clear) {
                    clearAttributes(item, "");
                    info(sender, "clear all attributes");
                } else {
                    getAttributes(item, player, "");
                }
                break;
            case "damage":
                final String damageAttrName = SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName();

                if (set) {
                    if (args.length < 3) {
                        error(sender, "set operation requires value argument");
                        return;
                    }

                    double value;
                    try {
                        value = Double.valueOf(args[2]);
                    } catch (NumberFormatException e) {
                        error(sender, "set value is not a valid number");
                        return;
                    }

                    final NBTTagList nbtTagList = prepareToSet(item, damageAttrName);
                    final AttributeModifier newMod = new AttributeModifier(OVERRIDE_ID, "Damage override", value, 0);
                    final NBTTagCompound newModNbt = writeAttributeModifierToNBT(newMod);
                    newModNbt.setString("AttributeName", damageAttrName);
                    nbtTagList.appendTag(newModNbt);

                    info(sender, "set damage to " + value);
                } else if (clear) {
                    clearAttributes(item, damageAttrName);
                    info(sender, "clear damage");
                } else {
                    getAttributes(item, player, damageAttrName);
                }
                break;
            default:
                error(sender, "unknown attribute");
        }
    }

    private static NBTTagList prepareToSet(ItemStack item, String attrName) {
        if (!item.hasTagCompound())
            item.setTagCompound(new NBTTagCompound());
        final NBTTagCompound tag = item.getTagCompound();

        if (!tag.hasKey("AttributeModifiers"))
            tag.setTag("AttributeModifiers", new NBTTagList());
        final NBTTagList modsList = tag.getTagList("AttributeModifiers", 10);

        for (int i = 0; i < modsList.tagCount(); ++i) {
            final NBTTagCompound modNbt = modsList.getCompoundTagAt(i);
            final String modAttrName = modNbt.getString("AttributeName");
            if (attrName.equals(modAttrName)) {
                final AttributeModifier mod = SharedMonsterAttributes.readAttributeModifierFromNBT(modNbt);
                if (mod.getID().equals(OVERRIDE_ID))
                    modsList.removeTag(i);
            }
        }

        return modsList;
    }

    private static void clearAttributes(ItemStack item, String attrName) {
        if (!item.hasTagCompound())
            return;
        final NBTTagCompound tag = item.getTagCompound();

        if (!tag.hasKey("AttributeModifiers"))
            return;
        final NBTTagList modsList = tag.getTagList("AttributeModifiers", 10);

        if (attrName.isEmpty()) {
            tag.removeTag("AttributeModifiers");
        } else {
            for (int i = 0; i < modsList.tagCount(); ++i) {
                final NBTTagCompound modNbt = modsList.getCompoundTagAt(i);
                final String modAttrName = modNbt.getString("AttributeName");
                if (attrName.equals(modAttrName)) {
                    modsList.removeTag(i);
                    --i;
                }
            }
            if (modsList.tagCount() == 0)
                tag.removeTag("AttributeModifiers");
        }

        if (tag.hasNoTags())
            item.setTagCompound(null);
    }

    private static void getAttributes(ItemStack item, EntityPlayer player, String attrName) {
        if (!item.hasTagCompound())
            return;
        final NBTTagCompound tag = item.getTagCompound();

        if (!tag.hasKey("AttributeModifiers"))
            return;
        final NBTTagList modsList = tag.getTagList("AttributeModifiers", 10);

        for (int i = 0; i < modsList.tagCount(); ++i) {
            final NBTTagCompound modNbt = modsList.getCompoundTagAt(i);
            final String modAttrName = modNbt.getString("AttributeName");
            if (attrName.isEmpty() || attrName.equals(modAttrName)) {
                final AttributeModifier mod = SharedMonsterAttributes.readAttributeModifierFromNBT(modNbt);
                info(player, String.format("%s - %s: %f", modAttrName, mod.getName(), mod.getAmount()));
            }
        }
    }

    private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier mod) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("Name", mod.getName());
        nbttagcompound.setDouble("Amount", mod.getAmount());
        nbttagcompound.setInteger("Operation", mod.getOperation());
        nbttagcompound.setLong("UUIDMost", mod.getID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", mod.getID().getLeastSignificantBits());
        return nbttagcompound;
    }
}
