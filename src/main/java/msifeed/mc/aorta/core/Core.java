package msifeed.mc.aorta.core;

import msifeed.mc.aorta.attributes.AttributeHandler;
import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.commands.TraitListCommand;
import msifeed.mc.aorta.core.commands.TraitSetCommand;
import msifeed.mc.aorta.core.meta.MetaCommand;
import msifeed.mc.aorta.core.rolls.RollRpc;
import msifeed.mc.aorta.core.traits.TraitDecoder;
import msifeed.mc.aorta.rpc.Rpc;
import net.minecraft.command.CommandHandler;

public class Core {
    private RollRpc rollRpcHandler = new RollRpc();

    public void init() {
        AttributeHandler.INSTANCE.registerAttribute(CharacterAttribute.INSTANCE);
        AttributeHandler.INSTANCE.registerAttribute(StatusAttribute.INSTANCE);

        Rpc.register(rollRpcHandler);
        TraitDecoder.init();
    }

    public void registerCommands(CommandHandler handler) {
        handler.registerCommand(new TraitListCommand());
        handler.registerCommand(new TraitSetCommand());
        handler.registerCommand(new MetaCommand());
    }
}
