package msifeed.mc.extensions.noclip;

import msifeed.mc.Bootstrap;
import msifeed.mc.more.More;
import msifeed.mc.sys.rpc.RpcMethodHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public enum NoclipRpc {
    INSTANCE;

    private static final String toggle = Bootstrap.MODID + ":noclip";

    public static void preInit() {
        More.RPC.register(INSTANCE);
    }

    public static void toggle(EntityPlayer player) {
        player.noClip = !player.noClip;
        More.RPC.sendTo((EntityPlayerMP) player, toggle);
    }

    @RpcMethodHandler(toggle)
    public void onToggle() {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        player.noClip = !player.noClip;
    }
}
