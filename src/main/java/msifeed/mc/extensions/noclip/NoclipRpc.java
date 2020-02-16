package msifeed.mc.extensions.noclip;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import msifeed.mc.Bootstrap;
import msifeed.mc.sys.rpc.Rpc;
import msifeed.mc.sys.rpc.RpcMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public enum NoclipRpc {
    INSTANCE;

    private static final String toggle = Bootstrap.MODID + ":noclip";

    public static void init() {
        Rpc.register(INSTANCE);
    }

    public static void toggle(EntityPlayer player) {
        player.noClip = !player.noClip;
        Rpc.sendTo((EntityPlayerMP) player, toggle);
    }

    @RpcMethod(toggle)
    public void onToggle(MessageContext ctx) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        player.noClip = !player.noClip;
    }
}
