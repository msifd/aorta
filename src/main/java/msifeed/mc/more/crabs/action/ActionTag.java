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

    public String format() {
        switch (this) {
            case melee:
                return "ближний";
            case ranged:
                return "дальний";
            case magical:
                return "магия";
            case intellectual:
                return "ум";
            case defencive:
                return "защита";
            case none:
                return "ничего";
            case apply:
                return "применение";
            case equip:
                return "эквип";
            case reload:
                return "перезарядка";
            case hidden:
                return "<>";
            default:
                return "";
        }
    }
}
