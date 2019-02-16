package msifeed.mc.aorta.core.traits;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TraitType {
    SYSTEM("__"), LANG("lang_");

    public String prefix;

    TraitType(String prefix) {
        this.prefix = prefix;
    }

    public static TraitType resolveType(String raw) {
        for (TraitType tt : values()) {
            if (raw.startsWith(tt.prefix))
                return tt;
        }
        return null;
    }
}
