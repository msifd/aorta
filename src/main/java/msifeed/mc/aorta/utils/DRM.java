package msifeed.mc.aorta.utils;

import com.google.common.net.InetAddresses;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;

public class DRM {
    private static final InetAddress SERVER = InetAddresses.forString("149.202.87.159");

    public DRM() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientConnectedToServerEvent(ClientConnectedToServerEvent event) {
        final SocketAddress socket = event.manager.getSocketAddress();
        if (!(socket instanceof InetSocketAddress)) return;

        final InetAddress address = ((InetSocketAddress) socket).getAddress();
        if (address.isLoopbackAddress() || address.equals(SERVER)) return;

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=cQ_b4_lw0Gg"));
            } catch (Exception e) {
            }
        }

        // Также еще и крашит клиент. Мвахаха.
        event.manager.channel().disconnect();
    }
}
