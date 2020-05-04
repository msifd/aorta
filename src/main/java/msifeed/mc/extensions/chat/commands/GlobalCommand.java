package msifeed.mc.extensions.chat.commands;

import msifeed.mc.commons.logs.ExternalLogs;
import msifeed.mc.extensions.chat.SpeechatRpc;
import msifeed.mc.extensions.chat.formatter.MiscFormatter;
import msifeed.mc.more.crabs.meta.MetaInfo;
import msifeed.mc.more.crabs.utils.MetaAttribute;
import msifeed.mc.sys.cmd.PlayerExtCommand;
import msifeed.mc.sys.utils.ChatUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class GlobalCommand extends PlayerExtCommand {
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
        if (!(sender instanceof EntityPlayerMP))
            return;

        final EntityPlayerMP player = (EntityPlayerMP) sender;
        final MetaInfo meta = MetaAttribute.require(player);
        if (args.length == 0) {
            meta.receiveGlobal = !meta.receiveGlobal;
            MetaAttribute.INSTANCE.set((Entity) sender, meta);
            sender.addChatMessage(new ChatComponentText(meta.receiveGlobal ? "global +" : "global -"));
            return;
        }

        if (!meta.receiveGlobal)
            return;

        final String name = ChatUtils.getPrettyName(player);
        final String text = String.join(" ", args);
        SpeechatRpc.sendGlobal(MiscFormatter.formatGlobal(name, text));
        ExternalLogs.log(sender, "global",  text);
    }
}
