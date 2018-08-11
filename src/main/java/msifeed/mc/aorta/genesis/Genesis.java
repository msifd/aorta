package msifeed.mc.aorta.genesis;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.Loader;
import msifeed.mc.aorta.genesis.blocks.BlockGenerator;
import msifeed.mc.aorta.genesis.items.ItemGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;

public class Genesis {
    private static final JsonParser jsonParser = new JsonParser();
    private static final ImmutableMap<GenesisTrait, Generator> generators = ImmutableMap.<GenesisTrait, Generator>builder()
            .put(GenesisTrait.block, new BlockGenerator())
            .put(GenesisTrait.item, new ItemGenerator())
            .build();

    public void init() {
        generate();
    }

    public void generate() {
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
                .filter(path -> path.toString().endsWith(".json"))
                .map(this::parseJson)
                .filter(JsonElement::isJsonArray)
                .forEach(el -> el.getAsJsonArray().forEach(this::generateFromFile));
    }

    private JsonElement parseJson(Path path) {
        try {
            return jsonParser.parse(Files.newBufferedReader(path));
        } catch (IOException e) {
            e.printStackTrace();
            return JsonNull.INSTANCE;
        }
    }

    private void generateFromFile(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            final JsonObject json = jsonElement.getAsJsonObject();
            final HashSet<GenesisTrait> traits = parseTraits(json);

            final Optional<GenesisTrait> generatorTrait = traits.stream()
                    .filter(generators::containsKey)
                    .findFirst();
            generatorTrait.ifPresent(genesisTrait -> {
                generators.get(genesisTrait).generate(json, traits);
            });
        }
    }

    private HashSet<GenesisTrait> parseTraits(JsonObject json) {
        final HashSet<GenesisTrait> traits = new HashSet<>();
        for (JsonElement je : json.getAsJsonArray("traits")) {
            try {
                traits.add(GenesisTrait.valueOf(je.getAsJsonPrimitive().getAsString().toLowerCase()));
            } catch (IllegalArgumentException e) {
            }
        }
        return traits;
    }
}
