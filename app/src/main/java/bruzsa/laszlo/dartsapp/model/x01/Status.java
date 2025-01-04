package bruzsa.laszlo.dartsapp.model.x01;

public enum Status {

    VALID,
    INVALID,
    PARTIAL,
    REMOVED;

    public static Status getStatus(boolean b) {
        return b ? VALID : INVALID;
    }
}
