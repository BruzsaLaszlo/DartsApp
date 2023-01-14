package bruzsa.laszlo.dartsapp.dao;

public class Player {

    private long id;
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
}
