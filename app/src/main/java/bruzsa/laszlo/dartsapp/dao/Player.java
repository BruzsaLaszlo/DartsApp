package bruzsa.laszlo.dartsapp.dao;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class Player {

    private final Long id;
    private final String name;
    private final String nickName;
    private final LocalDate dateOfBirth;
    private final byte[] image;

    public Player(Long id, String name, String nickName, LocalDate dateOfBirth, byte[] image) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
