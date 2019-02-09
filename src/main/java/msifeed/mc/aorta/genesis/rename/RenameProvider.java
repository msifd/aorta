package msifeed.mc.aorta.genesis.rename;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.*;

public class RenameProvider {
    public static boolean hasTitle(ItemStack itemStack) {
        return itemStack.hasTagCompound()
                && itemStack.stackTagCompound.hasKey("display", 10)
                && itemStack.stackTagCompound.getCompoundTag("display").hasKey("Name");
    }

    public static void setTitle(ItemStack itemStack, String title) {
        if (title == null) {
            if (itemStack.hasTagCompound() && itemStack.stackTagCompound.hasKey("display", 10))
                itemStack.stackTagCompound.getCompoundTag("display").removeTag("Name");
        } else {
            itemStack.setStackDisplayName(replaceFormattingCode(title));
        }
    }

    public static boolean hasDescription(ItemStack itemStack) {
        return itemStack.hasTagCompound()
                && itemStack.stackTagCompound.hasKey("display", 10)
                && itemStack.stackTagCompound.getCompoundTag("display").hasKey(Tags.description);
    }

    public static void addDescriptionToList(ItemStack itemStack, List<String> list) {
        if (!hasDescription(itemStack))
            return;
        final NBTTagList lines = itemStack.stackTagCompound
                .getCompoundTag("display")
                .getTagList(Tags.description, 8);
        for (int i = 0; i < lines.tagCount(); i++) {
            list.add(lines.getStringTagAt(i));
        }
    }

    public static void addDescription(ItemStack itemStack, String line) {
        createDescriptionIfNeeded(itemStack);
        final NBTTagList lines = itemStack.stackTagCompound
                .getCompoundTag("display")
                .getTagList(Tags.description, 8);
        lines.appendTag(new NBTTagString(replaceFormattingCode("\u00A7r" + line)));
    }

    public static void removeDescriptionLine(ItemStack itemStack) {
        if (!hasDescription(itemStack))
            return;
        final NBTTagList lines = itemStack.stackTagCompound
                .getCompoundTag("display")
                .getTagList(Tags.description, 8);
        if (lines.tagCount() > 1)
            lines.removeTag(lines.tagCount() - 1);
        else
            clearDescription(itemStack);
    }

    public static void clearDescription(ItemStack itemStack) {
        if (!hasDescription(itemStack))
            return;
        itemStack.stackTagCompound.getCompoundTag("display").removeTag(Tags.description);
    }

    private static void createDescriptionIfNeeded(ItemStack itemStack) {
        createCompoundIfNeeded(itemStack);
        if (!itemStack.stackTagCompound.hasKey("display"))
            itemStack.stackTagCompound.setTag("display", new NBTTagCompound());
        if (!itemStack.stackTagCompound.getCompoundTag("display").hasKey(Tags.description))
            itemStack.stackTagCompound.getCompoundTag("display").setTag(Tags.description, new NBTTagList());
    }

    public static boolean hasOverriddenValues(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(Tags.values);
    }

    public static void setValue(ItemStack itemStack, String key, String value) {
        if (value == null && !hasOverriddenValues(itemStack))
            return;

        createOverriddenValuesIfNeeded(itemStack);
        final NBTTagCompound values = itemStack.stackTagCompound.getCompoundTag(Tags.values);
        if (value == null)
            values.removeTag(key);
        else
            values.setString(replaceFormattingCode(key), replaceFormattingCode(value));
    }

    public static Map<String, String> getOverriddenValues(ItemStack itemStack) {
        if (!hasOverriddenValues(itemStack))
            return Collections.emptyMap();

        final NBTTagCompound values = itemStack.stackTagCompound.getCompoundTag(Tags.values);
        final HashMap<String, String> m = new HashMap<>();
        for (String k : (Set<String>) values.func_150296_c())
            m.put(k, values.getString(k));
        return m;
    }

    private static void createOverriddenValuesIfNeeded(ItemStack itemStack) {
        createCompoundIfNeeded(itemStack);
        if (!itemStack.stackTagCompound.hasKey(Tags.values))
            itemStack.stackTagCompound.setTag(Tags.values, new NBTTagCompound());
    }

    private static void createCompoundIfNeeded(ItemStack itemStack) {
        if (!itemStack.hasTagCompound())
            itemStack.stackTagCompound = new NBTTagCompound();
    }

    private static String replaceFormattingCode(String str) {
        return str.replace('&', '\u00A7');
    }

    static class Tags {
        static final String title = "Name";
        static final String description = "Lore";
        static final String values = "aorta.values";
    }
}
