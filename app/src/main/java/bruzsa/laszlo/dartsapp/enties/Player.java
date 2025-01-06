package bruzsa.laszlo.dartsapp.enties;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Player implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;

    public String getName() {
        Log.e("error", "getPlayer: " + name);
        return name;
    }

    @ColumnInfo(name = "nick_name")
    private String nickName;

    @ColumnInfo(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private byte[] image;


    public Player(String name) {
        if (name == null || name.isBlank()) {
            this.name = "Anonymus";
        } else {
            this.name = name;
        }
    }


    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
