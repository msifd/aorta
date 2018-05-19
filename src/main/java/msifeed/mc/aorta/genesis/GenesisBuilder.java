package msifeed.mc.aorta.genesis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import java.util.Collection;

public abstract class GenesisBuilder {
    protected final Side side = FMLCommonHandler.instance().getSide();
    protected final JsonObject json;
    protected final Collection<Trait> traits;

    protected String id;

    public GenesisBuilder(JsonObject json, Collection<Trait> traits) {
        this.json = json;
        this.traits = traits;
    }

    protected static boolean isString(JsonElement el) {
        return el.isJsonPrimitive() && el.getAsJsonPrimitive().isString();
    }

    public void build() {
        commons();
        produce();
        register();
    }

    protected void commons() {
        id = json.getAsJsonPrimitive("id").getAsString();
    }

    public abstract Collection<Trait> specificTraits();

    public abstract void produce();

    public abstract void register();
}
