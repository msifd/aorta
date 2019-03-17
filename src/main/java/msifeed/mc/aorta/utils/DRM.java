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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum  DRM {
    INSTANCE;

    private static final Set<InetAddress> SERVERS = Stream.of("137.74.214.123", "79.137.9.233")
            .map(s -> InetAddresses.forString(s))
            .collect(Collectors.toSet());

    public static void apply() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientConnectedToServerEvent(ClientConnectedToServerEvent event) {
        final SocketAddress socket = event.manager.getSocketAddress();
        if (!(socket instanceof InetSocketAddress)) return;

        final InetAddress address = ((InetSocketAddress) socket).getAddress();
        if (address.isLoopbackAddress() || SERVERS.contains(address)) return;

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
