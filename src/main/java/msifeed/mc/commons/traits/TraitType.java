package msifeed.mc.commons.traits;

public enum TraitType {
    SYSTEM("__"), LANG("lang_");

    public String prefix;

    TraitType(String prefix) {
        this.prefix = prefix;
    }

    public boolean is(Trait trait) {
        return trait.name().startsWith(prefix);
    }

    public static TraitType resolveType(String raw) {
        for (TraitType tt : values()) {
            if (raw.startsWith(tt.prefix))
                return tt;
        }
        return null;
    }
}
