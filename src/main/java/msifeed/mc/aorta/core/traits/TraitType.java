package msifeed.mc.aorta.core.traits;

import java.util.Set;
import java.util.stream.Collectors;

public enum TraitType {
    SYSTEM("__"), LANG("lang_");

    public String prefix;

    TraitType(String prefix) {
        this.prefix = prefix;
    }

    public Set<Trait> filter(Set<Trait> traits) {
        return traits.stream().filter(t -> t.type == this).collect(Collectors.toSet());
    }

    public static TraitType resolveType(String raw) {
        for (TraitType tt : values()) {
            if (raw.startsWith(tt.prefix))
                return tt;
        }
        return null;
    }
}
