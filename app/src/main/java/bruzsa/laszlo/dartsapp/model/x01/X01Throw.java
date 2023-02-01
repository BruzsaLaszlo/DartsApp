package bruzsa.laszlo.dartsapp.model.x01;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

import bruzsa.laszlo.dartsapp.dao.Player;
import lombok.Getter;

@Getter
public class X01Throw {

    private final UUID uuid = UUID.randomUUID();
    private final int value;
    private final Player player;
    private final boolean handicap;
    private final boolean valid;
    private final LocalDateTime time = LocalDateTime.now();
    private final int leg;
    private final int dart;
    private final boolean checkout;
    private boolean removed;

    public X01Throw(int value, boolean valid, int leg, int dart, boolean checkout, Player player) {
        this.value = value;
        this.valid = valid;
        handicap = valid && value > 180;
        this.leg = leg;
        this.dart = dart;
        this.checkout = checkout;
        this.player = player;
    }

    public boolean isNotHandicap() {
        return !handicap;
    }

    public void setRemoved() {
        removed = true;
    }

    @NonNull
    @Override
    public String toString() {
        String s = String.valueOf(value);
        if (checkout)
            return s + "^" + dart;
        return s;
    }


}
