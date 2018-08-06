package msifeed.mc.aorta.chat.obfuscation;

import msifeed.mc.aorta.chat.ChatUtils;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MenalaObfuscator implements LangObfuscator {
    @Override
    public String obfuscate(String message) {
        return shuffleWords(message);
    }

    private String shuffleWords(String source) {
        final ArrayList<Integer> wordCodes = new ArrayList<>(source.length() / 2);
        final StringBuilder sb = new StringBuilder();

        source = source.toLowerCase();

        for (int offset = 0; offset < source.length(); ) {
            final int code = Character.codePointAt(source, offset);
            offset += Character.charCount(code);

            if (Character.isLetter(code)) {
                wordCodes.add(code);
                continue;
            }
            if (!wordCodes.isEmpty()) {
                if (wordCodes.size() > 2)
                    shuffleCodes(wordCodes);
                for (int c : wordCodes)
                    sb.appendCodePoint(c);
                wordCodes.clear();
            }
            sb.appendCodePoint(code);
        }

        if (!wordCodes.isEmpty()) {
            shuffleCodes(wordCodes);
            for (int c : wordCodes)
                sb.appendCodePoint(c);
        }

        return sb.toString();
    }

    private void shuffleCodes(ArrayList<Integer> wordCodes) {
        final Random random = ChatUtils.codesSeededRandom(wordCodes);
        Collections.shuffle(wordCodes, random);
    }
}

