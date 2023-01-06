package bruzsa.laszlo.dartsapp.ui.darts501;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Darts501Player {

    private final long id;
    private final String name;
    private final List<Darts501Shoot> shoots = new ArrayList<>();


    public Darts501Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean newShoot(int shootValue) {
        int newScore = 501 - shoots.stream()
                .filter(Darts501Shoot::isValid)
                .mapToInt(Darts501Shoot::getShoot)
                .sum() - shootValue;
        shoots.add(new Darts501Shoot(shootValue, newScore > 1 || newScore == 0));
        return newScore == 0;
    }

    public Darts501SummaryStatistics getStat() {
        return new Darts501SummaryStatistics(shoots);
    }

    public void resetShoots() {
        shoots.clear();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Darts501Shoot> getShoots() {
        return Collections.unmodifiableList(shoots);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Darts501Player that = (Darts501Player) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
