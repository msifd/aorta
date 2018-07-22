package msifeed.mc.aorta.core.traits;

import java.util.Set;
import java.util.stream.Collectors;

public enum TraitTypes {
    LANG("lang_");

    public String prefix;

    TraitTypes(String prefix) {
        this.prefix = prefix;
    }

    public Set<Trait> filter(Set<Trait> traits) {
        return traits.stream().filter(t -> t.toString().startsWith(prefix)).collect(Collectors.toSet());
    }
}
