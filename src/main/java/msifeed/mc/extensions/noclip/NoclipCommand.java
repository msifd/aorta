package msifeed.mc.extensions.noclip;

import cpw.mods.fml.common.FMLCommonHandler;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class NoclipCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "noclip";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/noclip";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        final EntityPlayer player = (EntityPlayer) sender;

        if (!FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(player.getGameProfile())) {
            error(sender, "Not enough permissions!");
            return;
        }

        if (!player.capabilities.isCreativeMode) {
            error(sender, "You must be in creative mode!");
            return;
        }

        sender.addChatMessage(new ChatComponentText("Noclip " + (player.noClip ? "Disabled" : "Enabled")));
        NoclipRpc.toggle(player);
    }
}
