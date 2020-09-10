package msifeed.mc.more.crabs.action;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.more.More;
import msifeed.mc.more.crabs.action.parser.ActionJsonAdapter;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.ConfigEvent;
import msifeed.mc.sys.config.JsonConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ActionRegistry {
    INSTANCE;

    public static final Action NONE_ACTION = new Action("none", ".none", ActionTag.none);
    private static final Action APPLY_ACTION = new Action("apply", ".apply", ActionTag.apply);
    private static final Action EQUIP_ACTION = new Action("equip", ".equip", ActionTag.equip);
    private static final Action RELOAD_ACTION = new Action("reload", ".reload", ActionTag.reload);

    private static final Logger logger = LogManager.getLogger(ActionRegistry.class.getSimpleName());

    private JsonConfig<ActionsConfigContent> actionsConfig = ConfigBuilder.of(ActionsConfigContent.class, "actions.json")
            .addAdapter(Action.class, new ActionJsonAdapter())
            .create();

    private HashMap<String, Action> actions = new HashMap<>();
    private ArrayList<Combo> combos = new ArrayList<>();

    /// Client-side headers
    private HashMap<String, ActionHeader> actionHeaders = new HashMap<>();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        More.RPC.register(ActionRpc.INSTANCE);
    }

    public static ActionHeader getHeader(String id) {
        return INSTANCE.actionHeaders.get(id);
    }

    public static Action getFullAction(String id) {
        return INSTANCE.actions.get(id);
    }

    public static ArrayList<Combo> getCombos() {
        return INSTANCE.combos;
    }

    public static Collection<ActionHeader> getActionHeaders() {
        return INSTANCE.actionHeaders.values();
    }

    public static Collection<Action> getFullActions() {
        return INSTANCE.actions.values();
    }

    public static void setActionHeaders(Collection<ActionHeader> headers) {
        INSTANCE.actionHeaders.clear();
        for (ActionHeader h : headers)
            INSTANCE.actionHeaders.put(h.id, h);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP)
            ActionRpc.broadcastTo((EntityPlayerMP) event.player, this.actions.values());
    }

    @SubscribeEvent
    public void onConfigUpdate(ConfigEvent.AfterUpdate event) {
        final ActionsConfigContent newConfig = actionsConfig.get();

        final Map<String, Long> idCounts = Stream.concat(
                newConfig.actions.stream().map(ActionHeader::getId),
                newConfig.combos.stream().map(combo -> combo.action.getId())
        ).collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        boolean idsOk = true;
        for (Map.Entry<String, Long> e : idCounts.entrySet()) {
            if (e.getValue() == 1) continue;
            idsOk = false;
            logger.error("Action id '{}' is conflicting!", e.getKey());
        }

        final boolean combosOk = newConfig.combos.stream().allMatch(c -> verifyCombo(c, idCounts.keySet()));

        if (idsOk && combosOk) {
            applyConfigActions();
        } else {
            logger.error("Actions are not updated due to errors.");
        }
    }

    private void applyConfigActions() {
        final ActionsConfigContent newConfig = actionsConfig.get();

        this.actions.clear();
        this.combos.clear();
        this.actionHeaders.clear();

        for (Action a : newConfig.actions)
            this.actions.put(a.id, a);

        this.actions.put(NONE_ACTION.id, NONE_ACTION);
        this.actions.put(APPLY_ACTION.id, APPLY_ACTION);
        this.actions.put(EQUIP_ACTION.id, EQUIP_ACTION);
        this.actions.put(RELOAD_ACTION.id, RELOAD_ACTION);

        for (Map.Entry<String, Action> e : this.actions.entrySet())
            this.actionHeaders.put(e.getKey(), e.getValue());

        this.combos.addAll(newConfig.combos);

        ActionRpc.broadcastToAll(actions.values());
    }

    private static boolean verifyCombo(Combo combo, Set<String> actions) {
        final Set<String> casesIds = combo.cases.stream().flatMap(List::stream).collect(Collectors.toSet());
        if (!actions.containsAll(casesIds)) {
            logger.error("Combo '{}' has unknown case actions! ({})", combo.action.id, casesIds);
            return false;
        }

        return true;
    }

    public static final class ActionsConfigContent {
        public ArrayList<Action> actions = new ArrayList<>();
        public ArrayList<Combo> combos = new ArrayList<>();
    }
}
