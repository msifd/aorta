package msifeed.mc.more.crabs.combat.action;

import msifeed.mc.more.crabs.effects.Effect;
import msifeed.mc.more.crabs.effects.Modifier;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.stream.Stream;

public class Action {
    public static final Action ACTION_NONE = new Action("none", ".none", ActionType.PASSIVE);

    public final String name;
    public final String title;
    public final ActionType type;

    public ArrayList<Modifier> modifiers = new ArrayList<>();
    public ArrayList<Effect> targetEffects = new ArrayList<>();
    public ArrayList<Effect> selfEffects = new ArrayList<>();
    public EnumSet<ActionTag> tags = EnumSet.noneOf(ActionTag.class);

    public Action(String signature) {
        final String[] parts = signature.split("/");
        final Iterator<String> it = Stream.of(parts).iterator();

        this.name = it.next();
        this.title = it.next();
        this.type = ActionType.valueOf(it.next().toUpperCase());

        while (it.hasNext())
            tags.add(ActionTag.valueOf(it.next()));
    }

    Action(String name, String title, ActionType type) {
        this.name = name;
        this.title = title;
        this.type = type;
    }

    public boolean isDefencive() {
        return type.defencive() || tags.contains(ActionTag.DEFENCIVE);
    }

    public String pretty() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Action.class.isAssignableFrom(obj.getClass())) return false;
        final Action act = (Action) obj;
        return this.name.equals(act.name)
                && this.type.equals(act.type)
                && this.modifiers.equals(act.modifiers)
                && this.targetEffects.equals(act.targetEffects)
                && this.selfEffects.equals(act.selfEffects)
                && this.tags.equals(act.tags);
    }
}
