package msifeed.mc.aorta.core;

import msifeed.mc.aorta.core.character.CharRpc;
import msifeed.mc.aorta.core.commands.RollCommand;
import msifeed.mc.aorta.core.commands.TraitListCommand;
import msifeed.mc.aorta.core.commands.TraitSetCommand;
import msifeed.mc.aorta.core.meta.MetaRpc;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.genesis.meta.MetaCommand;
import msifeed.mc.aorta.sys.attributes.AttributeHandler;
import msifeed.mc.aorta.sys.rpc.Rpc;
import net.minecraft.command.CommandHandler;

public class Core {
    public void init() {
        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.INSTANCE.registerAttribute(MetaAttribute.INSTANCE);

        Rpc.register(CharRpc.INSTANCE);
        Rpc.register(MetaRpc.INSTANCE);
        Rpc.register(RollRpc.INSTANCE);

        TraitDecoder.init();
    }

    public void registerCommands(CommandHandler handler) {
        handler.registerCommand(new TraitListCommand());
        handler.registerCommand(new TraitSetCommand());
        handler.registerCommand(new MetaCommand());
        handler.registerCommand(new RollCommand());
    }
}
