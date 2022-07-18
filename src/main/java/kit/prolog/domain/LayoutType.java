package kit.prolog.domain;

public enum LayoutType {
    NONE(0),
    CONTEXT(1),
    IMAGE(2),
    CODES(3),
    HYPERLINK(4),
    MATHEMATICS(5),
    VIDEOS(6),
    DOCUMENTS(7);

    private final int value;
    LayoutType(int value) { this.value = value; }
    public int getValue() { return value; }
}
