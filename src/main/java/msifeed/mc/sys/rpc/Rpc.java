package msifeed.mc.sys.rpc;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import msifeed.mc.Bootstrap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Rpc {
    private static final Rpc INSTANCE = new Rpc();

    private final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Bootstrap.MODID + ".rpc");
    private final HashMap<String, Handler> handlers = new HashMap<>();

    private Rpc() {}

    public static void preInit() {
        INSTANCE.CHANNEL.registerMessage(RpcMessage.class, RpcMessage.class, 0, Side.SERVER);
        INSTANCE.CHANNEL.registerMessage(RpcMessage.class, RpcMessage.class, 1, Side.CLIENT);
    }

    public static void register(Object obj) {
        for (Method m : obj.getClass().getDeclaredMethods()) {
            if (!m.isAnnotationPresent(RpcMethod.class))
                continue;
            final RpcMethod a = m.getAnnotation(RpcMethod.class);

            final Class<?>[] types = m.getParameterTypes();
            if (types.length == 0 || types[0] != MessageContext.class)
                throw new RuntimeException(String.format("RPC method %s: missing leading MessageContext param", a.value()));
            for (int i = 1; i < types.length; ++i)
                if (RpcSerializer.getTypeId(types[i]) == null)
                    throw new RuntimeException(String.format("RPC method '%s': param %d (%s) is not supported", a.value(), i + 1, types[i].getSimpleName()));

            if (INSTANCE.handlers.containsKey(a.value()))
                throw new RuntimeException(String.format("RPC method %s: method duplication", a.value()));
            INSTANCE.handlers.put(a.value(), new Handler(obj, m));
        }
    }

    public static void sendToServer(String method, Object... args) {
        INSTANCE.CHANNEL.sendToServer(new RpcMessage(method, args));
    }

    public static void sendTo(EntityPlayerMP player, String method, Object... args) {
        if (serverStarted())
            INSTANCE.CHANNEL.sendTo(new RpcMessage(method, args), player);
    }

    public static void sendToAll(String method, Object... args) {
        if (serverStarted())
            INSTANCE.CHANNEL.sendToAll(new RpcMessage(method, args));
    }

    public static void sendToAllAround(String method, NetworkRegistry.TargetPoint point, Object... args) {
        if (serverStarted())
            INSTANCE.CHANNEL.sendToAllAround(new RpcMessage(method, args), point);
    }

    private static boolean serverStarted() {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        return server != null && server.getConfigurationManager() != null;
    }

    static void onMessage(RpcMessage message, MessageContext ctx) {
        final Handler h = INSTANCE.handlers.get(message.method);
        if (h == null)
            return;

        // Build invoke args array
        final Object[] args = new Object[message.args.length + 1];
        args[0] = ctx;
        System.arraycopy(message.args, 0, args, 1, message.args.length);

        try {
            h.method.invoke(h.object, args);
        } catch (IllegalArgumentException e) {
            System.err.println(String.format(
                    "RPC call IllegalArgumentException on method '%s'. Expected types: %s. Actual types: %s",
                    message.method, buildTypesString(h.method.getParameterTypes()), buildTypesString(args)
            ));
        } catch (InvocationTargetException ie) {
            try {
                throw ie.getTargetException();
            } catch (RpcMethodException e) {
                e.sendChatMessage();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildTypesString(Object[] objects) {
        final Class<?>[] types = new Class[objects.length];
        for (int i = 0; i < objects.length; i++) {
            types[i] = objects[i].getClass();
        }
        return buildTypesString(types);
    }

    private static String buildTypesString(Class<?>[] types) {
        final StringBuilder sb = new StringBuilder();

        for (Class<?> c : types) {
            if (c == MessageContext.class)
                sb.append("MessageContext,");
            else {
                sb.append(RpcSerializer.getTypeId(c));
                sb.append(',');
            }
        }

        return sb.toString();
    }

    private static class Handler {
        Object object;
        Method method;

        Handler(Object o, Method m) {
            this.object = o;
            this.method = m;
        }
    }
}
