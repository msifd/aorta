package msifeed.mc.aorta.core.chat;

import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LangMenala implements LangProcessor {
    @Override
    public List<ChatComponentText> process(List<ChatComponentText> components) {
        for (ChatComponentText c : components)
            LangUtils.setText(c, shuffleWords(c.getUnformattedTextForChat()));
        return components;
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
        final Random random = LangUtils.codesSeededRandom(wordCodes);
        Collections.shuffle(wordCodes, random);
    }
}

