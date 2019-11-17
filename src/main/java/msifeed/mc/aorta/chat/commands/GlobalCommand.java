package msifeed.mc.aorta.chat.commands;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.core.meta.MetaInfo;
import msifeed.mc.aorta.core.utils.MetaAttribute;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class GlobalCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "global";
    }

    @Override
    public List getCommandAliases() {
        return Collections.singletonList("g");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/global <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }

        final MetaInfo meta = MetaAttribute.require((Entity) sender);
        if (args.length == 0) {
            meta.receiveGlobal = !meta.receiveGlobal;
            MetaAttribute.INSTANCE.set((Entity) sender, meta);
            sender.addChatMessage(new ChatComponentText(meta.receiveGlobal ? "global +" : "global -"));
            return;
        }

        if (!meta.receiveGlobal)
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        final String text = String.join(" ", args);
        ChatHandler.sendGlobalChatMessage(player, Composer.makeMessage(SpeechType.GLOBAL, player, text));
    }
}
