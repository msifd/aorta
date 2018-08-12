package msifeed.mc.aorta.chat.parser;

public class SpeechPart {
    public String text;
    public PartType type;

    SpeechPart(String text, PartType type) {
        this.text = text;
        this.type = type;
    }

    public boolean isWord() {
        return type == PartType.WORD;
    }

    public enum PartType {
        WORD, WHITESPACE, PUNCTUATION, IGNORE_CODE, IGNORE;
    }
}
