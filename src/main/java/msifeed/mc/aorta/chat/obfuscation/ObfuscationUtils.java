package msifeed.mc.aorta.chat.obfuscation;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.util.List;
import java.util.Random;

class ObfuscationUtils {
    private static final HashFunction hasher = Hashing.murmur3_128(0);

    static Random nonrandomRandom() {
        return new Random(0);
    }

    static Random stringSeededRandom(String s) {
        return new Random(hasher.hashUnencodedChars(s).asLong());
    }

    static Random codesSeededRandom(List<Integer> ints) {
        final Hasher h = hasher.newHasher(ints.size() * Integer.BYTES);
        for (int i : ints)
            h.putInt(i);
        return new Random(h.hash().asLong());
    }
}
