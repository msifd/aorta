package msifeed.mc.more.crabs.action;

public enum ActionTag {
    melee, ranged, magical, intellectual,
    passive, defencive,
    equip, reload,
    ;

    public boolean isType() {
        return ordinal() <= intellectual.ordinal();
    }
}
