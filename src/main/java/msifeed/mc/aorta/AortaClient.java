package msifeed.mc.aorta;

import com.google.common.io.CharStreams;
import cpw.mods.fml.client.FMLClientHandler;
import msifeed.mc.aorta.utils.DRM;
import msifeed.mc.mellow.Mellow;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AortaClient extends Aorta {
    private DRM drm = new DRM();

    @Override
    public void init() {
        super.init();

        final IResourcePack resourcePack = FMLClientHandler.instance().getResourcePackFor(Aorta.MODID);
        final ResourceLocation themeSprite = new ResourceLocation(Aorta.MODID + ":theme/theme.png");
        final ResourceLocation themeMeta = new ResourceLocation(Aorta.MODID + ":theme/theme.json");

        try {
            final InputStream metaInput = resourcePack.getInputStream(themeMeta);
            final String json = CharStreams.toString(new InputStreamReader(metaInput, StandardCharsets.UTF_8));
            Mellow.loadTheme(themeSprite, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
