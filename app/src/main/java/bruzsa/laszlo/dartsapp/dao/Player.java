package bruzsa.laszlo.dartsapp.dao;

import java.util.Objects;

public class Player {

    private long id = (long) (Math.random()*10000000);
    private final String name;

    public Player(String name) {
        this.name = name;
    }



    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
