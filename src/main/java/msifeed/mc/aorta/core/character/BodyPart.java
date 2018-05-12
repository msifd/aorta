package msifeed.mc.aorta.core.character;

public class BodyPart {
    public Type type;
    public byte hits;
    public byte maxHits;

    public BodyPart() {
    }

    public BodyPart(Type type, int hits) {
        this.type = type;
        this.hits = (byte) hits;
        this.maxHits = (byte) hits;
    }

    public enum Type {
        UNKNOWN, HEAD, BODY, HAND, LEG
    }
}
