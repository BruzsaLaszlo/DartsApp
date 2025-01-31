package bruzsa.laszlo.dartsapp.enties;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class Player {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;

    @ColumnInfo(name = "nick_name")
    private String nickName;

    @ColumnInfo(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private byte[] image;

    @Ignore
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
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
