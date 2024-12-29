package bruzsa.laszlo.dartsapp.model.x01;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

import bruzsa.laszlo.dartsapp.dao.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class X01Throw {

    private final int value;
    private final Player player;
    private final boolean valid;
    private final LocalDateTime time = LocalDateTime.now();
    private final int leg;
    private final int dartCount;
    private final boolean checkout;
    private boolean removed;

    public X01Throw(int value, boolean valid, int leg, int dartCount, boolean checkout, Player player) {
        this.value = value;
        this.valid = valid;
        this.leg = leg;
        this.dartCount = dartCount;
        this.checkout = checkout;
        this.player = player;
    }

    public void setRemoved() {
        removed = true;
    }

    @NonNull
    @Override
    public String toString() {
        String s = String.valueOf(value);
        if (checkout)
            return s + "^" + dartCount;
        return s;
    }

}
