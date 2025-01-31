package bruzsa.laszlo.dartsapp.model.cricket;

import static java.util.concurrent.ThreadLocalRandom.current;

import android.util.Size;

import androidx.fragment.app.FragmentActivity;

import java.util.HashSet;
import java.util.List;

import bruzsa.laszlo.dartsapp.ui.cricket.ScreenSize;
import lombok.Getter;

@Getter
public class CricketSettings {


    public static final int BULL = 25;
    public static final List<Integer> defaultNumbers = List.of(15, 16, 17, 18, 19, 20, BULL);
    private Size size;
    private List<Integer> activeNumbers = defaultNumbers;

    private static final int NUMBER_COUNT = 6;

    private List<Integer> getNewRandomNumbers() {
        var list = new HashSet<Integer>();
        while (list.size() != NUMBER_COUNT) {
            list.add(current().nextInt(20) + 1);
        }
        list.add(BULL);
        return List.copyOf(list);
    }

    public void updateSize(FragmentActivity activity) {
        ScreenSize screenSize = new ScreenSize(activity);
        size = screenSize.getSize();
    }

    public void setRandomNumbers(boolean randomNumbers) {
        activeNumbers = randomNumbers ? getNewRandomNumbers() : defaultNumbers;
    }

}
