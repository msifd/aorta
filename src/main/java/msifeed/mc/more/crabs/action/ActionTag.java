package msifeed.mc.more.crabs.action;

public enum ActionTag {
    melee, ranged, magical, intellectual,
    defencive,
    none, apply, equip, reload,

    hidden,
    ;

    public boolean isType() {
        return ordinal() <= intellectual.ordinal();
    }
}
