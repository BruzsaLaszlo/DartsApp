package bruzsa.laszlo.dartsapp.ui.darts501;

import androidx.annotation.NonNull;

public class Darts501Shoot {

    private final int shoot;
    private final boolean handicap;
    private final boolean valid;

    public Darts501Shoot(int shoot, boolean valid) {
        this.shoot = shoot;
        this.valid = valid;
        handicap = valid && shoot > 180;
    }

    public int getShoot() {
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
