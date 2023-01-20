package bruzsa.laszlo.dartsapp.model.cricket;

import androidx.annotation.NonNull;

public class CricketThrow {
    private final int multiply;
    private final int value;
    private final CricketTeam player;
    private boolean removed;

    public CricketThrow(int multiply, int value, CricketTeam player) {
        this.multiply = multiply;
        this.value = value;
        this.player = player;
    }

    public int getMultiply() {
        return multiply;
    }

    public int getValue() {
        return value;
    }

    public CricketTeam getPlayer() {
        return player;
    }

    @NonNull
    public String toString() {
        return switch (multiply) {
            case 1 -> " " + value;
            case 2 -> "D" + value;
            case 3 -> "T" + value;
            default -> throw new IllegalStateException("Unexpected value: " + multiply);
        } + " " + player.toString();
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved() {
        removed = true;
    }
}
