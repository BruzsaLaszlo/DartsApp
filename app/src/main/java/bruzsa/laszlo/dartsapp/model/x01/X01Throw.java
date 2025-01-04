package bruzsa.laszlo.dartsapp.model.x01;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

import bruzsa.laszlo.dartsapp.enties.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class X01Throw {

    private final int value;
    private final int leg;
    private final int dartCount;
    private final boolean checkout;
    private final Player player;
    private final LocalDateTime time = LocalDateTime.now();
    private Status status;

    public X01Throw(int value, Status status, int leg, int dartCount, boolean checkout, Player player) {
        this.value = value;
        this.status = status;
        this.leg = leg;
        this.dartCount = dartCount;
        this.checkout = checkout;
        this.player = player;
    }

    public void setRemoved() {
        status = Status.REMOVED;
    }

    public boolean isRemoved() {
        return status == Status.REMOVED;
    }

    public boolean isValid() {
        return status == Status.VALID;
    }

    public void setPartial() {
        status = Status.PARTIAL;
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
