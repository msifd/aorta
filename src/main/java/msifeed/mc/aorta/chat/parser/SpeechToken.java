package msifeed.mc.aorta.chat.parser;

public class SpeechToken {
    public String text;
    public TokenType type;

    SpeechToken(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    public boolean isWord() {
        return type == TokenType.WORD;
    }
}
