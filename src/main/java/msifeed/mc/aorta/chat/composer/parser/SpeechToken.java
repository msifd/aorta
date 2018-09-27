package msifeed.mc.aorta.chat.composer.parser;

public class SpeechToken {
    public String text;
    private TokenType type;

    SpeechToken(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    public boolean isWord() {
        return type == TokenType.WORD;
    }

    public boolean isWhitespace() {
        return type == TokenType.WHITESPACE;
    }

    @Override
    public String toString() {
        return "'" + text + "'";
    }
}
