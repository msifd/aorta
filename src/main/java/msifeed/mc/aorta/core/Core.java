package msifeed.mc.aorta.core;

import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.meta.MetaRpc;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.sys.attributes.AttributeHandler;
import msifeed.mc.sys.rpc.Rpc;

public class Core {
    public static void init() {
        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.INSTANCE.registerAttribute(MetaAttribute.INSTANCE);

        Rpc.register(CharRpc.INSTANCE);
        Rpc.register(MetaRpc.INSTANCE);
        Rpc.register(RollRpc.INSTANCE);
    }
}
