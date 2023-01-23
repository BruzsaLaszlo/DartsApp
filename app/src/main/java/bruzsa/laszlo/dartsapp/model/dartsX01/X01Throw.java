package bruzsa.laszlo.dartsapp.model.dartsX01;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class X01Throw {

    private final int value;
    private final boolean handicap;
    private final boolean valid;
    private final LocalDateTime time = LocalDateTime.now();
    private final int leg;
    private final int dart;
    private final boolean checkout;

    public X01Throw(int value, boolean valid, int leg, int dart, boolean checkout) {
        this.value = value;
        this.valid = valid;
        handicap = valid && value > 180;
        this.leg = leg;
        this.dart = dart;
        this.checkout = checkout;
    }

    public int getThrow() {
        return value;
    }

    public boolean isNotHandicap() {
        return !handicap;
    }

    public boolean isValid() {
        return valid;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getLeg() {
        return leg;
    }

    public int getDart() {
        return dart;
    }

    public boolean isCheckout() {
        return checkout;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
