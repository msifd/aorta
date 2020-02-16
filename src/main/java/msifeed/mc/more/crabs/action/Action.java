package msifeed.mc.more.crabs.action;

import msifeed.mc.more.crabs.action.effects.Effect;
import msifeed.mc.more.crabs.action.effects.Score;

import java.util.ArrayList;
import java.util.Arrays;

public class Action extends ActionHeader {
    public ArrayList<Score> score = new ArrayList<>();
    public ArrayList<Effect> target = new ArrayList<>();
    public ArrayList<Effect> self = new ArrayList<>();

//    public Action(String signature) {
//        final String[] parts = signature.split("/");
//        final Iterator<String> it = Stream.of(parts).iterator();
//
//        this.name = it.next();
//        this.title = it.next();
//
//        while (it.hasNext())
//            tags.add(ActionTag.valueOf(it.next()));
//    }

    public Action(String id, String title) {
        super(id, title);
    }

    Action(String id, String title, ActionTag... tags) {
        this(id, title);
        this.tags.addAll(Arrays.asList(tags));
    }

//    public boolean isDefencive() {
//        return tags.contains(ActionTag.DEFENCIVE);
//    }

    @Override
    public boolean equals(Object obj) {
        if (!Action.class.isAssignableFrom(obj.getClass())) return false;
        final Action act = (Action) obj;
        return this.id.equals(act.id)
                && this.tags.equals(act.tags)
                && this.score.equals(act.score)
                && this.target.equals(act.target)
                && this.self.equals(act.self);
    }
}
