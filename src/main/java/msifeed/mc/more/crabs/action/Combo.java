package msifeed.mc.more.crabs.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Combo {
    public ArrayList<ArrayList<String>> cases = new ArrayList<>();
    public Action action;

    public ArrayList<String> match(Collection<String> actions) {
        final Map<String, Long> counted = actions.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (ArrayList<String> rule : cases) {
            final Map<String, Long> expected = rule.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            final boolean allMatch = expected.entrySet().stream()
                    .allMatch(e -> counted.getOrDefault(e.getKey(), 0L).equals(e.getValue()));
            if (allMatch)
                return rule;
        }
        return null;
    }

    public static ComboLookup find(Collection<Combo> combos, Collection<String> prevActions, String incoming) {
        // NOTE: Works only for 2-element rules

        final Stack<String> stack = new Stack<>();
        stack.push(incoming);

        for (String a : prevActions) {
            stack.push(a);

            for (Combo c : combos) {
                final ArrayList<String> m = c.match(stack);
                if (m != null)
                    return new ComboLookup(c, m);
            }

            stack.pop();
        }

        return null;
    }

    public static class ComboLookup {
        public final Combo c;
        public final ArrayList<String> match;

        ComboLookup(Combo c, ArrayList<String> m) {
            this.c = c;
            this.match = m;
        }
    }
}
