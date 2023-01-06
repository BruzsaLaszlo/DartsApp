package bruzsa.laszlo.dartsapp.ui.cricket;

import androidx.annotation.NonNull;

public class CricketShoot {
    private final int multiply;
    private final int value;
    private final CricketPlayer player;
    private boolean removed;

    public CricketShoot(int multiply, int value, CricketPlayer player) {
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

    public CricketPlayer getPlayer() {
        return player;
    }

    @NonNull
    public String toString() {
        return switch (multiply) {
            case 1 -> " " + value;
            case 2 -> "D" + value;
            case 3 -> "T" + value;
            default -> throw new IllegalStateException("Unexpected value: " + multiply);
        } + " " + player.getName();
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved() {
        removed = true;
    }
}
