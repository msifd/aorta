package msifeed.mc.aorta.chat.obfuscation;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import net.minecraft.util.ChatComponentText;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Random;

public class LangUtils {
    public static Random stringSeededRandom(String s) {
        return new Random(Hashing.goodFastHash(64).hashUnencodedChars(s).asLong());
    }

    public static Random codesSeededRandom(List<Integer> ints) {
        final Hasher h = Hashing.goodFastHash(64).newHasher(ints.size() * Integer.BYTES);
        for (int i : ints)
            h.putInt(i);
        return new Random(h.hash().asLong());
    }

    public static void setText(ChatComponentText comp, String s) {
        try {
            Field field = ChatComponentText.class.getDeclaredFields()[0];
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(comp, s);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
