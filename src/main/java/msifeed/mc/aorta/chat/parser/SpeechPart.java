package msifeed.mc.aorta.chat.parser;

public class SpeechPart {
    public String text;
    public SpeechPartParser.PartType type;

    SpeechPart(String text, SpeechPartParser.PartType type) {
        this.text = text;
        this.type = type;
    }

    public boolean isWord() {
        return type == SpeechPartParser.PartType.WORD;
    }

    public boolean isIgnored() {
        return type == SpeechPartParser.PartType.IGNORE_CODE || type == SpeechPartParser.PartType.IGNORE;
    }
}
