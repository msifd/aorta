package msifeed.mc.sys.cmd;

import net.minecraft.command.ICommandSender;

public abstract class GmExtCommand extends PlayerExtCommand {
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return super.canCommandSenderUseCommand(sender) && isGm(sender);
    }
}
