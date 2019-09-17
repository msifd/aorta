package msifeed.mc.aorta.defines;

import com.google.gson.Gson;
import msifeed.mc.aorta.Aorta;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.core.utils.CharacterAttribute;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import msifeed.mc.aorta.sys.config.ConfigManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefineCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "define";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/define [key] [value]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer && !CharacterAttribute.has((EntityPlayer) sender, Trait.__admin)) {
            error(sender, "Not enough permissions!");
            return;
        }

        switch (args.length) {
            case 0:
                break;
            case 1:
                printValue(sender, args[0]);
                break;
            default:
                setValue(sender, args[0], Arrays.stream(args, 1, args.length).collect(Collectors.joining(" ")));
                break;
        }
    }

    private void printValue(ICommandSender sender, String path) {
        final Optional<Object> opt = getValue(path);
        if (opt.isPresent()) {
            final String s = (new Gson()).toJson(opt.get());
            title(sender, "#define %s %s", path, s);
        } else {
            error(sender, "No such field!");
        }
    }

    private void setValue(ICommandSender sender, String path, String valueStr) {
        final Optional<Pair<Field, Object>> opt = getField(path);
        if (opt.isPresent()) {
            final Pair<Field, Object> pair = opt.get();
            final Class<?> type = pair.getLeft().getType();
            try {
                final Object value = (new Gson()).fromJson(valueStr, type);
                pair.getLeft().set(pair.getRight(), value);
                printValue(sender, path);
                ConfigManager.INSTANCE.broadcast();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                error(sender, "Invalid type! Expect '%s'.", type.getSimpleName());
            }
        } else {
            error(sender, "No such field!");
        }
    }

    private Optional<Object> getValue(String path) {
        final Optional<Pair<Field, Object>> opt = getField(path);
        if (opt.isPresent()) {
            try {
                return Optional.of(opt.get().getLeft().get(opt.get().getRight()));
            } catch (IllegalAccessException ignored) {
            }
        }
        return Optional.empty();
    }

    private Optional<Pair<Field, Object>> getField(String path) {
        final String[] parts = path.split("\\.");

        try {
            Object currentObject = Aorta.DEFINES.get();
            Field currentField = null;
            for (String p : parts) {
                if (currentField != null)
                    currentObject = currentField.get(currentObject);
                currentField = currentObject.getClass().getDeclaredField(p);
            }

            if (currentField == null)
                return Optional.empty();

            return Optional.of(Pair.of(currentField, currentObject));
        } catch (ReflectiveOperationException e) {
            return Optional.empty();
        }
    }
}
