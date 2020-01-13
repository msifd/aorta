package msifeed.mc.extensions.itemmeta;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public class ItemMetaProvider {
    private static final String OWNER_TAG = "aorta.meta.owner";
    private static final String LINES_TAG = "aorta.meta.lines";

    public static String getOwner(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            return "";
        return itemStack.getTagCompound().getString(OWNER_TAG);
    }

    public static void setOwner(ItemStack itemStack, String owner) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setString(OWNER_TAG, owner);
    }

    public static String[] getLines(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            return new String[0];
        final String text = itemStack.getTagCompound().getString(LINES_TAG);
        if (text.isEmpty())
            return new String[0];
        return text.split("\n");
    }

    public static void addLine(ItemStack itemStack, String line) {
        if (!itemStack.hasTagCompound())
            itemStack.setTagCompound(new NBTTagCompound());
        final NBTTagCompound compound = itemStack.getTagCompound();
        final String text = compound.getString(LINES_TAG);
        final String newText = text.isEmpty()
                ? line
                : text + '\n' + line;
        compound.setString(LINES_TAG, newText);
    }

    public static void removeLine(ItemStack itemStack) {
        final String[] lines = getLines(itemStack);
        if (lines.length < 1)
            return;
        final String[] newLines = Arrays.copyOfRange(lines, 0, lines.length - 1);
        final String newText = String.join("\n", newLines);
        itemStack.getTagCompound().setString(LINES_TAG, newText);
    }

    public static void clear(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            return;
        final NBTTagCompound compound = itemStack.getTagCompound();
        compound.removeTag(OWNER_TAG);
        compound.removeTag(LINES_TAG);
    }
}
