package bruzsa.laszlo.dartsapp.model.cricket;

import static java.time.LocalDateTime.now;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

import bruzsa.laszlo.dartsapp.model.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CricketThrow {
    private final int multiply;
    private final int number;
    private final CricketTeam player;
    private final Team team;
    private boolean removed;
    private final LocalDateTime dateTime = now();

    public boolean setRemoved() {
        if (removed) return false;
        removed = true;
        return true;
    }

    @NonNull
    public String toString() {
        return switch (multiply) {
            case 1 -> " " + number;
            case 2 -> "D" + number;
            case 3 -> "T" + number;
            default -> throw new IllegalStateException("Unexpected value: " + multiply);
        } + " " + player.toString();
    }

}
