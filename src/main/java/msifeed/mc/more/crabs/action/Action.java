package msifeed.mc.more.crabs.action;

import msifeed.mc.more.crabs.action.effects.Effect;

import java.util.ArrayList;
import java.util.Arrays;

public class Action extends ActionHeader {
    public ArrayList<Effect> self = new ArrayList<>();
    public ArrayList<Effect> target = new ArrayList<>();

    public Action(String id, String title) {
        super(id, title);
    }

    Action(String id, String title, ActionTag... tags) {
        this(id, title);
        this.tags.addAll(Arrays.asList(tags));
    }

    @Override
    public boolean equals(Object obj) {
        if (!Action.class.isAssignableFrom(obj.getClass())) return false;
        final Action act = (Action) obj;
        return this.id.equals(act.id)
                && this.tags.equals(act.tags)
                && this.self.equals(act.self)
                && this.target.equals(act.target);
    }
}
