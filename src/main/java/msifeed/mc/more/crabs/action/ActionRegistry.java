package msifeed.mc.more.crabs.action;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import msifeed.mc.more.crabs.action.parser.ActionJsonAdapter;
import msifeed.mc.sys.config.ConfigBuilder;
import msifeed.mc.sys.config.ConfigEvent;
import msifeed.mc.sys.config.JsonConfig;
import msifeed.mc.sys.rpc.Rpc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public enum ActionRegistry {
    INSTANCE;

    public static final Action NONE_ACTION = new Action("none", ".none", ActionTag.passive);
    private static final Action EQUIP_ACTION = new Action("equip", ".equip", ActionTag.passive, ActionTag.equip);
    private static final Action RELOAD_ACTION = new Action("reload", ".reload", ActionTag.passive, ActionTag.reload);

    private static final Logger logger = LogManager.getLogger(ActionRegistry.class);

    private TypeToken<ArrayList<Action>> actionsFileType = new TypeToken<ArrayList<Action>>() {};
    private JsonConfig<ArrayList<Action>> config = ConfigBuilder.of(actionsFileType, "actions.json")
            .addAdapter(Action.class, new ActionJsonAdapter())
            .create();

    private HashMap<String, Action> actions = new HashMap<>();
    private HashMap<String, ActionHeader> actionHeaders = new HashMap<>(); /// Client-side headers

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        Rpc.register(ActionRpc.INSTANCE);
    }

    public static Action get(String id) {
        return INSTANCE.actions.get(id);
    }

    public static ActionHeader getHeader(String id) {
        if (!INSTANCE.actions.isEmpty())
            return get(id);
        return INSTANCE.actionHeaders.get(id);
    }

    public static Collection<Action> getActions() {
        return INSTANCE.actions.values();
    }

    public static Collection<ActionHeader> getActionHeaders() {
        return INSTANCE.actionHeaders.values();
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
        final ArrayList<Action> newActions = config.get();

        boolean applyActions = true;
        for (Action a : newActions)
            applyActions &= verifyAction(a, newActions);

        if (applyActions)
            setActions(newActions);
        else
            logger.error("Actions are not updated due to errors.");
    }

    private void setActions(Collection<Action> newActions) {
        this.actions.clear();
        for (Action a : newActions)
            this.actions.put(a.id, a);

        this.actions.put(NONE_ACTION.id, NONE_ACTION);
        this.actions.put(EQUIP_ACTION.id, EQUIP_ACTION);
        this.actions.put(RELOAD_ACTION.id, RELOAD_ACTION);

        if (FMLCommonHandler.instance().getMinecraftServerInstance() != null)
            ActionRpc.broadcastToAll(actions.values());
    }

    private static boolean verifyAction(Action action, ArrayList<Action> list) {
        if (list.stream().noneMatch(a -> a.id.equals(action.id))) {
            logger.error("Action {} has id conflict!", action.id);
            return false;
        }
        for (String s : action.combo) {
            if (list.stream().noneMatch(a -> a.id.equals(s))) {
                logger.error("Action {} has unknown combo id {}!", action.id, s);
                return false;
            }
        }
        return true;
    }
}
