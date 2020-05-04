package msifeed.mc.sys.cmd;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class PlayerExtCommand extends ExtCommand {
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return super.canCommandSenderUseCommand(sender) && sender instanceof EntityPlayerMP;
    }
}
