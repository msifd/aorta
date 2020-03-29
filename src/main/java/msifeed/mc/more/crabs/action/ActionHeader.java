package msifeed.mc.more.crabs.action;

import msifeed.mc.sys.utils.L10n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;

public class ActionHeader {
    public final String id;
    public final String title;
    public EnumSet<ActionTag> tags = EnumSet.noneOf(ActionTag.class);
    public HashSet<String> combo = new HashSet<>();

    public ActionHeader(String id, String title) {
        this.id = id;
        this.title = title;
//        this.title = title.startsWith(".") ? L10n.tr("more.action" + title) : title;
    }

    public String getTitle() {
        return title.startsWith(".") ? L10n.tr("more.action" + title) : title;
    }

    public ActionTag getType() {
        return tags.stream().filter(ActionTag::isType).findAny().orElse(ActionTag.melee);
    }

    public boolean isDefencive() {
        return hasTag(ActionTag.defencive);
    }

    public boolean requiresNoRoll() {
        return hasTag(ActionTag.none) || hasTag(ActionTag.apply) || hasTag(ActionTag.equip) || hasTag(ActionTag.reload);
    }

    public boolean hasTag(ActionTag tag) {
        return tags.contains(tag);
    }

    public int compareTo(ActionHeader o) {
        return Comparator
                .comparingInt((ActionHeader a) -> a.combo.size())
                .reversed()
                .thenComparing((ActionHeader a) -> a.requiresNoRoll())
                .thenComparing((ActionHeader a) -> a.title)
                .compare(this, o);
    }

    public ActionHeader(NBTTagCompound nbt) {
        this.id = nbt.getString("id");
        this.title = nbt.getString("title");

        final int[] tags = nbt.getIntArray("tags");
        final ActionTag[] tagsValues = ActionTag.values();
        for (int i : tags) {
            this.tags.add(tagsValues[i]);
        }

        final NBTTagList combo = nbt.getTagList("combo", 8); // 8 - String
        for (int i = 0; i < combo.tagCount(); i++) {
            this.combo.add(combo.getStringTagAt(i));
        }
    }

    public final NBTTagCompound toHeaderNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setString("id", this.id);
        c.setString("title", this.title);
        c.setIntArray("tags", this.tags.stream().mapToInt(Enum::ordinal).toArray());

        final NBTTagList combo = new NBTTagList();
        for (String s : this.combo)
            combo.appendTag(new NBTTagString(s));
        c.setTag("combo", combo);

        return c;
    }
}
