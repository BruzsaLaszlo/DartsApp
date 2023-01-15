package bruzsa.laszlo.dartsapp.ui.darts501;

import androidx.annotation.NonNull;

import java.time.LocalTime;

public class Darts501Throw {

    private final int shoot;
    private final boolean handicap;
    private final boolean valid;
    private final LocalTime time = LocalTime.now();

    public Darts501Throw(int shoot, boolean valid) {
        this.shoot = shoot;
        this.valid = valid;
        handicap = valid && shoot > 180;
    }

    public int getThrow() {
        return shoot;
    }

    public boolean isNotHandicap() {
        return !handicap;
    }

    public boolean isValid() {
        return valid;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(shoot);
    }
}
