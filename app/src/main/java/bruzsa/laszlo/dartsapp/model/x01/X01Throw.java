package bruzsa.laszlo.dartsapp.model.x01;

import static java.util.Locale.US;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.time.LocalDateTime;

import bruzsa.laszlo.dartsapp.enties.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Entity
public class X01Throw {

    private final int value;
    private Status status;
    private final int leg;
    private final int dartCount;
    private final boolean checkout;
    private final Player player;
    private final LocalDateTime time = LocalDateTime.now();

    public void setRemoved() {
        status = Status.REMOVED;
    }

    public boolean isNotRemoved() {
        return status != Status.REMOVED;
    }

    public boolean isValid() {
        return status == Status.VALID;
    }

    @NonNull
    @Override
    public String toString() {
        return checkout
                ? String.format(US, "%d|%d", value, dartCount)
                : String.valueOf(value);
    }

}
