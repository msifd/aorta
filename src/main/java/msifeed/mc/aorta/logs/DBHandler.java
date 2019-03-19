package msifeed.mc.aorta.logs;

import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.config.ConfigEvent;
import msifeed.mc.aorta.config.ConfigManager;
import msifeed.mc.aorta.config.ConfigMode;
import msifeed.mc.aorta.config.JsonConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBHandler {
    private JsonConfig<ConfigSection> config = ConfigManager.getConfig(ConfigMode.SERVER, TypeToken.get(ConfigSection.class), "database.json");
    private Connection connection;

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Object sync = new Object();

    void logCommand(ICommandSender sender, String cmd, String text) {
        threadPool.submit(() -> {
            checkDbConnection();
            asyncLog(sender, cmd, text);
        });
    }

    private void asyncLog(ICommandSender sender, String cmd, String text) {
        try {
            final String chatTable = config.get().chat_table;
            final String query = "INSERT INTO `" + chatTable +
                    "` (`chara`,`uuid`,`time`,`world`,`X`,`Y`,`Z`,`command`,`text`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?);";

            final String uuid = (sender instanceof EntityPlayerMP)
                    ? ((EntityPlayerMP) sender).getUniqueID().toString()
                    : "";
            final ChunkCoordinates coord = sender.getPlayerCoordinates();

            synchronized (sync) {
                final PreparedStatement s = connection.prepareStatement(query);
                s.setString(1, sender.getCommandSenderName());
                s.setString(2, uuid);
                s.setLong(3, System.currentTimeMillis());
                s.setString(4, sender.getEntityWorld().getWorldInfo().getWorldName());
                s.setInt(5, coord.posX);
                s.setInt(6, coord.posY);
                s.setInt(7, coord.posZ);
                s.setString(8, cmd);
                s.setString(9, text);
                s.executeUpdate();
            }
        } catch (SQLException e) {
            Logs.LOGGER.error("Failed to send log to the database! {}", e);
        }
    }

    private void checkDbConnection() {
        try {
            if (connection == null || connection.isClosed())
                connectToDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onReloadDone(ConfigEvent.Updated event) {
        if (FMLCommonHandler.instance().getSide().isServer())
            connectToDb();
    }

    boolean connectToDb() {
        synchronized (sync) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                final ConfigSection.DB config = this.config.get().database;
                final String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=UTF-8&serverTimezone=Europe/Moscow", config.host, config.port, config.database);
                connection = DriverManager.getConnection(url, config.username, config.password);
                Logs.LOGGER.info("Successfully connected to database.");

                return true;
            } catch (SQLException e) {
                Logs.LOGGER.error("Failed to connect to database! {}", e.getMessage());
            } catch (ClassNotFoundException e) {
                Logs.LOGGER.error("Cannot find database driver!");
            } catch (Exception e) {
                Logs.LOGGER.error("Database connect exception {}!", e);
            }
            return false;
        }
    }

    public static class ConfigSection {
        DB database = new DB();
        String chat_table = "chat_logs";

        static class DB {
            String host = "localhost";
            int port = 3306;
            String database = "ortega";
            String username = "r00t";
            String password = "swordfish";
        }
    }
}
