package msifeed.mc.more.crabs.action;

import msifeed.mc.sys.utils.L10n;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Comparator;
import java.util.EnumSet;

public class ActionHeader {
    public final String id;
    public final String title;
    public EnumSet<ActionTag> tags = EnumSet.noneOf(ActionTag.class);

    public ActionHeader(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title.startsWith(".") ? L10n.tr("more.action" + title) : title;
    }

    public ActionTag getType() {
        return tags.stream().filter(ActionTag::isType).findAny().orElse(ActionTag.melee);
    }

    public boolean requiresNoRoll() {
        return hasAnyTag(ActionTag.none, ActionTag.apply, ActionTag.equip, ActionTag.reload);
    }

    public boolean hasAnyTag(ActionTag... tags) {
        for (ActionTag t : tags)
            if (this.tags.contains(t))
                return true;
        return false;
    }

    public boolean isOffencive() {
        return !hasAnyTag(ActionTag.defencive);
    }

    public boolean isValidDefencive(ActionTag offenceType) {
        return hasAnyTag(ActionTag.none)
                || offenceType == getType() && hasAnyTag(ActionTag.defencive);
    }

    public int compareTo(ActionHeader o) {
        return Comparator
                .comparing(ActionHeader::requiresNoRoll)
                .thenComparing(ActionHeader::getType)
                .thenComparing(ActionHeader::getTitle)
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
    }

    public final NBTTagCompound toHeaderNBT() {
        final NBTTagCompound c = new NBTTagCompound();

        c.setString("id", this.id);
        c.setString("title", this.title);
        c.setIntArray("tags", this.tags.stream().mapToInt(Enum::ordinal).toArray());

        return c;
    }
}
