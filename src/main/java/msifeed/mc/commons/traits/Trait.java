package msifeed.mc.commons.traits;

public enum Trait {
    __admin,
    gm,

    lang_vanilla
    ;

    public final int code;
    public final TraitType type;

    Trait() {
        this.code = TraitRegistry.register(this);
        this.type = TraitType.resolveType(name());
    }
}
