package msifeed.mc.more;

import com.google.common.io.CharStreams;
import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.Bootstrap;
import msifeed.mc.extensions.chat.Speechat;
import msifeed.mc.extensions.itemmeta.ItemMetaClient;
import msifeed.mc.extensions.tweaks.GameWindowOptions;
import msifeed.mc.mellow.Mellow;
import msifeed.mc.more.client.ResponsiveEntityStatus;
import msifeed.mc.more.client.common.Keybinds;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MoreClient extends More {
    private GameWindowOptions gameWindowOptions = new GameWindowOptions();
    private ItemMetaClient itemMeta = new ItemMetaClient();

    @Override
    public void preInit() {
        gameWindowOptions.preInit();
        super.preInit();
        initMellow();
    }

    @Override
    public void init() {
        super.init();

        DRM.apply();
        GUI_HANDLER.init();
        Keybinds.INSTANCE.init();
        ResponsiveEntityStatus.init();
//        BattleMarkRender.init();
        Speechat.initClient();
        itemMeta.init();
    }

    private void initMellow() {
        final IResourcePack resourcePack = FMLClientHandler.instance().getResourcePackFor(Bootstrap.MODID);
        final ResourceLocation themeSprite = new ResourceLocation(Bootstrap.MODID + ":theme/theme.png");
        final ResourceLocation themeMeta = new ResourceLocation(Bootstrap.MODID + ":theme/theme.json");

        try {
            final InputStream metaInput = resourcePack.getInputStream(themeMeta);
            final String json = CharStreams.toString(new InputStreamReader(metaInput, StandardCharsets.UTF_8));
            Mellow.loadTheme(themeSprite, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
