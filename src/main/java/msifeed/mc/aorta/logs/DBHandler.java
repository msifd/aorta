package msifeed.mc.aorta.logs;

import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.aorta.sys.config.ConfigEvent;
import msifeed.mc.aorta.sys.config.ConfigManager;
import msifeed.mc.aorta.sys.config.ConfigMode;
import msifeed.mc.aorta.sys.config.JsonConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBHandler {
    private JsonConfig<ConfigSection> config = ConfigManager.getConfig(ConfigMode.SERVER, TypeToken.get(ConfigSection.class), "database.json");

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private HikariDataSource dataSource;

    @SubscribeEvent
    public void onReloadDone(ConfigEvent.Updated event) {
        if (FMLCommonHandler.instance().getSide().isClient())
            return;

        try {
            final ConfigSection.DB dbConfig = this.config.get().database;

            final HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", dbConfig.host, dbConfig.port, dbConfig.database));
            config.setUsername(dbConfig.username);
            config.setPassword(dbConfig.password);
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.addDataSourceProperty("useUnicode","true");
            config.addDataSourceProperty("characterEncoding","utf8");
            config.addDataSourceProperty("serverTimezone", "Europe/Moscow");

            dataSource = new HikariDataSource(config);
            dataSource.validate();
        } catch (Exception e) {
            Logs.LOGGER.throwing(e);
        }
    }

    void logCommand(ICommandSender sender, String cmd, String text) {
        threadPool.submit(() -> asyncLog(sender, cmd, text));
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

            final PreparedStatement s = dataSource.getConnection().prepareStatement(query);
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
        } catch (SQLException e) {
            Logs.LOGGER.error("Failed to send log to the database! {}", e);
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
