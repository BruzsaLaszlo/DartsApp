package bruzsa.laszlo.dartsapp.model.cricket;

import static java.time.LocalDateTime.now;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CricketThrow {
    private final int multiply;
    private final int value;
    private final CricketTeam player;
    private boolean removed;
    private final LocalDateTime dateTime = now();

    public CricketThrow(int multiply, int value, CricketTeam player) {
        this.multiply = multiply;
        this.value = value;
        this.player = player;
    }

    public boolean setRemoved() {
        if (removed) return false;
        removed = true;
        return true;
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

}
