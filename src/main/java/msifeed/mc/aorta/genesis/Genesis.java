package msifeed.mc.aorta.genesis;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.Loader;
import msifeed.mc.aorta.genesis.blocks.BlockBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public enum Genesis {
    INSTANCE;

    private static final JsonParser jsonParser = new JsonParser();
//    private static final ImmutableSet<Class<? extends GenesisBuilder>> builders;

    private static JsonElement parseJson(Path path) {
        try {
            return jsonParser.parse(Files.newBufferedReader(path));
        } catch (IOException e) {
            e.printStackTrace();
            return JsonNull.INSTANCE;
        }
    }

    private static void generate(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            final JsonObject json = jsonElement.getAsJsonObject();
            final HashSet<GenesisTrait> traits = new HashSet<>();
            for (JsonElement je : json.getAsJsonArray("traits")) {
                traits.add(GenesisTrait.valueOf(je.getAsJsonPrimitive().getAsString().toLowerCase()));
            }

            GenesisBuilder blockBuilder = new BlockBuilder(json, traits);
            blockBuilder.build();
        }
    }

    public void init() {
        final File mcRootDir = Loader.instance().getConfigDir().getParentFile();
        final File genesisDir = new File(mcRootDir, "genesis");
        if (!genesisDir.exists())
            genesisDir.mkdirs();

        try {
            loadFolder(genesisDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFolder(File genesisDir) throws IOException {
        Files.walk(genesisDir.toPath())
                .filter(Files::isRegularFile)
                .map(Genesis::parseJson)
                .filter(JsonElement::isJsonArray)
                .forEach(el -> el.getAsJsonArray().forEach(Genesis::generate));
    }
}
