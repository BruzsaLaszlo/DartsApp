package bruzsa.laszlo.dartsapp.model.x01;

import static java.util.Locale.US;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(tableName = "x01throw")
public class X01Throw {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private int value;
    private Status status;
    private int leg;
    private int dartCount;
    private boolean checkout;
    private Long playerId;
    private LocalDateTime time;

    @Ignore
    public X01Throw(int value, Status status, int leg, int dartCount, boolean checkout, Long playerId) {
        this.value = value;
        this.status = status;
        this.leg = leg;
        this.dartCount = dartCount;
        this.checkout = checkout;
        this.playerId = playerId;
        time = LocalDateTime.now();
    }

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
